package servers;

import constants.C;
import org.apache.commons.configuration.ConfigurationException;

/**
 * This class creates and manages the server for the entire system.
 * Its main jobs are to set up the server, route pages, and handle top-level security.
 */
public class LocalServer {
    /**
     * Create the server and perform initial configuration.
     * @param args Any command line arguments; ignored in this application.
     */
    public static void main(String[] args) {
        //initialise constants
        try {
            C.initConstants();
            SqlServerConnection.acquireConnection();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }
    }
    /**
     * This will stop the server and effectively kill the application in the event of a fatal error
     * @param message The message to be delivered to the user
     */
    public static void fatalError(String message) {
        System.out.println("A fatal error occurred:" + message + " Please restart the server and try again.");
        System.exit(1);

    }
}
