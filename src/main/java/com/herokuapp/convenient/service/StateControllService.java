package com.herokuapp.convenient.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.herokuapp.convenient.repository.StateRepository;
import com.herokuapp.convenient.repository.impl.StateRepositoryImpl;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;

public class StateControllService {
	@Autowired
	StateRepository stateRepository;

	@Autowired
	StateRepositoryImpl stateRepositoryImpl;

	private MessageEvent<TextMessageContent> event;

	public StateControllService(MessageEvent<TextMessageContent> event) {
		this.event = event;
	}

}
