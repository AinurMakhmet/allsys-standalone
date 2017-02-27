package ui;

import entity_utils.TaskUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.Project;
import models.SystemData;
import models.Task;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by nura on 06/12/16.
 */
public class ProjectsPage extends AbstractPage implements ChangeListener, EventHandler<ActionEvent>{
    final ObservableList<Project> data = FXCollections.observableArrayList(SystemData.getAllProjectsMap().values());
    TableView table;
    private String[] cardValues;
    private HBox pageHBox;
    private List<Project> projectsToAllocate;
    private List<Project> result;
    private List<Project> selectedProjects = new LinkedList<>();
    private Button maxProfitRecButton;
    private Button selectProjectsButton;
    private List<Project> lastSavedAllocation;
    private ListChangeListener<Project> multipleSelectionListener;
    ChangeListener changeListener;


    private static ProjectsPage ourInstance = new ProjectsPage();

    public static ProjectsPage getInstance() {
        return ourInstance;
    }

    private ProjectsPage() {
        super();
        search.setPromptText("Search projects");
        maxProfitRecButton = new Button("MaxProfit REC");
        maxProfitRecButton.setOnAction(this);
        Button allocateButton = new Button("Allocate");
        allocateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedProjects.forEach(project -> {
                    try {
                        project.getTasks().forEach(task-> {
                            if (task.getEmployee()==null && task.getRecommendedAssigneeName()!=null) {
                                task.setEmployee(task.getRecommendedAssignee());
                                task.setRecommendedAssignee(null);
                                TaskUtils.updateEntity(task);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                table.refresh();

            }
        });


        Button deAllocateButton = new Button("Deallocate");
        deAllocateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedProjects.forEach(project -> {
                    try {
                        project.getTasks().forEach(task -> {
                            if (task.getEmployee() != null) {
                                task.setEmployee(null);
                                TaskUtils.updateEntity(task);
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                table.refresh();

            }
        });

        selectProjectsButton = new Button("Select Tasks");
        selectProjectsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(((Button)event.getSource()).getText().equals("Select Tasks")) {
                    table.getSelectionModel().setSelectionMode(
                            SelectionMode.MULTIPLE
                    );
                    table.getSelectionModel().getSelectedItems().addListener(multipleSelectionListener);
                    table.getSelectionModel().selectedItemProperty().removeListener(changeListener);

                    selectProjectsButton.setText("Finish Selection");
                } else {
                    table.getSelectionModel().setSelectionMode(
                            SelectionMode.SINGLE
                    );
                    table.getSelectionModel().getSelectedItems().removeListener(multipleSelectionListener);
                    table.getSelectionModel().selectedItemProperty().addListener(changeListener);
                    selectProjectsButton.setText("Select Tasks");

                }
            }
        });
        top.getChildren().add(maxProfitRecButton);
        top.getChildren().add(allocateButton);
        top.getChildren().add(deAllocateButton);
        top.getChildren().add(selectProjectsButton);
        pageHBox = new HBox();
        pageHBox.setSpacing(8);
        setCenter(pageHBox);
        pageHBox.getChildren().add(addTable("Projects"));
    }

    @Override
    TableView addTable(String pageName) {
        table = super.addTable(pageName);
        TableColumn id = new TableColumn("ID");
        TableColumn name = new TableColumn("Name");
        TableColumn cost = new TableColumn("Cost");
        TableColumn startTime = new TableColumn("Start time");
        TableColumn endTime = new TableColumn("End Time");

        id.setMinWidth(50);
        id.setCellValueFactory(
                new PropertyValueFactory<Task, String>("id"));

        name.setMinWidth(100);
        name.setCellValueFactory(
                new PropertyValueFactory<Task, String>("name"));

        startTime.setMinWidth(100);
        startTime.setCellValueFactory(
                new PropertyValueFactory<Task, String>("startTime"));

        endTime.setMinWidth(100);
        endTime.setCellValueFactory(
                new PropertyValueFactory<Task, String>("endTime"));

        cost.setMinWidth(150);
        cost.setCellValueFactory(
                new PropertyValueFactory<Task, String>("cost"));

        table.getColumns().addAll(id, name, startTime, endTime, cost);
        table.setItems(data);

        changeListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue obs, Object oldSelection, Object newSelection) {
                if (newSelection!=null) {
                    try {
                        String tasks = "---";
                        if (((Project) newSelection).getTasks()!=null){
                            tasks="";
                            for (Task task : ((Project) newSelection).getTasks()) {
                                tasks += task.getName() + "(ID=)"+task.getId()+": recommended assignee - "+ task.getRecommendedAssigneeName();
                            }
                        }
                        cardValues= new String[]{
                                ((Project)newSelection).getId().toString(),
                                ((Project)newSelection).getName(),
                                ((Project) newSelection).getDescription(),
                                ((Project) newSelection).getStartTime()==null ? "" : ((Project) newSelection).getStartTime().toString(),
                                ((Project) newSelection).getEndTime()==null ? "" : ((Project) newSelection).getEndTime().toString(),
                                tasks,
                        };
                        setNewCard(cardValues);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        table.getSelectionModel().selectedItemProperty().addListener(changeListener);


        multipleSelectionListener = new ListChangeListener<Project>() {
            @Override
            public void onChanged(Change<? extends Project>  c) {
                if (!selectedProjects.isEmpty()) {
                    selectedProjects.clear();
                }
                selectedProjects.addAll(c.getList());
            }
        };

        return table;
    }

    VBox addCard() {
        String[] names = {"ID", "Name", "Description", "Start date", "End Date",  "Tasks"};
        cardValues = new String[]{"1", "Project name", "", "2016-12-01", "2016-12-03", ""};
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
        if (selectedProjects ==null || selectedProjects.isEmpty()) {
            System.out.println("Please select the tasks");
            return;
        }
        projectsToAllocate = selectedProjects;
        assert(result.size()== selectedProjects.size());
        table.refresh();
        //MainUI.refreshTables();
        //System.out.print("Total number of unallocated tasks: "+ StrategyContext.numberOfUnnalocatedTasks);
        //System.out.println("Among them number of tasks non-valid for allocation: "+ StrategyContext.numberOfTasksUnvalidForAllocation);
    }
}
