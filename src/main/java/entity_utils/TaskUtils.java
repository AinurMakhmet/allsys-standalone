package entity_utils;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.support.ConnectionSource;
import models.Task;
import servers.SqlServerConnection;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class for reading data from the Task table in the database.
 */
public class TaskUtils extends AbstractEntityUtils {
	private static final String QUERY_TASKS_VALID_FOR_ALLOCATION =
			"select task.id from task join task_skill on task.id=task_skill.task_id where employee_id='null' and start_time!='null' and end_time!='null' group by task.id";

	private static final String QUERY_ALLOCATED_TASKS =
			"select task.id from task where employee_id!='null'";

	/**
	 * Gets the {@link Task} object with the specified ID.
	 *
	 * @param id the <code>Task</code> ID
	 * @return the associated <code>Task</code> object
	 */
	public static Task getTask(String id) {
		try {
			return getTask(Integer.parseInt(id));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Gets the {@link Task} object with the specified ID.
	 *
	 * @param id the <code>Task</code> ID
	 * @return the associated <code>Task</code> object
	 */
	public static Task getTask(int id) {
		return getEntityById(Task.class, id);
	}

	/**
	 * Gets all the {@link Task} objects in the database.
	 * <p>
	 * Can return data only for the <code>enabled</code> fields.
	 *
	 * @return a list of <code>Task</code> objects
	 */
	public static List<Task> getAllTasks() {
		return getAllEntities(Task.class);
	}

	/**
	 * Gets all the tasks that have at least one required skill  and have start and end dates.
	 *
	 * @return a list of <code>Task</code> objects
	 */
	public static List<Task> getAllTasksValidForAllocation() {
		ConnectionSource conn = SqlServerConnection.acquireConnection();
		List<Task> tasks= new ArrayList<>();
		if (conn != null) {
			try {
				Dao<Task, Integer> taskDao = DaoManager.createDao(conn, Task.class);
				GenericRawResults<String[]> rawResults = taskDao.queryRaw(QUERY_TASKS_VALID_FOR_ALLOCATION);
				List<String[]> results = rawResults.getResults();
				System.out.println("rawResults: "+ results.size());
				results.forEach(arrayOfString-> tasks.add(getTask(arrayOfString[0])));

				rawResults.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (tasks!= null) {
				return tasks;
			}
		}
		return null;
	}

	/**
	 * Gets all the allocated {@link Task} objects in the database.
	 *
	 * @return a list of <code>Task</code> objects
	 */
	public static List<Task> getAllocatedTask() {
		ConnectionSource conn = SqlServerConnection.acquireConnection();
		List<Task> tasks= new ArrayList<>();
		if (conn != null) {
			try {
				Dao<Task, Integer> taskDao = DaoManager.createDao(conn, Task.class);
				GenericRawResults<String[]> rawResults = taskDao.queryRaw(QUERY_ALLOCATED_TASKS);
				List<String[]> results = rawResults.getResults();
				System.out.println("rawResults: "+ results.size());
				results.forEach(arrayOfString-> tasks.add(getTask(arrayOfString[0])));

				rawResults.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			if (tasks!= null) {
				return tasks;
			}
		}
		return null;


	}


	/**
	 * Updates the specified record in the database.
	 *
	 * @param task the task to be updated
	 */
	public static void updateEntity(Task task) {
		// set up server connection
		updateEntity(Task.class, task);
	}
}
