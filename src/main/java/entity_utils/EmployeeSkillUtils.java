package entity_utils;


import models.EmployeeSkill;

import java.util.List;

/**
 * A class for reading data from the EmployeeSkill table in the database.
 */
public class EmployeeSkillUtils extends AbstractEntityUtils {

	/**
	 * Gets the {@link EmployeeSkill} object with the specified ID.
	 *
	 * @param id the <code>EmployeeSkill</code> ID
	 * @return the associated <code>EmployeeSkill</code> object
	 */
	public static EmployeeSkill getEmployee(String id) {
		try {
			return getEmployee(Integer.parseInt(id));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Gets the {@link EmployeeSkill} object with the specified ID.
	 *
	 * @param id the <code>EmployeeSkill</code> ID
	 * @return the associated <code>EmployeeSkill</code> object
	 */
	public static EmployeeSkill getEmployee(int id) {
		return getEntityById(EmployeeSkill.class, id);
	}

	/**
	 * Gets all the {@link EmployeeSkill} objects in the database.
	 * <p>
	 * Can return data only for the <code>enabled</code> fields.
	 *
	 * @param enabledOnly specifies whether the method should get only <code>enabled EmployeeSkill</code> records
	 * @return a list of <code>EmployeeSkill</code> objects
	 */
	public static List<EmployeeSkill> getAllEmployeeSkills(boolean enabledOnly) {
		if (enabledOnly) {
			return getEntitiesByField(EmployeeSkill.class, "enabled", "1");
		} else {
			return getAllEntities(EmployeeSkill.class);
		}
	}
}
