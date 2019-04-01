package com.herokuapp.convenient.domain;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.herokuapp.convenient.domain.State.Builder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tasks")
@Getter
//@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Task {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private int sourceType;

	@Column(nullable = false)
	private String userId;

	private String groupId;

	private String roomId;

	@Column(nullable = false)
	private String todoText;

	@CreatedDate
	private LocalTime createdAt;

	private LocalTime deletedAt;

	public static class Builder {
		private int sourceType;
		private String userId;
		private String groupId;
		private String roomId;
		private String todoText;
		private LocalTime createdAt;
		private LocalTime deletedAt;

		/**
		 * 
		 * @param sourceType
		 * @param userId
		 * @param stateKind
		 * @param status
		 */
		public Builder(int sourceType, String userId, String todoText) {
			this.sourceType = sourceType;
			this.userId = userId;
			this.todoText = todoText;
		}

		public Builder groupId(String groupId) {
			this.groupId = groupId;
			return this;
		}
		public Builder roomId(String roomId) {
			this.roomId = roomId;
			return this;
		}
		public Builder createdAt(LocalTime createdAt) {
			this.createdAt = createdAt;
			return this;
		}
		public Builder deletedAt(LocalTime deletedAt) {
			this.deletedAt = deletedAt;
			return this;
		}

		public Task build() {
			if (sourceType == 0 || 
				userId == null || 
				todoText == null) {

				throw new NullPointerException();
			}
			return new Task(this);
		}
	}

	private Task() { }
	
	private Task(Builder builder) {
		this.sourceType = builder.sourceType;
		this.userId = builder.userId;
		this.groupId = builder.groupId;
		this.roomId = builder.roomId;
		this.todoText = builder.todoText;
		this.createdAt = builder.createdAt;
		this.deletedAt = builder.deletedAt;
	}
}
