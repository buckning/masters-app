package occupationaltherapy.mainMenu;

import occupationalTherapies.activities.GenericActivity;
import occupationalTherapies.activities.LoadingScene;
import occupationalTherapies.activities.MusicActivity;
import occupationalTherapies.activities.game.GameActivity;
import occupationalTherapies.activities.paint.PaintActivity;
import occupationalTherapies.activities.reaction.ReactionActivity;
import occupationalTherapies.activities.reminiscence.PhotoActivity;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTPolygon;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.IPreDrawAction;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4j.util.math.Vertex;

import dateUtils.DateUtils;


import processing.core.PImage;
import sessionUtils.SessionManager;

public class MainMenuScene extends AbstractScene{
	private static MainMenuScene mainMenuScene;
	private String imagePath = "mainMenu"+ MTApplication.separator +
								"images"+ MTApplication.separator;
	private SessionManager sessionManager;
	
	private Vertex thumbnailSize = new Vertex(150, 150);
	public MainMenuScene(MTApplication mtapp, String name){
		super(mtapp, name);
		IFont messageFont = FontManager.getInstance().createFont(mtapp,"arial.ttf", 30,  MTColor.WHITE, MTColor.WHITE);
		final MTTextField timeField = new MTTextField(mtapp.width - 220, 40, mtapp.width*2/3, 100, messageFont, mtapp);
		timeField.setNoFill(true);
		timeField.setNoStroke(true);
		timeField.setSizeXYGlobal(200, 40);
		timeField.setText(DateUtils.get24HourTimeString());
		timeField.setPickable(false);
		getCanvas().addChild(timeField);
		
		registerPreDrawAction(new IPreDrawAction() {
			
			@Override
			public void processAction() {
				// TODO Auto-generated method stub
				timeField.setText(DateUtils.get24HourTimeString());		
			}
			
			@Override
			public boolean isLoop() {
				// TODO Auto-generated method stub
				return true;
			}
		});
		MTTextField instructionsField = new MTTextField(mtapp.width/3 - 50, 70, mtapp.width*2/3, 100, messageFont, mtapp);
		instructionsField.setNoFill(true);
		instructionsField.setNoStroke(true);
		instructionsField.setText("Touch activity to get started");
		instructionsField.setPickable(false);
		getCanvas().addChild(instructionsField);
		
		
		
		
		
		
		//GRADIENT BACKGROUND
		Vertex[] vertices = new Vertex[]{
				new Vertex(0, 			mtapp.height/3f,	0, 	0,0,0,255),
				new Vertex(mtapp.width, 	mtapp.height/3,	0, 	0,0,0,255),
				new Vertex(mtapp.width, 	mtapp.height/1.7f,0,	70,70,70,255),
				new Vertex(0,			mtapp.height/1.7f,0,	70,70,70,255),
				new Vertex(0, 			mtapp.height/3,	0,	0,0,0,255),
		};
		MTPolygon p = new MTPolygon(vertices, getMTApplication());
		p.setName("upper gradient");
		p.setNoStroke(true);
		p.generateAndUseDisplayLists();
		p.setPickable(false);
		this.getCanvas().addChild(p);
		
		Vertex[] vertices2 = new Vertex[]{
				new Vertex(0, 			mtapp.height/1.7f,	0, 	70,70,70,255),
				new Vertex(mtapp.width, mtapp.height/1.7f,	0, 	70,70,70,255),
				new Vertex(mtapp.width, 	mtapp.height,			0,	0,0,0,255),
				new Vertex(0,			mtapp.height,			0,	0,0,0,255),
				new Vertex(0, 			mtapp.height/1.7f,	0, 	70,70,70,255),
		};
		MTPolygon p2 = new MTPolygon(vertices2, getMTApplication());
		p2.setNoStroke(true);
		p2.generateAndUseDisplayLists();
		p2.setPickable(false);
		this.getCanvas().addChild(p2);
				
		mainMenuScene = this;
		sessionManager = new SessionManager();
		addMusicActivity();
		addPhotoActivity();
		addPaintActivity();
		addGameActivity();
		addReactionActivity();
		setClearColor(MTColor.BLACK);
	}
	
