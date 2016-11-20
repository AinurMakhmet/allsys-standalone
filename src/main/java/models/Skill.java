package models;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Model to represent a skill.
 */
@DatabaseTable(tableName = "skill")
public class Skill extends DatabaseEntity{
    @DatabaseField(canBeNull = false)
    private String name;
    @DatabaseField
    private String description;
    @DatabaseField(canBeNull = false)
    private String level;
    @ForeignCollectionField(eager = true)
    private ForeignCollection<EmployeeSkill> employees;
    @ForeignCollectionField(eager = true)
    private ForeignCollection<TaskSkill> tasks;


    /**
     * Blank constructor for ORM.
     */
    public Skill() {
    }

    public Skill(String name, String level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }


    /**
     * Gets a list of employees that possess the skill.
     *
     * @return a list of employees that have the skill.
     * @see models.Employee
     */
    public List<Employee> getEmployees() throws IOException {
        ArrayList<Employee> output = new ArrayList<>();
        try {
            employees.refreshCollection();
        } catch (SQLException | NullPointerException e) {
            return output;
        }
        try (CloseableIterator<EmployeeSkill> iterator = employees.closeableIterator()){
            Employee employee;
            while (iterator.hasNext()) {
                employee = iterator.next().getEmployee();
                if (employee != null) output.add(employee);
            }

            //TODO: review sorting
            // sort by sequence
            /*output.sort(new Comparator<Employee>() {
                @Override
                public int compare(Employee o1, Employee o2) {
                    return o1.getSequence() - o2.getSequence();
                }
            });*/
        }
        return output;
    }

    /**
     * Gets a list of tasks that requires the skill.
     *
     * @return a list of tasks that requires this skill.
     * @see models.Task
     */
    public List<Task> getTasks() throws IOException {
        ArrayList<Task> output = new ArrayList<>();
        try {
            tasks.refreshCollection();
        } catch (SQLException | NullPointerException e) {
            return output;
        }
        try (CloseableIterator<TaskSkill> iterator = tasks.closeableIterator()){
            Task task;
            while (iterator.hasNext()) {
                task = iterator.next().getTask();
                if (task != null) output.add(task);
            }

            //TODO: review sorting
            // sort by sequence
            /*output.sort(new Comparator<Task>() {
                @Override
                public int compare(Task o1, Task o2) {
                    return o1.getSequence() - o2.getSequence();
                }
            });*/
        }
        return output;
    }
}
