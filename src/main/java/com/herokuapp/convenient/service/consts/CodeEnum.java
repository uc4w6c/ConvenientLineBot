package com.herokuapp.convenient.service.consts;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// 参考:https://www.casleyconsulting.co.jp/blog/engineer/254/
public interface CodeEnum<E extends Enum<E>> {
	public String getName();
	public int getCode();

	/**　[デフォルト実装] 表示の順番を返却する　*/
	default int getOrder() {
		return getCode();
	}

	@SuppressWarnings("unchecked")
	default E toEnum() {
		return (E) this;
	}

	default boolean equalsByName(String name) {
		return getName().equals(name);
	}
 
 	default boolean equalsByCode(int code) {
		return (getCode() == code);
	}
 
	/**　[staticメソッド] 指定されたCodeEnumを実装したEnumを表示順にソートしたリストを返却する　*/
	static <E extends Enum<E>> List<E> getOrderedList(Class<? extends CodeEnum<E>> clazz) {
		return Arrays.stream(clazz.getEnumConstants())
				.sorted(Comparator.comparing(CodeEnum::getOrder))
				.map(CodeEnum::toEnum)
				.collect(Collectors.toList());
	}
 
	/**　[staticメソッド]　指定されたCodeEnumを実装したEnumの、指定された『コード』値の列挙子を返却する　*/
	static <E extends Enum<E>> E getEnumByCode(Class<? extends CodeEnum<E>> clazz, int code) {
		return Arrays.stream(clazz.getEnumConstants())
				.filter(e -> e.equalsByCode(code))
				.map(CodeEnum::toEnum)
				.findFirst()
				.orElse(null);
	}

	/**　[staticメソッド]　指定されたCodeEnumを実装したEnumの、指定された『Name』値の列挙子を返却する　*/
	static <E extends Enum<E>> E getEnumByName(Class<? extends CodeEnum<E>> clazz, String name) {
		return Arrays.stream(clazz.getEnumConstants())
				.filter(e -> e.equalsByName(name))
				.map(CodeEnum::toEnum)
				.findFirst()
				.orElse(null);
	}

	/**　[staticメソッド]　指定されたCodeEnumのコード値をキー、『コード』値に該当するCodeEnumを値に持つMapを返却する　*/
	static <E extends Enum<E>> Map<Integer, E> getCodeMap(Class<? extends CodeEnum<E>> clazz) {
		return Arrays.stream(clazz.getEnumConstants())
				.collect(Collectors.toMap(CodeEnum::getCode, CodeEnum::toEnum));
	}
 
	/**　[staticメソッド]　指定されたCodeEnumのコード値をキー、『Name』値に該当するCodeEnumを値に持つMapを返却する　*/
	static <E extends Enum<E>> Map<String, E> getNameMap(Class<? extends CodeEnum<E>> clazz) {
		return Arrays.stream(clazz.getEnumConstants())
				.collect(Collectors.toMap(CodeEnum::getName, CodeEnum::toEnum));
	}

	/**　[staticメソッド]　指定されたCodeEnumに、指定された『コード』値を持つ列挙子が存在するかチェックする　*/
	static <E extends Enum<E>> boolean hasCode(Class<? extends CodeEnum<E>> clazz, int code) {
		return Arrays.stream(clazz.getEnumConstants())
				.anyMatch(e -> e.equalsByCode(code));
	}

	/**　[staticメソッド]　指定されたCodeEnumに、指定された『Name』値を持つ列挙子が存在するかチェックする　*/
	static <E extends Enum<E>> boolean hasName(Class<? extends CodeEnum<E>> clazz, String name) {
		return Arrays.stream(clazz.getEnumConstants())
				.anyMatch(e -> e.equalsByName(name));
	}
}
