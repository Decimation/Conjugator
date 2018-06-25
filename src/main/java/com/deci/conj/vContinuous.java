package com.deci.conj;

import lombok.SneakyThrows;

import java.sql.ResultSet;
import java.util.Arrays;

class vContinuous extends Mood {

	private final TenseType[] tenseTypes;

	vContinuous() {
		super(5);

		tenseTypes = (TenseType[]) Arrays.asList(TenseType.PRESENT, TenseType.PRETERITE, TenseType.IMPERFECT, TenseType.CONDITIONAL,
				TenseType.FUTURE).toArray();
		for (int i = 0; i < getSize(); i++)
			setTense(i, new Tense(tenseTypes[i]));

	}

	@Override
	public String toString() {
		return super.toStringInternal("Continuous");
	}

	@SneakyThrows
	@Override
	void load(String infinitive) {

		ResultSet rs = Database.getStatement().executeQuery(String.format("select * from gerund where infinitive = '%s'", infinitive));
		final String gerund = rs.getString("gerund");
		final Verb estar = Verb.getEstar();
		//System.out.println(estar.getConjugation(Pronoun.EL, MoodType.INDICATIVE, TenseType.PRESENT));
		for (int i = 0; i < getSize(); i++) {
			for (Pronoun p : Pronoun.values()) {
				String estarConj = estar.getConjugation(p, MoodType.INDICATIVE, tenseTypes[i]);

				getTense(i).getTable().set(p, estarConj + " " + gerund);
				//System.out.println(getTense(i).getTable().get(p));
			}
			System.out.println();


		}
	}
}
