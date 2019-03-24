package com.herokuapp.convenient.service;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import com.herokuapp.convenient.domain.State;
import com.herokuapp.convenient.repository.StateRepository;
import com.herokuapp.convenient.repository.TaskRepository;
import com.herokuapp.convenient.service.consts.SourceType;
import com.herokuapp.convenient.service.consts.StateKind;
import com.herokuapp.convenient.service.consts.StatusKind;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.event.source.GroupSource;
import com.linecorp.bot.model.event.source.RoomSource;
import com.linecorp.bot.model.event.source.Source;

@Service
public class LineBotService {
	@Autowired
	StateRepository stateRepository;
	@Autowired
	TaskRepository taskRepository;
	
	private final String START_REQUEST = "にゃー";
	private final String END_REQUEST = "おわり";
	/** 
	 * 取得したメッセージから返却するメッセージを決める
	 * @param event
	 * @return String
	 */
	public String makeReply(MessageEvent<TextMessageContent> event) {
		String receivedMessage = event.getMessage().getText(); 
		String replyMessage;
		Source source = event.getSource();

		switch (receivedMessage) {
		case START_REQUEST: {
			State state;
			if (source instanceof GroupSource) {
				state = new 
						State.Builder(SourceType.GROUP.getCode(),
										event.getSource().getUserId(),
										StateKind.TASK.value(),
										StatusKind.ACCEPTING.value()
									).
							groupId( ((GroupSource) source).getGroupId() ).
							build();

			} else if (source instanceof RoomSource) {
				state = new 
						State.Builder(SourceType.ROOM.getCode(),
										event.getSource().getUserId(),
										StateKind.TASK.value(),
										StatusKind.ACCEPTING.value()
									).
							roomId( ((RoomSource) source).getRoomId() ).
							build();

			} else {
				state = new 
						State.Builder(SourceType.ROOM.getCode(),
										event.getSource().getUserId(),
										StateKind.TASK.value(),
										StatusKind.ACCEPTING.value()
									).build();

			}
			replyMessage = stateStatusChange(state);
		}
		case END_REQUEST: {
			
			break;
		}
		default:
			break;
		}

		String message = null;
		return message;
	}

	private String stateStatusChange(State state) {
		
		List<State> companies = this.stateRepository.findAll(Specifications
				.where(Predicate(criteriaBuilder.equal(root.get("sourceType"), state.getSourceType()) )));
		
		source_type	user_id	group_id	room_id
		
		stateRepository.save(state);
		return "";
	}
}