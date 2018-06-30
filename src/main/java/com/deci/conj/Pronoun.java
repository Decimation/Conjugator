package com.deci.conj;

import com.deci.razorcommon.Common;
import com.deci.razorcommon.RazorStringBuffer;

public enum Pronoun {
	YO, TU, EL, NOSOTROS, VOSOTROS, ELLOS;


	static String[] toTableStringArray() {
		final Pronoun[] pronouns = values();
		String[] arr = new String[pronouns.length];
		for (int i = 0; i < arr.length; i++)
			arr[i] = pronouns[i].toString();
		return arr;
	}

	static String aggregate(Pronoun[] pronouns) {
		RazorStringBuffer sb = new RazorStringBuffer();
		String[] arr = Common.toStringArray(pronouns);
		String[] strippedBold = new String[arr.length];
		for (int x = 0; x < arr.length; x++) {
			strippedBold[x] = Common.unbold(arr[x]);
		}

		for (int i = 0; i < pronouns.length; i++) {
			String unfmt = strippedBold[i].substring(0, strippedBold[i].length() - 1);
			if (i + 1 != pronouns.length) {


				sb.append("%s, ", Common.boldStr(unfmt));

			} else {
				sb.append("%s", Common.boldStr(unfmt));

			}
		}

		return sb.toString();
	}

	String toSQLString() {
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
		return Common.ERROR;
	}

	@Override
	public String toString() {
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
		return Common.ERROR;
	}
}
