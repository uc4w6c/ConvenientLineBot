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
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.herokuapp.convenient.service.consts.SourceType;
import com.herokuapp.convenient.service.consts.StateKind;
import com.herokuapp.convenient.service.consts.StatusKind;
import com.linecorp.bot.model.event.source.GroupSource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString // テスト用に追加
@Entity
@Table(name = "states")
@Getter
@AllArgsConstructor
//@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AccessLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private int sourceType;

	@Column(nullable = false)
	private String userId;

	private String groupId;

	private String roomId;

	private int stateKind;

	private int status;

	@CreatedDate
	@LastModifiedDate
	private LocalTime updatedAt;

	public static class Builder {
		private Integer id;
		private int sourceType;
		private String userId;
		private String groupId;
		private String roomId;
		private int stateKind;
		private int status;
		private LocalTime updatedAt;

		/**
		 * 
		 * @param sourceType
		 * @param userId
		 * @param stateKind
		 * @param status
		 */
		public Builder(int sourceType, String userId, int stateKind, int status) {
			this.sourceType = sourceType;
			this.userId = userId;
			this.stateKind = stateKind;
			this.status = status;
		}

		public Builder id(Integer id) {
			this.id = id;
			return this;
		}

		public Builder groupId(String groupId) {
			this.groupId = groupId;
			return this;
		}
		public Builder roomId(String roomId) {
			this.roomId = roomId;
			return this;
		}
		public Builder updatedAt(LocalTime updatedAt) {
			this.updatedAt = updatedAt;
			return this;
		}

		public AccessLog build() {
			if (sourceType == 0 || 
				userId == null || 
				stateKind == 0 || 
				status == 0) {

				throw new NullPointerException();
			}
			return new AccessLog(this);
		}
	}

	// privateコンテキストにしてみた。データ取得時にセット出来ないかも。
	private AccessLog() {}

	private AccessLog(Builder builder) {
		this.id = builder.id;
		this.sourceType = builder.sourceType;
		this.userId = builder.userId;
		this.groupId = builder.groupId;
		this.roomId = builder.roomId;
		this.stateKind = builder.stateKind;
		this.status = builder.status;
		this.updatedAt = builder.updatedAt;
	}

	public AccessLog changeStatus(int status) {
		return new AccessLog.Builder(this.sourceType, this.userId, this.stateKind, status)
						.id(this.id)
						.groupId(this.groupId)
						.roomId(this.roomId)
						.build();
	}

	public AccessLog changeKind(int kind, int status) {
		return new AccessLog.Builder(this.sourceType, this.userId, kind, status)
						.id(this.id)
						.groupId(this.groupId)
						.roomId(this.roomId)
						.build();
	}
}
