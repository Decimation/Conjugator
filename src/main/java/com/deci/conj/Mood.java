package com.deci.conj;

import com.deci.razorcommon.Common;
import com.deci.razorcommon.RazorStringBuffer;
import lombok.AccessLevel;
import lombok.Getter;

public abstract class Mood {

	//region Fields
	private final Tense[]  m_tenses;
	@Getter(AccessLevel.PACKAGE)
	private final int      size;
	private final MoodType mood;
	//endregion

	//region Constructors
	public Mood(int size) {
		this(size, null);
	}

	public Mood(int size, MoodType mood) {
		this.size = size;
		m_tenses = new Tense[size];
		this.mood = mood;
	}
	//endregion

	//region Methods
	final String getConjugation(Pronoun p, TenseType tense) {
		for (Tense t : m_tenses) {
			if (t.getType() == tense) {
				return t.getTable().get(p);
			}
		}
		return Common.ERROR;
	}

	abstract void load(String infinitive);

	final void loadInternal(String infinitive) {

		for (Tense tense : m_tenses) {
			tense.load(infinitive, mood);
		}
	}

	final void setTense(int index, Tense tense) {
		m_tenses[index] = tense;
	}

	final Tense getTense(int index) {
		return m_tenses[index];
	}

	final String toStringInternal(String tag) {
		RazorStringBuffer sb = new RazorStringBuffer();
		sb.appendLine("\t\t\t\t" + tag).appendLine();
		for (Tense tense : m_tenses) {
			sb.appendLine(tense);
		}
		return sb.toString();
	}
	//endregion
}
