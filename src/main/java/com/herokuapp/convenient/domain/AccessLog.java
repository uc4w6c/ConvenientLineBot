package com.herokuapp.convenient.domain;

import java.time.LocalDateTime;
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


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Entity
@Table(name = "access_log")
@Getter
@AllArgsConstructor
public class AccessLog {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private LocalDateTime requestAt;

	@Column(nullable = false)
	private LocalDateTime responseAt;

	private String sessionId;

	private String controllerName;

	private String methodName;

	private String arguments;

	private String httpMethod;

	private String requestUri;

	private String responseStatus;

	@Column(nullable = false)
	private int aroundMicrosecond;
}
