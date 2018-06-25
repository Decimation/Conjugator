package com.deci.conj;

import lombok.Getter;
import lombok.SneakyThrows;

import java.sql.ResultSet;

public class Verb {

	@Getter
	private String   infinitive;
	@Getter
	private String   gerund;
	private String   root;
	private VerbType type;
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
		this.root = getRoot();
		this.type = getType();
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

	private String getRoot() {
		return infinitive.substring(0, infinitive.length() - 2);
	}

	@SneakyThrows
	private VerbType getType() {
		if (infinitive.endsWith("er")) return VerbType.ER;
		if (infinitive.endsWith("ir")) return VerbType.IR;
		if (infinitive.endsWith("ar")) return VerbType.AR;
		else throw new Exception("Verb cannot be typed");
	}

	@Override
	public String toString() {
		AuxStringBuffer sb = new AuxStringBuffer();
		sb.appendLine("Infinitive: %s", infinitive);
		sb.appendLine("Gerund: %s", gerund);
		sb.appendLine("Root: %s", root);
		sb.appendLine("Type: %s", type).appendLine();
		sb.appendLine(indicative);
		sb.appendLine(subjunctive);
		sb.appendLine(imperative);
		if (continuous != null)
			sb.appendLine(continuous);
		return sb.toString();
	}
}
