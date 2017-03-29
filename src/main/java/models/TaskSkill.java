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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskSkill taskSkill = (TaskSkill) o;

        if (!getTask().equals(taskSkill.getTask())) return false;
        return getSkill().equals(taskSkill.getSkill());

    }

    @Override
    public int hashCode() {
        int result = getTask().hashCode();
        result = 31 * result + getSkill().hashCode();
        return result;
    }
}
