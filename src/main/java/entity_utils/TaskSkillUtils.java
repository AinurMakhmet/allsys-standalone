package entity_utils;

import models.TaskSkill;

import java.util.List;

/**
 * A class for reading data from the TaskSkill table in the database.
 */
public class TaskSkillUtils extends AbstractEntityUtils {

	/**
	 * Gets the {@link TaskSkill} object with the specified ID.
	 *
	 * @param id the <code>TaskSkill</code> ID
	 * @return the associated <code>TaskSkill</code> object
	 */
	public static TaskSkill getTaskSkill(String id) {
		try {
			return getTaskSkill(Integer.parseInt(id));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Gets the {@link TaskSkill} object with the specified ID.
	 *
	 * @param id the <code>TaskSkill</code> ID
	 * @return the associated <code>TaskSkill</code> object
	 */
	public static TaskSkill getTaskSkill(int id) {
		return getEntityById(TaskSkill.class, id);
	}

	/**
	 * Gets all the {@link TaskSkill} objects in the database.
	 * <p>
	 * Can return data only for the <code>enabled</code> fields.
	 *
	 * @param enabledOnly specifies whether the method should get only <code>enabled TaskSkill</code> records
	 * @return a list of <code>TaskSkill</code> objects
	 */
	public static List<TaskSkill> getAllTasks(boolean enabledOnly) {
		if (enabledOnly) {
			return getEntitiesByField(TaskSkill.class, "enabled", "1");
		} else {
			return getAllEntities(TaskSkill.class);
		}
	}
}
