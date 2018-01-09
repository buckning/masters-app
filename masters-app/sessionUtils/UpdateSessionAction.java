package sessionUtils;

import java.awt.Robot;

import org.mt4j.MTApplication;
import org.mt4j.sceneManagement.IPreDrawAction;

public class UpdateSessionAction implements IPreDrawAction, SessionListener{
	private SessionManager manager;
	private boolean receivedEvent = false;
	private String applicationName;
	private String logText = "";
	private Robot robot;
	private MTApplication mtapp;
	
	public UpdateSessionAction(MTApplication mtapp,SessionManager sessionManager, String applicationName){
		manager = sessionManager;
		manager.addSessionListener(this);
		
		this.mtapp = mtapp;
		this.applicationName = applicationName;
		try{
			robot = new Robot();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		manager.activateCurrentSession(applicationName);
	}
	
	@Override
	public void processAction() {
		manager.update();
		if(receivedEvent){
			receivedEvent = false;
			manager.activateCurrentSession(applicationName);
			try{
				manager.writeToLogFile(logText);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			logText = "";	//maybe this should be in the try block? 24/11/2011
//TODO			robot.mouseMove(mtapp.width, mtapp.height);
		}
	}
	
	public void writeToFile(String s){
		synchronized(this){
			logText += s;
		}
	}

	@Override
	public boolean isLoop() {
		return true;
	}

	public void receivedEvent(){
		receivedEvent = true;
	}

	@Override
	public void sessionOver() {
		// TODO Auto-generated method stub
		
	}
	
	
}
