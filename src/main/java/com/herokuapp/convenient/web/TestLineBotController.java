package com.herokuapp.convenient.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.herokuapp.convenient.service.LineBotService;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;

@Controller
@RequestMapping("/linebot")
public class TestLineBotController {

	private final LineBotService lineBotService;

	public TestLineBotController(LineBotService lineBotService) {
		this.lineBotService = lineBotService;
	}

	@PostMapping(path = "create")
	public Message create(@RequestBody MessageEvent<TextMessageContent> event, @RequestBody String body) {
		System.out.println(body);
		MessageEvent<TextMessageContent> = new MessageEvent<TextMessageContent>(body);
		System.out.println(event);
		return lineBotService.makeReply(event);
	}
}
