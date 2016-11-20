package servers;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import constants.C;
import models.*;

import java.io.IOException;
import java.sql.SQLException;

/**
 * This singleton class initialises and manages the SQL connection
 */
public class SqlServerConnection {

	/**
	 * Private constructor to enforce the singleton pattern
	 */
	private SqlServerConnection() {
	}

	private static ConnectionSource connection = null;

	/**
	 * Acquires the single instance of the SQL connection.
	 *
	 * @return The SQL connection source
	 */
	public static ConnectionSource acquireConnection() {
		return acquireConnection(C.MYSQL_URI, C.MYSQL_USERNAME, C.MYSQL_PASSWORD);
	}

	/**
	 * Acquires or creates the single instance of the SQL connection
	 *
	 * @param uri      The DB URI
	 * @param username The DB username
	 * @param password The DB password
	 * @return The SQL connection source
	 */
	public static ConnectionSource acquireConnection(String uri, String username, String password) {
		if (connection == null) {
			try {
				connection = new JdbcConnectionSource(uri);
				((JdbcConnectionSource) connection).setUsername(username);
				((JdbcConnectionSource) connection).setPassword(password);
				initDB(connection);
			} catch (Exception e) {
				e.printStackTrace();
				LocalServer.fatalError("a connection could not be made to the database");
			}
		}

		return connection;
	}

	/**
	 * Creates all tables (if needed)
	 *
	 * @param connection The connection source, linked to the DB to be used.
	 */
	public static void initDB(ConnectionSource connection) throws SQLException, IOException {
		try {
			TableUtils.createTableIfNotExists(connection, Employee.class);
			TableUtils.createTableIfNotExists(connection, Task.class);
			TableUtils.createTableIfNotExists(connection, Skill.class);
			TableUtils.createTableIfNotExists(connection, Project.class);
			TableUtils.createTableIfNotExists(connection, EmployeeSkill.class);
			TableUtils.createTableIfNotExists(connection, TaskSkill.class);
		} catch (NullPointerException | SQLException e) {
			e.printStackTrace();
			LocalServer.fatalError("database tables could not be fully created");
		}

		//TODO: add execution of queries that instnatiat database with the default values
		//export default database
		/*for (String q : DefaultDatabase.SqlQueries) {
			employeeDao.executeRaw(q);
		}*/

		//TODO: close connection to DB
	}
}
