package com.herokuapp.convenient.service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.herokuapp.convenient.domain.State;
import com.herokuapp.convenient.domain.Task;
import com.herokuapp.convenient.repository.StateRepository;
import com.herokuapp.convenient.repository.TaskRepository;
import com.herokuapp.convenient.repository.impl.StateRepositoryImpl;
import com.herokuapp.convenient.service.consts.SourceType;
import com.herokuapp.convenient.service.consts.StateKind;
import com.herokuapp.convenient.service.consts.StatusKind;
import com.herokuapp.convenient.service.linebot.StateService;
import com.herokuapp.convenient.service.linebot.TaskDeleteState;
import com.herokuapp.convenient.service.linebot.TaskEndState;
import com.herokuapp.convenient.service.linebot.TaskLoadState;
import com.herokuapp.convenient.service.linebot.TaskRecordState;
import com.herokuapp.convenient.service.linebot.TaskStartState;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.response.BotApiResponse;

import lombok.NonNull;

@Service
public class LineBotService {

	/**
	@Autowired
	StateRepository stateRepository;
	@Autowired
	StateRepositoryImpl stateRepositoryImpl;
	**/
	@Autowired
	TaskRepository taskRepository;


	@Autowired
	TaskStartState taskStartState;
	@Autowired
	TaskEndState taskEndState;
	@Autowired
	TaskRecordState taskRecordState;
	@Autowired
	TaskLoadState taskLoadState;
	@Autowired
	TaskDeleteState taskDeleteState;
	@Autowired
	LineMessagingClient lineMessagingClient;
	/**
	private final LineMessagingClient lineMessagingClient;

	LineBotService(LineMessagingClient lineMessagingClient) {
		this.lineMessagingClient = lineMessagingClient;
	}
	**/

	private final String START_REQUEST = "にゃー";
	private final String END_REQUEST = "おわり";
	private final String MEMO_REQUEST = "メモ";
	private final String DELETE_REQUEST = "削除";

	private final String DELETE_REPLY_MSG = "削除したにゃ";
	private final String NOTING_TASK_REPLY_MSG = "削除対象は存在しないにゃ";
	
	/** 
	 * 取得したメッセージから返却するメッセージを決める
	 * @param event
	 * @return String
	 */
	public Message makeReply(MessageEvent<TextMessageContent> event,
						TextMessageContent content) throws Exception {

		State state = stateBuildByEvent(event);
		StateService stateService = null;
		//ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		//ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		//BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext.xml");

		String receivedMessage = content.getText();
		//try (ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml")) {
		switch (receivedMessage) {
		case START_REQUEST: {
			//stateService = new TaskStartState();
			//stateService = ctx.getBean(TaskStartState.class);
			stateService = taskStartState;
			break;
		}
		case END_REQUEST: {
			//stateService = new TaskEndState();
			//stateService = ctx.getBean(TaskEndState.class);
			stateService = taskEndState;
			break;
		}
		case MEMO_REQUEST: {
			stateService = taskLoadState;
			break;
		}
		case DELETE_REQUEST: {
			stateService = taskDeleteState;
			break;
		}
		default:
			//stateService = new TaskRecordState();
			//stateService = ctx.getBean(TaskRecordState.class);
			stateService = taskRecordState;
			break;
		}

		// このインターフェースのメソッド設計が微妙だな。変えたい。
		State newState = stateService.stateStatusChange(state);
		return stateService.createMessage(event, newState);

		//}
		//replyMessage = stateStatusChange(state);
		//return replyMessage;
	}

	private State stateBuildByEvent(MessageEvent<TextMessageContent> event) {
		Source source = event.getSource();
		if (source instanceof GroupSource) {
			return new State.Builder(SourceType.GROUP.getCode(),
									event.getSource().getUserId(),
									StateKind.TASK.value(),
									StatusKind.WAITING.value()
								).
						groupId( ((GroupSource) source).getGroupId() ).
						build();

		} else if (source instanceof RoomSource) {
			return new State.Builder(SourceType.ROOM.getCode(),
									event.getSource().getUserId(),
									StateKind.TASK.value(),
									StatusKind.WAITING.value()
								).
						roomId( ((RoomSource) source).getRoomId() ).
						build();

		} else {
			return new State.Builder(SourceType.USER.getCode(),
									event.getSource().getUserId(),
									StateKind.TASK.value(),
									StatusKind.WAITING.value()
								).build();
		}
	}

	/**
	 * 削除を指示した場合のコールバックで利用
	 * 指定したIDのタスクを削除する
	 * @param event
	 * @param deleteId
	 * @return
	 */
	public Message deleteTask(PostbackEvent event, String deleteId) {
		// WARNING: deleteIdが整数型ではない場合のエラーチェックはしていない
		Task task = taskRepository.getOne(Integer.getInteger(deleteId));
		if (task == null) {
			return new TextMessage(NOTING_TASK_REPLY_MSG);
		}
		taskRepository.delete(task);
		return new TextMessage(DELETE_REPLY_MSG);
	}

}