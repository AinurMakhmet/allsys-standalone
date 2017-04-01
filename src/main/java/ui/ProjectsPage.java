package ui;

import entity_utils.ProjectUtils;
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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
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
    private List<Project> result;
    private List<Project> selectedProjects = new LinkedList<>();
    private Button maxProfitRecButton, allocateButton, deAllocateButton;
    private TableColumn name, price, startTime, endTime;
    private ListChangeListener<Project> multipleSelectionListener;
    ChangeListener changeListener;


    private static ProjectsPage ourInstance = new ProjectsPage();

    public static ProjectsPage getInstance() {
        return ourInstance;
    }

    private ProjectsPage() {
        super();
        //search.setPromptText("Search projects");

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
                        table.getSelectionModel().clearAndSelect(0);
                        allocateButton.setVisible(false);
                        deAllocateButton.setVisible(false);
                        maxProfitRecButton.setVisible(false);
                    }
                }
            }
        });
        top.getChildren().add(viewModeButton);
        top.getChildren().add(allocateModeButton);
        constructAllocationMode();

        final TextField addName = new TextField();
        addName.setPromptText("Name");
        addName.setMaxWidth(100);
        final TextField addPrice = new TextField();
        addPrice.setPrefWidth(100);
        addPrice.setPromptText("Price");

        addButton.setTooltip(new Tooltip("Add project"));
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    Integer price = null;
                    if (!addPrice.getText().equals("")) {
                        price = Integer.parseInt(addPrice.getText());
                    }
                    if (price!=null && price<0) {
                        MainUI.alertError("Invalid input", "Employee salary cannot be of a negative value ");
                        return;
                    }
                    if (addName.getText().equals("")) {
                        MainUI.alertError("Invalid input", "A project must have a name.");
                        return;
                    }
                    Project newProject = new Project(addName.getText(), Integer.parseInt(addPrice.getText()));;
                    ProjectUtils.createEntity(Project.class, newProject);
                    Integer id = newProject.getId();
                    if (id!=null) {
                        newProject = ProjectUtils.getProject(newProject.getId());
                        SystemData.getAllProjectsMap().put(id, newProject);
                        data.add(newProject);
                        addName.clear();
                        addPrice.clear();
                        table.refresh();
                    }
                } catch (NumberFormatException | ClassCastException exc) {
                    MainUI.alertError("Invalid input", "Please enter only numbers to the Price field or leave it blank.");
                }
            }
        });

        bottom.getChildren().addAll(addName, addPrice, addButton);
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
        price = new TableColumn("Price");
        startTime = new TableColumn("Start time");
        endTime = new TableColumn("End Time");
        TableColumn cost = new TableColumn("Cost");
        TableColumn profit = new TableColumn("Profit");
        TableColumn estimatedCost = new TableColumn("Estimated Cost");
        TableColumn estimatedProfit = new TableColumn("Estimated Profit");

        id.setMinWidth(40);
        id.setCellValueFactory(
                new PropertyValueFactory<Task, String>("id"));

        name.setMinWidth(60);
        name.setCellValueFactory(
                new PropertyValueFactory<Task, String>("name"));

        startTime.setMinWidth(100);
        startTime.setCellValueFactory(
                new PropertyValueFactory<Task, String>("startTime"));

        endTime.setMinWidth(100);
        endTime.setCellValueFactory(
                new PropertyValueFactory<Task, String>("endTime"));

        price.setMinWidth(60);
        price.setCellValueFactory(
                new PropertyValueFactory<Task, String>("price"));

        cost.setMinWidth(60);
        cost.setCellValueFactory(
                new PropertyValueFactory<Task, String>("cost"));

        profit.setMinWidth(60);
        profit.setCellValueFactory(
                new PropertyValueFactory<Task, String>("profit"));

        estimatedCost.setMinWidth(60);
        estimatedCost.setCellValueFactory(
                new PropertyValueFactory<Task, String>("estimatedCost"));

        estimatedProfit.setMinWidth(60);
        estimatedProfit.setCellValueFactory(
                new PropertyValueFactory<Task, String>("estimatedProfit"));

        table.getColumns().addAll(id, name, startTime, endTime, price, cost, estimatedCost, estimatedProfit);
        table.setItems(data);

        setEditableCells();

        changeListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue obs, Object oldSelection, Object newSelection) {
                Project project = null;
                project = (Project) newSelection;
                if (project!=null) {
                    try {
                        String tasks = "---";
                        if (project.getTasks()!=null){
                            tasks="";
                            for (Task t : project.getTasks()) {
                                Task task = SystemData.getAllTasksMap().get(t.getId());
                                tasks += task.getName() + "(ID=)"+task.getId()
                                        +"\n\t recommended assignee - "+ task.getRecommendedAssigneeName()
                                        +"\n\t assigned employee - "+ task.getEmployeeName()+"\n";
                            }
                        }
                        cardValues= new String[]{
                                project.getId().toString(),
                                project.getName(),
                                project.getDescription(),
                                project.getStartTime()==null ? "" : project.getStartTime().toString(),
                                project.getEndTime()==null ? "" : project.getEndTime().toString(),
                                tasks,
                        };
                        setNewCard(cardValues, (Project) newSelection);
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

        deleteEntryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ObservableList selectedCells = table.getSelectionModel().getSelectedCells();
                TablePosition tablePosition = (TablePosition) selectedCells.get(0);
                Project toRemove = ((Project) table.getItems().get(tablePosition.getRow()));

                ProjectUtils.deleteEntity(Project.class, toRemove);
                if (ProjectUtils.getProject(toRemove.getId())==null) {
                    SystemData.getAllProjectsMap().remove(toRemove.getId());
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
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Project, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Project, String> t) {
                        Project project = (Project) t.getTableView().getItems().get(t.getTablePosition().getRow());
                        project.setName(t.getNewValue());
                    }
                }
        );

        price.setCellFactory(col -> new TextFieldTableCell<Project, Integer>(new EmployeesPage.EditIntegerStringConverter()) {
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
        price.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Project, Integer>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Project, Integer> t) {
                        Integer price = t.getNewValue();
                        if (price!=null && price>=0) {
                            Project project = (Project) t.getTableView().getItems().get(t.getTablePosition().getRow());
                            project.setPrice(t.getNewValue());
                        }
                    }
                }
        );
    }

    HBox addCard() {
        String[] names = {"ID", "Name", "Description", "Start date", "End Date",  "Tasks"};
        cardValues = new String[]{"", "", "", "", "", ""};
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
            MainUI.alertError("Invalid selection.", "Please select projects to allocate.");
            return;
        }

        LocalServer.iLogger.info("MAX_PROFIT");
        result = new StrategyContext(MaximumProfitAlgorithm.getInstance()).maxProfitAlgorithm(selectedProjects);
        Assert.assertEquals(result.size(), selectedProjects.size());
        table.refresh();
        MainUI.alertInformation("Allocation result", "Total number of unallocated tasks: "+ StrategyContext.getNumOfUnnalocatedProjects()
                + ". \nTotal profit from the selected projects is equal to: "+ StrategyContext.getTotalProfitFromSelectedProjects());
    }

    enum Mode {
        View,
        Allocate;
    }
}
