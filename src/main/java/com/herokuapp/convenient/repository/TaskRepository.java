package com.herokuapp.convenient.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.herokuapp.convenient.domain.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

	//@Query("SELECT X FROM Customer X ORDER BY firstName, lastName")
	//List<TodoList> findAllOrderByName();

}
