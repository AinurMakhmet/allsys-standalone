package models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Model to represent a particular skill of a particular employee.
 */
@DatabaseTable(tableName = "employee_skill")
public class EmployeeSkill implements DatabaseEntity{
    @DatabaseField(generatedId = true)
    private Integer id;
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
    public Integer getId() {
        return id;
    }
}
