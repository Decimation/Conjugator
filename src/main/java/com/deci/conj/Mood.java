package com.deci.conj;

import lombok.Getter;

abstract class Mood {

	private static final String   ERROR = "ERROR";
	private final        Tense[]  m_tenses;
	@Getter
	private final        int      size;
	private final        MoodType mood;

	Mood(int size) {
		this(size, null);
	}

	Mood(int size, MoodType mood) {
		this.size = size;
		m_tenses = new Tense[size];
		this.mood = mood;
	}

	final String getConjugation(Pronoun p, TenseType tense) {
		for (Tense t : m_tenses) {
			if (t.getType() == tense) {
				return t.getTable().get(p);
			}
		}
		return ERROR;
	}

	abstract void load(String infinitive);
	//abstract String getConjugation(Pronoun p, MoodType mood, TenseType tense);

	final void loadInternal(String infinitive) {
		for (Tense tense : m_tenses) {
			tense.load(infinitive, mood);
		}
	}

	final void setTense(int index, Tense tense) {
		m_tenses[index] = tense;
	}

	final Tense getTense(int index) {
		return m_tenses[index];
	}

	final String toStringInternal(String tag) {
		AuxStringBuffer sb = new AuxStringBuffer();
		sb.appendLine(tag).appendLine();
		for (Tense tense : m_tenses) {
			sb.appendLine(tense);
		}
		return sb.toString();
	}
}
