package models;

import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import entity_utils.ProjectUtils;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Model to represent a project.
 */
@DatabaseTable(tableName = "project")
public class Project extends DatabaseEntity{
    @DatabaseField(canBeNull = false)
    private String name;
    @DatabaseField
    private String description;
    @DatabaseField
    private Integer price;
    @DatabaseField
    private Integer cost;
    @DatabaseField
    private Integer profit;
    @ForeignCollectionField(eager = true)
    private ForeignCollection<Task> tasks;

    private Date endTime;
    private Date startTime;

    private Integer estimatedCost, estimatedProfit;

    /**
     * Blank constructor for ORM.
     */
    public Project() {
    }

    public Project(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        ProjectUtils.updateEntity(this);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        ProjectUtils.updateEntity(this);
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
        ProjectUtils.updateEntity(this);
    }

    public Date getEndTime() {
        try {
            if(!getTasks().isEmpty()) {
                Date latestEndTime = getTasks().get(0).getEndTime();
                for (Task task : getTasks()) {
                    if (task.getEndTime().after(latestEndTime)) {
                        latestEndTime = task.getEndTime();
                    }
                }
                endTime = latestEndTime;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return endTime;
    }

    public Date getStartTime() {
        try {
            if (!getTasks().isEmpty()) {
                Date earliestStartTime = getTasks().get(0).getStartTime();
                for (Task task : getTasks()) {
                    if (task.getStartTime().before(earliestStartTime)) {
                        earliestStartTime = task.getStartTime();
                    }
                }
                startTime = earliestStartTime;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return startTime;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
        ProjectUtils.updateEntity(this);
    }

    public Integer getProfit() {
        return profit;
    }

    public void setProfit(Integer profit) {
        this.profit = profit;
        ProjectUtils.updateEntity(this);
    }

    public Integer getEstimatedCost() {
        return estimatedCost;
    }

    public void setEstimatedCost(Integer estimatedCost) {
        this.estimatedCost = estimatedCost;
    }

    public Integer getEstimatedProfit() {
        return estimatedProfit;
    }

    public void setEstimatedProfit(Integer estimatedProfit) {
        this.estimatedProfit = estimatedProfit;
    }

    /**
     * Gets a list of tasks that the project includes.
     *
     * @return a list of tasks that the project includes.
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Project project = (Project) o;

        if (!getId().equals(project.getId())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
