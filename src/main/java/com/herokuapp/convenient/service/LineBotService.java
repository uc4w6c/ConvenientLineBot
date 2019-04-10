package com.herokuapp.convenient.service;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import com.herokuapp.convenient.domain.State;
import com.herokuapp.convenient.repository.StateRepository;
import com.herokuapp.convenient.repository.TaskRepository;
import com.herokuapp.convenient.repository.impl.StateRepositoryImpl;
import com.herokuapp.convenient.service.consts.SourceType;
import com.herokuapp.convenient.service.consts.StateKind;
import com.herokuapp.convenient.service.consts.StatusKind;
import com.herokuapp.convenient.service.linebot.StateService;
import com.herokuapp.convenient.service.linebot.TaskEndState;
import com.herokuapp.convenient.service.linebot.TaskRecordState;
import com.herokuapp.convenient.service.linebot.TaskStartState;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.Message;

@Service
public class LineBotService {
	@Autowired
	StateRepository stateRepository;

	@Autowired
	StateRepositoryImpl stateRepositoryImpl;

	@Autowired
	TaskRepository taskRepository;
	
	private final String START_REQUEST = "にゃー";
	private final String END_REQUEST = "おわり";
	/** 
	 * 取得したメッセージから返却するメッセージを決める
	 * @param event
	 * @return String
	 */
	public Message makeReply(MessageEvent<TextMessageContent> event) {
		String receivedMessage = event.getMessage().getText(); 
		
		System.out.println(event);
		State state = stateBuildByEvent(event);
		System.out.println(state);
		StateService stateService = null;
		//ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		//BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext.xml");

		//try (ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml")) {
			switch (receivedMessage) {
			case START_REQUEST: {
				//stateService = new TaskStartState();
				stateService = ctx.getBean(TaskStartState.class);
				break;
			}
			case END_REQUEST: {
				//stateService = new TaskEndState();
				stateService = ctx.getBean(TaskEndState.class);
				break;
			}
			default:
				//stateService = new TaskRecordState();
				stateService = ctx.getBean(TaskRecordState.class);
				break;
			}
	
			System.out.println(state);
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
}