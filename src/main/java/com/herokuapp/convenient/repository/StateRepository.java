package com.herokuapp.convenient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.herokuapp.convenient.domain.State;

@Repository
public interface StateRepository  extends JpaRepository<State, Integer> {
}
