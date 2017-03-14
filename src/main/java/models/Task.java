package models;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import entity_utils.TaskUtils;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Model to represent task.
 */
@DatabaseTable(tableName = "task")
public class Task implements DatabaseEntity{
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
    public ArrayList<Employee> possibleAssignee;
    private Employee recommendedAssignee;
    private String recommendedAssigneeName;

    // ORMLite needs a no-arg constructor
    public Task() {
    }

    public Task(String name, Date startTime, Date endTime, Priority priority) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priority = priority;
    }


    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (!getId().equals(task.getId())) return false;
        return getName().equals(task.getName());

    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getName().hashCode();
        return result;
    }

    public enum Priority {
        HIGH, MEDIUM, LOW
    }

    private String employeeName;

    public String getEmployeeId() {
        if (employee!=null){
            return employee.getId().toString();
        }
        return null;
    }

    public String getProjectId() {
        if (project!=null) {
            return project.getId().toString();
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        TaskUtils.updateEntity(this);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        TaskUtils.updateEntity(this);
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
        TaskUtils.updateEntity(this);
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
        TaskUtils.updateEntity(this);
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
        TaskUtils.updateEntity(this);
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        if (possibleAssignee!=null && possibleAssignee.size()>0) {
            possibleAssignee.clear();
        }
        this.employee = employee;
        if (employee!=null) {
            employeeName = employee.getFirstName() + " " + employee.getLastName();
        } else {
            employeeName =null;
        }
        TaskUtils.updateEntity(this);
    }

    public String getEmployeeName() {
        if (employeeName==null && employee!=null) {
            employeeName = employee.getFirstName() + " "+ employee.getLastName();
        }
        return employeeName;
    }

    public Employee getRecommendedAssignee() {
        return recommendedAssignee;
    }

    public void setRecommendedAssignee(Employee recommendedAssignee) {
        if (recommendedAssignee!=null) {
            if (this.recommendedAssignee!=null) {
                this.recommendedAssignee.getMatchedTasks().remove(this);
            }
            this.recommendedAssignee = SystemData.getAllEmployeesMap().get(recommendedAssignee.getId());
            recommendedAssigneeName = this.recommendedAssignee.getFirstName()+ " " + this.recommendedAssignee.getLastName();
            this.recommendedAssignee.addMatchedTasks(this);
        } else {
            if (this.recommendedAssignee!=null) {
                this.recommendedAssignee.getMatchedTasks().remove(this);
            }
            this.recommendedAssignee=null;
            this.recommendedAssigneeName=null;
        }
    }

    public String getRecommendedAssigneeName() {
        return recommendedAssigneeName;
    }

    public void setRecommendedAssigneeName(String recommendedAssigneeName) {
        this.recommendedAssigneeName = recommendedAssigneeName;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
        TaskUtils.updateEntity(this);
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
        //TODO: check the whether it is strongly greater and strongly less 0;
/*
        if (getStartTime().equals(anotherTask.getStartTime())
                || getStartTime().compareTo(anotherTask.getEndTime())<=0
                || getEndTime().compareTo(anotherTask.getStartTime())>=0) return true;
*//*
        if (getStartTime().equals(anotherTask.getStartTime())
                || (getStartTime().before(anotherTask.getEndTime())&& getEndTime().after(anotherTask.getStartTime()))
                || getEndTime().after(anotherTask.getStartTime())
                || getStartTime().before(anotherTask.getStartTime())) return true;
        */
        if (getEndTime().before(anotherTask.getStartTime())
                || anotherTask.getEndTime().before(getStartTime())) return false;
        return true;
    }

    public String toString() {
        String toReturn =  "Task "+ name + "(ID = " + getId()+")"
                + " that starts on " + startTime+ " and ends on " + endTime;

        if (recommendedAssignee!=null) {
            toReturn += " is recommended to be assigned to employee " + recommendedAssignee.getFirstName()
                    + " " + recommendedAssignee.getLastName() + "(ID = " + recommendedAssignee.getId() + ")";
        } else {
            toReturn+=" with no recommended assignee";
        }
            return toReturn;
    }
}
