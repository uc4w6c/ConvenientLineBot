package com.herokuapp.convenient.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.herokuapp.convenient.domain.State;
import com.herokuapp.convenient.domain.Task;

@Repository
public interface StateRepository extends JpaRepository<State, Integer> {
}
