package com.herokuapp.convenient.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.herokuapp.convenient.domain.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

	@Query("SELECT todo_text FROM tasks "
			+ "WHERE source_type = :source_type"
			+ " and user_id = :user_id"
			+ " and group_id = :group_id"
			+ " and room_id = :room_id")
	List<String> findAllOrderByCreatedAt(@Param("source_type") int sourceType,
										@Param("user_id") String userId,
										@Param("group_id") String groupId,
										@Param("room_id") String roomId);

}
