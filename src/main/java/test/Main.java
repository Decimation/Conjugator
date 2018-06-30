package test;

import com.deci.conj.*;
import com.deci.razorcommon.Info;
import com.deci.razorcommon.RazorLogger;
import lombok.SneakyThrows;

class Main {

	/**
	 * todo: broken verbs:
	 * - extinguir !(o -> ue, i -> y, c -> z)
	 * - ofender (e -> ie) (?)
	 *
	 */

	/**
	 * todo: add vos support, add continuous subjunctive, add present stem changes* (-guir, -ger, -gir), add
	 * todo: subjunctive imperfect 2, yo change, perfect continuous, subjunctive perfect continuous
	 * todo: perfect participle (having [participle])
	 * <p>
	 */
	@SneakyThrows
	public static void main(String[] args) {
		System.out.printf("RazorCommon %s\n", Info.version());
		SQLDatabase.init();
		WebDatabase.init();
		RazorLogger.disableLog("vIndicative", "vContinuous", "vSubjunctive", "vImperative");

		/*System.out.println(System.getProperty("java.version"));
		System.out.println(System.getProperty("java.specification.version"));
		Verb v = new Verb("tener");
		v.init();
		System.out.println(v);*/
		//WebDatabase.parseDiscrepancies();
		//Common.printArray(v.findCommonInfinitives().toArray());

		//Serializer.write(WebDatabase.parseAll());

		Verb v = new Verb("vestir");

		WebVerb wv = Serializer.readWebVerb("vestir");
		System.out.println(v.compareTo(wv));
		System.out.println(v.toWebVerbString());
		System.out.println(wv);

		SQLDatabase.close();
		WebDatabase.close();
	}
}
