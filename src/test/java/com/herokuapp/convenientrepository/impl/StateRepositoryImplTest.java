package com.herokuapp.convenientrepository.impl;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.herokuapp.convenient.domain.State;
import com.herokuapp.convenient.repository.impl.StateRepositoryImpl;
import com.herokuapp.convenient.service.consts.SourceType;
import com.herokuapp.convenient.service.consts.StateKind;
import com.herokuapp.convenient.service.consts.StatusKind;

public class StateRepositoryImplTest {

	private State state;

	@Autowired
	//private StateRepositoryImpl stateRepositoryImpl;

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
		/**
		StateRepositoryImpl stateRepositoryImpl = new StateRepositoryImpl();
		stateRepositoryImpl.fetchState(state);
		stateRepositoryImpl.changeStatus(state);
		**/
	}

}
