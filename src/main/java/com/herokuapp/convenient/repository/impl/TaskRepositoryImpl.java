package com.herokuapp.convenient.repository.impl;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;

import com.herokuapp.convenient.domain.State;
import com.herokuapp.convenient.domain.Task;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import com.herokuapp.convenient.repository.StateRepository;
import com.herokuapp.convenient.repository.StateRepositoryCustom;
import com.herokuapp.convenient.repository.TaskRepository;
import com.herokuapp.convenient.repository.TaskRepositoryCustom;
import com.herokuapp.convenient.service.consts.CodeEnum;
import com.herokuapp.convenient.service.consts.SourceType;
import com.herokuapp.convenient.service.consts.StateKind;
import com.herokuapp.convenient.service.consts.StatusKind;

@Component
public class TaskRepositoryImpl implements TaskRepositoryCustom {
	@Autowired
	private TaskRepository repository;

	@Autowired
	private EntityManager manager;

	private final String SELECT_TASK = "SELECT * FROM tasks "
								+ "WHERE source_type = :type and "
								+ "user_id = :userId";

	public List<Task> fetchState(State state) {
		int type = state.getSourceType();
		Query query;

		if (type == SourceType.USER.getCode()) {
			query = manager.createNativeQuery(SELECT_TASK, Task.class)
						.setParameter("type", state.getSourceType())
						.setParameter("userId", state.getUserId());

		} else if (type == SourceType.GROUP.getCode()) {
			query = manager.createNativeQuery
					(SELECT_TASK + " and group_id = :groupId", Task.class)
					.setParameter("type", state.getSourceType())
					.setParameter("userId", state.getUserId())
					.setParameter("groupId", state.getGroupId());

		} else if (type == SourceType.ROOM.getCode()) {
			query = manager.createNativeQuery
					(SELECT_TASK + " and room_id = :roomId", Task.class)
					.setParameter("type", state.getSourceType())
					.setParameter("userId", state.getUserId())
					.setParameter("roomId", state.getRoomId());

		} else {
			throw new IllegalArgumentException("SourceTypeに設定の値が想定外の値です。");
		}

		return query.getResultList();
	}
}
