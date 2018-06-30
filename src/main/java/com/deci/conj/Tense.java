package com.deci.conj;

import com.deci.razorcommon.RazorStringBuffer;
import lombok.Getter;
import lombok.SneakyThrows;

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
		RazorStringBuffer sb = new RazorStringBuffer();
		sb.append("\t\t\t\t");
		sb.appendLine(type);
		sb.appendLine(table);
		return sb.toString();
	}
}
