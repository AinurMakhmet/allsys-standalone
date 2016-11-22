package entity_utils;

import models.Employee;

import java.util.List;

/**
 * A class for reading data from the Employee table in the database.
 */
public class EmployeeUtils extends AbstractEntityUtils {

	/**
	 * Gets the {@link models.Employee} object with the specified ID.
	 *
	 * @param id the <code>Employee</code> ID
	 * @return the associated <code>Employee</code> object
	 */
	public static Employee getEmployee(String id) {
		try {
			return getEmployee(Integer.parseInt(id));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Gets the {@link models.Employee} object with the specified ID.
	 *
	 * @param id the <code>Employee</code> ID
	 * @return the associated <code>Employee</code> object
	 */
	public static Employee getEmployee(int id) {
		return getEntityById(Employee.class, id);
	}

	/**
	 * Gets all the {@link models.Employee} objects in the database.
	 * <p>
	 * Can return data only for the <code>enabled</code> fields.
	 *
	 * @return a list of <code>Employee</code> objects
	 */
	public static List<Employee> getAllEmployees() {
		return getAllEntities(Employee.class);
	}
}
