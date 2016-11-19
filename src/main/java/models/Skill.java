package models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by nura on 19/11/16.
 */
@DatabaseTable(tableName = "skill")
public class Skill extends DatabaseEntity{
    @DatabaseField
    private String name;
    @DatabaseField
    private String description;
    @DatabaseField
    private String level;

    public Skill() {
        // ORMLite needs a no-arg constructor
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
}
