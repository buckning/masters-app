package occupationalTherapies.activities.reminiscence;

import java.io.File;
import java.util.ArrayList;

import occupationalTherapies.activities.GenericActivity;
import occupationaltherapy.mainMenu.MainMenuScene;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTBackgroundImage;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateEvent;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleEvent;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;


public class PhotoActivity extends GenericActivity{
	private String imagesPath = "occupationalTherapies" + MTApplication.separator + 
				"activities"+ MTApplication.separator+"reminiscence" +MTApplication.separator+
				"images"+MTApplication.separator;
	private PImage pimage;
	private int index;
	
	private MTApplication mtapp;
	public PhotoActivity(MTApplication mtapp){
		super(mtapp, "Reminiscence Activity", MainMenuScene.getSessionManager());
		
		this.mtapp = mtapp;
		useGradientBackground();
	}

	@Override
	public void load() {
		
		File dir = new File("./masters-app/occupationalTherapies/activities/reminiscence/images");
		System.out.println(dir.getAbsolutePath());
		String[] dirListing = dir.list();
		final ArrayList<String> imageFiles = new ArrayList<String>();
		//get all the filenames that end in ".jpg" 
		for(String file: dirListing){
			if(file.endsWith(".jpg")){
				imageFiles.add(file);
			}
		}	
		String widgetPath = "occupationalTherapies"+ MTApplication.separator +
		"activities"+ MTApplication.separator+ "images" + MTApplication.separator;
		//load in all the ".jpg" files in the directory
		pimage = getMTApplication().loadImage(imagesPath + imageFiles.get(0));	
		
		PImage back = mtapp.loadImage(widgetPath+"backBtn.png");
		back.resize(back.width*2/3, back.height*2/3);
		PImage next = mtapp.loadImage(widgetPath+"forwardBtn.png");
		next.resize(next.width*2/3, next.height*2/3);
		final MTRectangle nextBtn = new MTImageButton(next, mtapp);
		float r = nextBtn.getFillColor().getR();
		float g = nextBtn.getFillColor().getG();
		float b = nextBtn.getFillColor().getB();
		nextBtn.setFillColor(new MTColor(r,g,b,255));
		final MTRectangle previousBtn = new MTImageButton(back, mtapp);
		r = previousBtn.getFillColor().getR();
		g = previousBtn.getFillColor().getG();
		b = previousBtn.getFillColor().getB();
		previousBtn.setFillColor(new MTColor(r,g,b,255));
		final MTBackgroundImage backgroundImage = new MTBackgroundImage(mtapp, pimage, false);
		getCanvas().addChild(backgroundImage);
		
		previousBtn.setPositionGlobal(new Vector3D(200,560));
		nextBtn.setPositionGlobal(new Vector3D(600,560));
		
		nextBtn.removeAllGestureEventListeners();
		nextBtn.unregisterAllInputProcessors();
		
		nextBtn.registerInputProcessor(new TapProcessor(mtapp));
		nextBtn.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				if(ge.getId() == MTGestureEvent.GESTURE_DETECTED){
					logInteraction(600, 560, "nxt");
					index++;
					if(index > imageFiles.size()-1){
						index = 0;
					}
					pimage = mtapp.loadImage(imagesPath + imageFiles.get(index));
					backgroundImage.setTexture(pimage);
					System.out.println("next: "+index);
				}
				return false;
			}
		});
		
		previousBtn.removeAllGestureEventListeners();
		previousBtn.unregisterAllInputProcessors();
		
		previousBtn.registerInputProcessor(new TapProcessor(mtapp));
		previousBtn.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				if(ge.getId() == MTGestureEvent.GESTURE_DETECTED){
					logInteraction(200, 560, "prv");
					index--;
					if(index < 0){
						index = imageFiles.size()-1;
					}
					pimage = mtapp.loadImage(imagesPath + imageFiles.get(index));
					backgroundImage.setTexture(pimage);
					System.out.println("previous: "+index);
				}
				return false;
			}
		});
		
		previousBtn.setNoStroke(true);
		nextBtn.setNoStroke(true);
		
		getCanvas().addChild(nextBtn);
		getCanvas().addChild(previousBtn);
		homeButton.sendToFront();
		infoButton.sendToFront();
		MTRectangle infoPanel = new MTRectangle(0,0,40,40,mtapp);
		MTTextArea textArea = new MTTextArea(0, 0, 100, 100, mtapp);
		textArea.setText("This is the instructions");
		infoPanel.addChild(textArea);
		MTRectangle info = new MTRectangle(0, 0, 400, 300, mtapp);
		IFont font = FontManager.getInstance().createFont(mtapp,"arial.ttf", 30,  MTColor.WHITE, MTColor.WHITE);
		MTTextArea infoArea = new MTTextArea(0, 300, 400, 300, font, mtapp);
		infoArea.setText("Touch the buttons to change the photo\n");
		infoArea.setNoFill(true);
		infoArea.setNoStroke(true);
		PImage texture = mtapp.loadImage(imagePath +"photo.png");
		texture.resize(400, 300);
		MTImage image = new MTImage(texture, mtapp);
		info.addChild(image);
		info.addChild(infoArea);
		setInformation(info);
		
	}
}

