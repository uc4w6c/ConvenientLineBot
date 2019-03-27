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

import lombok.extern.slf4j.Slf4j;

@LineMessageHandler
public class LineBotController {

	private final LineBotService lineBotService;

	public LineBotController(LineBotService lineBotService) {
		this.lineBotService = lineBotService;
	}

	@EventMapping
	public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
		// Stateパターンを実装。
		// 戻り値は汎用性を持たせるためにMessage型にしているので、
		// 各実装クラス（ConcreteState）で選択する。

		// 状態テーブルからデータを取得する → いや実装クラスから呼び出すようにしよう


		// if文でこの後の処理を決める。
		String message = lineBotService.makeReply(event);

		// 戻り値はMessage型にする
		return new TextMessage(message);
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
