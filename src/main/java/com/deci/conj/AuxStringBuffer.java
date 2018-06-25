package com.deci.conj;

class AuxStringBuffer {
	private final StringBuilder sb;

	AuxStringBuffer() {
		sb = new StringBuilder();
	}

	static String repeat(char c, int i) {
		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < i; x++)
			sb.append(c);
		return sb.toString();
	}

	AuxStringBuffer append(Object obj) {
		sb.append(obj);
		return this;
	}

	AuxStringBuffer appendLine() {
		sb.append('\n');
		return this;
	}

	AuxStringBuffer appendLine(Object obj) {
		sb.append(obj).append('\n');
		return this;
	}

	AuxStringBuffer append(String str, Object obj) {
		sb.append(String.format(str, obj));
		return this;
	}

	AuxStringBuffer appendLine(String str, Object obj) {
		append(str, obj).append('\n');
		return this;
	}

	@Override
	public String toString() {
		return sb.toString();
	}
}
