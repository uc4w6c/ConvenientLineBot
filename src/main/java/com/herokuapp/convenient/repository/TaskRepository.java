package com.herokuapp.convenient.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.herokuapp.convenient.domain.Task;


public interface TaskRepository extends JpaRepository<Task, Integer> {

	//@Query("SELECT X FROM Customer X ORDER BY firstName, lastName")
	//List<TodoList> findAllOrderByName();

}
