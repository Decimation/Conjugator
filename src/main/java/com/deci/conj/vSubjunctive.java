package com.deci.conj;

import java.util.Arrays;

class vSubjunctive extends Mood {


	vSubjunctive() {
		super(6, MoodType.SUBJUNCTIVE);
		TenseType[] tenseTypes = {TenseType.PRESENT, TenseType.IMPERFECT, TenseType.FUTURE,
				TenseType.PRESENT_PERFECT, TenseType.FUTURE_PERFECT, TenseType.PLUPERFECT};
		for (int i = 0; i < getSize(); i++)
			setTense(i, new Tense(tenseTypes[i]));
	}

	@Override
	void load(String infinitive) {
		super.loadInternal(infinitive);
	}

	@Override
	public String toString() {
		return super.toStringInternal("Subjunctive");
	}
}
