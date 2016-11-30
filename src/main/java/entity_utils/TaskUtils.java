package entity_utils;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import models.Task;
import servers.SqlServerConnection;

import java.sql.SQLException;
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
	 * Updates the specified record in the database.
	 *
	 * @param task the task to be updated
	 */
	public static void updateEntity(Task task) {
		// set up server connection
		updateEntity(Task.class, task);
	}
}
