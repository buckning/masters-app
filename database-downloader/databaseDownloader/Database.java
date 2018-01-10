package databaseDownloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import zipFileUtils.ZipFileCreator;

public class Database {
	private static Database db;
	private static Connection conn;
	private GUIUpdater updater;
	private String databaseName;
	
	private Database(String databaseName, String username, String password, int port){
		this.databaseName = databaseName;
		this.updater = null;
	}
	
	public void setGUIUpdater(GUIUpdater updater){
		this.updater = updater;
	}
	
	public static Database connectToDatabase(int port, String databaseName, String username, String password){
		db = null;
		
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:"+port+"/"+databaseName+"?user="+username,username,password);
			db = new Database(databaseName,username,password,port);
		}
		catch(Exception e){
			db = null;
		}
		
		return db;
	}
	
	public static Database getInstance(){
		return db;
	}
	
	public int getTableSize(String tableName){
		String querySize = "SELECT COUNT(*) FROM "+tableName;
		int size = -1;
		
		try{
			Statement query = conn.createStatement();
			ResultSet rs = query.executeQuery(querySize);
			
			rs.next();
			size = rs.getInt(1);
		}
		catch(SQLException sqle){
			sqle.printStackTrace();
		}
		
		return size;
	}
	
	public boolean deleteTable(String tableName){
		String deleteQuery = "DELETE FROM "+tableName;
		
		try{
			Statement query = conn.createStatement();
			query.executeUpdate(deleteQuery);
			return true;
		}
		catch(SQLException sqle){
			sqle.printStackTrace();
			return false;
		}
	}
	
	public void downloadAllSessions(String tbl, String location){
		final String query = "SELECT * FROM "+ tbl;
		final String table = tbl;
		final String fileLocation = location;
		
		Thread t = new Thread(){
			@Override
			public void run(){
				try{
					ZipFileCreator zip = new ZipFileCreator(fileLocation);
					
					int databaseSize = getTableSize(table);
					int currentVal = 0;
					
					conn.setAutoCommit(false);
					Statement statement = conn.createStatement();
					ResultSet rs = statement.executeQuery(query);
					
					InputStream imageInput = null;
					
					while(rs.next()){
						//update gui that is attached
						if(updater != null){
							updater.fireProgressUpdate(0, databaseSize, ++currentVal);
						}
						
						String imgFile;
						String logFile;
						int sessionID = rs.getInt("id");
						
						String appName = rs.getString("applicationName");
						String startTime = rs.getString("startTime");
						String endTime = rs.getString("endTime");
						
						logFile = "session"+sessionID+".xml";
						imgFile = "session"+sessionID+".jpg";
						
						String interactionInfo = rs.getString("interactionInfo");
						OutputStream logOs = new FileOutputStream(logFile);
						logOs.write("<SESSION>\n".getBytes());
						logOs.write(("<ID>"+sessionID+"</ID>\n").getBytes());
						logOs.write(("<APP>"+ appName + "</APP>\n").getBytes());
						logOs.write(("<STARTTIME>"+startTime+"</STARTTIME>\n").getBytes());
						logOs.write(("<ENDTIME>"+endTime+"</ENDTIME>\n").getBytes());
						logOs.write("<INTERACTIONS>\n".getBytes());
						logOs.write(interactionInfo.getBytes());
						logOs.write("</INTERACTIONS>\n".getBytes());
						logOs.write("</SESSION>\n".getBytes());
						String length = rs.getString("photo");	//get the photo as a string to get the size of it
						int len = length.length();
						byte[] b = new byte[len];
						imageInput = rs.getBinaryStream("photo");
						int index = imageInput.read(b, 0 , len);
						
						OutputStream os = new FileOutputStream(imgFile);
						while(index != -1){
							os.write(b, 0, index);
							index = imageInput.read(b, 0, len);
						}
						os.close();
						logOs.close();
						zip.addFile(imgFile);
						zip.addFile(logFile);
						
						File deleteFile = new File(imgFile);
						deleteFile.delete();
						deleteFile = new File(logFile);
						deleteFile.delete();
					}
					zip.finalize();
					updater.fireTaskComplete();
				}
				catch(SQLException e){
					e.printStackTrace();
				}
				catch(Exception e){
					JOptionPane.showMessageDialog(null, "Unable to save to file\nplease try again", "Save Unsuccessful!" , JOptionPane.ERROR_MESSAGE);
				}
			}
		};
		t.start();
		
	}
}
