package com.herokuapp.convenient.service.linebot;

import com.herokuapp.convenient.domain.State;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

public class TaskEndStateService implements StateService {
	public void stateStatusChange(State state) {
		
	};
	public Message createMessage(MessageEvent<TextMessageContent> event) {
		Message message = new TextMessage("");
		return message;
	};
}
