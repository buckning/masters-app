package occupationaltherapy;

import occupationaltherapy.mainMenu.MainMenuScene;

import org.mt4j.MTApplication;

public class StartHere extends MTApplication{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		initialize();
	}
	
	@Override
	public void startUp(){
		addScene(new MainMenuScene(this, "Main Menu"));
	}

}
