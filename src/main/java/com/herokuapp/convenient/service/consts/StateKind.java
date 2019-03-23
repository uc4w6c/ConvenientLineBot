package com.herokuapp.convenient.service.consts;

public enum StateKind {
	TASK (1); // お買い物リスト

	private final int value;

	StateKind(int value) {
		this.value = value;
	}

	public int value() { return value; }
}
