package com.herokuapp.convenient.service.linebot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.herokuapp.convenient.domain.State;
import com.herokuapp.convenient.repository.StateRepository;
import com.herokuapp.convenient.repository.impl.StateRepositoryImpl;
import com.herokuapp.convenient.service.consts.StatusKind;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import org.springframework.beans.factory.annotation.Configurable;

@Component
public class TaskStartState implements StateService {

	@Autowired
	private StateRepositoryImpl stateRepositoryImpl;

	@Autowired
	private StateRepository stateRepository;

	private final String REPLY_MESSAGE = "メモをとるにゃー \n\r"
										+ "「おわり」って言ったら終わるにゃ!";

	public State stateStatusChange(State state) {
		//StateRepositoryImpl stateRepositoryImpl = new StateRepositoryImpl();
		State newState = state.changeStatus(StatusKind.ACCEPTING.value());
		State updateAfterState = stateRepositoryImpl.save(newState);
		//State updateAfterState = stateRepository.save(newState);
		return updateAfterState;
	};

	public Message createMessage(MessageEvent<TextMessageContent> event, State state) {
		return new TextMessage(REPLY_MESSAGE);
	};
}
