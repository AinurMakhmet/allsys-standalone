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
	 * Gets all the tasks that have at least one required skill.
	 * <p>
	 * Can return data only for the <code>enabled</code> fields.
	 *
	 * @return a list of <code>TaskIds</code> objects
	 */
	public static List<Task> getAllTasksHaveSkill() {
		ConnectionSource conn = SqlServerConnection.acquireConnection();
		List<Integer> taskIds= new ArrayList<>();
		List<Task> tasks= new ArrayList<>();
		if (conn != null) {
			try {
				// search for user
				Dao<Task, Integer> taskDao = DaoManager.createDao(conn, Task.class);
				GenericRawResults<String[]> rawResults = taskDao.queryRaw(
						"select task.id from task join task_skill on task.id=task_skill.task_id where employee_id='null' group by task.id");
				List<String[]> results = rawResults.getResults();
				System.out.println("rawResults: "+ results.size());
				for (String[] result : results) {
					taskIds.add(Integer.parseInt(result[0]));
					//System.out.println(result.length)	;
					/*for (int i=0; i<result.length; i++) {
						System.out.print(result[i]);
					}*/
					tasks.add(getTask(result[0]));
				}

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
