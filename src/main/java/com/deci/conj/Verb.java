package com.deci.conj;

import com.deci.razorcommon.Common;
import com.deci.razorcommon.RazorLogger;
import com.deci.razorcommon.RazorStringBuffer;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.val;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

public class Verb implements Comparable<WebVerb> {

	//region Fields
	private static final Verb     ESTAR    = new Verb("estar", true);
	private static final String   TAG_VERB = "Verb";
	@Getter
	private              String   infinitive;
	@Getter
	private              String   gerund;
	@Getter
	private              String   pastParticiple;
	@Getter
	private              String   root;
	@Getter
	private              VerbInfo info;
	private              Mood     indicative;
	private              Mood     subjunctive;
	private              Mood     imperative;
	private              Mood     continuous;
	//endregion

	//region Constructors
	@SneakyThrows
	public Verb(String infinitive) {
		this(infinitive, false);
	}

	@SneakyThrows
	private Verb(String infinitive, boolean estarMode) {
		this.infinitive = infinitive;
		// Verb not in database?
		var available = available(infinitive);
		if (!available) {
			throw new Exception(String.format("Verb %s not found", infinitive));
		}
		init(estarMode);
	}

	static Verb getEstar() {
		return ESTAR;
	}

	/**
	 * For present tense changes
	 */
	private static boolean assertStemChangeAux(String base, StemChange change, String _new) {
		if (base.contains(change.getOld()) && _new.contains(change.getNew())) {
			return base.indexOf(change.getOld()) == _new.indexOf(change.getNew());
		}
		return false;
	}
	//endregion

	//region Assert StemChange

	/**
	 * For preterite tense changes
	 */
	private static boolean assertStemChange(String base, StemChange change, String _new) {
		if (base.indexOf(change.getOld()) == _new.indexOf(change.getNew())) {
			//assert base.contains(change.getOld()) && _new.contains(change.getNew());
			return base.contains(change.getOld()) && _new.contains(change.getNew());
		}
		return false;
	}

	@SneakyThrows
	public static boolean available(String infinitive) {
		// First check the infinitives table
		ResultSet rs = SQLDatabase.getStatement().executeQuery(String.format("select * from infinitive where infinitive = '%s'", infinitive));
		String out = "";
		while (rs.next()) {
			out = rs.getString("infinitive");
		}

		boolean available = out.equals(infinitive);

		if (!available) {
			RazorLogger.out(TAG_VERB, "CRITICAL: %s not available", infinitive);
		}
		if (available) {
			// Then check the verbs table
			out = "";
			rs = SQLDatabase.getStatement().executeQuery(String.format("select * from verbs where infinitive = '%s'", infinitive));
			while (rs.next()) {
				out = rs.getString("infinitive");
			}
			if (out.equals(infinitive)) {
				return true;
			} else {

				RazorLogger.out(TAG_VERB, "CRITICAL: %s not available", infinitive);
				return false;
			}
		}
		return available;
	}

	@SneakyThrows
	private void init(boolean estarMode) {

		ResultSet rs = SQLDatabase.getStatement().executeQuery(String.format("select * from gerund where infinitive = '%s'", infinitive));
		while (rs.next()) {
			this.gerund = rs.getString("gerund");
		}
		ResultSet rs2 = SQLDatabase.getStatement().executeQuery(String.format("select * from pastparticiple where infinitive = '%s'", infinitive));
		while (rs2.next()) {
			this.pastParticiple = rs2.getString("pastparticiple");
		}
		this.info = new VerbInfo(parseType(), this.infinitive.endsWith("se") || this.infinitive.endsWith("(se)"));
		this.root = parseRoot();
		this.indicative = new vIndicative();
		this.indicative.load(infinitive);
		this.subjunctive = new vSubjunctive();
		this.subjunctive.load(infinitive);


		if (estarMode) {
			this.continuous = null;
			this.imperative = null;
		} else {
			this.continuous = new vContinuous();
			this.continuous.load(infinitive);
			this.imperative = new vImperative();
			this.imperative.load(infinitive);
		}
		this.info.setFutureConditionalStem(parseFutureConditionalStem());
		this.info.setSubjunctiveStem(parseSubjunctiveStem());
		this.info.setStemChange(parseStemChange());
		this.info.setPreteriteOrthographicChange(parsePreteriteOrthographicChange());
		this.info.setPreteriteStem(parsePreteriteStem());
		this.info.setPresentYoChange(parsePresentYoChange());
	}

