package com.deci.conj;

import lombok.Getter;
import lombok.Setter;

class VerbInfo {
	@Getter
	private final VerbType type;

	private final boolean isReflexive;

	@Getter
	@Setter
	private String futureConditionalStem;

	@Getter
	@Setter
	private String preteriteStem;

	@Getter
	@Setter
	private String subjunctiveStem;

	@Getter
	@Setter
	private StemChange stemChange;

	@Getter
	@Setter
	private StemChange preteriteOrthographicChange;


	VerbInfo(VerbType type, boolean isReflexive) {
		this.type = type;
		this.isReflexive = isReflexive;
	}

	boolean isReflexive() {
		return isReflexive;
	}

	@Override
	public String toString() {
		AuxStringBuffer sb = new AuxStringBuffer();
		//sb.appendLine(Common.align(Common.boldStr("Type: %-15s"), 12 + 30), type);
		//sb.appendLine(Common.align(Common.boldStr("Reflexive: %-15s"), 12 + 30),isReflexive ? "yes" : "no", type);


		sb.appendLineBoldAlign("Type: ", Common.ALIGN, type);
		sb.appendLineBoldAlign("Reflexive: ", Common.ALIGN, isReflexive ? "yes" : "no");
		sb.appendLineBoldAlign("Future/Conditional stem: ", Common.ALIGN, futureConditionalStem);
		sb.appendLineBoldAlign("Preterite stem: ", Common.ALIGN, preteriteStem);
		sb.appendLineBoldAlign("Subjunctive stem: ", Common.ALIGN, subjunctiveStem);
		sb.appendLineBoldAlign("Stem change: ", Common.ALIGN, stemChange == null ? "-" : stemChange);
		sb.appendLineBoldAlign("Preterite orthogfx change: ", Common.ALIGN, preteriteOrthographicChange == null ? "-" : preteriteOrthographicChange);

		return sb.toString();
	}
}
