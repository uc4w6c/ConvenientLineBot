package com.herokuapp.convenient.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.Source;

@Service
public class LineBotService {
	/** 
	 * 取得したメッセージから返却するメッセージを決める
	 * @param event
	 * @return String
	 */
	public String makeReply(MessageEvent<TextMessageContent> event) {
		Source source = event.getSource();
		((GroupSource)source).getGroupId();
		
		String message = null;
		return message;
	}
}