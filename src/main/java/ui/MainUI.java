package ui;

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

        Scene scene = new Scene(borderPane, 800, 500);
        scene.setFill(Color.GHOSTWHITE);
        scene.getStylesheets().add(String.valueOf(getClass().getClassLoader().getResource("main.css")));

        borderPane.setLeft(Sidebar.getInstance());
        borderPane.setCenter(TasksPage.getInstance());
        borderPane.setRight(TasksPage.getInstance().addCard());
        stage.setScene(scene);
        stage.show();
    }
}
