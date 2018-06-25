package com.deci.conj;

import java.util.Arrays;

public class vIndicative {
	private Tense[] m_tenses;
	private static final MoodType mood = MoodType.INDICATIVE;
	private static final byte SIZE = 10;

	vIndicative() {
		m_tenses = new Tense[SIZE];

		TenseType[] tenseTypes = (TenseType[]) Arrays.asList(TenseType.PRESENT, TenseType.FUTURE, TenseType.IMPERFECT, TenseType.PRETERITE,
				TenseType.CONDITIONAL, TenseType.PRESENT_PERFECT, TenseType.FUTURE_PERFECT, TenseType.PLUPERFECT,
				TenseType.PRETERITE_PERFECT, TenseType.CONDITIONAL_PERFECT).toArray();
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
		for (Tense tense: m_tenses) {
			sb.appendLine(tense);
		}
		return sb.toString();
	}
}
