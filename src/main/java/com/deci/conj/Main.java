package com.deci.conj;

public class Main {

	/**
	 * todo: add vos support, add continuous subjunctive
	 */
	public static void main(String[] args) {
		Verb v = new Verb("destruir");
		System.out.println(v);
		Database.close();


	}
}
