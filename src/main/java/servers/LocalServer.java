package servers;

import constants.C;
import javafx.application.Application;
import javafx.stage.Stage;
import models.SystemData;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ui.MainUI;

import java.io.File;

/**
 * This class creates and manages the server for the entire system.
 * Its main jobs are to set up the server, route pages, and handle top-level security.
 */
public class LocalServer extends Application{
    public static Logger gLogger = LogManager.getLogger("gLogger");
    public static Logger ffLogger = LogManager.getLogger("ffLogger");
    public static Logger iLogger = LogManager.getLogger("iLogger");

    /**
     * Create the server and perform initial configuration.
     * @param args Any command line arguments; ignored in this application.
     */
    public static void main(String[] args) {
        //initialise constants
        try {
            C.initConstants();
            SqlServerConnection.acquireConnection();
            SystemData.getDataFromDatabase();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{
        MainUI.getInstance().startUI(stage);
    }

    /**
     * This will stop the server and effectively kill the application in the event of a fatal error
     * @param message The message to be delivered to the user
     */
    public static void fatalError(String message) {
        System.out.println("A fatal error occurred:" + message + " Please restart the server and try again.");
        System.exit(1);

    }

    public static Logger createLogger() {
        Logger aLogger = LogManager.getRootLogger();
        aLogger.debug("\n" + "Inside createLogger - Logger is being set up. New test setup beginning.");
        aLogger.info("Logger has been set up.");
        return aLogger;
    }
}
