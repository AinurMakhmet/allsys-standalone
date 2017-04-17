package constants;
/**
 * The class is based on class of another software developed in
 * 2nd year Software Engineering Group project at King's College London-
 * https://github.com/musalbas/Nuclibook/blob/master/src/main/java/nuclibook/constants/C.java
 *
 */
public class C {
	// server
	public static final String MYSQL_URI = "jdbc:mysql://localhost:3306/allsyssmall";
	public static final String MYSQL_USERNAME = "allsys";
	public static final String MYSQL_PASSWORD = "allsys";

	// security
	public static int AUTOMATIC_TIMEOUT;

    //private constructor to prevent construction
    private C(){
    }
}
