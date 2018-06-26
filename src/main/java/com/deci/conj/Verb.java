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
	private String   root;
	private VerbInfo info;

	private Mood indicative;
	private Mood subjunctive;
	private Mood imperative;
	private Mood continuous;

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
		this.info.setFutureStem(parseFutureStem());
		this.info.setPreteriteStem(parsePreteriteStem());
		this.info.setSubjunctiveStem(parseSubjunctiveStem());
	}

	static Verb getEstar() {
		return new Verb("estar", false);
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
		return "ERROR";
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

	private String parseRoot() {
		return infinitive.substring(0, infinitive.length() - 2);
	}

	private String parseFutureStem() {
		String futureEl = getConjugation(Pronoun.EL, MoodType.INDICATIVE, TenseType.FUTURE);
		return futureEl.substring(0, futureEl.length() - 1);
	}

	private String parsePreteriteStem() {
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

		final int align  = 30 + 12;

		sb.appendLineBoldAlign("Infinitive: ", align, infinitive);
		sb.appendLineBoldAlign("Gerund: ", align, gerund);
		sb.appendLineBoldAlign("Root: ", align, root);

		sb.appendLine(info);
		sb.appendLine(indicative);
		sb.appendLine(subjunctive);
		sb.appendLine(imperative);
		if (continuous != null)
			sb.appendLine(continuous);
		return sb.toString();
	}
}
