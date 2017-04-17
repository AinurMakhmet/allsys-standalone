Allsys is a prototype for a computer-aided decision system that can be used in employee staffing process at IT consulting company. It has been developed in the context of Computer Science final year project.
Allsys is a standalone application designed to run on one local machine only, where both a server and application client will be running.

The instructions below can also be found in User Guide in the Appendix

********************************************
Installation

1. Set up a MySQL server and a MySQL user.
The application requires MySQL database to be installed.
For the installation of the database, follow a tutorial from http://www.tutorialspoint.com/mysql/mysql-installation.htm

NOTE: In order to be able to run application and provided jar file: A database named 'allsyssmall' should be created in the local machine using port 3306, so that the URI of the database becomes jdbc:mysql://localhost:3306/allsyssmall. MysSQL user named allsys with the password 'allsys' must be created and must be assigned with all user privileges to the allsyssmall database.

2. Run the application.
The application can be launched directly from IDE or using the provided executable jar file located in app folder. The lib folder 	presented there is neccessary to successdully run the jar file. Instructions on how to generate a new executable file can be found below.
Using command line cd into directory where the executable file is located and run command java -jar allsys-1.0-jfx.jar

Note: With every launch of the application, a new database schema is created.


******************************************
Generating a New Executable File
1. Ensure that maven is installed on the machine (https://maven.apache.org/download.cgi).
2. In the C.java file of 'constants' package located in allsys-standalone/src/main/java/ directory change the values of the following three fields from default to the appropriate one.

public static final String MYSQL_URI;
public static final String MYSQL_USERNAME;
public static final String MYSQL_PASSWORD;

3. Using command line cd to the root of the source code folder and execute mvn clean and then mvn jfx:jar commands. Executable file can be found in target/jfx/app directory. The jar file in the target directory is not valid for execution.
