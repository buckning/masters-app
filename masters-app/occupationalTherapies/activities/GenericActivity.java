package occupationalTherapies.activities;


import java.io.File;

import occupationaltherapy.mainMenu.MainMenuScene;

import org.mt4j.MTApplication;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.IPreDrawAction;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import processing.core.PImage;
import sessionUtils.SessionManager;
import sessionUtils.UpdateSessionAction;
import webcamUtils.FrameGrabberAction;

/**
 * Adding an application to this system would require using this class. Setting up the application
 * requires adding startup code to the load method. This class includes session monitoring tools 
 * to check for and performs timeouts. The generic activity also contains a home button, making this
 * design more flexible for other  
 * @author amcglynn
 *
 */
public abstract class GenericActivity extends AbstractScene implements ILoadable{
	
	protected String imagePath =  "occupationalTherapies"+ MTApplication.separator +
								"activities"+ MTApplication.separator+ "images" + MTApplication.separator;
	protected MTRectangle homeButton, infoButton, infoRect, infoPanel;
	private UpdateSessionAction action;
	private FrameGrabberAction frameGrabber;
	private MTColor gc1;
	private MTColor gc2;
	private MTApplication mtapp;
	private int timeBetweenframes = 5000;
	private IGestureEventListener listener;
	private MTTextArea instructionsArea;
	
	public GenericActivity(MTApplication mtApp, String name, SessionManager sessionManager){
		super(mtApp, name);
		this.mtapp = mtApp;
		
		//TODO HOSPITAL CODE
		listener = new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				infoRect.setPickable(false);
				infoRect.setVisible(false);
				
//				quitBtn.setVisible(false);
//				quitBtn.setPickable(false);
				return false;
			}
		};
		
		infoPanel = new MTRectangle(200, 150, 400, 300, mtapp);
		infoPanel.unregisterAllInputProcessors();
		infoPanel.removeAllGestureEventListeners();
		infoPanel.registerInputProcessor(new TapProcessor(mtapp));
		infoPanel.addGestureListener(TapProcessor.class, listener);
		instructionsArea = new MTTextArea(200, 150, 400, 300, mtapp);
		instructionsArea.setPickable(false);
		infoPanel.addChild(instructionsArea);
//		PImage quitTexture = mtapp.loadImage(imagePath+"quitBtn.png");
//		quitTexture.resize(quitTexture.width*2/3, quitTexture.height*2/3);
//		quitBtn = new MTRectangle(quitTexture, mtapp);
//		
//		quitBtn.setPositionGlobal(new Vector3D(200,450));
//		getCanvas().addChild(quitBtn);
//		quitBtn.removeAllGestureEventListeners();
//		quitBtn.unregisterAllInputProcessors();
//		quitBtn.registerInputProcessor(new TapProcessor(mtapp));
//		quitBtn.addGestureListener(TapProcessor.class,listener);
		
		infoRect = new MTRectangle(0,0,mtapp.width,mtapp.height,mtapp);
		infoRect.unregisterAllInputProcessors();
		infoRect.removeAllGestureEventListeners();
		infoRect.registerInputProcessor(new TapProcessor(mtapp));
		infoRect.addGestureListener(TapProcessor.class, listener);
		
		infoRect.setFillColor(new MTColor(0, 0, 0, 150));
		infoRect.setPickable(false);
		infoRect.setVisible(false);
		infoRect.addChild(infoPanel);
		getCanvas().addChild(infoRect);
		//TODO HOSPITAL CODE
		
		action = new UpdateSessionAction(mtapp, sessionManager, name){
			//when the session has not been used and a timeout occurs
			@Override
			public void sessionOver(){
				sceneChange();
				getMTApplication().changeScene(MainMenuScene.getMainMenuScene());
//TODO maybe		destroy();
			}
		};
	
		File frameSaveDirectory = new File("E:/test/"+sessionManager.getSessionTimeStamp()+"/");
		System.out.println(frameSaveDirectory.toString());
		frameGrabber = new FrameGrabberAction(frameSaveDirectory,timeBetweenframes);
		
		registerPreDrawAction(new IPreDrawAction() {
			@Override
			public void processAction() {
				frameGrabber.processAction();
				action.processAction();	//update the session
				preDrawAction();	//custom predraw action
			}
			
			@Override
			public boolean isLoop() {
				return true;	// predraw loops forever
			}
		});
		
		//set up the home icon to return to the main menu
		PImage homeImage = mtapp.loadImage(imagePath+"home_32x32.png");
		
		homeButton = new MTRectangle(homeImage, getMTApplication());
		homeButton.setNoStroke(true);
		homeButton.unregisterAllInputProcessors();
		homeButton.removeAllGestureEventListeners();
		homeButton.registerInputProcessor(new TapProcessor(mtapp));
		homeButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				
				//when a tap event occurs, change the scene and log the interaction
				if(te.getId() == TapEvent.GESTURE_ENDED){
					sceneChange();
					//construct interaction string
					int x = (int)homeButton.getCenterPointGlobal().x;
					int y = (int)homeButton.getCenterPointGlobal().y;
					long t = System.currentTimeMillis();
					
					logInteraction(x, y, "terminated-by-user");
					action.processAction();
					MainMenuScene.getSessionManager().deactivateCurrentSession();
					getMTApplication().changeScene(MainMenuScene.getMainMenuScene());
//TODO maybe		destroy();
				}
				
				return false;
			}
		});
		
		getCanvas().addChild(homeButton);
		
		
		
		
		
		/*TODO ADD THIS TO HOSPITAL*/
		PImage infoTexture = mtapp.loadImage(imagePath+"info.png");
		infoTexture.resize(infoTexture.width*2/3, infoTexture.height*2/3);
		infoButton = new MTRectangle(infoTexture, mtapp);
		infoButton.setNoStroke(true);
		infoButton.setPositionGlobal(new Vector3D(getMTApplication().width - infoButton.getWidthXY(TransformSpace.GLOBAL)/2, 500));
		
		infoButton.unregisterAllInputProcessors();
		infoButton.removeAllGestureEventListeners();
		infoButton.registerInputProcessor(new TapProcessor(mtapp));
		infoButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				infoRect.setVisible(true);
				infoRect.setPickable(true);
