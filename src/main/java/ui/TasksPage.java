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
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import logic.EdmondsKarpStrategy;
import logic.GreedyStrategy;
import logic.StrategyContext;
import models.*;
import servers.LocalServer;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.LinkedList;
import java.util.List;

/**
 * models gui page Tasks
 */
public class TasksPage extends AbstractPage implements ChangeListener, EventHandler<ActionEvent>{
    final ObservableList<Task> data = FXCollections.observableArrayList(SystemData.getAllTasksMap().values());
    private List<Task> tasksToAllocate;
    private List<Task> selectedTasks = new LinkedList<>();
    private Button greedyRecButton, ekRecButton, allocateButton, deAllocateButton;
    private ListChangeListener<Task> multipleSelectionListener;
    private ChangeListener changeListener;
    private TableColumn name, employeeId, startTime, endTime, projectId, priorityLevel;
    static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static TasksPage ourInstance = new TasksPage();

    public static TasksPage getInstance() {
        return ourInstance;
    }

    private TasksPage() {
        super();
        //search.setPromptText("Search tasks");
        final ToggleGroup group = new ToggleGroup();
        RadioButton viewModeButton = new RadioButton("View Mode");
        viewModeButton.setToggleGroup(group);
        viewModeButton.setSelected(true);
        viewModeButton.setUserData(Mode.View);

        RadioButton allocateModeButton = new RadioButton("Allocation Mode");
        allocateModeButton.setToggleGroup(group);
        allocateModeButton.setUserData(Mode.Allocate);

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov,
                                Toggle old_toggle, Toggle new_toggle) {
                Toggle toggle = group.getSelectedToggle();
                if (toggle!=null) {
                    if (toggle.getUserData().equals(Mode.Allocate)) {
                        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                        table.getSelectionModel().getSelectedItems().addListener(multipleSelectionListener);
                        table.getSelectionModel().selectedItemProperty().removeListener(changeListener);
                        allocateButton.setVisible(true);
                        deAllocateButton.setVisible(true);
                        ekRecButton.setVisible(true);
                        greedyRecButton.setVisible(true);
                    } else if (toggle.getUserData().equals(Mode.View)) {
                        table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
                        table.getSelectionModel().getSelectedItems().removeListener(multipleSelectionListener);
                        table.getSelectionModel().selectedItemProperty().addListener(changeListener);
                        allocateButton.setVisible(false);
                        deAllocateButton.setVisible(false);
                        ekRecButton.setVisible(false);
                        greedyRecButton.setVisible(false);
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
        final TextField addStartTime = new TextField();
        addStartTime.setPrefWidth(180);
        addStartTime.setPromptText("Start Date (yyyy-MM-dd)");
        final TextField addEndTime = new TextField();
        addEndTime.setMaxWidth(190);
        addEndTime.setPromptText("End Date (yyyy-MM-dd)");

        addButton.setTooltip(new Tooltip("Add task"));
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    if (addName.getText().equals("")) {
                        MainUI.alertError("Invalid input", "A task must have a name.");
                        return;
                    }
                    LocalDate startDateL = LocalDate.parse(addStartTime.getText(), dateFormat);
                    LocalDate endDateL = LocalDate.parse(addEndTime.getText(), dateFormat);
                    Date startTime = Date.valueOf(startDateL);
                    Date endTime = Date.valueOf(endDateL);
                    Task newTask = new Task(addName.getText(), startTime, endTime, Task.Priority.LOW);
                    TaskUtils.createEntity(Task.class, newTask);
                    Integer id = newTask.getId();
                    if (id!=null) {
                        newTask = TaskUtils.getTask(newTask.getId());
                        SystemData.getAllTasksMap().put(id, newTask);
                        data.add(newTask);
                        addName.clear();
                        addStartTime.clear();
                        addEndTime.clear();
                        table.refresh();
                    }
                } catch (DateTimeParseException exception) {
                    MainUI.alertError(null, "Please enter a date in the format of yyyy-MM-dd");
                }


            }
        });

        bottom.getChildren().addAll(addName, addStartTime, addEndTime, addButton);
        setCenter(addTable("Tasks"));
    }

    @Override
    TableView addTable(String pageName) {
        table = super.addTable(pageName);
        TableColumn id = new TableColumn("ID");
        name = new TableColumn("Name");
        employeeId = new TableColumn("Employee ID");
        startTime = new TableColumn("Start time");
        endTime = new TableColumn("End Time");
        projectId = new TableColumn("Project ID");
        priorityLevel = new TableColumn("Priority");
        TableColumn recommendedAssignee = new TableColumn("Recommended Assignee (Employee)");

        id.setMinWidth(40);
        id.setCellValueFactory(
                new PropertyValueFactory<Task, String>("id"));

        name.setMinWidth(60);
        name.setCellValueFactory(
                new PropertyValueFactory<Task, String>("name"));

        employeeId.setMinWidth(80);
        employeeId.setCellValueFactory(
                new PropertyValueFactory<Task, Integer>("employeeId"));

        startTime.setMinWidth(100);
        startTime.setCellValueFactory(
                new PropertyValueFactory<Task, String>("startTime"));

        endTime.setMinWidth(100);
        endTime.setCellValueFactory(
                new PropertyValueFactory<Task, String>("endTime"));

        projectId.setMinWidth(80);
        projectId.setCellValueFactory(
                new PropertyValueFactory<Task, Integer>("projectId"));

        priorityLevel.setMinWidth(70);
        priorityLevel.setCellValueFactory(new PropertyValueFactory<Task, String>("priority"));

        recommendedAssignee.setMinWidth(150);
        recommendedAssignee.setCellValueFactory(
                new PropertyValueFactory<Task, String>("recommendedAssigneeName"));

        table.getColumns().addAll(id, name, startTime, endTime, priorityLevel, projectId, employeeId, recommendedAssignee);
        table.setItems(data);

        setEditableCells();

        changeListener = new ChangeListener() {
            @Override
            public void changed(ObservableValue obs, Object oldSelection, Object newSelection) {
                if (newSelection!=null) {
                    try {
                        String skills = "---";
                        if (((Task) newSelection).getSkills()!=null){
                            skills="";
                            for (Skill skill : ((Task) newSelection).getSkills()) {
                                skills += skill.getName()+ " (Level " +skill.getLevel()+")\n ";
                            }
                        }
                        cardValues= new String[]{
                                ((Task)newSelection).getId().toString(),
                                ((Task)newSelection).getName(),
                                ((Task) newSelection).getDescription(),
                                ((Task) newSelection).getProject()==null ? "task is not part of a project" : ((Task) newSelection).getProject().getId().toString(),
                                ((Task) newSelection).getStartTime()==null ? "" : ((Task) newSelection).getStartTime().toString(),
                                ((Task) newSelection).getEndTime()==null ? "" : ((Task) newSelection).getEndTime().toString(),
                                ((Task) newSelection).getPriority().toString(),
                                skills,
                                ((Task) newSelection).getRecommendedAssigneeName()==null ? "no recommendation" : ((Task) newSelection).getRecommendedAssigneeName(),
                                ((Task) newSelection).getEmployeeName()==null ? "not allocated" : ((Task) newSelection).getEmployeeName()
                        };
                        setNewCard(cardValues, (Task)newSelection);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        table.getSelectionModel().selectedItemProperty().addListener(changeListener);

        multipleSelectionListener = new ListChangeListener<Task>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends Task>  c) {
                if (!selectedTasks.isEmpty()) {
                    selectedTasks.clear();
                }
                selectedTasks.addAll(c.getList());
            }
        };

        deleteEntryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ObservableList selectedCells = table.getSelectionModel().getSelectedCells();
                TablePosition tablePosition = (TablePosition) selectedCells.get(0);
                Task toRemove = ((Task) table.getItems().get(tablePosition.getRow()));

                TaskUtils.deleteEntity(Task.class, toRemove);
                if (TaskUtils.getTask(toRemove.getId())==null) {
                    SystemData.getAllTasksMap().remove(toRemove.getId());
                    data.remove(toRemove);
                    table.refresh();
                } else {
                    MainUI.alertError("Cannot delete", "There might be some problem connecting to the database.");
                }
            }
        });

        return table;
    }

    private void constructAllocationMode() {
        greedyRecButton = new Button("GREEDY REC");
        greedyRecButton.setTooltip(new Tooltip("Greedy strategy"));
        greedyRecButton.setOnAction(this);
        greedyRecButton.getStyleClass().add("simple");
        ekRecButton = new Button("EK REC");
        ekRecButton.setTooltip(new Tooltip("Edmonds-Karp strategy"));
        ekRecButton.setOnAction(this);
        ekRecButton.getStyleClass().add("simple");
        allocateButton = new Button("ALLOCATE");
        allocateButton.getStyleClass().add("simple");
        allocateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedTasks.forEach(task -> {
                    if (task!=null && task.getEmployee()==null && task.getRecommendedAssigneeName()!=null) {
                        task.setEmployee(task.getRecommendedAssignee());
                        task.setRecommendedAssignee(null);
                    }
                });
                table.refresh();

            }
        });

        deAllocateButton = new Button("DEALLOCATE");
        deAllocateButton.getStyleClass().add("simple");
        deAllocateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                selectedTasks.forEach(task -> {
                    if (task!=null && task.getEmployee()!=null ) {
                        task.setEmployee(null);
                    }
                });
                table.refresh();

            }
        });
        allocateButton.setVisible(false);
        deAllocateButton.setVisible(false);
        ekRecButton.setVisible(false);
        greedyRecButton.setVisible(false);
        top.getChildren().add(greedyRecButton);
        top.getChildren().add(ekRecButton);
        top.getChildren().add(allocateButton);
        top.getChildren().add(deAllocateButton);
    }

    private void setEditableCells() {

        //The following snippet of code is based on http://stackoverflow.com/a/34701925
        employeeId.setCellFactory(col -> new TextFieldTableCell<Task, Integer>(new EmployeesPage.EditIntegerStringConverter()) {
            @Override
            public void updateItem(Integer item, boolean empty) {
                if (empty) {
                    super.updateItem(item, empty) ;
                } else {
                    // if out of range, revert to previous value:
                    if (item!=null && item.intValue() < 0) {
                        item = getItem();
                        MainUI.alertError("Invalid input", "Please enter a positive number.");
                    } else if (item!=null) {
                        Integer employeeId = item.intValue();
                        Employee employee = SystemData.getAllEmployeesMap().get(employeeId);
                        if (employee == null) {
                            MainUI.alertError("Invalid employee id", "Employee with such id does not exist.");
                            item = getItem();
                        }

                    }
                    super.updateItem(item, empty);
                }
            }
        });

        employeeId.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Task, Integer>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Task, Integer> t) {
                        Integer employeeId = t.getNewValue();
                        if (employeeId!=null && employeeId<1) {
                        } else {
                            Employee employee = SystemData.getAllEmployeesMap().get(employeeId);
                            if (employeeId!=null && employee == null) {
                            } else {
                                ((Task) t.getTableView().getItems().get(
                                        t.getTablePosition().getRow())
                                ).setEmployee(employee);
                            }
                        }
                    }
                }
        );

        //The following snippet of code is based on http://stackoverflow.com/a/34701925
        projectId.setCellFactory(col -> new TextFieldTableCell<Task, Integer>(new EmployeesPage.EditIntegerStringConverter()) {
            @Override
            public void updateItem(Integer item, boolean empty) {
                if (empty) {
                    super.updateItem(item, empty) ;
                } else {
                    // if out of range, revert to previous value:
                    if (item!=null && item.intValue() < 0) {
                        item = getItem();
                        MainUI.alertError("Invalid input", "Please enter a positive number.");
                    } else if (item!=null) {
                        Integer projectId = item.intValue();
                        Project project = SystemData.getAllProjectsMap().get(projectId);
                        if (project==null) {
                            item = getItem();
                            MainUI.alertError("Invalid project id", "Project with such id does not exist.");
                        }
                    }
                    super.updateItem(item, empty);
                }
            }
        });
        projectId.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Task, Integer>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Task, Integer> t) {
                        Integer projectId = t.getNewValue();
                        if (projectId!=null && projectId<1) {
                        } else {
                            Project project = SystemData.getAllProjectsMap().get(projectId);
                            if (projectId!=null && project == null) {
                            } else {
                                ((Task) t.getTableView().getItems().get(
                                        t.getTablePosition().getRow())
                                ).setProject(project);
                            }
                        }
                    }
                }
        );


        //startTime.setCellFactory(TextFieldTableCell.forTableColumn(new TaskStringToDateConverter()));
        startTime.setCellFactory(col -> new TextFieldTableCell<Task, Date>(new TaskStringToDateConverter()) {
            @Override
            public void updateItem(Date item, boolean empty) {
                if (empty) {
                    super.updateItem(item, empty) ;
                } else {
                    // if out of range, revert to previous value;
                    Task task = (Task) getTableView().getItems().get(table.getFocusModel().getFocusedCell().getRow());
                    /*if (item!=null && item.equals(task.getStartTime())) {

                    } else if (item!=null && item.after(task.getEndTime())) {
                        item = getItem();
                        //MainUI.alertWarning(null, "Start date of the task must be before its end date");
                    }*/
                    super.updateItem(item, empty);
                }
            }
        });

        startTime.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Task, Date>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Task, Date> t) {
                        try {
                            Task task = (Task) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow());
                            if (t.getNewValue()==null
                                    || (t.getNewValue()!=null
                                        && task.getEndTime()!=null &&
                                        !(t.getNewValue()).after(task.getEndTime()))
                                    || task.getEndTime()==null) {
                                task.setStartTime(t.getNewValue());
                            } else {
                                MainUI.alertWarning(null, "Start date of the task must be before its end date");
                            }
                        } catch (DateTimeParseException exception) {
                            MainUI.alertError(null, "Please enter the date in the format of yyyy-MM-dd");
                        }
                    }
                }
        );
        //endTime.setCellFactory(TextFieldTableCell.forTableColumn(new TaskStringToDateConverter()));
        endTime.setCellFactory(col -> new TextFieldTableCell<Task, Date>(new TaskStringToDateConverter()) {
            @Override
            public void updateItem(Date item, boolean empty) {
                if (empty) {
                    super.updateItem(item, empty) ;
                } else {
                    // if out of range, revert to previous value;
                    Task task = (Task) getTableView().getItems().get(table.getFocusModel().getFocusedCell().getRow());
                    /*if (item!=null && item.equals(task.getEndTime())) {

                    } else if (item!=null && item.before(task.getStartTime())) {
                        item = getItem();
                        //MainUI.alertWarning(null, "End date of the task must be after its start date");
                    }*/
                    super.updateItem(item, empty);
                }
            }
        });
        endTime.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Task, Date>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Task, Date> t) {
                        //TODO: handle null values
                        try {
                            Task task = (Task) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow());
                            if (t.getNewValue()==null
                                    || (t.getNewValue()!=null
                                        && task.getStartTime()!=null
                                        && !(t.getNewValue()).before(task.getStartTime()))
                                    || task.getStartTime()==null) {
                                task.setEndTime(t.getNewValue());
                            } else {
                                MainUI.alertWarning(null, "End date of the task must be after its start date");
                            }

                        } catch (DateTimeParseException exception) {
                            MainUI.alertError(null ,"Please enter a date in the format of yyyy-MM-dd");
                        }
                    }
                }
        );
    }

    public static class TaskStringToDateConverter extends StringConverter<Date> {

        @Override
        public String toString(Date t) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            if (t==null) {
                return "" ;
            } else {
                return df.format(t);
            }
        }

        @Override
        public Date fromString(String string) {
            if (string.equals("")) return null;
            try {
                return Date.valueOf(LocalDate.parse(string, dateFormat));
            } catch (DateTimeParseException | IllegalStateException e) {
                MainUI.alertError(null, "Please enter a date in the format of yyyy-MM-dd");
            }
            return null;
        }

    };

    HBox addCard() {
        String[] names = {"ID", "Name", "Description", "Project id", "Start date", "End date", "Priority level", "Skills required", "Recommended assignee", "Allocated to employee"};
        cardValues = new String[]{"", "", "", "", "", "", "", "", "", ""};
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
        if (selectedTasks==null || selectedTasks.isEmpty()) {
            MainUI.alertError("Invalid selection", "Please select tasks to allocate.");
            return;
        }
        tasksToAllocate = selectedTasks;
        if (((Button)event.getSource()).equals(greedyRecButton)) {
            LocalServer.gLogger.info("GREEDY STRATEGY");
            new StrategyContext(GreedyStrategy.getInstance(), tasksToAllocate);
        } else if (((Button)event.getSource()).equals(ekRecButton)) {
            LocalServer.ekLogger.info("EDMONDS-KARP STRATEGY");
            new StrategyContext(EdmondsKarpStrategy.getInstance(), tasksToAllocate);
        } else {
            return;
        }
        for (Task task: SystemData.getAllTasksMap().values()) {
            LocalServer.iLogger.info(task.getRecommendation());
        }
        table.refresh();
        MainUI.alertInformation("Allocation result", "Total number of unallocated tasks: "+ StrategyContext.getNumOfUnallocatedTasks()
                + ". \nAmong them number of tasks invalid for allocation: "+ StrategyContext.getNumOfTasksInvalidForAllocation());
    }

    enum Mode {
        View,
        Allocate;
    }
}
