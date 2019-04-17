package com.herokuapp.convenient.service.linebot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.herokuapp.convenient.domain.State;
import com.herokuapp.convenient.domain.Task;
import com.herokuapp.convenient.repository.TaskRepository;
import com.herokuapp.convenient.repository.impl.StateRepositoryImpl;
import com.herokuapp.convenient.repository.impl.TaskRepositoryImpl;
import com.herokuapp.convenient.service.consts.StatusKind;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

@Component
public class TaskRecordState implements StateService {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private StateRepositoryImpl stateRepositoryImpl;

	private final String REPLY_MESSAGE = "メモったにゃ!";

	public State stateStatusChange(State state) {
		// 一旦は何も更新せずに返却（将来的には受付回数をカウントとかしようかな）
		//StateRepositoryImpl stateRepositoryImpl = new StateRepositoryImpl();
		State fetchState = stateRepositoryImpl.fetchState(state);
		return fetchState;
	};
	public Message createMessage(MessageEvent<TextMessageContent> event, State state) {
		if (state == null) {
			return new TextMessage("");
		}

		if (state.getStatus() != StatusKind.ACCEPTING.value()) {
			return new TextMessage("");
		}

		Task task = new Task.Builder(
						state.getSourceType(), state.getUserId(), event.getMessage().getText())
						.groupId(state.getGroupId())
						.roomId(state.getRoomId())
						.build();
		
		Task newTask = taskRepository.save(task);
		if (newTask == null) {
			return new TextMessage("");
		}
		return new TextMessage(REPLY_MESSAGE);
	};
}
