/*
   Copyright 2014-2016 PetaByte Research Ltd.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package hu.petabyte.redflags.engine.util;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Simple utility to load CSV into MySQL using
 * <code>LOAD DATA LOCAL INFILE ... REPLACE INTO TABLE ...</code> command. The
 * row and column delimiters can be specified.
 *
 * @author Zsolt Jurányi
 *
 */
public class CSV2MySQL {

	private final Connection connection;

	/**
	 * Creates a new instance of <code>CSV2MySQL</code> and connects to MySQL
	 * with the specified parameters.
	 *
	 * @param host
	 *            Host name and port number of the MySQL server, e.g.
	 *            "localhost:3306" or "127.0.0.1:3306"
	 * @param name
	 *            Name of the database you want to use.
	 * @param user
	 *            Username for the database.
	 * @param pass
	 *            Password for the database.
	 * @throws SQLException
	 */
	public CSV2MySQL(String host, String name, String user, String pass) throws SQLException {
		checkNotNull(host, "host should not be null.");
		checkNotNull(name, "name should not be null.");
		checkNotNull(user, "user should not be null.");
		checkNotNull(pass, "pass should not be null.");
		checkArgument(!host.isEmpty(), "host should not be empty.");
		checkArgument(!name.isEmpty(), "name should not be empty.");
		checkArgument(!user.isEmpty(), "user should not be empty.");
		this.connection = DriverManager.getConnection(
				String.format("jdbc:mysql://%s/%s?useUnicode=true&characterEncoding=utf-8", host, name), user, pass);
	}

	/**
	 * Closes the MySQL connection.
	 *
	 * @throws SQLException
	 */
	public void closeConnection() throws SQLException {
		connection.close();
	}

	/**
	 * Retrieves the JDBC connection used by this <code>CSV2MySQL</code>
	 * instance.
	 *
	 * @return JDBC connection.
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Loads the specified CSV file into the appropriate table of the database.
	 * The CSV file must contain the same columns as the MySQL table. The SQL
	 * command will look like this:<br/>
	 * <br/>
	 * <code>LOAD DATA LOCAL INFILE '...' REPLACE INTO TABLE ...</code>
	 *
	 * @param f
	 *            The CSV file.
	 * @param table
	 *            The table you want to update.
	 * @param colDelimiter
	 *            Column delimiter of the CSV file.
	 * @param rowDelimiter
	 *            Row delimiter of the CSV file.
	 * @throws SQLException
	 */
	public void loadCSV(File f, String table, String colDelimiter, String rowDelimiter) throws SQLException {
		Statement s = connection.createStatement();
		String filename = f.getAbsolutePath().replaceAll("\\\\", "/");
		s.execute(String.format(
				"LOAD DATA LOCAL INFILE '%s' " + //
						"REPLACE INTO TABLE %s " + //
						"CHARACTER SET %s " + //
						"COLUMNS TERMINATED BY '%s' " + //
						"LINES TERMINATED BY '%s';", //
				filename, table, "UTF8", colDelimiter,  rowDelimiter));
		s.close();
	}
}
