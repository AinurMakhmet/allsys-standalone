package models;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import models.graph_models.BipartiteGraphNode;
import models.graph_models.BipartiteGraphNodeType;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Model to represent an employee.
 */
@DatabaseTable(tableName = "employee")
public class Employee extends BipartiteGraphNode implements DatabaseEntity{
    @DatabaseField(generatedId = true)
    private Integer id;
    @DatabaseField(canBeNull = false, columnName = "first_name")
    private String firstName;
    @DatabaseField(canBeNull = false, columnName = "last_name")
    private String lastName;
    @DatabaseField(columnName = "monthly_salary")
    private String monthlySalary;
    @ForeignCollectionField(eager = true)
    private ForeignCollection<Task> tasks;
    @ForeignCollectionField(eager = true)
    private ForeignCollection<EmployeeSkill> skills;


    /**
     * Blank constructor for ORM.
     */
    public Employee() {
        nodeType = BipartiteGraphNodeType.EMPLOYEE;
    }

    public Employee(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        nodeType = BipartiteGraphNodeType.EMPLOYEE;
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(String monthlySalary) {
        this.monthlySalary = monthlySalary;
    }

    /**
     * Gets a list of skills that the employee possess.
     *
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

            //TODO: review sorting
            // sort by sequence
            /*output.sort(new Comparator<Skill>() {
                @Override
                public int compare(Skill o1, Skill o2) {
                    return o1.getSequence() - o2.getSequence();
                }
            });*/
        }
        return output;
    }

    /**
     * Gets a list of tasks that the employee is assigned to.
     *
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
