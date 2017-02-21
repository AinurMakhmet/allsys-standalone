package ui;

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
import models.Skill;
import models.Task;
import scala.reflect.internal.util.TableDef;

import java.io.IOException;
import java.util.List;

/**
 * Created by nura on 06/12/16.
 */
public class TasksPage extends AbstractPage implements ChangeListener, EventHandler<ActionEvent>{
    TableView table;
    private String[] cardValues;
    private HBox pageHBox;
    private List<Task> tasksToAllocate;
    private List<Task> result;
    private Button greedyRecButton;
    private Button ffRecButton;
    final ObservableList<Task> data = FXCollections.observableArrayList(
            TaskUtils.getAllTasks());

    private static TasksPage ourInstance = new TasksPage();

    public static TasksPage getInstance() {
        return ourInstance;
    }

    private TasksPage() {
        super();
        search.setPromptText("Search tasks");
        greedyRecButton = new Button("G REC");
        greedyRecButton.setOnAction(this);
        ffRecButton = new Button("FF REC");
        ffRecButton.setOnAction(this);
        Button allocateButton = new Button("Allocate");
        allocateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                result.forEach(task -> {
                    if (task.getEmployee()==null && task.getRecommendedAssigneeName()!=null) {
                        task.setEmployee(task.getRecommendedAssignee());
                        task.setRecommendedAssignee(null);
                        TaskUtils.updateEntity(task);
                    }
                });
                MainUI.refreshTables();

            }
        });
        top.getChildren().add(greedyRecButton);
        top.getChildren().add(ffRecButton);
        top.getChildren().add(allocateButton);
        pageHBox = new HBox();
        pageHBox.setSpacing(8);
        setCenter(pageHBox);
        pageHBox.getChildren().add(addTable("Tasks"));
    }

    @Override
    TableView addTable(String pageName) {
        table = super.addTable(pageName);
        TableColumn id = new TableColumn("ID");
        TableColumn name = new TableColumn("Name");
        TableColumn employeeName = new TableColumn("Employee");
        TableColumn startTime = new TableColumn("Start time");
        TableColumn endTime = new TableColumn("End Time");
        TableColumn priorityLevel = new TableColumn("Priority Level");
        TableColumn recommendedAssignee = new TableColumn("Recommended Assignee (Employee)");

        id.setMinWidth(50);
        id.setCellValueFactory(
                new PropertyValueFactory<Task, String>("id"));

        name.setMinWidth(100);
        name.setCellValueFactory(
                new PropertyValueFactory<Task, String>("name"));

        employeeName.setMinWidth(150);
        employeeName.setCellValueFactory(
                new PropertyValueFactory<Task, String>("employeeName"));

        startTime.setMinWidth(100);
        startTime.setCellValueFactory(
                new PropertyValueFactory<Task, String>("startTime"));

        endTime.setMinWidth(100);
        endTime.setCellValueFactory(
                new PropertyValueFactory<Task, String>("endTime"));

        priorityLevel.setMinWidth(130);
        priorityLevel.setCellValueFactory(new PropertyValueFactory<Task, String>("priority"));

        recommendedAssignee.setMinWidth(150);
        recommendedAssignee.setCellValueFactory(
                new PropertyValueFactory<Task, String>("recommendedAssigneeName"));

        table.getColumns().addAll(id, name, startTime, endTime, priorityLevel, employeeName, recommendedAssignee);
        table.setItems(data);

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
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
                            ((Task) newSelection).getPriority().toString(),
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

        return table;
    }

    VBox addCard() {
        String[] names = {"ID", "Name", "Description", "Start date", "End Date", "Priority Level", "Skills required", "Recommended Assignee", "Allocated to employee"};
        cardValues = new String[]{"1", "Java development", "", "2016-12-01", "2016-12-03", "Priority Level", "Java, git, JUnit, Spring", "Employee name here", "Employee name here"};
        return super.addCard(names, cardValues);
    }

    @Override
    public void changed(ObservableValue observable, Object oldValue, Object newValue) {
        if (newValue != null) {
            ObservableList selectedCells = table.getSelectionModel().getSelectedCells();
            TablePosition tablePosition = (TablePosition) selectedCells.get(0);
            Object val = tablePosition.getTableColumn().getCellData(newValue);
        }
    }

    @Override
    public void handle(ActionEvent event) {
        tasksToAllocate = data;
        if (((Button)event.getSource()).equals(greedyRecButton)) {
            result = Strategy.getMatchingUsingGreedy(tasksToAllocate);
            System.out.println("GREEDY");
        } else if (((Button)event.getSource()).equals(ffRecButton)) {
            result = Strategy.getLargestMatching(tasksToAllocate);
            System.out.println("FF");
        } else {
            return;
        }
        data.clear();
        data.addAll(result);
        table.setItems(data);
        //System.out.print("Total number of unallocated tasks: "+ Strategy.numberOfUnnalocatedTasks);
        //System.out.println("Among them number of tasks non-valid for allocation: "+ Strategy.numberOfTasksUnvalidForAllocation);
    }
}
