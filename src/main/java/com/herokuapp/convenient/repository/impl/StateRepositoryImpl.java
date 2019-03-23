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
	EntityManager manager;
	
	public List<State> findBySpec(String prop) {
		//final EntityManager em = context.GetEntityManagerByManagedType(State.class);

		String sql = "";
		Query query = manager.createQuery(sql.toString());
		return query.getResultList();
	}
}
