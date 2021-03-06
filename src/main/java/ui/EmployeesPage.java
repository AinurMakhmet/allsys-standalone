package ui;

import entity_utils.EmployeeUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.util.converter.IntegerStringConverter;
import models.Employee;
import models.Skill;
import models.SystemData;
import models.Task;

import java.io.IOException;

/**
 * models gui page Employees
 */
public class EmployeesPage extends AbstractPage {
    final ObservableList<Employee> data = FXCollections.observableArrayList(SystemData.getAllEmployeesMap().values());
    private TableColumn firstName, lastName, dailySalary;
    private static EmployeesPage ourInstance = new EmployeesPage();

    public static EmployeesPage getInstance() {
        return ourInstance;
    }

    private EmployeesPage() {
        super();
        setCenter(addTable("Employees"));

        final TextField addFirstName = new TextField();
        addFirstName.setPromptText("First Name");
        addFirstName.setMaxWidth(100);
        final TextField addLastName = new TextField();
        addLastName.setPromptText("Last Name");
        addLastName.setMaxWidth(100);
        final TextField addDailySalary = new TextField();
        addDailySalary.setPromptText("Daily Salary");
        addDailySalary.setMaxWidth(150);

        addButton.setTooltip(new Tooltip("Add employee"));
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    Integer salary = null;
                    if (!addDailySalary.getText().equals("")) {
                        salary = Integer.parseInt(addDailySalary.getText());
                    }
                    if (salary!=null && salary<0) {
                        MainUI.alertError("Invalid input", "Employee salary cannot be of a negative value ");
                        return;
                    }
                    if (addFirstName.getText().equals("") || addLastName.getText().equals("")) {
                        MainUI.alertError("Invalid input", "An employee must have a first and a last names.");
                        return;
                    }
                    Employee newEmployee = new Employee(addFirstName.getText(), addLastName.getText(), salary);
                    EmployeeUtils.createEntity(Employee.class, newEmployee);
                    Integer id = newEmployee.getId();
                    if (id!=null) {
                        newEmployee = EmployeeUtils.getEmployee(newEmployee.getId());
                        SystemData.getAllEmployeesMap().put(id, newEmployee);
                        data.add(newEmployee);
                        addFirstName.clear();
                        addLastName.clear();
                        addDailySalary.clear();
                        table.refresh();
                    }
                } catch (NumberFormatException | ClassCastException exc) {
                    MainUI.alertError("Invalid input", "Please enter only numbers to the Salary field or leave it blank.");
                }
            }
        });
        bottom.getChildren().addAll(addFirstName, addLastName, addDailySalary, addButton);
    }

    @Override
    TableView addTable(String pageName) {
        table = super.addTable(pageName);
        TableColumn id = new TableColumn("ID");
        firstName = new TableColumn("First Name");
        lastName= new TableColumn("Last Name");
        dailySalary = new TableColumn("Daily Salary");
        id.setMinWidth(50);
        id.setCellValueFactory(
                new PropertyValueFactory<Employee, String>("id"));
        firstName.setMinWidth(100);
        firstName.setCellValueFactory(
                new PropertyValueFactory<Employee, String>("firstName"));
        lastName.setMinWidth(150);
        lastName.setCellValueFactory(
                new PropertyValueFactory<Employee, String>("lastName"));
        dailySalary.setMinWidth(100);
        dailySalary.setCellValueFactory(
                new PropertyValueFactory<Employee, String>("dailySalary"));
        table.getColumns().addAll(id, firstName, lastName, dailySalary);
        table.setItems(data);
        setEditableCells();
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection!=null) {
                try {
                    String skills = "---";
                    if (((Employee) newSelection).getTasks()!=null) {
                        skills = "";
                        for (Skill skill : ((Employee) newSelection).getSkills()) {
                            skills += skill.getName()+ " (Level " +skill.getLevel()+")\n ";
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
                            ((Employee) newSelection).getDailySalary()==null ? "" : ((Employee) newSelection).getDailySalary().toString(),
                            skills,
                            tasks
                    };
                    setNewCard(cardValues, (Employee)newSelection);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        deleteEntryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ObservableList selectedCells = table.getSelectionModel().getSelectedCells();
                TablePosition tablePosition = (TablePosition) selectedCells.get(0);
                Employee toRemove = ((Employee) table.getItems().get(tablePosition.getRow()));

                EmployeeUtils.deleteEntity(Employee.class, toRemove);
                if (EmployeeUtils.getEmployee(toRemove.getId())==null) {
                    SystemData.getAllEmployeesMap().remove(toRemove.getId());
                    data.remove(toRemove);
                    table.refresh();
                } else {
                    MainUI.alertError("Cannot delete", "There might be some problem connecting to the database.");
                }
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

        //The following snippet of code is based on http://stackoverflow.com/a/34701925
        dailySalary.setCellFactory(col -> new TextFieldTableCell<Employee, Integer>(new EditIntegerStringConverter()) {
            @Override
            public void updateItem(Integer item, boolean empty) {
                if (empty) {
                    super.updateItem(item, empty) ;
                } else {
                    // if out of range, revert to previous value:
                    if (item!=null && item.intValue() < 0) {
                        item = getItem();
                        MainUI.alertError("Invalid input", "Please enter a positive number.");
                    }
                    super.updateItem(item, empty);
                }
            }
        });

        dailySalary.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Employee, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Employee, Integer> event) {
                Integer salary = event.getNewValue();
                if (salary!=null && salary<=0) {
                } else {
                    ((Employee) event.getTableView().getItems().get(
                            event.getTablePosition().getRow())
                    ).setDailySalary(salary);
                }
            }
        });

    }

    HBox addCard() {
        String[] names = {"ID", "First Name", "Last Name", "Daily Salary", "Skills", "Allocated to tasks"};
        cardValues = new String[]{"", "", "", "", "", ""};
        return super.addCard(names, cardValues);
    }

    public static class EditIntegerStringConverter extends IntegerStringConverter {
        @Override
        public Integer fromString(String value) {
            // If the invlid format show the dialog
            try {
                if (value.equals("")) return null;
                Integer.valueOf(value);
                return  super.fromString(value);
            } catch (NumberFormatException  | ClassCastException e) {
                return -1;
            }
        }
    }
}
