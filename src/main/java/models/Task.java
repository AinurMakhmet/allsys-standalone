package models;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Model to represent task.
 */
@DatabaseTable(tableName = "task")
public class Task extends DatabaseEntity{
    @DatabaseField(canBeNull = false)
    private String name;
    @DatabaseField
    private String description;
    //private String state;
    @DatabaseField(columnName = "priority", dataType = DataType.ENUM_STRING, defaultValue = "LOW")
    private Priority priority;
    //how many days it takes to complete a task
    @DatabaseField
    private int duration;
    @DatabaseField(columnName = "latest_completion_time")
    private Date latestCompletionTime;
    @DatabaseField(columnName = "earliest_start_time")
    private Date earliestStartTime;
    @DatabaseField(columnName = "employee_id", foreign = true, foreignAutoRefresh = true, defaultValue = "0")
    private Employee employee;
    @DatabaseField(columnName = "project_id", foreign = true, foreignAutoRefresh = true)
    private Project project;
    @ForeignCollectionField(eager = true)
    private ForeignCollection<TaskSkill> skills;

    public ArrayList<Employee> possibleAssignee = new ArrayList<>();

    public enum Priority {
        HIGH, MEDIUM, LOW
    }

    public Task() {
        // ORMLite needs a no-arg constructor
    }

    public Task(String name) {
        this.name = name;
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

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Date getLatestCompletionTime() {
        return latestCompletionTime;
    }

    public void setLatestCompletionTime(Date latestCompletionTime) {
        this.latestCompletionTime = latestCompletionTime;
    }

    public Date getEarliestStartTime() {
        return earliestStartTime;
    }

    public void setEarliestCompletionTime(Date earliestCompletionTime) {
        this.earliestStartTime = earliestStartTime;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        possibleAssignee.clear();
        this.employee = employee;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * Gets a list of skills that this task requires.
     *
     * @return a list of skills that this task requires.
     * @see models.Skill
     */
    public List<Skill> getSkills() throws IOException {
        ArrayList<Skill > output = new ArrayList<>();
        try {
            skills.refreshCollection();
        } catch (SQLException | NullPointerException e) {
            return output;
        }
        try (CloseableIterator<TaskSkill> iterator = skills.closeableIterator()){
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
}
