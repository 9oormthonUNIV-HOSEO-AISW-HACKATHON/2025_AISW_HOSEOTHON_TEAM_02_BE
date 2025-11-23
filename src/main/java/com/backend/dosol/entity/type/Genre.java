package com.backend.dosol.entity.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;

public enum Genre {
	EMOTIONAL("emo"),
	DANCE("dance"),
	HIPHOP("hip"),
	VOCAL("vocal");

	private final String value;

	Genre(String value) {
		this.value = value;
	}

	@JsonCreator
	public static Genre fromString(String symbol) {
		if (symbol == null) {
			return null;
		}
		// Handle aliases from JSON keys
		if ("emo".equalsIgnoreCase(symbol)) {
			return EMOTIONAL;
		}
		if ("hip".equalsIgnoreCase(symbol)) {
			return HIPHOP;
		}
		return Stream.of(Genre.values())
			.filter(g -> g.value.equalsIgnoreCase(symbol) || g.name().equalsIgnoreCase(symbol))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("Unsupported genre: " + symbol));
	}

	@JsonValue
	public String getValue() {
		return value;
	}
}