package ui;

import entity_utils.EmployeeUtils;
import entity_utils.TaskUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import logic.Strategy;
import models.Employee;
import models.Task;

import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nura on 06/12/16.
 */
public class AllocationPage extends AbstractPage {
    TableView taskTable;
    TableView employeeTable;
    HBox pageHBox;
    String[] cardValues;
    final ObservableList<Task> data = FXCollections.observableArrayList(
            TaskUtils.getAllTasks());

    private static AllocationPage ourInstance = new AllocationPage();

    public static AllocationPage getInstance() {
        return ourInstance;
    }

    private AllocationPage() {
        super();
        Button recommendButton = new Button("Recommend Allocation");
        recommendButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                List<Task> result = Strategy.getLargestMatching(TaskUtils.getAllTasksValidForAllocation());
                taskTable.refresh();
            }
        });
        Button allocateButton = new Button("Allocate");
        top.getChildren().add(recommendButton);
        top.getChildren().add(allocateButton);
        pageHBox = new HBox();
        pageHBox.setSpacing(8);
        setCenter(pageHBox);
        addTables();
        //setCenter(addTable("Tasks-Employees"));
    }

    void addTables() {
        taskTable = TasksPage.addTable("Tasks");
        //TODO: add new column for recommendation
        //TableColumn employee = new TableColumn("Recommended Assignee (Employee)");
        //employee.setMinWidth(150);
        //employee.setCellValueFactory(
        //        new PropertyValueFactory<Task, String>("employeeName"));

        //taskTable.getItems().add();
        pageHBox.getChildren().add(taskTable);
    }

    VBox addCard() {
        String[] names = {"ID", "Name", "Description", "Start date", "End Date", "Skills required", "Allocated to employee"};
        String[] cardValues = {"1", "Java development", "", "2016-12-01", "2016-12-03", "Java, git, JUnit, Spring", "Aydin Dede"};
        return super.addCard(names, cardValues);
    }

}
