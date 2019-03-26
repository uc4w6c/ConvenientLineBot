package com.herokuapp.convenient.service;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.message.MessageContent;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.event.source.UserSource;

public class LineBotServiceTest {

	private LineBotService lineBotService;

	@Before
	public void setUp() {
		lineBotService = new LineBotService();
	}

	@Test
	public void makeReplyTest() {
		TextMessageContent textMessageContent = 
				new TextMessageContent("325708", "にゃー");
		Source source = new UserSource("U4af4980629...");

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
