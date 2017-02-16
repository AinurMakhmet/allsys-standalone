package ui;

import entity_utils.EmployeeUtils;
import entity_utils.SkillUtils;
import entity_utils.TaskUtils;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Created by nura on 07/02/17.
 */
public class MainUI {
    private static MainUI ourInstance = new MainUI();

    public static MainUI getInstance() {
        return ourInstance;
    }

    private MainUI() {
    }
    static BorderPane borderPane = new BorderPane();

    public void startUI(Stage stage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main-view.fxml"));
        stage.setTitle("Allsys - Task Allocation system");

        Scene scene = new Scene(borderPane, 1000, 500);
        scene.setFill(Color.GHOSTWHITE);
        scene.getStylesheets().add(String.valueOf(getClass().getClassLoader().getResource("main.css")));

        borderPane.setLeft(Sidebar.getInstance());
        borderPane.setCenter(TasksPage.getInstance());
        borderPane.setRight(TasksPage.getInstance().addCard());
        stage.setScene(scene);
        stage.show();
    }

    static void refreshTables() {
        TasksPage.data.clear();
        TasksPage.data.addAll(TaskUtils.getAllTasks());
        TasksPage.table.refresh();
        EmployeesPage.data.clear();
        EmployeesPage.data.addAll(EmployeeUtils.getAllEmployees());
        EmployeesPage.table.refresh();
        SkillsPage.data.clear();
        SkillsPage.data.addAll(SkillUtils.getAllSkills());
        SkillsPage.table.refresh();
        AllocationPage.data.clear();
        AllocationPage.data.addAll(TaskUtils.getAllTasks());
        AllocationPage.table.refresh();
    }

}
