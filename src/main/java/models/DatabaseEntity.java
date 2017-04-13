package models;

import com.j256.ormlite.field.DatabaseField;

/**
 * Models database entity.
 */
public abstract class DatabaseEntity {
    @DatabaseField(generatedId = true)
    private int id;

    public Integer getId() {
        return id;
    }
}
