package models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by nura on 19/11/16.
 */
@DatabaseTable(tableName = "task")
public class Task extends DatabaseEntity{
    @DatabaseField
    private String name;
    @DatabaseField
    private String description;
    //private String state;
    @DatabaseField
    private boolean highPriority;
    @DatabaseField
    private int duration;
    @DatabaseField
    private Date latestCompletionTime;
    @DatabaseField
    private Date earliestCompletionTime;
    @DatabaseField
    private int employeeId;
    @DatabaseField
    private int projectId;
    private ArrayList<Integer> skillIdList;

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

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public ArrayList<Integer> getSkillIdList() {
        return skillIdList;
    }

    public boolean addSkillId(int skillId) {
        return this.skillIdList.add(skillId);
    }

}