//package occupationalTherapies.activities.reminiscence;
//
//import java.io.File;
//import java.util.ArrayList;
//
//import occupationalTherapies.activities.GenericActivity;
//import occupationalTherapy.mainMenu.MainMenuScene;
//
//import org.mt4j.MTApplication;
//import org.mt4j.components.visibleComponents.shapes.MTRectangle;
//import org.mt4j.input.inputProcessors.IGestureEventListener;
//import org.mt4j.input.inputProcessors.MTGestureEvent;
//import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
//import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
//import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateEvent;
//import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
//import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleEvent;
//import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
//import org.mt4j.util.MTColor;
//import org.mt4j.util.math.Vector3D;
//
//import processing.core.PImage;
//
//
//public class PhotoActivity extends GenericActivity{
//	private String imagesPath = "occupationalTherapies" + MTApplication.separator + 
//				"activities"+ MTApplication.separator+"reminiscence" +MTApplication.separator+
//				"images"+MTApplication.separator;
//	
//	public PhotoActivity(MTApplication mtapp){
//		super(mtapp, "Reminiscence Activity", MainMenuScene.getSessionManager());
//		useGradientBackground();
//	}
//
//	@Override
//	public void load() {
//		File dir = new File("./v1_NOV2011/occupationalTherapies/activities/reminiscence/images");
//		System.out.println(dir.getAbsolutePath());
//		String[] dirListing = dir.list();
//		ArrayList<String> imageFiles = new ArrayList<String>();
//		//get all the filenames that end in ".jpg" 
//		for(String file: dirListing){
//			if(file.endsWith(".jpg")){
//				imageFiles.add(file);
//			}
//		}	
//		
//		//load in all the ".jpg" files in the directory
//		for(String s: imageFiles){
//			PImage pimage = getMTApplication().loadImage(imagesPath + s);
//			MTRectangle image = new MTRectangle(pimage,getMTApplication());
//		
//			image.addGestureListener(ScaleProcessor.class, new IGestureEventListener() {
//				
//				@Override
//				public boolean processGestureEvent(MTGestureEvent ge) {
//					ScaleEvent se = (ScaleEvent)ge;						
//					int x =	(int)se.getSecondCursor().getCurrentEvtPosX();
//					int y =	(int)se.getSecondCursor().getCurrentEvtPosY();
//					logInteraction(x, y, "s");
//					x =	(int)se.getFirstCursor().getCurrentEvtPosX();
//					y =	(int)se.getFirstCursor().getCurrentEvtPosY();
//					logInteraction(x, y, "s");
//					return false;
//				}
//			});
//			
//			image.addGestureListener(RotateProcessor.class, new IGestureEventListener() {
//				
//				@Override
//				public boolean processGestureEvent(MTGestureEvent ge) {
//					RotateEvent re = (RotateEvent)ge;
//					int x =	(int)re.getSecondCursor().getCurrentEvtPosX();
//					int y =	(int)re.getSecondCursor().getCurrentEvtPosY();
//					logInteraction(x, y, "r");
//					x =	(int)re.getFirstCursor().getCurrentEvtPosX();
//					y =	(int)re.getFirstCursor().getCurrentEvtPosY();
//					logInteraction(x, y, "r");
//					return false;
//				}
//			});
//			
//			image.addGestureListener(DragProcessor.class, new IGestureEventListener() {
//				
//				@Override
//				public boolean processGestureEvent(MTGestureEvent ge) {
//					
//					DragEvent de = (DragEvent)ge;
//					int x =	(int)de.getDragCursor().getCurrentEvtPosX();
//					int y =	(int)de.getDragCursor().getCurrentEvtPosY();
//					logInteraction(x, y, "g");			
//					return false;
//				}
//			});
//
//			image.setPositionGlobal(new Vector3D((int)(Math.random()*getMTApplication().width), 
//					(int)(Math.random()*getMTApplication().height)));
//			image.rotateZ(image.getCenterPointGlobal(), (float)(Math.random()*360));
//			addChild(image);	
//		}
//		homeButton.sendToFront();
//	}
//}
