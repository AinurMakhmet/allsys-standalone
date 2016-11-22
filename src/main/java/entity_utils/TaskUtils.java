package entity_utils;

import models.Task;

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
}
