package entity_utils;

import models.Project;

import java.util.List;

/**
 * A class for reading data from the Project table in the database.
 */
public class ProjectUtils extends AbstractEntityUtils {

	/**
	 * Gets the {@link Project} object with the specified ID.
	 *
	 * @param id the <code>Project</code> ID
	 * @return the associated <code>Project</code> object
	 */
	public static Project getProject(String id) {
		try {
			return getProject(Integer.parseInt(id));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Gets the {@link Project} object with the specified ID.
	 *
	 * @param id the <code>Project</code> ID
	 * @return the associated <code>Project</code> object
	 */
	public static Project getProject(int id) {
		return getEntityById(Project.class, id);
	}

	/**
	 * Gets all the {@link Project} objects in the database.
	 * <p>
	 * Can return data only for the <code>enabled</code> fields.
	 *
	 * @param enabledOnly specifies whether the method should get only <code>enabled Project</code> records
	 * @return a list of <code>Project</code> objects
	 */
	public static List<Project> getAllProjects(boolean enabledOnly) {
		if (enabledOnly) {
			return getEntitiesByField(Project.class, "enabled", "1");
		} else {
			return getAllEntities(Project.class);
		}
	}
}
