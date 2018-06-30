package com.deci.conj;

final class StemChangesInfo {
	//region Fields
	static final String     CAR               = "car";
	static final String     GAR               = "gar";
	static final String     ZAR               = "zar";
	static final String     GUAR              = "guar";
	static final String[]   I_Y_MORPHEMES     = {"caer", "eer", "oer", "oír", "uir"};
	static final StemChange PRETERITE_YO_CAR  = new StemChange("c", "qu", TenseType.PRETERITE, Pronoun.YO);
	static final StemChange PRETERITE_YO_GAR  = new StemChange("g", "gu", TenseType.PRETERITE, Pronoun.YO);
	static final StemChange PRETERITE_YO_ZAR  = new StemChange("z", "c", TenseType.PRETERITE, Pronoun.YO);
	static final StemChange PRETERITE_YO_GUAR = new StemChange("gu", "gü", TenseType.PRETERITE, Pronoun.YO);
	static final StemChange PRETERITE_TP_O_U  = new StemChange("o", "u", TenseType.PRETERITE, Pronoun.EL, Pronoun.ELLOS);
	static final StemChange PRETERITE_TP_E_I  = new StemChange("e", "i", TenseType.PRETERITE, Pronoun.EL, Pronoun.ELLOS);
	static final StemChange PRETERITE_TP_I_Y  = new StemChange("i", "y", TenseType.PRETERITE, Pronoun.EL, Pronoun.ELLOS);
	static final StemChange PRESENT_O_UE      = new StemChange("o", "ue");
	static final StemChange PRESENT_U_UE      = new StemChange("u", "ue");
	static final StemChange PRESENT_I_IE      = new StemChange("i", "ie");
	static final StemChange PRESENT_E_I       = new StemChange("e", "i");
	static final StemChange PRESENT_E_IE      = new StemChange("e", "ie");
	//static final StemChange PRESENT_GU_G = new StemChange("gu", "g");
	// Unique cases
	static final StemChange PRESENT_I_ACC_I   = new StemChange("i", "í");
	static final StemChange PRESENT_O_HUE     = new StemChange("o", "hue");
	static final String     CER               = "cer";

	// Yo changes
	static final String     CIR                = "cir";
	static final StemChange PRESENT_YO_CIR_CER = new StemChange("c", "zc", TenseType.PRESENT, Pronoun.YO);
	static final String     GER                = "ger";
	static final String     GIR                = "gir";
	static final StemChange PRESENT_YO_GIR_GER = new StemChange("g", "j", TenseType.PRESENT, Pronoun.YO);
	static final String     UIR                = "uir";
	static final StemChange PRESENT_YO_UIR     = new StemChange("yo", Pronoun.YO);
	// *
	static final String     IAR                = "iar";
	static final StemChange PRESENT_YO_ACC_I_O = new StemChange("ío", Pronoun.YO);
	// No association (dar, estar...)
	static final StemChange PRESENT_YO_OY      = new StemChange("oy", Pronoun.YO);
	// No association (venir, tener, poner...)
	static final StemChange PRESENT_YO_GO      = new StemChange("go", Pronoun.YO);
	// No association (traer...)
	static final StemChange PRESENT_YO_IGO     = new StemChange("igo", Pronoun.YO);
	// No association (ejercer...)
	static final StemChange PRESENT_YO_Z       = new StemChange("c", "z", TenseType.PRESENT, Pronoun.YO);
	//endregion

	private StemChangesInfo() {
	}

	static StemChange[] getPresentYoChange() {
		return new StemChange[]{PRESENT_YO_CIR_CER, PRESENT_YO_GIR_GER, PRESENT_YO_UIR, PRESENT_YO_ACC_I_O, PRESENT_YO_OY,
								PRESENT_YO_GO, PRESENT_YO_IGO, PRESENT_YO_Z};
	}

	static StemChange[] getPresentStemChanges() {
		return new StemChange[]{PRESENT_I_IE, PRESENT_E_IE, PRESENT_E_I, PRESENT_O_UE, PRESENT_U_UE,/*PRESENT_GU_G*/
								PRESENT_I_ACC_I, PRESENT_O_HUE,};
	}
}
