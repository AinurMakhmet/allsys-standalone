Allsys is a prototype for a computer-aided decision system that can be used in employee staffing process at IT consulting company. It has been developed in the context of Computer Science final year project.
Allsys is a standalone application designed to run on one local machine only, where both a server and application client will be running.

The instructions below can also be found in User Guide in the Appendix

********************************************
Installation

1. Set up a MySQL server and a MySQL user.
The application requires use of MySQL server as an application database.
For the installation of MySQL database, follow a tutorial from http://www.tutorialspoint.com/mysql/mysql-installation.htm

NOTE: In order to be able to run application from IDE or using the provided jar file, the following is required: a database created in the local machine should be named ‘allsyssmall’ and should use port 3306, so that the URI of the database is  jdbc:mysql://localhost:3306/allsyssmall; create MySQL user with a name ‘allsys’ an a password ‘allsys’; assign all user privileges to the user for ‘allsyssmall’ database.

2. Run the application.
The application can be launched directly from IDE or using the provided executable jar file located in app folder. The lib folder presented there is neccessary to successdully run the jar file. Instructions on how to generate a new executable file can be found below.
Using command line cd into the directory where the executable file is located and run command 'java -jar allsys-1.0-jfx.jar'.

NOTE:  With every launch of the application, a new database schema is created. Therefore, all changes made to the database will vanish every time the application is rerun.


******************************************
Generating a New Executable File

1. Ensure that maven is installed on the machine (https://maven.apache.org/download.cgi).
2. In the C.java file of 'constants' package located in allsys-standalone/src/main/java/ directory change the values of the following three fields from default to the ones that reflect your database properties.

public static final String MYSQL_URI;
public static final String MYSQL_USERNAME;
public static final String MYSQL_PASSWORD;

3. Using command line cd to the allsys-standalone folder, where the pom.xml file is located, and execute sequentially 'mvn clean' and 'mvn jfx:jar' commands. The generated executable file will be located in allsys-standalone/target/jfx/app directory. The jar file in the allsys-standalone/target directory is not valid for execution.


***************************************************
Libraries & External APIs Used

Database related:
    MySQL-Connector-Java
    ORMLite JDBC (http://ormlite.com/javadoc/ormlite-core/doc-files/ormlite.html#Top)
GUI related:
    FontAwesomeFX (http://www.jensd.de/wordpress/?p=132)
    FontAwesome (http://fontawesome.io/)
Debugging and Logging purposes related:
    Log4j (https://logging.apache.org/log4j/2.x/)
Other:
    Commons Configuration (http://commons.apache.org/proper/commons-configuration/)
