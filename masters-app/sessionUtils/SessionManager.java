package sessionUtils;

import java.awt.Robot;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.swing.JOptionPane;

import webcamUtils.Webcam;

/**
 * 
 * @author Andrew McGlynn
 *
 */
public class SessionManager {
	private int sessionId = 0;
	private Webcam webcam;
	private InteractiveSession currentSession;
	private Robot robot;
	private SessionListener sessionListener;
	
	public SessionManager(){
		try{
			webcam = Webcam.getInstance();
			currentSession = new InteractiveSession(webcam, sessionId);
			robot = new Robot();
		}
		catch(Exception e){
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "No webcam detected! Insert webcam and start again", "Webcam Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public String getSessionTimeStamp(){
		return currentSession.getTimeStamp();
	}
	
	public void addSessionListener(SessionListener listener){
		this.sessionListener = listener;
	}
	
	public void deactivateCurrentSession(){
		currentSession.deactivate();
		try{
			currentSession.closeSession();
			
		}
		catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	/**
	 * Update all the sessions
	 */
	public void update(){
		//if there is a timeout, reset the current session
		try{
			currentSession.update();
		}
		catch(TimeoutException e){
			if(currentSession.isActive()){
				currentSession.deactivate();
				if(sessionListener != null)sessionListener.sessionOver();
				try{
					currentSession.closeSession();
					
				}
				catch(IOException ioe){
					ioe.printStackTrace();
				}
			}
			currentSession = new InteractiveSession(webcam, sessionId);
		}
	}
	/**
	 * Activate a session, if the session is already active, this keeps it alive.
	 */
	public void activateCurrentSession(String applicationName){
		if(!currentSession.isActive()){
			currentSession = new InteractiveSession(webcam, sessionId);//this was added
			currentSession.activate();
			currentSession.startSession(applicationName);
			sessionId++;
			try{
				robot.mouseMove(800, 600);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		else{
			currentSession.newInteraction();
		}
	}
	
	/**
	 * Write a string to the log-file for the session
	 * @param s String to be written to the file
	 * @throws IOException error writing to the file
	 */
	public void writeToLogFile(String s)throws IOException{
		if(currentSession.isActive()){
			currentSession.writeToLog(s);
		}
	}
}
