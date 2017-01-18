package models;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import models.graph_models.BipartiteGraphNode;
import models.graph_models.BipartiteGraphNodeType;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Model to represent task.
 */
@DatabaseTable(tableName = "task")
public class Task extends BipartiteGraphNode implements DatabaseEntity{
    @DatabaseField(generatedId = true)
    private Integer id;
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
    @DatabaseField(columnName = "end_time")
    private Date endTime;
    @DatabaseField(columnName = "start_time")
    private Date startTime;
    @DatabaseField(columnName = "employee_id", foreign = true, foreignAutoRefresh = true, defaultValue = "0")
    private Employee employee;
    @DatabaseField(columnName = "project_id", foreign = true, foreignAutoRefresh = true)
    private Project project;
    @ForeignCollectionField(eager = true)
    private ForeignCollection<TaskSkill> skills;

    public ArrayList<BipartiteGraphNode> connectedEmployeeNodes = new ArrayList<>();
    public ArrayList<Employee> possibleAssignee;

    @Override
    public Integer getId() {
        return id;
    }

    public enum Priority {
        HIGH, MEDIUM, LOW
    }

    private String employeeName;

    public Task() {
        // ORMLite needs a no-arg constructor
        nodeType = BipartiteGraphNodeType.TASK;
    }

    public Task(String name) {
        this.name = name;
        nodeType = BipartiteGraphNodeType.TASK;
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

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        possibleAssignee.clear();
        this.employee = employee;
        employeeName = employee.getFirstName()+ " " + employee.getLastName();
    }

    public String getEmployeeName() {
        return employeeName;
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

    /**
     * Compares two Tasks for time overlapping.
     *
     * @param   anotherTask   the Task to be compared.
     * @return  the value true if the argument Task starts on the same day as
     *          this Task; or if this Task starts before the argument Task ends;
     *          or if this Task ends after the Task argument starts.
     * @exception NullPointerException if <code>anotherDate</code> is null.
     */
    public boolean timeOverlapWith(Task anotherTask) {
        if (getStartTime().equals(anotherTask.getStartTime())
                || getStartTime().compareTo(anotherTask.getEndTime())<0
                || getEndTime().compareTo(anotherTask.getStartTime())>0) return true;
        return false;
    }

    public String toString() {
        return "Task "+ name + "(ID = " + getId()+")"
                + " that starts on " + startTime+ " and ends on " + endTime;
//                + " is allocated to employee " + getEmployee().getFirstName()
//                + " " + getEmployee().getLastName()+"(ID = "+ getEmployee().getId()+ ")";
    }
}
