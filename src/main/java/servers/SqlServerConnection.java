package servers;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import constants.C;
import constants.DefaultDatabase;
import constants.ReadFromFileDatabase;
import models.*;
import scalability.LargeDatasetGenerator;

import java.io.FileNotFoundException;
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
			dropTables(connection);

			createTables(connection);

		} catch (NullPointerException | SQLException e) {
			e.printStackTrace();
			LocalServer.fatalError("database tables could not be fully created");
		}

		insertData(connection);

		//TODO: close connection to DB
	}

	private static void dropTables(ConnectionSource connection) throws SQLException {
		TableUtils.dropTable(connection, Employee.class, false);
		TableUtils.dropTable(connection, Skill.class, false);
		TableUtils.dropTable(connection, EmployeeSkill.class, false);
		TableUtils.dropTable(connection, Task.class, false);
		TableUtils.dropTable(connection, TaskSkill.class, false);
		TableUtils.dropTable(connection, Project.class, false);
	}

	private static void createTables(ConnectionSource connection) throws SQLException {
		TableUtils.createTableIfNotExists(connection, Employee.class);
		TableUtils.createTableIfNotExists(connection, Skill.class);
		TableUtils.createTableIfNotExists(connection, EmployeeSkill.class);
		TableUtils.createTableIfNotExists(connection, Task.class);
		TableUtils.createTableIfNotExists(connection, TaskSkill.class);
		TableUtils.createTableIfNotExists(connection, Project.class);
	}

	private static void insertData(ConnectionSource connection) throws FileNotFoundException {


		//TODO: add execution of queries that instnatiat database with the default values
		//export default database
		try {
			Dao<Employee, Integer> employeeDao = DaoManager.createDao(connection, Employee.class);
			Dao<Task, Integer> taskDao = DaoManager.createDao(connection, Task.class);
			Dao<Skill, Integer> skillDao = DaoManager.createDao(connection, Skill.class);
			Dao<EmployeeSkill, Integer> employeeSkillDao = DaoManager.createDao(connection, EmployeeSkill.class);
			Dao<TaskSkill, Integer> taskSkillDao = DaoManager.createDao(connection, TaskSkill.class);
			Dao<Project, Integer> projectDao = DaoManager.createDao(connection, Project.class);

			/*LargeDatasetGenerator.generateTestFiles();

			ReadFromFileDatabase.createInsertQueries(
					"src/main/resources/large_dataset/Employees.txt",
					"src/main/resources/large_dataset/Tasks.txt",
					"src/main/resources/large_dataset/Skills.txt",
					"src/main/resources/large_dataset/EmployeeSkills.txt",
					"src/main/resources/large_dataset/TaskSkills.txt",
					"src/main/resources/large_dataset/Projects.txt"
			);*/

			for (String q : DefaultDatabase.InsertQueriesEmployee) {
				employeeDao.executeRaw(q);
			}

			for (String q : DefaultDatabase.InsertQueriesTask) {
				taskDao.executeRaw(q);
			}


			for (String q : DefaultDatabase.InsertQueriesSkill) {
				skillDao.executeRaw(q);
			}

			for (String q : DefaultDatabase.InsertQueriesEmployeeSkill) {
				employeeSkillDao.executeRaw(q);
			}


			for (String q : DefaultDatabase.InsertQueriesTaskSkill) {
				taskSkillDao.executeRaw(q);
			}

			for (String q : DefaultDatabase.InsertQueriesProject) {
				projectDao.executeRaw(q);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			LocalServer.fatalError("database tables could not be fully initialised");
		}
	}
}
