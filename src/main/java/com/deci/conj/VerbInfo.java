package com.deci.conj;

import com.deci.razorcommon.Common;
import com.deci.razorcommon.RazorStringBuffer;
import lombok.Getter;
import lombok.Setter;

public class VerbInfo {

	@Getter
	private final VerbType type;

	private final boolean isReflexive;

	@Setter
	private String futureConditionalStem;


	@Setter
	private String preteriteStem;


	@Setter
	private String subjunctiveStem;

	@Getter
	@Setter
	private StemChange stemChange;

	@Getter
	@Setter
	private StemChange preteriteOrthographicChange;

	@Getter
	@Setter
	private StemChange presentYoChange;

	public VerbInfo(VerbType type, boolean isReflexive) {
		this.type = type;
		this.isReflexive = isReflexive;
	}

	public String getFutureConditionalStem() {
		return futureConditionalStem + '-';
	}

	public String getPreteriteStem() {
		return preteriteStem + '-';
	}

	public String getSubjunctiveStem() {
		return subjunctiveStem + '-';
	}

	public boolean isReflexive() {
		return isReflexive;
	}


	@Override
	public String toString() {
		RazorStringBuffer sb = new RazorStringBuffer();
		//sb.appendLine(Common.align(Common.boldStr("Type: %-15s"), 12 + 30), type);
		//sb.appendLine(Common.align(Common.boldStr("Reflexive: %-15s"), 12 + 30),isReflexive ? "yes" : "no", type);


		sb.appendLineBoldAlign("Type: ", Common.ALIGN, type);
		sb.appendLineBoldAlign("Reflexive: ", Common.ALIGN, isReflexive ? "yes" : "no");
		sb.appendLineBoldAlign("Future/Conditional stem: ", Common.ALIGN, getFutureConditionalStem());
		sb.appendLineBoldAlign("Preterite stem: ", Common.ALIGN, getPreteriteStem());
		sb.appendLineBoldAlign("Subjunctive stem: ", Common.ALIGN, getSubjunctiveStem());
		sb.appendLineBoldAlign("Stem change: ", Common.ALIGN, stemChange == null ? "-" : stemChange);
		sb.appendLineBoldAlign("Preterite orthogfx change: ", Common.ALIGN, preteriteOrthographicChange == null ? "-" : preteriteOrthographicChange);
		sb.appendLineBoldAlign("Present yo: ", Common.ALIGN, presentYoChange == null ? "-" : presentYoChange);
		return sb.toString();
	}
}
