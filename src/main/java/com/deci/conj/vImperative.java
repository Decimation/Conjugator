package com.deci.conj;

import java.util.Arrays;

public class vImperative {
	private Tense[] m_tenses;
	private static final byte SIZE = 2;

	vImperative() {
		m_tenses = new Tense[SIZE];
		TenseType[] tenseTypes = (TenseType[]) Arrays.asList(TenseType.PRESENT, TenseType.PRESENT).toArray();
		for (int i = 0; i < SIZE; i++)
			m_tenses[i] = new Tense(tenseTypes[i]);

	}

	void load(String infinitive) {
		m_tenses[0].load(infinitive, MoodType.IMP_AFFIRMATIVE);
		m_tenses[1].load(infinitive, MoodType.IMP_NEGATIVE);
	}

	@Override
	public String toString() {
		AuxStringBuffer sb = new AuxStringBuffer();
		sb.appendLine("Imperative").appendLine();
		for (Tense tense: m_tenses) {
			sb.appendLine(tense);
		}
		return sb.toString();
	}
}
