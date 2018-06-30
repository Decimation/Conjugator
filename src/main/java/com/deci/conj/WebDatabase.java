package com.deci.conj;

import com.deci.razorcommon.RazorLogger;
import com.deci.razorcommon.SeleniumCommon;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Controls the WebDriver that reads the data from http://www.intro2spanish.com/verbs/listas/all.htm
 * then serializes it using Serializer.
 */
public final class WebDatabase {
	//region Fields
	private static final int       START          = 4;
	private static final int       MAX            = 826;
	private static final int       COUNT          = MAX - START;
	private static final String    URL            = "http://www.intro2spanish.com/verbs/listas/all.htm";
	private static final String    INFINITIVE_CSS = "tr:nth-child(%d) > td:nth-child(3) > font > b";
	private static final String    STEMCHANGE_CSS = "table:nth-child(2) > tbody > tr:nth-child(%d) > td:nth-child(4)";
	private static final String    YO_CSS         = "table:nth-child(2) > tbody > tr:nth-child(%d) > td:nth-child(5)";
	private static final String    SUBJSTEM_CSS   = "tr:nth-child(%d) > td:nth-child(6)";
	private static final String    PRETSTEM_CSS   = "tr:nth-child(%d) > td:nth-child(7)";
	private static final String    FUTSTEM_CSS    = "tr:nth-child(%d) > td:nth-child(8)";
	private static final String    TAG_WEBDB      = "WebDatabase";
	private static       WebDriver driver;
	//endregion

	static {
	}

	private WebDatabase() {
	}

	public static void init() {
		SeleniumCommon.suppressLogging();
		ChromeOptions co = new ChromeOptions();
		co.addArguments("--headless");
		driver = new ChromeDriver(co);
		driver.navigate().to(URL);
		RazorLogger.out(TAG_WEBDB, "Init");
	}

	/**
	 * Compare SQL database verbs with online list verbs
	 */
	public static List<Verb> parseDiscrepancies() {
		var brokenSQLVerbs = new CopyOnWriteArrayList<Verb>();
		var sqlVerbs = SQLDatabase.parseAll();
		var webVerbs = Serializer.read();
		int missingSQL = 0;
		int totalErrors = 0;
		int verbs = 0;

		for (int i = 0; i < COUNT; i++) {
			WebVerb webVerb = webVerbs.get(i);
			//System.out.printf("%s\n", webVerb.getInfinitive());
			if (Verb.available(webVerb.getInfinitive())) {
				verbs++;
				//System.out.printf("%s available\n", webVerb.getInfinitive());
				Verb sqlVerb = new Verb(webVerb.getInfinitive());

				System.out.printf("\nWebVerb: %s\n", webVerb);
				System.out.printf("SQLVerb: %s\n", sqlVerb.toWebVerbString());
				int errCnt = sqlVerb.compareTo(webVerb);
				System.out.printf("Error count: %d\n", errCnt);
				totalErrors += errCnt;
			} else missingSQL++;

		}

		System.out.printf("\nErrors/verb: %f\n", (double) totalErrors / verbs);
		System.out.printf("Missing verbs: %s\n", missingSQL);
		return brokenSQLVerbs;
	}

	public static void close() {
		driver.close();
		driver.quit();
		RazorLogger.out(TAG_WEBDB, "Closed");
	}

	public static List<WebVerb> parseAll() {
		var ls = new ArrayList<WebVerb>();
		for (int i = START; i <= MAX; i++) {
			String inf = driver.findElement(By.cssSelector(String.format(INFINITIVE_CSS, i))).getText();
			String sc = driver.findElement(By.cssSelector(String.format(STEMCHANGE_CSS, i))).getText();
			String yo = driver.findElement(By.cssSelector(String.format(YO_CSS, i))).getText();
			String subj = driver.findElement(By.cssSelector(String.format(SUBJSTEM_CSS, i))).getText();
			String pret = driver.findElement(By.cssSelector(String.format(PRETSTEM_CSS, i))).getText();
			String fut = driver.findElement(By.cssSelector(String.format(FUTSTEM_CSS, i))).getText();
			var lr = new WebVerb(inf, sc, yo, subj, pret, fut);
			ls.add(lr);

			//System.out.printf("\r%d / %d | %s", i, MAX, lr);
		}
		return ls;
	}
}
