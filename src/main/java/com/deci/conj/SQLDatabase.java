package com.deci.conj;

import com.deci.razorcommon.Common;
import com.deci.razorcommon.RazorLogger;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class SQLDatabase {
	//region Fields
	private static final String     SQL_URL   = "https://raw.githubusercontent.com/ghidinelli/fred-jehle-spanish-verbs/master/jehle_verb_sqlite3.sql";
	private static final String     SQL_FILE  = "C:\\Users\\Viper\\Desktop\\data.sql"; //todo
	private static final String     DB_FILE   = "C:\\Users\\Viper\\Desktop\\data.db"; //todo
	private static final String     TAG_SQLDB = "SQLDatabase";
	private static       Connection connection;
	@Getter
	private static       Statement  statement;
	//endregion

	static {
	}

	private SQLDatabase() {
	}

	public static void init() {
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/Viper/Desktop/data.db"); //todo
			statement = connection.createStatement();
			statement.setQueryTimeout(30);//todo

		} catch (SQLException x) {
			x.printStackTrace();
		}
		RazorLogger.out(TAG_SQLDB, "Init");
	}

	//todo
	public static List<Verb> parseAll() {
		int notAvailable = 0;
		var ls = new ArrayList<Verb>();
		for (String s : getInfinitives()) {
			if (Verb.available(s)) {
				ls.add(new Verb(s));
			} else notAvailable++;

		}
		RazorLogger.out(TAG_SQLDB, "Parsed %d verbs, %d not available", ls.size(), notAvailable);
		return ls;
	}

	@SneakyThrows
	public static Set<String> getInfinitives() {
		Set<String> ls = new HashSet<>();
		ResultSet rs = statement.executeQuery("select * from infinitive");
		while (rs.next()) {

			ls.add(rs.getString("infinitive"));
		}
		return ls;
	}

	@SneakyThrows
	public static void close() {
		connection.close();
		RazorLogger.out(TAG_SQLDB, "Closed");
	}


	private static void download() {
		File sql = new File(SQL_FILE);
		if (!sql.exists()) {
			Common.download(SQL_URL, SQL_FILE);
		}
		// doesn't work?
		//Common.exec(String.format("sqlite3 %s < %s", DB_FILE, SQL_FILE));
	}
}
