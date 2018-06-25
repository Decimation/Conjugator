package com.deci.conj;

public enum Pronoun {
	YO, TU, EL, NOSOTROS, VOSOTROS, ELLOS;

	static Pronoun[] toArray() {
		return new Pronoun[]{YO, TU, EL, NOSOTROS, VOSOTROS, ELLOS};
	}

	@Override
	public String toString() {
		switch (this) {
			case YO:
				return "form_1s";
			case TU:
				return "form_2s";
			case EL:
				return "form_3s";
			case NOSOTROS:
				return "form_1p";
			case VOSOTROS:
				return "form_2p";
			case ELLOS:
				return "form_3p";
		}
		return "ERROR";
	}
}
