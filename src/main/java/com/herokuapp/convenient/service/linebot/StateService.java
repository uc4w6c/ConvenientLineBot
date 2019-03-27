package com.herokuapp.convenient.service.linebot;

import com.herokuapp.convenient.domain.State;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;

public interface StateService {
	public abstract void stateStatusChange(State state);
	public abstract Message createMessage(MessageEvent<TextMessageContent> event);
}
