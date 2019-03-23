package com.herokuapp.convenient.service.consts;

public enum SourceType implements CodeEnum<SourceType> {
	USER ("user", 1),
	GROUP ("group", 2),
	ROOM ("room", 3);

	private final String name;
	private final int code;

	SourceType(String name, int code) {
		this.name = name;
		this.code = code;
	}

	/**
	 * @return コード値
	 */
	@Override
	public String getName() {
		return name;
	}


	/**
	 * @return 値
	 */
	@Override
	public int getCode() {
		return code;
	}
}
