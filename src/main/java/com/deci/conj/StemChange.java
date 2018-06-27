package com.deci.conj;

class StemChange {

	private final String _old;
	private final String _new;

	private final Pronoun[] pronouns;

	/**
	 * If no pronoun is specified, it is implied the change applies to all
	 * subjects
	 */
	StemChange(String _old, String _new) {
		this(_old, _new, (Pronoun[]) null);
	}


	StemChange(String _old, String _new, Pronoun... pronouns) {
		this._old = _old;
		this._new = _new;
		this.pronouns = pronouns;
	}

	String getOld() {
		return _old;
	}

	String getNew() {
		return _new;
	}

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
		if (pronouns != null) {
			return String.format("%s -> %s (%s)", _old, _new, Pronoun.aggregate(pronouns));
		}
		return String.format("%s -> %s", _old, _new);
	}
}
