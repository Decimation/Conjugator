package com.deci.conj;

class StemChangesInfo {
	static final String     CAR               = "car";
	static final String     GAR               = "gar";
	static final String     ZAR               = "zar";
	static final String     GUAR              = "guar";
	static final String[]   I_Y_MORPHEMES     = {"caer", "eer", "oer", "oír", "uir"};
	static final StemChange PRETERITE_YO_CAR  = new StemChange("c", "qu", Pronoun.YO);
	static final StemChange PRETERITE_YO_GAR  = new StemChange("g", "gu", Pronoun.YO);
	static final StemChange PRETERITE_YO_ZAR  = new StemChange("z", "c", Pronoun.YO);
	static final StemChange PRETERITE_YO_GUAR = new StemChange("gu", "gü", Pronoun.YO);
	static final StemChange PRETERITE_TP_O_U  = new StemChange("o", "u", Pronoun.EL, Pronoun.ELLOS);
	static final StemChange PRETERITE_TP_E_I  = new StemChange("e", "i", Pronoun.EL, Pronoun.ELLOS);
	static final StemChange PRETERITE_TP_I_Y  = new StemChange("i", "y", Pronoun.EL, Pronoun.ELLOS);
	static final StemChange PRESENT_O_UE      = new StemChange("o", "ue");
	static final StemChange PRESENT_U_UE      = new StemChange("u", "ue");
	static final StemChange PRESENT_I_IE      = new StemChange("i", "ie");
	static final StemChange PRESENT_E_I       = new StemChange("e", "i");
	static final StemChange PRESENT_E_IE      = new StemChange("e", "ie");
	// Unique cases
	static final StemChange PRESENT_I_ACC_I   = new StemChange("i", "í");
	static final StemChange PRESENT_O_HUE     = new StemChange("o", "hue");

	private StemChangesInfo() {
	}
}
