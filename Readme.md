This repository contains a multi-touch application, which was developed as part of a research project as part of my Master's Degree between 2010 - 2012.
The thesis for this project and conference paper is available on the following repository:
https://bitbucket.org/andrewmcglynn86/masters-docs

This project has been tested on Windows 10 using Eclipse EE (Kepler) Build: 20130614-0229 and works as is.

The project is broken into 3 applications:

* Multi-touch recreational activity program
* Database data downloader
* Data analysis tool

This system was built for a custom made multi-touch screen. More information about building a multi-touch screen can be found here:
https://sites.google.com/site/gmitresearch/home

This project expects to receive TUIO events from multi-touch tracking software, such as [Community Core Vision (CCV)](http://ccv.nuigroup.com/). Although this project was intended to be used with CCV, it does function with input from a mouse.

# Prerequisites
* Java 7 or above must be available on the host machine
* Sound card must be enabled to play MIDI sounds
* Eclipse IDE

## MySQL Configuration

* Port: 3306
* Database Name: hospitalData
* Username: root
* Password: gmitresearch
* Host: localhost (same machine that the project is running on)

### Database Schema
Create Database
> create database hospitalData;

Select database
> use hospitalData;

Create the database schema
> CREATE TABLE sessionData (sessionId INT NOT NULL AUTO_INCREMENT, applicationName VARCHAR(255), startTime VARCHAR(16), endTime VARCHAR(16), photo BLOB, interactionInfo BLOB, PRIMARY KEY (sessionId));

Note that the application can run without a database installed or configured.

## Import Project into Eclipse
* Create and open an Eclipse workspace
* File -> Import -> General -> Existing Projects Into Workspace
* Check **Select root directory** button and click **Browse**
* Select the base directory of where this repository was cloned
* In the Projects panel, select **masters-app** and click **Finish**

# Running the projects

## Start the Multi-touch project
* In the project, expand the **masters-app** directory
* Expand the **occupationaltherapy** package
* Double click **StartHere.java**
* Click the run button
* Run the program as a Java Application

## Start the Database Downloader
* Expand the **database-downloader** directory
* Expand the **databaseDownloader** package
* Double click **StartDownloader.java**
* Click the run button
* Run the program as a Java Application

## Start the Data Analysis Tool
* Expand the **analysis-tool** directory
* Expand the **analysisTool** package
* Double click **StartAnalysis.java**
* Click the run button
* Run the program as a Java Application