//				quitBtn.setPickable(true);
//				quitBtn.setVisible(true);
				infoRect.sendToFront();
				infoPanel.sendToFront();
//				infoButton.sendToFront();
//				quitBtn.sendToFront();
				return false;
			}
		});
		
		getCanvas().addChild(infoButton);
		/*END TODO*/
		
		setClearColor(MainMenuScene.getMainMenuScene().getClearColor());
		
		gc1 = new MTColor(0, 0, 0, 255);
		gc2 = new MTColor(130,130,130, 255);
	}
	public void useGradientBackground(){
		Vertex[] vertices = new Vertex[]{
				new Vertex(0, 			mtapp.height,	0, 	gc1.getR(),gc1.getG(),gc1.getB(),gc1.getAlpha()),
				new Vertex(mtapp.width, mtapp.height,	0, 	gc1.getR(),gc1.getG(),gc1.getB(),gc1.getAlpha()),
				new Vertex(mtapp.width, 0,				0,	gc2.getR(),gc2.getG(),gc2.getB(),gc2.getAlpha()),
				new Vertex(0,			0,				0,	gc2.getR(),gc2.getG(),gc2.getB(),gc2.getAlpha()),
				//new Vertex(0, 			app.height/3,	0,	0,0,0,255),
		};
		MTPolygon p = new MTPolygon(vertices, getMTApplication());
		p.setName("upper gradient");
		p.setNoStroke(true);
		p.generateAndUseDisplayLists();
		p.setPickable(false);
		this.getCanvas().addChild(p);
	}
	
	public void setGradientColours(MTColor gc1, MTColor gc2){
		this.gc1 = gc1;
		this.gc2 = gc2;
	}
	/**
	 * Add a MTComponent to the scenes canvas
	 * @param comp multitouch component
	 */
	public void addChild(MTComponent comp){
		getCanvas().addChild(comp);
	}
	
	protected void setInformation(String instructions){
		instructionsArea.setText(instructions+"\nPress anywhere on the screen to continue");
	}
	
	protected void setInformation(MTRectangle infopanel){
		this.infoPanel.removeAllChildren();
		infopanel.setPositionGlobal(infoPanel.getCenterPointGlobal());
		for(MTComponent comp: infopanel.getChildren()){
			comp.unregisterAllInputProcessors();
			comp.removeAllGestureEventListeners();
			comp.registerInputProcessor(new TapProcessor(mtapp));
			comp.addGestureListener(TapProcessor.class, listener);
		}
		
		this.infoPanel.addChild(infopanel);
	}
	
	/**
	 * Logs an interaction to the database
	 * @param x	the x coordinate of the interaction
	 * @param y the y coordinate of the interaction
	 * @param timestamp the time of the interaction
	 * @param note the information about the interaction
	 */
	public void logInteraction(int x, int y, String note){
		String s = x + " " + y + " " + note+"\n";
		action.writeToFile(s);
		action.receivedEvent();
	}
	
	/**
	 * to be called to keep the session active. This will not record information
	 */
	public void interactionOccured(){
		action.receivedEvent();
	}
	
	/**
	 * to be overridden by child class if it wants to get notified of an upcoming scene change 
	 */
	public void sceneChange(){	}
	
	/**
	 * To be overridden. Useful if a physics engine needs to be updated
	 */
	public void preDrawAction(){	}
	
	@Override
	public void init(){	}
	
	@Override
	public void shutDown(){		}
}
