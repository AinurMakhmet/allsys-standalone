package models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Model to represent a relationship between a particular skill and an employee that possess that skill.
 */
@DatabaseTable(tableName = "employee_skill")
public class EmployeeSkill extends DatabaseEntity{
    @DatabaseField(columnName = "employee_id", foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Employee employee;
    @DatabaseField(columnName = "skill_id", foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Skill skill;

    /**
     * Blank constructor for ORM.
     */
    public EmployeeSkill() {
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public Skill getSkill() {
        return skill;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmployeeSkill that = (EmployeeSkill) o;

        if (!getEmployee().equals(that.getEmployee())) return false;
        return getSkill().equals(that.getSkill());

    }

    @Override
    public int hashCode() {
        int result = getEmployee().hashCode();
        result = 31 * result + getSkill().hashCode();
        return result;
    }
}
