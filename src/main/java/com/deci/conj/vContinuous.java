package com.deci.conj;

import com.deci.razorcommon.RazorLogger;
import lombok.SneakyThrows;

import java.sql.ResultSet;

class vContinuous extends Mood {

	private final TenseType[] tenseTypes;

	vContinuous() {
		super(5);

		tenseTypes = new TenseType[]{TenseType.PRESENT, TenseType.PRETERITE, TenseType.IMPERFECT, TenseType.CONDITIONAL,
									 TenseType.FUTURE};
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
		RazorLogger.out("vContinuous", "loading %s", infinitive);
		String gerund = "";


		ResultSet rs = SQLDatabase.getStatement().executeQuery(String.format("select * from gerund where infinitive = '%s'", infinitive));
		gerund = rs.getString("gerund");


		final Verb estar = Verb.getEstar();
		//System.out.println(estar.getConjugation(Pronoun.EL, MoodType.INDICATIVE, TenseType.PRESENT));
		for (int i = 0; i < getSize(); i++) {
			for (Pronoun p : Pronoun.values()) {
				String estarConj = estar.getConjugation(p, MoodType.INDICATIVE, tenseTypes[i]);

				getTense(i).getTable().set(p, estarConj + " " + gerund);
				//System.out.println(getTense(i).getTable().get(p));
			}


		}
	}
}
