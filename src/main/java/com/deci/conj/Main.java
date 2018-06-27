package com.deci.conj;

public class Main {

	/**
	 * todo: add vos support, add continuous subjunctive, add present stem changes* (-guir, -ger, -gir)
	 */
	public static void main(String[] args) {
		System.out.println(System.getProperty("java.version"));
		System.out.println(System.getProperty("java.specification.version"));
		Verb v = new Verb("destruir");
		System.out.println(v);

		Common.printArray(v.findCommonInfinitives().toArray());
		Database.close();
	}
}
