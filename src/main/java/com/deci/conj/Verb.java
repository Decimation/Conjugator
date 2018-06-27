package com.deci.conj;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

public class Verb {

	@Getter
	private String   infinitive;
	@Getter
	private String   gerund;
	private String   pastParticiple;
	private String   root;
	private VerbInfo info;
	private Mood     indicative;
	private Mood     subjunctive;
	private Mood     imperative;
	private Mood     continuous;

	@SneakyThrows
	Verb(String infinitive) {
		this(infinitive, true);
	}

	@SneakyThrows
	private Verb(String infinitive, boolean enableContinuous) {
		this.infinitive = infinitive;
		ResultSet rs = Database.getStatement().executeQuery(String.format("select * from gerund where infinitive = '%s'", infinitive));
		while (rs.next()) {
			this.gerund = rs.getString("gerund");
		}
		rs = Database.getStatement().executeQuery(String.format("select * from pastparticiple where infinitive = '%s'", infinitive));
		while (rs.next()) {
			this.pastParticiple = rs.getString("pastparticiple");
		}
		this.root = parseRoot();
		this.info = new VerbInfo(parseType(), this.infinitive.endsWith("se"));

		this.indicative = new vIndicative();
		this.indicative.load(infinitive);
		this.subjunctive = new vSubjunctive();
		this.subjunctive.load(infinitive);
		this.imperative = new vImperative();
		this.imperative.load(infinitive);
		if (enableContinuous) {
			this.continuous = new vContinuous();
			this.continuous.load(infinitive);
		} else {
			this.continuous = null;
		}
		this.info.setFutureConditionalStem(parseFutureConditionalStem());
		this.info.setSubjunctiveStem(parseSubjunctiveStem());
		this.info.setStemChange(parseStemChange());
		this.info.setPreteriteOrthographicChange(parsePreteriteOrthographicChange());
		this.info.setPreteriteStem(parsePreteriteStem());
	}

	static Verb getEstar() {
		return new Verb("estar", false);
	}

	private static boolean assertStemChange(String base, StemChange change, String _new) {
		if (base.indexOf(change.getOld()) == _new.indexOf(change.getNew())) {
			assert base.contains(change.getOld()) && _new.contains(change.getNew());
			return true;
		}
		return false;
	}

	private static boolean assertStemChangeAux(String base, StemChange change, String _new) {
		if (base.contains(change.getOld()) && _new.contains(change.getNew())) {
			return base.indexOf(change.getOld()) == _new.indexOf(change.getNew());
		}
		return false;
	}

	String getConjugation(Pronoun p, MoodType mood, TenseType tense) {
		switch (mood) {
			case INDICATIVE:
				return indicative.getConjugation(p, tense);
			case SUBJUNCTIVE:
				return subjunctive.getConjugation(p, tense);
			case IMP_NEGATIVE:
				return ((vImperative) imperative).getConjugation(p, MoodType.IMP_NEGATIVE, tense);
			case IMP_AFFIRMATIVE:
				return ((vImperative) imperative).getConjugation(p, MoodType.IMP_AFFIRMATIVE, tense);
		}
		return Common.ERROR;
	}

	Set<String> findCommonInfinitives() {
		val verbs = Database.getInfinitives();
		Set<String> infinitives = new HashSet<>();
		for (String s : verbs) {
			if (s.contains(infinitive)) {
				infinitives.add(s);
			}
		}
		return infinitives;
	}

	Set<Verb> findCommonVerbs() {
		Set<Verb> commonVerbs = new HashSet<>();
		for (String s : findCommonInfinitives()) {
			commonVerbs.add(new Verb(s));
		}
		return commonVerbs;
	}

	private StemChange parsePreteriteOrthographicChange() {
		val pretYo = getConjugation(Pronoun.YO, MoodType.INDICATIVE, TenseType.PRETERITE);

		// Parse 1st person singular (yo) changes

		// c -> qu
		if (infinitive.endsWith(StemChangesInfo.CAR) && assertStemChange(infinitive, StemChangesInfo.PRETERITE_YO_CAR, pretYo)) {
			return StemChangesInfo.PRETERITE_YO_CAR;
		}

		// g -> gu
		if (infinitive.endsWith(StemChangesInfo.GAR) && assertStemChange(infinitive, StemChangesInfo.PRETERITE_YO_GAR, pretYo)) {
			return StemChangesInfo.PRETERITE_YO_GAR;
		}

		// z -> c
		if (infinitive.endsWith(StemChangesInfo.ZAR) && assertStemChange(infinitive, StemChangesInfo.PRETERITE_YO_ZAR, pretYo)) {
			return StemChangesInfo.PRETERITE_YO_ZAR;
		}

		// gu -> gü
		if (infinitive.endsWith(StemChangesInfo.GUAR) && assertStemChange(infinitive, StemChangesInfo.PRETERITE_YO_GUAR, pretYo)) {
			return StemChangesInfo.PRETERITE_YO_GUAR;
		}

		// Parse the 3rd person changes
		if (info.getType() == VerbType.IR || info.getType() == VerbType.ER) {
			if (info.getType() == VerbType.IR) {
				String pretEl = getConjugation(Pronoun.EL, MoodType.INDICATIVE, TenseType.PRETERITE);
				if (infinitive.endsWith("ducir")) {
					//todo?
				}
				// o -> u
				if (assertStemChange(infinitive, StemChangesInfo.PRETERITE_TP_O_U, pretEl)) {
					return StemChangesInfo.PRETERITE_TP_O_U;
				}
				// e -> i
				if (assertStemChange(infinitive, StemChangesInfo.PRETERITE_TP_E_I, pretEl)) {
					return StemChangesInfo.PRETERITE_TP_E_I;
				}
			}
			// i -> y
			if (Common.endsWithAny(infinitive, StemChangesInfo.I_Y_MORPHEMES)) {
				return StemChangesInfo.PRETERITE_TP_I_Y;
			}
		}
		return null;
	}

