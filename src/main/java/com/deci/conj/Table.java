package com.deci.conj;

import lombok.SneakyThrows;

import java.sql.ResultSet;

public class Table {

	private static final byte     SIZE = 3;
	private              String[] m_singular;
	private              String[] m_plural;

	Table() {
		m_singular = new String[SIZE];
		m_plural = new String[SIZE];
	}

	@SneakyThrows
	void load(String infinitive, MoodType mood, TenseType tense) {
		String query = String.format("select * from verbs where infinitive = '%s' and mood = '%s' and tense = '%s'", infinitive, mood, tense);
		ResultSet rs = Database.getStatement().executeQuery(query);


		String[] buffer = new String[SIZE * 2];
		Pronoun[] pronouns = Pronoun.toArray();
		while (rs.next()) {
			for (int i = 0; i < pronouns.length; i++) {
				buffer[i] = rs.getString(pronouns[i].toString());
			}
		}
		System.arraycopy(buffer, 0, m_singular, 0, SIZE);
		System.arraycopy(buffer, 3, m_plural, 0, SIZE);

	}

	void set(Pronoun p, String s) {
		if (p.ordinal() <= 2) {
			m_singular[p.ordinal()] = s;
		} else {
			m_plural[p.ordinal() % SIZE] = s;
		}
	}

	String get(Pronoun p) {
		switch (p) {
			case YO:
				return m_singular[0];
			case TU:
				return m_singular[1];
			case EL:
				return m_singular[2];
			case NOSOTROS:
				return m_plural[0];
			case VOSOTROS:
				return m_plural[1];
			case ELLOS:
				return m_plural[2];
		}
		return null;
	}

	@Override
	public String toString() {
		AuxStringBuffer sb = new AuxStringBuffer();

		sb.appendLine("yo: %s", get(Pronoun.YO));
		sb.appendLine("tú: %s", get(Pronoun.TU));
		sb.appendLine("el, ella, usted: %s", get(Pronoun.EL));
		sb.appendLine("nosotros: %s", get(Pronoun.NOSOTROS));
		sb.appendLine("vosotros: %s", get(Pronoun.VOSOTROS));
		sb.appendLine("ellos, ellas, ustedes: %s", get(Pronoun.ELLOS));
		return sb.toString();
	}
}
