package com.herokuapp.convenient.web;

import org.springframework.beans.factory.annotation.Autowired;

import com.herokuapp.convenient.service.LineBotService;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.AudioMessageContent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.message.VideoMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@LineMessageHandler
public class LineBotController2 {

	private final LineBotService lineBotService;

	public LineBotController2(LineBotService lineBotService) {
		this.lineBotService = lineBotService;
	}

	@EventMapping
	public TextMessage testEvent(MessageEvent<TextMessageContent> event) {
		return (TextMessage)lineBotService.makeReply(event);  
	}
}
