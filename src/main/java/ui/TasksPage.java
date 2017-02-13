package ui;

import entity_utils.TaskUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import models.Skill;
import models.Task;

import java.io.IOException;

/**
 * Created by nura on 06/12/16.
 */
public class TasksPage extends AbstractPage implements ChangeListener{
    static TableView table;
    static String[] cardValues;
    static final ObservableList<Task> data = FXCollections.observableArrayList(
            TaskUtils.getAllTasks());

    private static TasksPage ourInstance = new TasksPage();

    public static TasksPage getInstance() {
        return ourInstance;
    }

    private TasksPage() {
        super();
        setCenter(addTable("Tasks"));
    }

    static TableView addTable(String pageName) {
        table = AbstractPage.addTable(pageName);
        TableColumn id = new TableColumn("ID");
        TableColumn name = new TableColumn("Name");
        TableColumn employee = new TableColumn("Employee");
        TableColumn startTime = new TableColumn("Start time");
        TableColumn endTime = new TableColumn("End Time");

        id.setMinWidth(50);
        id.setCellValueFactory(
                new PropertyValueFactory<Task, String>("id"));

        name.setMinWidth(100);
        name.setCellValueFactory(
                new PropertyValueFactory<Task, String>("name"));

        employee.setMinWidth(150);
        employee.setCellValueFactory(
                new PropertyValueFactory<Task, String>("employeeName"));

        startTime.setMinWidth(100);
        startTime.setCellValueFactory(
                new PropertyValueFactory<Task, String>("startTime"));

        endTime.setMinWidth(100);
        endTime.setCellValueFactory(
                new PropertyValueFactory<Task, String>("endTime"));

        table.getColumns().addAll(id, name, startTime, endTime, employee);
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
                            skills,
                            ((Task) newSelection).getEmployee()==null ? "not allocated" : ((Task) newSelection).getEmployee().getFirstName()+ ((Task) newSelection).getEmployee().getLastName()
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
        String[] names = {"ID", "Name", "Description", "Start date", "End Date", "Skills required", "Allocated to employee"};
        cardValues = new String[]{"1", "Java development", "", "2016-12-01", "2016-12-03", "Java, git, JUnit, Spring", "Aydin Dede"};
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
}
