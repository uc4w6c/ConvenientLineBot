/**
package com.herokuapp.convenient.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.herokuapp.convenient.service.LineBotService;
import com.linecorp.bot.model.event.CallbackRequest;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;

@RestController
@RequestMapping("/linebot")
public class TestLineBotController {

	private final LineBotService lineBotService;

	public TestLineBotController(LineBotService lineBotService) {
		this.lineBotService = lineBotService;
	}

	@PostMapping(path = "create")
	public Message create(@RequestBody CallbackRequest callBackRequest) {
		System.out.println(callBackRequest);

		// TODO: 色々汚いから変えたい
		// 暫定的に Listの0番目のものを取得
		// com.linecorp.bot.spring.boot.support.LineMessageHandlerSupportを参考
		//MessageEvent<TextMessageContent> event = callBackRequest.getEvents().get(0);
		Event event = callBackRequest.getEvents().get(0);
		if (event instanceof MessageEvent) {
			MessageEvent<TextMessageContent> messageEvent= (MessageEvent<TextMessageContent>)event;
			System.out.println(messageEvent);
			return lineBotService.makeReply(messageEvent);
		} else {
			return null;
		}
	}

	@PostMapping(path = "bodyprint")
	public String create(@RequestBody String body) {
		System.out.println(body);
		return body;
	}
}
**/