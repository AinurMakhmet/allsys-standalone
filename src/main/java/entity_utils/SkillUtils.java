package entity_utils;

import models.Skill;

import java.util.List;

/**
 * A class for reading data from the Skill table in the database.
 */
public class SkillUtils extends AbstractEntityUtils {

	/**
	 * Gets the {@link Skill} object with the specified ID.
	 *
	 * @param id the <code>Skill</code> ID
	 * @return the associated <code>Skill</code> object
	 */
	public static Skill getSkill(String id) {
		try {
			return getSkill(Integer.parseInt(id));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Gets the {@link Skill} object with the specified ID.
	 *
	 * @param id the <code>Skill</code> ID
	 * @return the associated <code>Skill</code> object
	 */
	public static Skill getSkill(int id) {
		return getEntityById(Skill.class, id);
	}

	/**
	 * Gets all the {@link Skill} objects in the database.
	 * <p>
	 * Can return data only for the <code>enabled</code> fields.
	 *
	 * @return a list of <code>Skill</code> objects
	 */
	public static List<Skill> getAllSkills() {
		return getAllEntities(Skill.class);
	}

	/**
	 * Updates the specified record in the database.
	 *
	 * @param skill the task to be updated
	 */
	public static void updateEntity(Skill skill) {
		// set up server connection
		updateEntity(Skill.class, skill);
	}
}