	/**
	 * For present tense changes
	 */
	private StemChange assertStemChangesAux(StemChange[] changes, String cmp) {
		for (StemChange sc : changes) {
			if (assertStemChangeAux(infinitive, sc, cmp)) {
				return sc;
			}
		}
		return null;
	}

	//endregion

	//region Methods

	private boolean assertYoStemChange(StemChange change, String _new) {
		/*String scRoot = parseStemChangeRoot();
		if (change.getOld() == null) {
			Common.printf("assertYoStemChange", "%s", ((scRoot + change.getNew()) + "o"));
			return ((scRoot + change.getNew()) + "o").equals(_new);
		}

		return scRoot.replace(change.getOld(), change.getNew()).equals(_new);*/

		if (change.getOld() == null) {
			if ((root + change.getNew()).equals(_new)) {
				RazorLogger.out(TAG_VERB, "[%s] + [%s] = [%s]", root, change.getNew(), _new);
				return true;
			}
		}

		if (change.getOld() != null && parseStemChangeRoot() != null) {
			String tmp = parseStemChangeRoot().replace(change.getOld(), change.getNew());
			if (tmp.equals(parseStemChangeRoot())) {
				RazorLogger.out(TAG_VERB, "[%s] = [%s], no change", tmp, parseStemChangeRoot());
				return false;
			}
			//Common.printf("assertYo", "%s %s", tmp, parseStemChangeRoot());
			tmp += 'o';
			RazorLogger.out(TAG_VERB, "[%s] = [%s]", tmp, _new);
			return tmp.equals(_new);

		}

		return false;
	}

	public String toWebVerbString() {
		RazorStringBuffer sb = new RazorStringBuffer();
		sb.append("%s | ", infinitive);
		sb.append("%s | ", info.getStemChange() == null ? "" : info.getStemChange().toWebString());
		sb.append("%s | ", info.getPresentYoChange() == null ? "" : info.getPresentYoChange().toWebString());
		sb.append("%s | ", info.getSubjunctiveStem() == null ? "" : info.getSubjunctiveStem());
		sb.append("%s | ", info.getPreteriteStem() == null ? "" : info.getPreteriteStem());
		sb.append("%s", info.getFutureConditionalStem());
		return sb.toString();
	}


	public String getConjugation(Pronoun p, MoodType mood, TenseType tense) {
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

	public Set<String> findCommonInfinitives() {
		val verbs = SQLDatabase.getInfinitives();
		Set<String> infinitives = new HashSet<>();
		for (String s : verbs) {
			if (s.contains(infinitive)) {
				infinitives.add(s);
			}
		}
		return infinitives;
	}

	public Set<Verb> findCommonVerbs() {
		Set<Verb> commonVerbs = new HashSet<>();
		for (String s : findCommonInfinitives()) {
			commonVerbs.add(new Verb(s));
		}
		return commonVerbs;
	}

	//endregion

	//region Parsers
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
		// u -> ue
		// e -> ie
		// i -> ie
		// e -> i
		// i -> í (rare)

		// Unique cases
		if (infinitive.equals("oler")) {
			return StemChangesInfo.PRESENT_O_HUE;
		}

		return assertStemChangesAux(StemChangesInfo.getPresentStemChanges(), presEl);
	}

	// (stem)
	private String parseRoot() {
		int cut = 2;
		if (info.isReflexive()) {
			cut += 2;
		}
		return infinitive.substring(0, infinitive.length() - cut);
	}

	private String parseFutureConditionalStem() {
		String futureEl = getConjugation(Pronoun.EL, MoodType.INDICATIVE, TenseType.FUTURE);
		return futureEl.substring(0, futureEl.length() - 1);
	}

