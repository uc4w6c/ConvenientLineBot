package com.herokuapp.convenient.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.dbunit.DataSourceBasedDBTestCase;
import org.dbunit.dataset.IDataSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
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
@TestExecutionListeners({
	DependencyInjectionTestExecutionListener.class,
	TransactionalTestExecutionListener.class, // (1)
	DbUnitTestExecutionListener.class
})
@Transactional //(2)
public class TestLineBotIntegrationTest {
	
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate template;

	@Before
	public void setUp() {

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
		System.out.println(expectedMessage);

		assertThat(responseBody).isEqualTo(expectedMessage);
	}

}
