package com.deci.conj;

public enum TenseType {
	PRESENT, FUTURE, IMPERFECT, PRETERITE, CONDITIONAL,
	PRESENT_PERFECT, FUTURE_PERFECT, PLUPERFECT, PRETERITE_PERFECT,
	CONDITIONAL_PERFECT;



	@Override
	public String toString() {
		switch (this) {
			case PRESENT:
				return "Presente";
			case FUTURE:
				return "Futuro";
			case IMPERFECT:
				return "Imperfecto";
			case PRETERITE:
				return "Pretérito";
			case CONDITIONAL:
				return "Condicional";
			case PRESENT_PERFECT:
				return "Presente perfecto";
			case FUTURE_PERFECT:
				return "Futuro perfecto";
			case PLUPERFECT:
				return "Pluscuamperfecto";
			case PRETERITE_PERFECT:
				return "Pretérito anterior";
			case CONDITIONAL_PERFECT:
				return "Condicional perfecto";
		}
		return Common.ERROR;
	}


}
