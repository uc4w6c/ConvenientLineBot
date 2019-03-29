package com.herokuapp.convenient.service.linebot;

import com.herokuapp.convenient.domain.State;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;

public interface StateService {
	/**
	 * LineBotの受付状態を変更させる
	 * @param state
	 */
	public abstract void stateStatusChange(State state);

	/**
	 * Botで返却するメッセージを作成する
	 * @param event
	 * @return Message
	 */
	public abstract Message createMessage(MessageEvent<TextMessageContent> event);
}
