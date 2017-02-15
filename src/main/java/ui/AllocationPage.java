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
import models.Skill;
import models.Task;

import javax.swing.plaf.basic.BasicOptionPaneUI;
import java.io.IOException;
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
                List<Task> tasksToAllocate = data;
                List<Task> result = Strategy.getLargestMatching(tasksToAllocate);
                data.clear();
                //data.removeAll();
                data.addAll(result);
                taskTable.setItems(data);
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
        taskTable = TasksPage.setTable("Tasks");
        //TODO: add new column for recommendation
        TableColumn matchedEmployeeCol = new TableColumn("Recommended Assignee (Employee)");
        matchedEmployeeCol.setMinWidth(150);
        matchedEmployeeCol.setCellValueFactory(
                new PropertyValueFactory<Task, String>("recommendedAssigneeName"));

        taskTable.getColumns().add(matchedEmployeeCol);

        taskTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection!=null) {
                try {
                    String skills = "---";
                    if (((Task) newSelection).getSkills()!=null){
                        skills="";
                        for (Skill skill : ((Task) newSelection).getSkills()) {
                            skills += skill.getName() + ", ";
                        }
                    }
                    cardValues= new String[]{
                            ((Task)newSelection).getId().toString(),
                            ((Task)newSelection).getName(),
                            ((Task) newSelection).getDescription(),
                            ((Task) newSelection).getStartTime().toString(),
                            ((Task) newSelection).getEndTime().toString(),
                            skills,
                            ((Task) newSelection).getRecommendedAssigneeName()==null ? "not recommendation" : ((Task) newSelection).getRecommendedAssigneeName(),
                            ((Task) newSelection).getEmployeeName()==null ? "not allocated" : ((Task) newSelection).getEmployeeName()
                    };
                    setNewCard(cardValues);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        pageHBox.getChildren().add(taskTable);
    }

    VBox addCard() {
        String[] names = {"ID", "Name", "Description", "Start date", "End Date", "Skills required", "Recommended Assignee", "Allocated to employee"};
        String[] cardValues = {"1", "Java development", "", "2016-12-01", "2016-12-03", "Java, git, JUnit, Spring", "", "Aydin Dede"};
        return super.addCard(names, cardValues);
    }

}
