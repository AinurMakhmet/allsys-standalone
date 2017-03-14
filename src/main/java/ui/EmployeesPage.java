package ui;

import entity_utils.EmployeeUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import models.Employee;
import models.Skill;
import models.SystemData;
import models.Task;

import java.io.IOException;

/**
 * Created by nura on 06/12/16.
 */
public class EmployeesPage extends AbstractPage {
    final ObservableList<Employee> data = FXCollections.observableArrayList(SystemData.getAllEmployeesMap().values());
    TableView table;
    private TableColumn firstName, lastName, monthlySalary;
    private String[] cardValues;

    private static EmployeesPage ourInstance = new EmployeesPage();

    public static EmployeesPage getInstance() {
        return ourInstance;
    }

    private EmployeesPage() {
        super();
        search.setPromptText("Search employees");
        setCenter(addTable("Employees"));

        final TextField addFirstName = new TextField();
        addFirstName.setPromptText("First Name");
        addFirstName.setMaxWidth(100);
        final TextField addLastName = new TextField();
        addLastName.setPromptText("Last Name");
        addLastName.setMaxWidth(100);
        final TextField addMonthlySalary = new TextField();
        addMonthlySalary.setPromptText("Monthly Salary");
        addMonthlySalary.setMaxWidth(150);

        final Button addButton = new Button("Add Employee");
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                Employee newEmployee = new Employee(addFirstName.getText(), addLastName.getText(), Integer.parseInt(addMonthlySalary.getText()));
                //TODO: handle null values
                Integer id = SystemData.getAllEmployeesMap().size();
                EmployeeUtils.createEntity(Employee.class, newEmployee);
                if (EmployeeUtils.getEmployee(id)!=null) {
                    SystemData.getAllEmployeesMap().put(id, newEmployee);
                    data.add(newEmployee);
                    addFirstName.clear();
                    addLastName.clear();
                    addMonthlySalary.clear();
                    table.refresh();
                }
            }
        });
        bottom.getChildren().addAll(addFirstName, addLastName, addMonthlySalary, addButton);
    }

    @Override
    TableView addTable(String pageName) {
        table = super.addTable(pageName);
        TableColumn id = new TableColumn("ID");
        firstName = new TableColumn("First Name");
        lastName= new TableColumn("Last Name");
        monthlySalary = new TableColumn("Monthly Salary");

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

        setEditableCells();

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

    private void setEditableCells() {
        firstName.setCellFactory(TextFieldTableCell.forTableColumn());
        firstName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Employee, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Employee, String> event) {
                String name = event.getNewValue();
                ((Employee) event.getTableView().getItems().get(
                        event.getTablePosition().getRow())
                ).setFirstName(name);
            }
        });
        lastName.setCellFactory(TextFieldTableCell.forTableColumn());
        lastName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Employee, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Employee, String> event) {
                String name = event.getNewValue();
                ((Employee) event.getTableView().getItems().get(
                        event.getTablePosition().getRow())
                ).setLastName(name);
            }
        });
        monthlySalary.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        monthlySalary.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Employee, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Employee, Integer> event) {
                //TODO: handle numberFormatException
                ((Employee) event.getTableView().getItems().get(
                        event.getTablePosition().getRow())
                ).setMonthlySalary(event.getNewValue());
            }
        });
    }

    VBox addCard() {
        String[] names = {"ID", "First Name", "Last Name", "Monthly Salary", "Skills", "Allocated to tasks"};
        cardValues = new String[]{"--", "---", "---", "----", "---", "not allocated to tasks"};
        return super.addCard(names, cardValues);
    }
}
