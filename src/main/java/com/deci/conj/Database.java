package com.deci.conj;

import lombok.Getter;
import lombok.SneakyThrows;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class Database {
	private static final String     SQL_URL  = "https://raw.githubusercontent.com/ghidinelli/fred-jehle-spanish-verbs/master/jehle_verb_sqlite3.sql";
	private static final String     SQL_FILE = "C:\\Users\\Viper\\Desktop\\data.sql";
	private static final String     DB_FILE  = "C:\\Users\\Viper\\Desktop\\data.db";
	private static       Connection connection;
	@Getter
	private static       Statement  statement;


	static {
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/Viper/Desktop/data.db");
			statement = connection.createStatement();
			statement.setQueryTimeout(30);//todo

		} catch (SQLException x) {
			x.printStackTrace();
		}

	}

	private Database() {
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
		Common.exec(String.format("sqlite3 %s < %s", DB_FILE, SQL_FILE));
	}
}
