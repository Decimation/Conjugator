package com.deci.conj;

import lombok.Getter;
import lombok.Setter;

class VerbInfo {
	@Getter
	private final VerbType type;

	private final boolean isReflexive;

	@Getter
	@Setter
	private String futureStem;

	@Getter
	@Setter
	private String preteriteStem;

	@Getter
	@Setter
	private String subjunctiveStem;


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
		final int align = 30 + 12;

		sb.appendLineBoldAlign("Type: ",align, type);
		sb.appendLineBoldAlign("Reflexive: ", align, isReflexive ? "yes" : "no");
		sb.appendLineBoldAlign("Future stem: ", align, futureStem);
		sb.appendLineBoldAlign("Preterite stem: ", align, preteriteStem);
		sb.appendLineBoldAlign("Subjunctive stem: ", align, subjunctiveStem);

		return sb.toString();
	}
}
