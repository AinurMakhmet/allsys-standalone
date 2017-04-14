package models;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import entity_utils.EmployeeUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * Model to represent an employee.
 */
@DatabaseTable(tableName = "employee")
public class Employee extends DatabaseEntity{
    @DatabaseField(canBeNull = false, columnName = "first_name")
    private String firstName;
    @DatabaseField(canBeNull = false, columnName = "last_name")
    private String lastName;
    @DatabaseField(columnName = "daily_salary")
    private Integer dailySalary;
    @ForeignCollectionField(eager = true)
    private ForeignCollection<Task> tasks;
    @ForeignCollectionField(eager = true)
    private ForeignCollection<EmployeeSkill> skills;
    private Set<Task> matchedTasks = new HashSet<>();


    /**
     * Blank constructor for ORM.
     */
    public Employee() {
    }

    public Employee(String firstName, String lastName, int dailySalary) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dailySalary = dailySalary;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        EmployeeUtils.updateEntity(this);
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        EmployeeUtils.updateEntity(this);
    }

    public Integer getDailySalary() {
        return dailySalary;
    }

    public void setDailySalary(Integer dailySalary) {
        this.dailySalary = dailySalary;
        EmployeeUtils.updateEntity(this);
    }

    /**
     * Gets a list of skills that the employee possess.
     * The method has been developed in the context
     * of another project -  2nd year Software Engineering Group project at King's College London-
     * https://github.com/musalbas/Nuclibook
     * @return a list of skills that the employee possess.
     * @see models.Skill
     */
    public List<Skill> getSkills() throws IOException {
        ArrayList<Skill > output = new ArrayList<>();
        try {
            skills.refreshCollection();
        } catch (SQLException | NullPointerException e) {
            return output;
        }
        try (CloseableIterator<EmployeeSkill> iterator = skills.closeableIterator()){
            Skill skill;
            while (iterator.hasNext()) {
                skill = iterator.next().getSkill();
                if (skill != null) output.add(skill);
            }
        }
        return output;
    }

    /**
     *
     * @return collection of employeSkill objects that associated with this employee.
     * @see models.EmployeeSkill
     */
    public ForeignCollection<EmployeeSkill> getEmployeeSkillObjects() throws IOException {
        return skills;
    }

    /**
     * Gets a list of tasks that the employee is assigned to.
     * The method has been developed in the context
     * of another project -  2nd year Software Engineering Group project at King's College London-
     * https://github.com/musalbas/Nuclibook
     * @return a list of tasks the employee is assigned to.
     * @see models.Task
     */
    public List<Task> getTasks() throws IOException {
        ArrayList<Task> output = new ArrayList<>();
        try {
            tasks.refreshCollection();
        } catch (SQLException | NullPointerException e) {
            return output;
        }
        try (CloseableIterator<Task> iterator = tasks.closeableIterator()){
            Task task;
            while (iterator.hasNext()) {
                task = iterator.next();
                if (task != null) output.add(task);
            }
        }
        return output;
    }

    public Set<Task> getMatchedTasks() {
        return matchedTasks;
    }

    public void setMatchedTasks(Set<Task> matchedTasks) {
        this.matchedTasks = matchedTasks;
    }

    public void addMatchedTasks(Task matchedTask) {
        matchedTasks.add(matchedTask);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Employee employee = (Employee) o;

        if (!getId().equals(employee.getId())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    @Override
    public String toString() {
        String matchedTasksString = "";
        for (Task t: matchedTasks) {
            matchedTasksString += t.toString()+ ", ";
        }

        String skillsString = "";
        try {
            for (Skill s: getSkills()) {
                skillsString += s.toString()+ ", ";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Employee{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", dailySalary=" + dailySalary +
                ", skills=[" + skillsString +"]"+
                //", matchedTasks=[" + matchedTasksString +"]"+
                '}';
    }
}
