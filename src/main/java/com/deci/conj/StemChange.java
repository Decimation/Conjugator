package com.deci.conj;

import lombok.Getter;

public class StemChange {

	//region Fields
	private final String _old;
	private final String _new;

	@Getter
	private final Pronoun[] pronouns;

	@Getter
	private final TenseType tense;
	//endregion

	//region Constructors

	/**
	 * If no pronoun is specified, it is implied the change applies to all
	 * subjects (excluding vosotros, nosotros in present)
	 */
	StemChange(String _old, String _new) {
		this(_old, _new, TenseType.PRESENT, (Pronoun[]) null);
	}

	StemChange(String _old, String _new, TenseType tense, Pronoun... pronouns) {
		this._old = _old;
		this._new = _new;
		this.tense = tense;
		this.pronouns = pronouns;

	}

	StemChange(String _new, Pronoun pronoun) {
		this(null, _new, TenseType.PRESENT, pronoun);
	}
	//endregion

	//region Properties
	public String getOld() {
		return _old;
	}

	public String getNew() {
		return _new;
	}
	//endregion


	public String toWebString() {
		if (pronouns != null && pronouns[0] == Pronoun.YO) {
			return String.format("-%s", _new);
		}
		return String.format("%s-%s", _old, _new);
	}

	//region Overrides

	// Doesn't compare pronouns
	@Override
	public boolean equals(Object obj) {
		if (obj.getClass().equals(getClass())) {
			StemChange sc = (StemChange) obj;
			return _new.equals(sc._new) && _old.equals(sc._old);
		}
		return false;
	}

	@Override
	public String toString() {
		if (_old == null) {
			return String.format("-%s (%s)", _new, Pronoun.aggregate(pronouns));
		}
		if (pronouns != null) {
			return String.format("%s -> %s (%s)", _old, _new, Pronoun.aggregate(pronouns));
		}

		return String.format("%s -> %s", _old, _new);
	}
	//endregion
}
