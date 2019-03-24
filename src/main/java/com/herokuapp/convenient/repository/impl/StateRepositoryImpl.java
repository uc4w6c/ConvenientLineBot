package com.herokuapp.convenient.repository.impl;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaContext;

import com.herokuapp.convenient.domain.State;
import com.herokuapp.convenient.repository.StateRepository;
import com.herokuapp.convenient.repository.StateRepositoryCustom;

public class StateRepositoryImpl implements StateRepositoryCustom {
	@Autowired
	private StateRepository repository;

	@Autowired
	private EntityManager manager;
	
	public int findBySpec(String prop) {
		//final EntityManager em = context.GetEntityManagerByManagedType(State.class);

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT g From Goods g WHERE ");
		Query query = manager.createQuery(sql.toString());
		List<State> states = query.getResultList();
		return states.size();
	}

	public State fetchState(String userId) {
		//final EntityManager em = context.GetEntityManagerByManagedType(State.class);

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT g From Goods g WHERE ");
		Query query = manager.createQuery(sql.toString());
		List<State> states = query.getResultList();
		return states.get(0);
	}

	public State fetchState(String userId, String type, String keyId) {
		//final EntityManager em = context.GetEntityManagerByManagedType(State.class);

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT g From Goods g WHERE ");
		Query query = manager.createQuery(sql.toString());
		List<State> states = query.getResultList();
		return states.get(0);
	}
}
