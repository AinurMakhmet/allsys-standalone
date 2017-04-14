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
import java.util.concurrent.TimeUnit;

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
    public ArrayList<Employee> possibleAssignees;
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

    public Integer getPriorityNumber(){
        if (getPriority().equals(Task.Priority.HIGH)) {
            return 1;
        } else if (getPriority().equals(Task.Priority.MEDIUM)) {
            return 2;
        } else if (getPriority().equals(Task.Priority.LOW)) {
            return 3;
        }
        return 3;
    }

    private String employeeName;

    public Integer getEmployeeId() {
        if (employee!=null){
            return employee.getId();
        }
        return null;
    }

    public Integer getProjectId() {
        if (project!=null) {
            return project.getId();
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
        if (possibleAssignees !=null && possibleAssignees.size()>0) {
            possibleAssignees.clear();
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

    public int getDuration() {
        long duration  = endTime.getTime() - startTime.getTime();
        int diffInDays = (int)TimeUnit.MILLISECONDS.toDays(duration);
        return diffInDays;
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
     * The method has been developed in the context
     * of another project -  2nd year Software Engineering Group project at King's College London-
     * https://github.com/musalbas/Nuclibook
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
        }
        return output;
    }

    /**
     *
     * @return collection of taskSkill objects that associated with this task.
     * @see models.TaskSkill
     */
    public ForeignCollection<TaskSkill> getTaskSkillObjects() throws IOException {
        return skills;
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
        if (this.equals(anotherTask) || getEndTime().before(anotherTask.getStartTime())
                || anotherTask.getEndTime().before(getStartTime())) return false;
        return true;
    }

    /*public String toString() {
        String toReturn =  "Task "+ name + "(ID = " + getId()+")"
                + " that starts on " + startTime+ " and ends on " + endTime;

        if (recommendedAssignee!=null) {
            toReturn += " is recommended to be assigned to employee " + recommendedAssignee.getFirstName()
                    + " " + recommendedAssignee.getLastName() + "(ID = " + recommendedAssignee.getId() + ")";
        } else {
            toReturn+=" with no recommended assignee";
        }
        String skillsString = "";
        try {
            for (Skill s: getSkills()) {
                skillsString += s.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        toReturn+=" requires skills "+ skillsString;

            return toReturn;
    }*/

    @Override
    public String toString() {
        String toReturn="";

        if (recommendedAssignee!=null) {
            toReturn = "Task{" +
                    "name='" + name + '\'' +
                    '}';
            toReturn += " is recommended to be assigned to employee " + recommendedAssignee.getFirstName()
                    + " " + recommendedAssignee.getLastName() + "(ID = " + recommendedAssignee.getId() + ")";
        } else {

            String skillsString = "";
            try {
                for (Skill s: getSkills()) {
                    skillsString += s.toString();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            toReturn = "Task{" +
                    "name='" + name + '\'' +
                    ", priority=" + priority +
                    ", endTime=" + endTime +
                    ", startTime=" + startTime +
                    //", employee=" + employee +
                    //", project=" + project +
                    ", requiredSkills=[" + skillsString +"]"+
                    //", possibleAssignees=" + possibleAssignees +
                    //", recommendedAssignee=" + recommendedAssignee +
                    //", recommendedAssigneeName='" + recommendedAssigneeName + '\'' +
                    //", employeeName='" + employeeName + '\'' +
                    '}';
        }

        return toReturn;
    }

    public String getRecommendation() {
        String toReturn="Task{" +
                "name='" + name + '\'' +
                '}';

        if (recommendedAssignee!=null) {
            toReturn += " is recommended to be assigned to employee " + recommendedAssignee.getFirstName()
                    + " " + recommendedAssignee.getLastName() + "(ID = " + recommendedAssignee.getId() + ")";
        } else {
            toReturn+=" with no recommended assignee";
        }
        return toReturn;
    }
}
