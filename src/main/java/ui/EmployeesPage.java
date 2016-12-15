package ui;

import entity_utils.EmployeeUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Employee;

/**
 * Created by nura on 06/12/16.
 */
public class EmployeesPage extends AbstractPage {
    final ObservableList<Employee> data = FXCollections.observableArrayList(
            EmployeeUtils.getAllEmployees());


    private static EmployeesPage ourInstance = new EmployeesPage();

    public static EmployeesPage getInstance() {
        return ourInstance;
    }

    private EmployeesPage() {
        super();
        setCenter(addTable("Employees"));
    }


    @Override
    public TableView addTable(String pageName) {
        TableView table = super.addTable(pageName);
        TableColumn id = new TableColumn("ID");
        TableColumn firstName = new TableColumn("First Name");
        TableColumn lastName= new TableColumn("Last Name");
        TableColumn monthlySalary = new TableColumn("Monthly Salary");

        id.setMinWidth(50);
        id.setCellValueFactory(
                new PropertyValueFactory<Employee, String>("id"));

        firstName.setMinWidth(100);
        firstName.setCellValueFactory(
                new PropertyValueFactory<Employee, String>("firstName"));

        lastName.setMinWidth(150);
        lastName.setCellValueFactory(
                new PropertyValueFactory<Employee, String>("lastName"));

        monthlySalary.setMinWidth(100);
        monthlySalary.setCellValueFactory(
                new PropertyValueFactory<Employee, String>("monthlySalary"));


        table.getColumns().addAll(id, firstName, lastName, monthlySalary);
        table.setItems(data);
        return table;
    }
}
