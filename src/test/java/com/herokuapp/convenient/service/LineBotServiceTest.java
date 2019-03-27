package com.herokuapp.convenient.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.herokuapp.convenient.domain.State;
import com.herokuapp.convenient.repository.impl.StateRepositoryImpl;
import com.herokuapp.convenient.service.consts.SourceType;
import com.herokuapp.convenient.service.consts.StateKind;
import com.herokuapp.convenient.service.consts.StatusKind;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.event.source.UserSource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class LineBotServiceTest {
// TODO：Autowiredが思うように動かない。。。時間がある時に対応したい。

	@Autowired
	StateRepositoryImpl stateRepositoryImpl;

	private LineBotService lineBotService;

	@Before
	public void setUp() {
		lineBotService = new LineBotService();
	}

	@Test
	public void makeReplyTest() {
		TextMessageContent textMessageContent = 
				new TextMessageContent("325708", "にゃー");
		Source source = new UserSource("U4af4980629");

		LocalDateTime ldt = LocalDateTime.now();
		Instant instant = ldt.toInstant(ZoneOffset.UTC);

		MessageEvent<TextMessageContent> event = 
				new MessageEvent<TextMessageContent>(
						"0f3779fba3b349968c5d07db31eab56f", 
						source, 
						textMessageContent, 
						instant);

		String expectedValue = "こんにちは \r\n今からメモるにゃー";
		assertThat(lineBotService.makeReply(event), is(expectedValue));
	}

}
