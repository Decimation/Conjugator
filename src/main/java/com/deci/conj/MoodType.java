package com.deci.conj;

import lombok.SneakyThrows;

public enum MoodType {
	INDICATIVE, SUBJUNCTIVE, IMP_AFFIRMATIVE, IMP_NEGATIVE;


	@SneakyThrows
	@Override
	public String toString() {
		switch (this) {
			case INDICATIVE:
				return "Indicativo";
			case IMP_AFFIRMATIVE:
				return "Imperativo Afirmativo";
			case IMP_NEGATIVE:
				return "Imperativo Negativo";
			case SUBJUNCTIVE:
				return "Subjuntivo";
		}
		return "ERROR";
	}
}
