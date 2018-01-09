package sessionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.concurrent.TimeoutException;

import dateUtils.DateUtils;

import webcamUtils.Webcam;
import zipFileUtils.ZipFileCreator;

public class InteractiveSession {
	
	private static final long TIMEOUT = 120000;	//60 second timeout
	
	private FileWriter writer;
	private int id;
	private File dir;
	private File imgDir;
	private File zipDir;
	private File logFile;
	private Webcam webcam;
	private String timeStamp;
	private String logFileName;
	private String userImageFileName;
	private long lastInteraction = 0;
	private boolean active = false;
	
	private String imgPath;
	private String logPath;
	private String applicationName;
	
	public InteractiveSession(Webcam webcam, int id){
		this.webcam = webcam;
		this.id = id;
		
		//create the log file name
		String startTime = DateUtils.get24HourTimeString();
		String startDate = DateUtils.getDateString();
		timeStamp = startDate + "_" + startTime;
		logFileName = "log"+timeStamp+".log";
		userImageFileName = "user" +timeStamp+ ".jpg";
//		userImageFileName = "0.jpg";
		//if the file directories don't exist, create them
		dir = new File("./logFiles");
		if(!dir.exists()){
			dir.mkdir();
		}
		imgDir = new File("./userImages");
		if(!imgDir.exists()){
			imgDir.mkdir();
		}
		
		imgPath = imgDir.getPath() + File.separator + userImageFileName;
		logPath = dir.getPath() + File.separator + logFileName;
	}
	
	public String getTimeStamp(){
		return timeStamp;
	}
	
	public void writeToLog(String s)throws IOException{
		writer.write(s);
		writer.flush();
	}
	
	/**
	 * Start a new session, this saves a new log file to store the events and the user's image
	 */
	public void startSession(String applicationName){
		this.applicationName = applicationName;
		try{
			logFile = new File(dir.toString()+"/"+logFileName);
			writer = new FileWriter(logFile.toString());

			System.out.println("webcam = " + webcam + " dir = " + 
					imgDir.toString()+"/"+userImageFileName);
			webcam.saveFrame(imgDir.toString()+"/"+userImageFileName);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Close the log file and zip the two files 
	 * @throws IOException
	 */
	public void closeSession()throws IOException{
		writer.close();
		
		String endTime = DateUtils.get24HourTimeString();
		String endDate = DateUtils.getDateString();
		String endTimeStamp = endDate + "_" + endTime;
		
//		String imgPath = imgDir.getPath() + File.separator + userImageFileName;
		String imgPath = imgDir.toString()+"/"+userImageFileName;
		String logPath = dir.getPath() + File.separator + logFileName;
		
		File logFile = new File(logPath);
		File imgFile = new File(imgPath);
		
		int port = 3306;
		String databaseName = "hospitalData";
		String userName = "root";
		String password = "gmitresearch";
		
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:"+port+"/"+databaseName+"?user="+userName,""+userName,""+password);
			String insertSession = "insert into sessionData(applicationName, startTime, endTime, photo, interactionInfo) values (?, ?, ?, ?, ?)";
			
			FileInputStream imgfis = null;
			FileInputStream logfis = null;
			PreparedStatement ps = null;
			conn.setAutoCommit(false);
			
			imgfis = new FileInputStream(imgFile);
			logfis = new FileInputStream(logFile);
			ps = conn.prepareStatement(insertSession);
			ps.setString(1, ""+applicationName);
			ps.setString(2, ""+timeStamp);
			ps.setString(3, ""+endTimeStamp);
			ps.setBinaryStream(4, imgfis, (int)imgFile.length());
			ps.setBinaryStream(5, logfis, (int)logFile.length());
			ps.executeUpdate();
			conn.commit();
			ps.close();
			imgfis.close();
			logfis.close();
			imgFile.delete();
			logFile.delete();
		}
		catch(Exception e){
			System.out.println("problem: could not complete task");
			e.printStackTrace();
		}
		/*
		 *     // File (or directory) to be moved
    File file = new File("filename");
    
    // Destination directory
    File dir = new File("directoryname");
    
    // Move file to new directory
    boolean success = file.renameTo(new File(dir, file.getName()));
    if (!success) {
        // File was not successfully moved
    }
		 */
//		logFile.
		
		
//		System.out.println(imgDir.isDirectory()+"");
		//TODO store the image file and log file in a zip folder
	}
	
	
	
	public void update()throws TimeoutException{
		long now = System.currentTimeMillis();
		if((now - lastInteraction) > TIMEOUT){
			throw new TimeoutException();
		}
	}
	
	public boolean isActive(){
		return active;
	}
	
	public void activate(){
		active = true;
		newInteraction();
	}
	
	public void deactivate(){
		active = false;
	}
	
	public void newInteraction(){
		lastInteraction = System.currentTimeMillis();
	}
}
