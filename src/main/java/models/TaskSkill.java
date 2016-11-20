package models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Model to represent a particular skill that a particular task requires.
 */
@DatabaseTable(tableName = "task_skill")
public class TaskSkill extends DatabaseEntity{
    @DatabaseField(columnName = "task_id", foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Task task;
    @DatabaseField(columnName = "skill_id", foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Skill skill;

    /**
     * Blank constructor for ORM.
     */
    public TaskSkill() {
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

}
