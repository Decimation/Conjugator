package com.deci.conj;

import lombok.Getter;
import lombok.SneakyThrows;

import java.sql.ResultSet;

public class Tense {

	@Getter
	private final TenseType type;

	@Getter
	private final Table table;

	Tense(TenseType type) {
		this.type = type;
		this.table = new Table();
	}

	@SneakyThrows
	void load(String infinitive, MoodType mood) {
		table.load(infinitive, mood, type);
	}

	@Override
	public String toString() {
		AuxStringBuffer sb = new AuxStringBuffer();
		sb.appendLine(type);
		sb.appendLine(table);
		return sb.toString();
	}
}
