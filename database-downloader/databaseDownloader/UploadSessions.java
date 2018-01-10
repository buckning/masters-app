package databaseDownloader;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class UploadSessions {
	private String databaseName = "hospitalData";
	public static void main(String[] args){
		new UploadSessions();
	}
	
	public UploadSessions(){
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+databaseName+"?user=root","root","");
			String insertSession = "insert into sessionData(applicationName, startTime, endTime, photo, interactionInfo) values (?, ?, ?, ?, ?)";
			
			FileInputStream imgfis = null;
			FileInputStream logfis = null;
			PreparedStatement ps = null;
			conn.setAutoCommit(false);
			for(int i = 0; i <=13; i++){
				File imgFile = new File("userImages"+File.separator+""+i+".jpg");
				File logFile = new File("logFiles"+File.separator+""+i+".log");
				imgfis = new FileInputStream(imgFile);
				logfis = new FileInputStream(logFile);
				ps = conn.prepareStatement(insertSession);
				ps.setString(1, "App:"+i);
				ps.setString(2, "10:4"+i+"am");
				ps.setString(3, "10:4"+(i+1)+"am");
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
			System.out.println("A OK");
		}
		catch(Exception e){
			System.out.println("problem: could not complete task");
			e.printStackTrace();
		}
	}
}