	//todo
	private String parsePreteriteStem() {
		if (info.getPreteriteOrthographicChange() != null) {
			if (info.getPreteriteOrthographicChange().equals(StemChangesInfo.PRETERITE_TP_I_Y)) {
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

	private String parseStemChangeRoot() {
		if (info.getStemChange() == null) return null;
		return root.replace(info.getStemChange().getOld(), info.getStemChange().getNew());
	}

	private StemChange parsePresentYoChange() {
		String presYo = getConjugation(Pronoun.YO, MoodType.INDICATIVE, TenseType.PRESENT);

		if ((infinitive.endsWith(StemChangesInfo.CIR) || infinitive.endsWith(StemChangesInfo.CER)) &&
				assertYoStemChange(StemChangesInfo.PRESENT_YO_CIR_CER, presYo)) {
			return StemChangesInfo.PRESENT_YO_CIR_CER;
		}

		if ((infinitive.endsWith(StemChangesInfo.GIR) || infinitive.endsWith(StemChangesInfo.GER)) &&
				assertYoStemChange(StemChangesInfo.PRESENT_YO_GIR_GER, presYo)) {
			return StemChangesInfo.PRESENT_YO_GIR_GER;
		}

		if (infinitive.endsWith(StemChangesInfo.UIR) &&
				assertYoStemChange(StemChangesInfo.PRESENT_YO_UIR, presYo)) {
			return StemChangesInfo.PRESENT_YO_UIR;
		}

		if (infinitive.endsWith(StemChangesInfo.IAR) &&
				assertYoStemChange(StemChangesInfo.PRESENT_YO_ACC_I_O, presYo)) {
			return StemChangesInfo.PRESENT_YO_ACC_I_O;
		}

		if (assertYoStemChange(StemChangesInfo.PRESENT_YO_OY, presYo)) {
			return StemChangesInfo.PRESENT_YO_OY;
		}

		if (assertYoStemChange(StemChangesInfo.PRESENT_YO_GO, presYo)) {
			return StemChangesInfo.PRESENT_YO_GO;
		}

		if (assertYoStemChange(StemChangesInfo.PRESENT_YO_IGO, presYo)) {
			return StemChangesInfo.PRESENT_YO_IGO;
		}

		if (assertYoStemChange(StemChangesInfo.PRESENT_YO_Z, presYo)) {
			return StemChangesInfo.PRESENT_YO_Z;
		}

		return null;
	}

	@SneakyThrows
	private VerbType parseType() {
		String infinitive = this.infinitive;
		// Reflexive
		if (infinitive.endsWith("se")) {
			infinitive = infinitive.substring(0, infinitive.length() - 2);
		} else if (infinitive.endsWith("(se)")) {
			infinitive = infinitive.substring(0, infinitive.length() - 4);
		}
		if (infinitive.endsWith("er")) return VerbType.ER;
		if (infinitive.endsWith("ir") || infinitive.endsWith("ír")) return VerbType.IR;
		if (infinitive.endsWith("ar")) return VerbType.AR;
		else throw new Exception(String.format("Verb %s cannot be typed", infinitive));
	}
	//endregion

	//region Overrides
	@Override
	public String toString() {
		RazorStringBuffer sb = new RazorStringBuffer();

		sb.append('\n');
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

	@Override
	public int compareTo(WebVerb lr) {
		int errors = 0;
		final String CMP = "CMP";
		final String FMT = "[%s] %s != %s";

		// Infinitive...

		if (info.getStemChange() != null) {
			if (!lr.getStemChange().equals(info.getStemChange().toWebString())) {
				RazorLogger.out(CMP, FMT, infinitive, lr.getStemChange(), info.getStemChange().toWebString());
				errors++;
			}
		}
		if (info.getPresentYoChange() != null) {
			if (!lr.getYo().equals(info.getPresentYoChange().toWebString())) {
				RazorLogger.out(CMP, FMT, infinitive, lr.getYo(), info.getPresentYoChange().toWebString());
				errors++;
			}
		}
		if (!lr.getPreteriteStem().equals(info.getPreteriteStem())) {
			if (!lr.getPreteriteStem().equals(" ") && info.getPreteriteStem().equals(infinitive + '-')) {
				RazorLogger.out(CMP, FMT, infinitive, lr.getPreteriteStem(), info.getPreteriteStem());
				errors++;
			}
		}
		if (!lr.getFutureStem().equals(info.getFutureConditionalStem())) {
			if (!lr.getFutureStem().equals(" ") && info.getSubjunctiveStem().equals(infinitive + '-')) {
				RazorLogger.out(CMP, FMT, infinitive, lr.getFutureStem(), info.getFutureConditionalStem());
				errors++;
			}

		}
		if (!lr.getSubjunctiveStem().equals(info.getSubjunctiveStem())) {
			if (!lr.getSubjunctiveStem().equals(" ") && info.getSubjunctiveStem().equals(root + '-')) {
				RazorLogger.out(CMP, FMT, infinitive, lr.getSubjunctiveStem(), info.getSubjunctiveStem());
				errors++;
			}
		}
		return errors;
	}
	//endregion
}
