package com.deci.conj;

import com.deci.razorcommon.Common;
import com.deci.razorcommon.RazorLogger;
import lombok.SneakyThrows;

import java.sql.ResultSet;

public class vImperative extends Mood {

	vImperative() {
		super(2);
		TenseType[] tenseTypes = {TenseType.PRESENT, TenseType.PRESENT};
		for (int i = 0; i < getSize(); i++)
			setTense(i, new Tense(tenseTypes[i]));
	}

	@SneakyThrows
	String getConjugation(Pronoun p, MoodType mood, TenseType tense) {
		switch (mood) {
			case IMP_AFFIRMATIVE:
				if (getTense(0).getType() == tense) {
					return getTense(0).getTable().get(p);
				}
			case IMP_NEGATIVE:
				if (getTense(0).getType() == tense) {
					return getTense(0).getTable().get(p);
				}
			default:
				return Common.ERROR;
		}
	}

	@Override
	@SneakyThrows
	void load(String infinitive) {
		RazorLogger.out("vImperative", "loading %s", infinitive);
		getTense(0).load(infinitive, MoodType.IMP_AFFIRMATIVE);
		getTense(1).load(infinitive, MoodType.IMP_NEGATIVE);

		// The database does not contain nosotros commands,
		// so we need to load the Subjunctive nosotros form which is the same as the
		// Imperative nosotros
		String queryAffirm = String.format("select * from verbs where infinitive = '%s' and mood = '%s' and tense = '%s'", infinitive, MoodType.SUBJUNCTIVE, TenseType.PRESENT);

		String subjNosotros = "";

		ResultSet rs = SQLDatabase.getStatement().executeQuery(queryAffirm);
		//assert !rs.isClosed();
		subjNosotros = rs.getString(Pronoun.NOSOTROS.toSQLString());


		getTense(0).getTable().set(Pronoun.NOSOTROS, subjNosotros);
		getTense(1).getTable().set(Pronoun.NOSOTROS, "no " + subjNosotros);
		for (int i = 0; i < getSize(); i++) {
			getTense(i).getTable().set(Pronoun.YO, "-");
		}
	}

	@Override
	public String toString() {
		return super.toStringInternal("Imperative");
	}
}
