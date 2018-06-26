package com.deci.conj;

import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

class Database {
	private static final String     SQL_URL  = "https://raw.githubusercontent.com/ghidinelli/fred-jehle-spanish-verbs/master/jehle_verb_sqlite3.sql";
	private static final String     SQL_FILE = "C:\\Users\\Viper\\Desktop\\data.sql"; //todo
	private static final String     DB_FILE  = "C:\\Users\\Viper\\Desktop\\data.db"; //todo
	private static       Connection connection;
	@Getter
	private static       Statement  statement;


	static {
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/Viper/Desktop/data.db"); //todo
			statement = connection.createStatement();
			statement.setQueryTimeout(30);//todo

		} catch (SQLException x) {
			x.printStackTrace();
		}

	}

	private Database() {
	}

	@SneakyThrows
	static Set<String> getInfinitives() {
		Set<String> ls = new HashSet<>();
		ResultSet rs = statement.executeQuery("select * from infinitive");
		while (rs.next()) {

			ls.add(rs.getString("infinitive"));
		}
		return ls;
	}

	@SneakyThrows
	static void close() {
		connection.close();
	}


	private static void download() {
		File sql = new File(SQL_FILE);
		if (!sql.exists()) {
			Common.download(SQL_URL, SQL_FILE);
		}
		// doesn't work?
		Common.exec(String.format("sqlite3 %s < %s", DB_FILE, SQL_FILE));
	}
}
