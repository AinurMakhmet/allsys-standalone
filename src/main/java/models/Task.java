package models;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
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
    @DatabaseField(columnName = "high_priority")
    private boolean highPriority;
    @DatabaseField
    private int duration;
    @DatabaseField(columnName = "latest_completion_time")
    private Date latestCompletionTime;
    @DatabaseField(columnName = "earliest_completion_time")
    private Date earliestCompletionTime;
    @DatabaseField(columnName = "employee_id", foreign = true, foreignAutoRefresh = true)
    private Employee employee;
    @DatabaseField(columnName = "project_id", foreign = true, foreignAutoRefresh = true)
    private Project project;
    @ForeignCollectionField(eager = true)
    private ForeignCollection<TaskSkill> skills;

    public HashMap<Integer, Integer> possibleAssignee = new HashMap<>();


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

    public boolean isHighPriority() {
        return highPriority;
    }

    public void setHighPriority(boolean highPriority) {
        this.highPriority = highPriority;
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

    public Date getEarliestCompletionTime() {
        return earliestCompletionTime;
    }

    public void setEarliestCompletionTime(Date earliestCompletionTime) {
        this.earliestCompletionTime = earliestCompletionTime;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
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
