package com.herokuapp.convenient.service.consts;

public enum StatusKind {
	ACCEPTING (1),   // 受付中状態
	WAITING (2);     // 受付待ち状態

	private final int value;

	StatusKind(int value) {
		this.value = value;
	}

	public int value() { return value; }
}
