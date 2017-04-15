package ui;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Class that constructs UI
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
        pStage = stage;
        stage.setTitle("Allsys - Task Allocation system");

        Scene scene = new Scene(borderPane);
        scene.setFill(Color.GHOSTWHITE);
        scene.getStylesheets().add(String.valueOf(getClass().getClassLoader().getResource("main.css")));
        /*Font.loadFont(App.class.getResource("/fontawesome-webfont.ttf").
                toExternalForm(), 12);*/
        Font.loadFont(getClass().getResource("/fontawesome-webfont.ttf").
                toExternalForm(), 12);
        borderPane.setLeft(Sidebar.getInstance());
        borderPane.setCenter(TasksPage.getInstance());
        borderPane.setRight(TasksPage.getInstance().addCard());
        stage.setScene(scene);
        stage.show();
    }

    public static Stage getPrimaryStage() {
        return pStage;
    }

    /**
     * Displays warning popup window
     * @param header
     * @param message
     */
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

    /**
     * Displays error popup window
     * @param header
     * @param message
     */
    public static void alertError(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(MainUI.getPrimaryStage());
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.setResizable(true);
        alert.showAndWait();
    }

    /**
     * Displays infomration popup window
     * @param header
     * @param message
     */
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
