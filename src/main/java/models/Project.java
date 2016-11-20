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
 * Model to represent a project.
 */
@DatabaseTable(tableName = "project")
public class Project extends DatabaseEntity{
    @DatabaseField(canBeNull = false)
    private String name;
    @DatabaseField
    private String description;
    @DatabaseField
    private String cost;
    @ForeignCollectionField(eager = true)
    private ForeignCollection<Task> tasks;


    /**
     * Blank constructor for ORM.
     */
    public Project() {
    }

    public Project(String name) {
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

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
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
}