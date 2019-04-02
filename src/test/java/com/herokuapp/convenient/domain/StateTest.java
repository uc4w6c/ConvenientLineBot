package com.herokuapp.convenient.domain;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.herokuapp.convenient.service.LineBotService;
import com.herokuapp.convenient.service.consts.SourceType;
import com.herokuapp.convenient.service.consts.StateKind;
import com.herokuapp.convenient.service.consts.StatusKind;
import com.linecorp.bot.model.event.source.GroupSource;

public class StateTest {

	private State state;

	@Before
	public void setUp() {
		state = new State.Builder(SourceType.GROUP.getCode(),
						"testuser",
						StateKind.TASK.value(),
						StatusKind.WAITING.value()
					).
					build();
	}

	@Test
	public void changeStatusTest() {
		System.out.println(state.getStatus());
		State changedState = state.changeStatus(StatusKind.ACCEPTING.value());
		System.out.println(changedState.getStatus());
		assertThat(changedState.getStatus(), is(StatusKind.ACCEPTING.value()));
	}
}
