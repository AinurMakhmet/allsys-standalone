package ui;

import entity_utils.EmployeeUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import models.Employee;
import models.Skill;
import models.SystemData;
import models.Task;

import java.io.IOException;
import java.util.List;

/**
 * Created by nura on 06/12/16.
 */
public class EmployeesPage extends AbstractPage {
    final ObservableList<Employee> data = FXCollections.observableArrayList(SystemData.getAllEmployeesMap().values());
    TableView table;
    private String[] cardValues;

    private static EmployeesPage ourInstance = new EmployeesPage();

    public static EmployeesPage getInstance() {
        return ourInstance;
    }

    private EmployeesPage() {
        super();
        search.setPromptText("Search employees");
        setCenter(addTable("Employees"));
    }

    @Override
    TableView addTable(String pageName) {
        table = super.addTable(pageName);
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

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection!=null) {
                try {
                    String skills = "---";
                    if (((Employee) newSelection).getTasks()!=null) {
                        skills = "";
                        for (Skill skill : ((Employee) newSelection).getSkills()) {
                            skills += skill.getName() + ", ";
                        }
                    }

                    String tasks = "not allocated";
                    if (((Employee) newSelection).getTasks()!=null) {
                        tasks = "";
                        for (Task task: ((Employee) newSelection).getTasks()) {
                            tasks+=task.getName() +", ";
                        }
                    }

                    cardValues= new String[]{
                            ((Employee)newSelection).getId().toString(),
                            ((Employee)newSelection).getFirstName(),
                            ((Employee) newSelection).getLastName(),
                            ((Employee) newSelection).getMonthlySalary()==null ? "---" : ((Employee) newSelection).getMonthlySalary().toString(),
                            skills,
                            tasks
                    };
                    setNewCard(cardValues);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //setNewCard(newSelection.toString());
            }
        });
        return table;
    }

    VBox addCard() {
        String[] names = {"ID", "First Name", "Last Name", "Monthly Salary", "Skills", "Allocated to tasks"};
        cardValues = new String[]{"--", "---", "---", "----", "---", "not allocated to tasks"};
        return super.addCard(names, cardValues);
    }
}
