package com.herokuapp.convenient.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.dbunit.DataSourceBasedDBTestCase;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.QueryDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mssql.InsertIdentityOperation;
import org.dbunit.operation.DatabaseOperation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.linecorp.bot.model.event.CallbackRequest;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.UserSource;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
//@TestExecutionListeners({
//	DependencyInjectionTestExecutionListener.class,
//	TransactionalTestExecutionListener.class,
//	DbUnitTestExecutionListener.class
//})
//@Transactional
public class TestLineBotIntegrationTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate template;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	/** データベースのバックアップ */
	private File backupFile;

	@Before
	public void setUp() {

		// バックアップ取得
		// 参考 https://qiita.com/tamurashingo@github/items/e37697796001bb40f0d2
		Connection conn = null;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			IDatabaseConnection dbconn = new DatabaseConnection(conn);
			QueryDataSet partialDataSet = new QueryDataSet(dbconn);

			// バックアップを取りたいテーブルを列挙
			partialDataSet.addTable("states");
			partialDataSet.addTable("tasks");
			// バックアップ内容を保持するファイルを生成
			backupFile = File.createTempFile("testdb_bak", ".xml");
			// テーブルの内容をファイルに書き込む
			FlatXmlDataSet.write(partialDataSet, new FileOutputStream(backupFile));
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}	
			}catch(SQLException e){}
		}
	}

	@Test
	@DatabaseSetup("/dbunit/startTask.xml")
	public void タスク開始Test() throws Exception {
		URI uri = new URI("http://localhost:" + port + "/linebot/create");

		CallbackRequest callBackRequest;
		List<Event> events = new ArrayList<>();
		MessageEvent<TextMessageContent> event = 
				new MessageEvent<>("0f3779fba3b349968c5d07db31eab56f",
									new UserSource("U4af4980629..."),
									new TextMessageContent("325708", "にゃー"),
									LocalDateTime.now().toInstant(ZoneOffset.UTC));
		events.add(event);

		callBackRequest = new CallbackRequest(events);

		Message responseBody = template.postForObject(uri, callBackRequest, Message.class);
		String expected = "メモをとるにゃー \n\r"
						+ "「おわり」って言ったら終わるにゃ!";
		TextMessage expectedMessage = new TextMessage(expected);

		assertThat(responseBody).isEqualTo(expectedMessage);
	}

	@Test
	@DatabaseSetup("/dbunit/recordTask.xml")
	public void メモTest() throws Exception {
		URI uri = new URI("http://localhost:" + port + "/linebot/create");

		CallbackRequest callBackRequest;
		List<Event> events = new ArrayList<>();
		MessageEvent<TextMessageContent> event = 
				new MessageEvent<>("0f3779fba3b349968c5d07db31eab56f",
									new UserSource("U4af4980629..."),
									new TextMessageContent("325708", "メモ投入"),
									LocalDateTime.now().toInstant(ZoneOffset.UTC));
		events.add(event);

		callBackRequest = new CallbackRequest(events);

		Message responseBody = template.postForObject(uri, callBackRequest, Message.class);
		String expected = "メモったにゃ!";
		TextMessage expectedMessage = new TextMessage(expected);

		assertThat(responseBody).isEqualTo(expectedMessage);
	}

	@After
	public void tearDown() throws Exception {
		if (backupFile == null) {
			return;
		}

		Connection conn = null;
		try {
			conn = jdbcTemplate.getDataSource().getConnection();
			IDatabaseConnection dbconn = new DatabaseConnection(conn);

			IDataSet dataSet = new FlatXmlDataSetBuilder().build(backupFile);
			new InsertIdentityOperation(DatabaseOperation.CLEAN_INSERT).execute(dbconn, dataSet);
			backupFile = null;

			// dbconn.getConnection().commit();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

}
