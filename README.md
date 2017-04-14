# allsys-standalone
Allsys is a prototype for a computer-aided decision system that can be used in employee staffing process at IT consulting company. It has been developed in the context of Computer Science final year project.

Allsys is a standalone application designed to run on one local machine only, where both a server and application client will be running.

Installation:
The application requires MySQL database to be installed.
For the installation of the database, follow a tutorial from http://www.tutorialspoint.com/mysql/mysql-installation.htm

In the allsys-standalone folder, find a file called database.properties. Fill out the folowing three fields.
database.URI=[LINK TO YOUR MYSQL SERVER]
database.user.name=[USERNAME]
database.user.password=[PASSWORD]

NOTE: Ensure not to add the square brackets [ ] when filling out the fields.

The executable jar file creates the database schema at runtime everytime when run.

*****************************************************************
Libraries & External APIs Used

FontAwesome
MySQL-Connector-Java
commons-configuration
ORMLite JDBC
FontAwesomeFX