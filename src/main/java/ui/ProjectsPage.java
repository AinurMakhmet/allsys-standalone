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
import logic.MaximumProfitAlgorithm;
import logic.StrategyContext;
import models.Project;
import models.SystemData;
import models.Task;
import org.junit.Assert;
import servers.LocalServer;

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
    private List<Project> projectsToAllocate;
    private List<Project> result;
    private List<Project> selectedProjects = new LinkedList<>();
    private Button maxProfitRecButton, allocateButton, deAllocateButton;
    private List<Project> lastSavedAllocation;
    private TableColumn name, cost;
    private ListChangeListener<Project> multipleSelectionListener;
    ChangeListener changeListener;


    private static ProjectsPage ourInstance = new ProjectsPage();

    public static ProjectsPage getInstance() {
        return ourInstance;
    }

    private ProjectsPage() {
        super();
        search.setPromptText("Search projects");

        final ToggleGroup group = new ToggleGroup();
        RadioButton viewModeButton = new RadioButton("View Mode");
        viewModeButton.setToggleGroup(group);
        viewModeButton.setSelected(true);
        viewModeButton.setUserData(ProjectsPage.Mode.View);

        RadioButton allocateModeButton = new RadioButton("Allocation Mode");
        allocateModeButton.setToggleGroup(group);
        allocateModeButton.setUserData(ProjectsPage.Mode.Allocate);

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                Toggle toggle = group.getSelectedToggle();
                if (toggle!=null) {
                    if (toggle.getUserData().equals(ProjectsPage.Mode.Allocate)) {
                        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                        table.getSelectionModel().getSelectedItems().addListener(multipleSelectionListener);
                        table.getSelectionModel().selectedItemProperty().removeListener(changeListener);
                        allocateButton.setVisible(true);
                        deAllocateButton.setVisible(true);
                        maxProfitRecButton.setVisible(true);
                    } else if (toggle.getUserData().equals(ProjectsPage.Mode.View)) {
                        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                        table.getSelectionModel().getSelectedItems().removeListener(multipleSelectionListener);
                        table.getSelectionModel().selectedItemProperty().addListener(changeListener);
                        allocateButton.setVisible(false);
                        deAllocateButton.setVisible(false);
                        maxProfitRecButton.setVisible(false);
                    }
                }
            }
        });
        constructAllocationMode();
        top.getChildren().add(viewModeButton);
        top.getChildren().add(allocateModeButton);
        setCenter(addTable("Projects"));
    }

    private void constructAllocationMode() {
        maxProfitRecButton = new Button("MaxProfit REC");
        maxProfitRecButton.setOnAction(this);
        allocateButton = new Button("Allocate");
        allocateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedProjects.forEach(project -> {
                    try {
                        project.getTasks().forEach(t-> {
                            Task task = SystemData.getAllTasksMap().get(t.getId());
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


        deAllocateButton = new Button("Deallocate");
        deAllocateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedProjects.forEach(project -> {
                    try {
                        project.getTasks().forEach(t -> {
                            Task task = SystemData.getAllTasksMap().get(t.getId());
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
        allocateButton.setVisible(false);
        deAllocateButton.setVisible(false);
        maxProfitRecButton.setVisible(false);
        top.getChildren().add(maxProfitRecButton);
        top.getChildren().add(allocateButton);
        top.getChildren().add(deAllocateButton);
    }

    @Override
    TableView addTable(String pageName) {
        table = super.addTable(pageName);
        TableColumn id = new TableColumn("ID");
        name = new TableColumn("Name");
        cost = new TableColumn("Cost");
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
                            for (Task t : ((Project) newSelection).getTasks()) {
                                Task task = SystemData.getAllTasksMap().get(t.getId());
                                tasks += task.getName() + "(ID=)"+task.getId()
                                        +"\n\t recommended assignee - "+ task.getRecommendedAssigneeName()
                                        +"\n\t assigned employee - "+ task.getEmployeeName()+"\n";
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
        LocalServer.iLogger.info("MAX_PROFIT");
        result = new StrategyContext(MaximumProfitAlgorithm.getInstance()).executeProjectStrategy(selectedProjects);
        Assert.assertEquals(result.size(), selectedProjects.size());
        table.refresh();
        //MainUI.refreshTables();
        //System.out.print("Total number of unallocated tasks: "+ StrategyContext.numberOfUnnalocatedTasks);
        //System.out.println("Among them number of tasks non-valid for allocation: "+ StrategyContext.numberOfTasksUnvalidForAllocation);
    }

    enum Mode {
        View,
        Allocate;
    }
}
