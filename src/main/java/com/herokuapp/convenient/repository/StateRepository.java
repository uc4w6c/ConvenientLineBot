package com.herokuapp.convenient.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.herokuapp.convenient.domain.State;

public interface StateRepository  extends JpaRepository<State, Integer> {
}
