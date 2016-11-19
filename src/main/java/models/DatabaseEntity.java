package models;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by nura on 19/11/16.
 */
public abstract class DatabaseEntity {
    @DatabaseField(generatedId = true)
    private int id;

    public int getId() {
        return id;
    }
}