	private StemChange parseStemChange() {
		val presEl = getConjugation(Pronoun.EL, MoodType.INDICATIVE, TenseType.PRESENT);


		// o -> ue
		if (assertStemChangeAux(infinitive, StemChangesInfo.PRESENT_O_UE, presEl)) {
			return StemChangesInfo.PRESENT_O_UE;
		}

		// u -> ue
		if (assertStemChangeAux(infinitive, StemChangesInfo.PRESENT_U_UE, presEl)) {
			return StemChangesInfo.PRESENT_U_UE;
		}

		// e -> ie
		if (assertStemChangeAux(infinitive, StemChangesInfo.PRESENT_E_IE, presEl)) {
			return StemChangesInfo.PRESENT_E_IE;
		}

		// i -> ie
		if (assertStemChangeAux(infinitive, StemChangesInfo.PRESENT_I_IE, presEl)) {
			return StemChangesInfo.PRESENT_I_IE;
		}

		// e -> i
		if (assertStemChangeAux(infinitive, StemChangesInfo.PRESENT_E_I, presEl)) {
			return StemChangesInfo.PRESENT_E_I;
		}


		// i -> í (rare)
		if (assertStemChangeAux(infinitive, StemChangesInfo.PRESENT_I_ACC_I, presEl)) {
			return StemChangesInfo.PRESENT_I_ACC_I;
		}

		// Unique cases
		if (infinitive.equals("oler")) {
			return StemChangesInfo.PRESENT_O_HUE;
		}

		return null;
	}

	// (stem)
	private String parseRoot() {
		return infinitive.substring(0, infinitive.length() - 2);
	}

	private String parseFutureConditionalStem() {
		String futureEl = getConjugation(Pronoun.EL, MoodType.INDICATIVE, TenseType.FUTURE);
		return futureEl.substring(0, futureEl.length() - 1);
	}

	private String getYoChange() {
		return null;
	}


	//todo
	private String parsePreteriteStem() {
		if (info.getPreteriteOrthographicChange() != null) {
			if (info.getPreteriteOrthographicChange().equals(new StemChange("i", "y"))) {
				return parseRoot();
			}
		}
		String preteriteEl = getConjugation(Pronoun.EL, MoodType.INDICATIVE, TenseType.PRETERITE);
		return preteriteEl.substring(0, preteriteEl.length() - 1);
	}

	private String parseSubjunctiveStem() {
		String subjEl = getConjugation(Pronoun.EL, MoodType.SUBJUNCTIVE, TenseType.PRESENT);
		return subjEl.substring(0, subjEl.length() - 1);
	}

	@SneakyThrows
	private VerbType parseType() {
		String infinitive = this.infinitive;
		// Reflexive
		if (infinitive.endsWith("se")) {
			infinitive = infinitive.substring(0, infinitive.length() - 2);
		}
		if (infinitive.endsWith("er")) return VerbType.ER;
		if (infinitive.endsWith("ir")) return VerbType.IR;
		if (infinitive.endsWith("ar")) return VerbType.AR;
		else throw new Exception(String.format("Verb %s cannot be typed", infinitive));
	}

	@Override
	public String toString() {
		AuxStringBuffer sb = new AuxStringBuffer();


		sb.appendLineBoldAlign("Infinitive: ", Common.ALIGN, infinitive);
		sb.appendLineBoldAlign("Gerund: ", Common.ALIGN, gerund);
		sb.appendLineBoldAlign("Past participle: ", Common.ALIGN, pastParticiple);
		sb.appendLineBoldAlign("Root: ", Common.ALIGN, root);


		sb.appendLine(info);
		sb.appendLine(indicative);
		sb.appendLine(subjunctive);
		sb.appendLine(imperative);
		if (continuous != null)
			sb.appendLine(continuous);
		return sb.toString();
	}
}
