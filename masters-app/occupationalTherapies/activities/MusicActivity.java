package occupationalTherapies.activities;


import occupationaltherapy.mainMenu.MainMenuScene;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;

import demos.GridListener;
import demos.MusicGrid;

public class MusicActivity extends GenericActivity{
	private MTApplication mtapp;
	
	public MusicActivity(MTApplication mtApp){
		super(mtApp, "MusicActivity", MainMenuScene.getSessionManager());
		this.mtapp = mtApp;
		useGradientBackground();
	}

	@Override
	public void load() {
		MusicGrid grid = new MusicGrid(getMTApplication());
		grid.addGridListener(new GridListener() {
			@Override
			public void gridPressed(int x, int y, int gridNum) {
				logInteraction(x, y, ""+gridNum);
			}
		});
		grid.rotateZ(grid.getCenterPointGlobal(), -90);
		grid.translate(new Vector3D(110, 250));
		addChild(grid);
		homeButton.sendToFront();
		infoButton.sendToFront();
		MTRectangle info = new MTRectangle(0, 0, 400, 300, mtapp);
		IFont font = FontManager.getInstance().createFont(mtapp,"arial.ttf", 30,  MTColor.WHITE, MTColor.WHITE);
		MTTextArea infoArea = new MTTextArea(0, 300, 400, 300, font, mtapp);
		infoArea.setText("Make music by touching the squares");
		infoArea.setNoFill(true);
		infoArea.setNoStroke(true);
		PImage texture = mtapp.loadImage(imagePath +"music.png");
		texture.resize(400, 300);
		MTImage image = new MTImage(texture, mtapp);
		info.addChild(image);
		info.addChild(infoArea);
		setInformation(info);
	}
	
	//maybe update some variables
	@Override
	public void preDrawAction(){}
	
	//maybe save some information
	@Override
	public void sceneChange(){}
}
