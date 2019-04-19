package com.herokuapp.convenient.web;

//import org.springframework.beans.factory.annotation.Autowired;

import com.herokuapp.convenient.service.LineBotService;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.AudioMessageContent;
import com.linecorp.bot.model.event.message.ImageMessageContent;
import com.linecorp.bot.model.event.message.StickerMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.message.VideoMessageContent;
import com.linecorp.bot.model.event.postback.PostbackContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@LineMessageHandler
public class LineBotController {

	//@Autowired
	//private LineBotService lineBotService;

	private final LineBotService lineBotService;

	public LineBotController(LineBotService lineBotService) {
		this.lineBotService = lineBotService;
	}

	@EventMapping
	public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
		TextMessageContent message = event.getMessage();
		return lineBotService.makeReply(event, message);
	}

	@EventMapping
	public Message handlePostBackEvent(PostbackEvent event) throws Exception {
		String deleteId = event.getPostbackContent().getData();
		return lineBotService.deleteTask(event, deleteId);
	}

	@EventMapping
	public void handleDefaultMessageEvent(Event event) {
		System.out.println("event: " + event);
	}

	@EventMapping
	public Message handleStickerMessage(MessageEvent<StickerMessageContent> event) {
		return new TextMessage("Thank you for sending stamp ：）");
	}

	@EventMapping
	public Message handleImageMessage(MessageEvent<ImageMessageContent> event) {
		return new TextMessage("Thank you for sending image ：D");
	}

	@EventMapping
	public Message handleVideoMessage(MessageEvent<VideoMessageContent> event) {
		return new TextMessage("Thank you for sending video XD");
	}

	@EventMapping
	public Message handleAudioMessage(MessageEvent<AudioMessageContent> event) {
		return new TextMessage("Thank you for sending audio ；）");
	}

}
