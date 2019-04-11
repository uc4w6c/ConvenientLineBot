package com.herokuapp.convenient.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.herokuapp.convenient.domain.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

	@Query(value = "SELECT task FROM Task task "
					+ "WHERE task.sourceType = :sourceType"
					+ " and task.userId = :userId"
					+ " and task.groupId = :groupId"
					+ " and task.roomId = :roomId")
	List<Task> findAllOrderByCreatedAt(@Param("sourceType") int sourceType,
										@Param("userId") String userId,
										@Param("groupId") String groupId,
										@Param("roomId") String roomId);

}
