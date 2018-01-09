package occupationalTherapies.activities.paint;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.imageio.ImageIO;

import occupationalTherapies.activities.GenericActivity;
import occupationaltherapy.mainMenu.MainMenuScene;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.widgets.MTSceneTexture;
import org.mt4j.components.visibleComponents.widgets.MTSlider;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;

public class PaintActivity extends GenericActivity{
	
	private MTSceneTexture sceneTexture;
	private DrawSurfaceScene drawCanvas;
	
	private String imagesPath = "occupationalTherapies" + MTApplication.separator + "activities" + MTApplication.separator+
								"paint" + MTApplication.separator + "images" + MTApplication.separator;
	private String newFileImage = imagesPath + "newFile.png";
	
	public PaintActivity(MTApplication mtapp){
		super(mtapp, "PaintActivity", MainMenuScene.getSessionManager());
	}

	@Override
	public void load() {
		PImage newCanvasImage = getMTApplication().loadImage(newFileImage);
		newCanvasImage.resize((int)homeButton.getWidthXY(TransformSpace.GLOBAL), (int)homeButton.getHeightXY(TransformSpace.GLOBAL));
		
		final MTImageButton newCanvasBtn = new MTImageButton(newCanvasImage, getMTApplication());
		newCanvasBtn.setNoStroke(true);
		newCanvasBtn.setPositionGlobal(new Vector3D(150, newCanvasImage.height/2));
		newCanvasBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clearDrawCanvas();

				Vector3D pos = newCanvasBtn.getPosition(TransformSpace.GLOBAL);
				int x = (int) pos.x;
				int y = (int)pos.y;
				logInteraction(x, y, "c");
			}
		});
		
		drawCanvas = new DrawSurfaceScene(getMTApplication(), "draw surface", this);
		drawCanvas.setClear(false);
		MTApplication mtapp = getMTApplication();
		sceneTexture = new MTSceneTexture(mtapp,0, 0, mtapp.width, mtapp.height, drawCanvas);
        this.getCanvas().addChild(sceneTexture);
        clearDrawCanvas();
		
        MTEllipse brushTexture = new MTEllipse(mtapp,new Vector3D(10,10), 10, 10);
        brushTexture.setNoStroke(true);
		drawCanvas.setBrush(brushTexture);
        
		final MTSlider slider = new MTSlider(35, 500, 200, 38, 0.05f,	2.0f, mtapp);
        slider.setValue(1.0f);
        slider.setPositionGlobal(new Vector3D(150,550));
        slider.setStrokeColor(new MTColor(0,0,0));
        slider.setFillColor(new MTColor(220, 220, 220));
        slider.addPropertyChangeListener("value", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				drawCanvas.setBrushScale((Float)arg0.getNewValue());
				int x = (int)slider.getKnob().getCenterPointGlobal().x;
				int y = (int)slider.getKnob().getCenterPointGlobal().y;
				logInteraction(x, y, "z");
			}
		});
        getCanvas().addChild(slider);
        
        ColorPallet colourPallet = new ColorPallet(mtapp);
		colourPallet.addColorChangeListener(new ColorChangeListener() {
			
			@Override
			public void colorChanged(int x, int y, MTColor color) {
				drawCanvas.setBrushColor(color);
				logInteraction(x, y, "n");
			}
		});
    	getCanvas().addChild(colourPallet);
		colourPallet.setPositionGlobal(new Vector3D(mtapp.width*2/3, 550));  
		colourPallet.rotateZ(colourPallet.getCenterPointGlobal(), -90);
		colourPallet.setNoFill(true);
		colourPallet.setNoStroke(true);
		colourPallet.unregisterAllInputProcessors();
		
        drawCanvas.setBrushColor(colourPallet.getCurrentColor());   
        
		addChild(newCanvasBtn);
		homeButton.sendToFront();
	}
	

	/**
	 * Clear the textured draw canvas
	 */
	public void clearDrawCanvas(){
		getMTApplication().invokeLater(new Runnable() {
			@Override
			public void run() {
				sceneTexture.getFbo().clear(true, 255, 255, 255, 255, true);
			}
		});
	}
	
	//maybe update some variables
	@Override
	public void preDrawAction(){}
	
	//maybe save some information
	@Override
	public void sceneChange(){
		saveScreen();
	}
		
	private void saveScreen(){
		try{
			Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			final BufferedImage capture = new Robot().createScreenCapture(screenRect);
			
			Thread t = new Thread(){
				@Override
				public void run() {
					try{
						long time = System.currentTimeMillis();
						//save the image before leaving the art program
						//this is in a seperate thread for performance reasons
						ImageIO.write(capture, "png", new File("C:/Documents and Settings/amcglynn/Desktop/images/"+time+".png"));
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
			};
			t.start();
		}
		catch(Exception e){
			e.printStackTrace();
		}	
	}
}
