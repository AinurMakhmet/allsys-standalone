package constants;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * A class for loading and storing constants from a database.properties file
 *
 * The class is taken from the source code of another software developed in
 * 2nd year Software Engineering Group project at King's College London-
 * https://github.com/musalbas/Nuclibook/blob/master/src/main/java/nuclibook/constants/C.java
 *
 */
public class C {

    public static String path = "database.properties";

	// server
	public static String MYSQL_URI = "jdbc:mysql://localhost:3306/allsyssmall";
	public static String MYSQL_USERNAME = "allsys";
	public static String MYSQL_PASSWORD = "allsys";

	// security
	public static int AUTOMATIC_TIMEOUT;

    //private constructor to prevent construction
    private C(){
    }

    /**
     * Loads the constants from the .properties file using a
     * PropertiesConfiguration object
     */
	public static void initConstants() throws ConfigurationException {
		PropertiesConfiguration config = new PropertiesConfiguration(path);
		MYSQL_URI = config.getString("database.URI");
		MYSQL_USERNAME = config.getString("database.user.name");
		MYSQL_PASSWORD = config.getString("database.user.password");
		AUTOMATIC_TIMEOUT = config.getInt("security.automatictimeout");
	}

}
