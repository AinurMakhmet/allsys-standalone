package ui;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Created by nura on 07/02/17.
 */
public class MainUI {
    private static Stage pStage;
    private static MainUI ourInstance = new MainUI();

    public static MainUI getInstance() {
        return ourInstance;
    }

    private MainUI() {
    }
    static BorderPane borderPane = new BorderPane();

    public void startUI(Stage stage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main-view.fxml"));
        pStage = stage;
        stage.setTitle("Allsys - Task Allocation system");

        Scene scene = new Scene(borderPane);
        scene.setFill(Color.GHOSTWHITE);
        scene.getStylesheets().add(String.valueOf(getClass().getClassLoader().getResource("main.css")));

        borderPane.setLeft(Sidebar.getInstance());
        borderPane.setCenter(TasksPage.getInstance());
        borderPane.setRight(TasksPage.getInstance().addCard());
        stage.setScene(scene);
        stage.show();
    }

    public static Stage getPrimaryStage() {
        return pStage;
    }

    static void refreshTables() {
        /*TasksPage.getInstance().data.clear();
        TasksPage.getInstance().data.addAll(TaskUtils.getAllTasks());
        */TasksPage.getInstance().table.refresh();
        /*EmployeesPage.getInstance().data.clear();
        EmployeesPage.getInstance().data.addAll(EmployeeUtils.getAllEmployees());
        */EmployeesPage.getInstance().table.refresh();
        /*SkillsPage.getInstance().data.clear();
        SkillsPage.getInstance().data.addAll(SkillUtils.getAllSkills());
        */SkillsPage.getInstance().table.refresh();
    }

    public static void alertWarning(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(MainUI.getPrimaryStage());
        alert.setHeaderText("Invalid data");
        if (header!=null) {
            alert.setHeaderText(header);
        }
        alert.setContentText(message);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setResizable(true);
        alert.showAndWait();
    }

    public static void alertError(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(MainUI.getPrimaryStage());
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setResizable(true);
        alert.showAndWait();
    }


    public static void alertInformation(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(MainUI.getPrimaryStage());
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setResizable(true);
        alert.showAndWait();
    }

}
