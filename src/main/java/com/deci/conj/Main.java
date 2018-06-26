package com.deci.conj;

public class Main {

	/**
	 * todo: add vos support, add continuous subjunctive, add stem changers and misc info, common subverbs
	 */
	public static void main(String[] args) {

		Verb v = new Verb("poner");
		System.out.println(v);

		Common.printArray(v.findCommonInfinitives().toArray());
		Database.close();
	}
}
