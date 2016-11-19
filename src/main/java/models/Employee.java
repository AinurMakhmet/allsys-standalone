package models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;

/**
 * Created by nura on 19/11/16.
 */
@DatabaseTable(tableName = "employee")
public class Employee extends DatabaseEntity{
    @DatabaseField
    private String firstName;
    @DatabaseField
    private String lastName;
    @DatabaseField
    private String monthlySalary;

    private ArrayList<Integer> skillIdList;

    public Employee() {
        // ORMLite needs a no-arg constructor
    }

    public Employee(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMonthlySalary() {
        return monthlySalary;
    }

    public void setMonthlySalary(String monthlySalary) {
        this.monthlySalary = monthlySalary;
    }

    public ArrayList<Integer> getSkillIdList() {
        return skillIdList;
    }

    public boolean setSkillId(int skillId) {
        return this.skillIdList.add((Integer)skillId);
    }
}
