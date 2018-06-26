package com.deci.conj;

public enum Pronoun {
	YO, TU, EL, NOSOTROS, VOSOTROS, ELLOS;

	static String[] toTableStringArray() {
		final Pronoun[] pronouns = values();
		String[] arr = new String[pronouns.length];
		for (int i = 0; i < arr.length; i++)
			arr[i] = pronouns[i].toTableString();
		return arr;
	}

	String toTableString() {
		switch (this) {
			case YO:
				return Common.boldStr("yo:");
			case TU:
				return Common.boldStr("tú:");
			case EL:
				return Common.boldStr("el, ella, usted:");
			case NOSOTROS:
				return Common.boldStr("nosotros:");
			case VOSOTROS:
				return Common.boldStr("vosotros:");
			case ELLOS:
				return Common.boldStr("ellos, ellas, ustedes:");
		}
		return "ERROR";
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
