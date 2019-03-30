package com.herokuapp.convenient.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.herokuapp.convenient.domain.State;
import com.herokuapp.convenient.repository.StateRepository;
import com.herokuapp.convenient.repository.TaskRepository;
import com.herokuapp.convenient.repository.impl.StateRepositoryImpl;
import com.herokuapp.convenient.service.consts.CodeEnum;
import com.herokuapp.convenient.service.consts.SourceType;
import com.herokuapp.convenient.service.consts.StateKind;
import com.herokuapp.convenient.service.consts.StatusKind;
import com.herokuapp.convenient.service.linebot.StateService;
import com.herokuapp.convenient.service.linebot.TaskEndState;
import com.herokuapp.convenient.service.linebot.TaskStartState;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.event.source.Source;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;

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
		String replyMessage = null;

		State state = stateBuildByEvent(event);
		StateService stateService = null;

		switch (receivedMessage) {
		case START_REQUEST: {
			stateService = new TaskStartState();
			break;
		}
		case END_REQUEST: {
			stateService = new TaskEndState();
			break;
		}
		default:
			break;
		}

		// 一旦nullチェックロジックを入れるけど、
		// 後で上のswitch文で必ずセットするように変更したらロジック削除
		if (stateService == null) {
			return new TextMessage("");
		}

		stateService.stateStatusChange(state);
		return stateService.createMessage(event);
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

	private String stateStatusChange(State state) {
		// TODO:全体的にリファクタリングしたい

		State nowState = null;
		/**
		switch (CodeEnum.getEnumByCode(SourceType.class, state.getSourceType()).getName()) {
		case "user": {
			nowState = this.stateRepositoryImpl.fetchState(state.getUserId());
			break;
		}
		case "group": {
			nowState = this.stateRepositoryImpl.fetchState(state.getUserId(),
					CodeEnum.getEnumByCode(SourceType.class, state.getSourceType()).getName(),
					state.getGroupId());
		}
		case "room": {
			nowState = this.stateRepositoryImpl.fetchState(state.getUserId(),
					CodeEnum.getEnumByCode(SourceType.class, state.getSourceType()).getName(),
					state.getRoomId());
		}
		default: {
			break;
		}
		}
		**/

		int result = 0;
		if (nowState == null) {
			result = this.stateRepositoryImpl.insertStatus(state);
		} else {
			result = this.stateRepositoryImpl.changeStatus(nowState);
		}

		if (result == 0) {
			return "ごめんにゃー。 \r\n"
					+ "エラーが発生したから再度試してみてにゃー";
		}

		return "こんにちは \r\n"
				+ "今からメモるにゃー";
	}
}