	private void addGameActivity(){
		PImage gameImage = getMTApplication().loadImage(imagePath+"game1.JPG");
		addActivity(gameImage, "Play a game", 260, 250, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if(te.getId() == TapEvent.GESTURE_DETECTED){
					GenericActivity gameActivity = new GameActivity(getMTApplication());
					LoadingScene scene = new LoadingScene(getMTApplication(), gameActivity);
					getMTApplication().changeScene(scene);
				}
				return false;
			}
		});
	}
	
	private void addReactionActivity(){
		PImage reactionImage = getMTApplication().loadImage(imagePath+"reaction.jpg");
		addActivity(reactionImage, "Test your reactions", 530, 250, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if(te.getId() == TapEvent.GESTURE_DETECTED){
					GenericActivity reactionActivity = new ReactionActivity(getMTApplication());
					LoadingScene scene = new LoadingScene(getMTApplication(), reactionActivity);
					getMTApplication().changeScene(scene);
				}
				return false;
			}
		});
	}

	private void addPaintActivity(){
		PImage paintImage = getMTApplication().loadImage(imagePath+"paint.jpg");
		addActivity(paintImage, "Paint a picture", 150, 450, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if(te.getId() == TapEvent.GESTURE_DETECTED){
					GenericActivity photoActivity = new PaintActivity(getMTApplication());
					LoadingScene scene = new LoadingScene(getMTApplication(), photoActivity);
					getMTApplication().changeScene(scene);
				}
				return false;
			}
		});
	}
	
	private void addMusicActivity(){
		PImage musicImage = getMTApplication().loadImage(imagePath + "music.jpg");
		addActivity(musicImage, "Make music", 400, 450, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if(te.getId() == TapEvent.GESTURE_DETECTED){
					GenericActivity musicActivity = new MusicActivity(getMTApplication());
					LoadingScene scene = new LoadingScene(getMTApplication(), musicActivity);
					getMTApplication().changeScene(scene);
				}
				return false;
			}
		});
	}
	
	private void addPhotoActivity(){
		PImage photoImage = getMTApplication().loadImage(imagePath+"polaroid.jpg");
		addActivity(photoImage, "Look at photos", 670, 450, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if(te.getId() == TapEvent.GESTURE_DETECTED){
					GenericActivity photoActivity = new PhotoActivity(getMTApplication());
					LoadingScene scene = new LoadingScene(getMTApplication(), photoActivity);
					getMTApplication().changeScene(scene);
				}
				return false;
			}
		});
	}
	
	private void addActivity(PImage image, String text,float x, float y, IGestureEventListener listener){
		image.resize((int)thumbnailSize.x, (int)thumbnailSize.y);
		
		MTImage button = new MTImage(image, getMTApplication());
		button.setPositionGlobal(new Vector3D(x, y));
		button.unregisterAllInputProcessors();
		button.removeAllGestureEventListeners();
		button.registerInputProcessor(new TapProcessor(getMTApplication()));
		button.addGestureListener(TapProcessor.class, listener);
		
		IFont font = FontManager.getInstance().createFont(getMTApplication(),"arial.ttf", 20,  MTColor.WHITE, MTColor.WHITE);
		
		//TODO append text onto the button
		getCanvas().addChild(button);
		MTTextField textField = new MTTextField(x-thumbnailSize.x/2-15, y+thumbnailSize.y/2, thumbnailSize.x+30, 30, font, getMTApplication());
		textField.setText(text);
		textField.setNoFill(true);
		textField.setNoStroke(true);
		
		textField.unregisterAllInputProcessors();
		textField.removeAllGestureEventListeners();
		textField.registerInputProcessor(new TapProcessor(getMTApplication()));
		textField.addGestureListener(TapProcessor.class, listener);
		getCanvas().addChild(textField);
	}
	
	
	
	public static MainMenuScene getMainMenuScene(){
		return mainMenuScene;
	}
	
	public static SessionManager getSessionManager(){
		return mainMenuScene.sessionManager;
	}
	
	@Override
	public void init(){	}
	@Override
	public void shutDown(){	}
}
