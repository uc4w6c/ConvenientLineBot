package com.herokuapp.convenient.service.linebot;

import java.util.List;

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
public class TaskLoadState implements StateService {

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TaskRepositoryImpl taskRepositoryImpl;

	private static final String NOTING_MEMO_MSG = "メモがないにゃー\r\n「にゃー」でメモを開始するにゃ";

	public State stateStatusChange(State state) {
		// 一旦は何も更新せずに返却
		return state;
	};
	public Message createMessage(MessageEvent<TextMessageContent> event, State state) {
		List<Task> tasks = taskRepositoryImpl.fetchState(state);

		/**
		Task task = new Task.Builder(
						state.getSourceType(), state.getUserId(), event.getMessage().getText())
						.groupId(state.getGroupId())
						.roomId(state.getRoomId())
						.build();
		List<Task> tasks = taskRepository
							.findAllOrderByCreatedAt(task.getSourceType(),
													task.getUserId(),
													task.getGroupId(),
													task.getRoomId());
		**/

		StringBuilder replyMessage = new StringBuilder();

		if (tasks.size() == 0) {
			replyMessage.append(NOTING_MEMO_MSG);
		}

		boolean isFirst = true;
		for (Task taskTodo : tasks) {
			if (!isFirst) {
				replyMessage.append("\r\n");
			}
			replyMessage.append(taskTodo.getTodoText());
		}
		return new TextMessage(replyMessage.toString());
	};
}
