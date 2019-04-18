package com.herokuapp.convenient.service.linebot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.herokuapp.convenient.domain.State;
import com.herokuapp.convenient.domain.Task;
import com.herokuapp.convenient.repository.impl.StateRepositoryImpl;
import com.herokuapp.convenient.repository.impl.TaskRepositoryImpl;
import com.herokuapp.convenient.service.consts.StatusKind;
import com.linecorp.bot.model.action.Action;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.message.template.Template;

@Component
public class TaskDeleteState implements StateService {

	@Autowired
	private StateRepositoryImpl stateRepositoryImpl;
	@Autowired
	private TaskRepositoryImpl taskRepositoryImpl;

	private final String REPLY_MESSAGE = "削除するものを選んでにゃ!";
	private final String DELETE_MESSAGE = "削除するにゃ!";

	public State stateStatusChange(State state) {
		// TODO: ACCEPTINGを削除待ち的なものに変更したい。
		//       もしくは削除するためのURLを渡してあげる？いやブラウザ起動するとうざいな
		
		// 一旦実装を消しておく
		//State newState = state.changeStatus(StatusKind.ACCEPTING.value());
		//State updateAfterState = stateRepositoryImpl.save(newState);
		//return updateAfterState;
				
		return state;
	};

	public Message createMessage(MessageEvent<TextMessageContent> event, State state) {
		List<Task> tasks = taskRepositoryImpl.fetchState(state);
		List<CarouselColumn> carouselColumns = new ArrayList<>();
		int i = 0;
		for (Task taskTodo : tasks) {
			List<Action> action = Collections.singletonList(new PostbackAction(DELETE_MESSAGE, taskTodo.getId().toString()));
			CarouselColumn carouselColumn = new CarouselColumn("", Integer.toString(i), taskTodo.getTodoText(), action);
			carouselColumns.add(carouselColumn);
			i++;
		}
		return new TemplateMessage("テスト", new CarouselTemplate(carouselColumns));
	};
}
