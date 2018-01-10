This repository contains a Multi-touch application, which was developed as part of a research project as part of my Master's Degree between 2010 - 2012. 
The thesis for this project and conference paper is available on the following repository:
https://bitbucket.org/andrewmcglynn86/college-masters

This project has been tested on Windows 10 and will work as is. 

# Prerequisites
* Java 7 or above must be available on the host machine
* Sound card must be enabled to play MIDI sounds

## MySQL Configuration
Port: 3306
Database Name: hospitalData
Username: root
Password: gmitresearch
Host: localhost (same machine that the project is running on)

### Database Schema
Create Database
> create database hospitalData;

Select database
> use hospitalData;

Create the database schema
> CREATE TABLE sessionData (sessionId INT NOT NULL AUTO_INCREMENT, applicationName VARCHAR(255), startTime VARCHAR(16), endTime VARCHAR(16), photo BLOB, interactionInfo BLOB, PRIMARY KEY (sessionId));

Note that the application can run without a database installed or configured.

# Import Project into Eclipse
* Create and open an Eclipse workspace
* File -> Import -> General -> Existing Projects Into Workspace
* Check **Select root directory** button and click **Browse**
* Select the base directory of where this repository was cloned
* In the Projects panel, select **masters-app** and click **Finish**

# Start the project
* In the project, expand the **masters-app** directory
* Expand the **occupationaltherapy** package
* Double click **StartHere.java**
* Click the run button
* Run the program as a Java Application

