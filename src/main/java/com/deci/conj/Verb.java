package com.deci.conj;

import lombok.SneakyThrows;

import java.sql.ResultSet;

public class Verb {

	private String infinitive;
	private String gerund;
	private String root;
	private VerbType type;
	private vIndicative indicative;
	private vSubjunctive subjunctive;
	private vImperative imperative;

	@SneakyThrows
	Verb(String infinitive) {
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
		return sb.toString();
	}
}
