package com.deci.conj;

import java.util.Arrays;

class vIndicative extends Mood {

	vIndicative() {
		super(10, MoodType.INDICATIVE);

		TenseType[] tenseTypes = {TenseType.PRESENT, TenseType.FUTURE, TenseType.IMPERFECT, TenseType.PRETERITE,
				TenseType.CONDITIONAL, TenseType.PRESENT_PERFECT, TenseType.FUTURE_PERFECT, TenseType.PLUPERFECT,
				TenseType.PRETERITE_PERFECT, TenseType.CONDITIONAL_PERFECT};
		for (int i = 0; i < getSize(); i++)
			setTense(i, new Tense(tenseTypes[i]));
	}

	@Override
	void load(String infinitive) {
		super.loadInternal(infinitive);
	}

	@Override
	public String toString() {
		return super.toStringInternal("Indicative");
	}
}
