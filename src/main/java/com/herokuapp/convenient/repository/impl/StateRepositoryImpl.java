package com.herokuapp.convenient.repository.impl;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;

import com.herokuapp.convenient.domain.State;
import com.herokuapp.convenient.repository.StateRepository;
import com.herokuapp.convenient.repository.StateRepositoryCustom;
import com.herokuapp.convenient.service.consts.CodeEnum;
import com.herokuapp.convenient.service.consts.SourceType;
import com.herokuapp.convenient.service.consts.StateKind;
import com.herokuapp.convenient.service.consts.StatusKind;

public class StateRepositoryImpl implements StateRepositoryCustom {
	@Autowired
	private StateRepository repository;

	@Autowired
	private EntityManager manager;

	private final String SELECT_STATE = "SELECT * FROM states "
								+ "WHERE source_type = :type and "
								+ "user_id = :userId";

	private final String INSERT_STATE = "INSERT INTO states "
								+ "(source_type, {KEY}, state_kind, status) "
								+ "values ({VALUE})";

	private final String UPDATE_STATE = "UPDATE states "
								+ "SET status = {STATUS} "
								+ "WHERE source_type = {TYPE} and "
								+ "user_id = '{USERID}' ";

	public State fetchState(State state) {
		int type = state.getSourceType();
		Query query;

		if (type == SourceType.USER.getCode()) {
			query = manager.createNativeQuery(SELECT_STATE, State.class)
						.setParameter("type", state.getSourceType())
						.setParameter("userId", state.getUserId());

		} else if (type == SourceType.GROUP.getCode()) {
			query = manager.createNativeQuery
					(SELECT_STATE + " and group_id = :groupId", State.class)
					.setParameter("type", state.getSourceType())
					.setParameter("userId", state.getUserId())
					.setParameter("groupId", state.getGroupId());

		} else if (type == SourceType.ROOM.getCode()) {
			query = manager.createNativeQuery
					(SELECT_STATE + " and group_id = :groupId", State.class)
					.setParameter("type", state.getSourceType())
					.setParameter("userId", state.getUserId())
					.setParameter("room_id", state.getRoomId());

		} else {
			throw new IllegalArgumentException("SourceTypeに設定の値が想定外の値です。");
		}

		List<State> states = query.getResultList();

		if (states.size() == 0) {
			return null;
		}
		return (State)states.get(0);
	}

	public int insertStatus(State state) {
		
		String key = null;
		String value = null;
		switch (CodeEnum.getEnumByCode(SourceType.class, state.getSourceType()).getName()) {
		case "user" : {
			key = "user_id";
			value = state.getSourceType() + ", " 
					+ "'" + state.getUserId() + "', "
					+ StateKind.TASK.value() + ", "
					+ StatusKind.ACCEPTING.value();
			break;
		}
		case "group" : {
			key = "user_id, group_id";
			value = state.getSourceType() + ", " 
					+ "'" + state.getUserId() + "', "
					+ "'" + state.getGroupId() + "', "
					+ StateKind.TASK.value() + ", "
					+ StatusKind.ACCEPTING.value();
			break;
		}
		case "room": {
			key = "user_id, room_id";
			value = state.getSourceType() + ", " 
					+ "'" + state.getUserId() + "', "
					+ "'" + state.getRoomId() + "', "
					+ StateKind.TASK.value() + ", "
					+ StatusKind.ACCEPTING.value();
			break;
		}
		default: {
			throw new IllegalArgumentException("typeに設定の値が想定外の値です。");
		}
		}

		String sql = INSERT_STATE.replace("{KEY}", key).replace("{VALUE}", value);
		
		EntityManager manager2 = manager.getEntityManagerFactory().createEntityManager();
		manager2.getTransaction().begin();
		Query query = manager2.createNativeQuery(sql.toString());
		int result = query.executeUpdate();
		manager2.getTransaction().commit();
		return result;
		/**
		try {
			manager.getTransaction().begin();
			manager.createNativeQuery(sql.toString());
			manager.getTransaction().commit();
			return 1;
		}catch (Exception ex)
		{
			return 0;
		}
		*/
	}

	public int changeStatus(State state) {

		int patchStatus = 0;

		if (state.getStateKind() == 0 ) {
			patchStatus = StatusKind.ACCEPTING.value();
		} else if (state.getStateKind() == StatusKind.ACCEPTING.value()) {
			patchStatus = StatusKind.WAITING.value();
		} else if (state.getStateKind() == StatusKind.WAITING.value()) {
			patchStatus = StatusKind.ACCEPTING.value();
		}

		StringBuilder sql = new StringBuilder(
				UPDATE_STATE.replace("{STATUS}", Integer.toString(patchStatus)).
							replace("{TYPE}", Integer.toString(state.getSourceType())).
							replace("{USERID}", state.getUserId()));

		if (state.getGroupId() != null) {
			sql.append("and group_id = '" + state.getGroupId() + "'");
		}
		if (state.getRoomId() != null) {
			sql.append("and room_id = '" + state.getRoomId() + "'");
		}

		EntityManager manager2 = manager.getEntityManagerFactory().createEntityManager();
		manager2.getTransaction().begin();
		Query query = manager2.createNativeQuery(sql.toString());
		int result = query.executeUpdate();
		manager2.getTransaction().commit();

		/**
		Query query = manager.createNamedQuery(sql.toString());
		int result = query.executeUpdate();
		**/
		return result;
	}
}
