package com.deci.conj;

import java.util.Arrays;

public class vSubjunctive {
	private final Tense[] m_tenses;
	private static final MoodType mood = MoodType.SUBJUNCTIVE;
	private static final byte SIZE = 6;

	vSubjunctive() {
		m_tenses = new Tense[SIZE];
		TenseType[] tenseTypes = (TenseType[]) Arrays.asList(TenseType.PRESENT, TenseType.IMPERFECT, TenseType.FUTURE,
				TenseType.PRESENT_PERFECT, TenseType.FUTURE_PERFECT, TenseType.PLUPERFECT).toArray();
		for (int i = 0; i < SIZE; i++)
			m_tenses[i] = new Tense(tenseTypes[i]);
	}

	void load(String infinitive) {
		for (Tense tense : m_tenses) {
			tense.load(infinitive, mood);
		}
	}

	@Override
	public String toString() {
		AuxStringBuffer sb = new AuxStringBuffer();
		sb.appendLine("Subjunctive").appendLine();
		for (Tense tense: m_tenses) {
			sb.appendLine(tense);
		}
		return sb.toString();
	}
}
