package waterApplication;

import org.mt4j.MTApplication;

import sessionUtils.SessionManager;

public class Start extends MTApplication{
	public static void main(String[] args){
		initialize();
	}
	
	public void startUp(){
		SessionManager sessionManager = new SessionManager();
		PondScene pond = new PondScene(this, "",sessionManager);
		addScene(pond);
	}
}
