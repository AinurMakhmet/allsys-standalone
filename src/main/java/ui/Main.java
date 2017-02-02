package ui;

import constants.C;
import entity_utils.TaskUtils;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import logic.Strategy;
import org.apache.commons.configuration.ConfigurationException;
import servers.SqlServerConnection;

public class Main extends Application {
    static BorderPane borderPane = new BorderPane();

    @Override
    public void start(Stage stage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("main-view.fxml"));
        stage.setTitle("Allsys - Task Allocation system");


        Scene scene = new Scene(borderPane, 800, 500);
        scene.setFill(Color.GHOSTWHITE);
        scene.getStylesheets().add(String.valueOf(getClass().getClassLoader().getResource("main.css")));

        borderPane.setLeft(Sidebar.getInstance());
        borderPane.setCenter(TasksPage.getInstance());
        borderPane.setRight(TasksPage.getInstance().addCard());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        try {
            C.initConstants();
            SqlServerConnection.acquireConnection();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        Strategy.getInstance().allocate();
        //TaskUtils.getAllocatedTask();

        launch(args);
    }
}
