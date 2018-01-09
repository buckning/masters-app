package occupationalTherapies.activities.reaction;

import java.util.Timer;
import java.util.TimerTask;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTImage;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import occupationalTherapies.activities.GenericActivity;
import occupationaltherapy.mainMenu.MainMenuScene;
import processing.core.PImage;

public class ReactionActivity extends GenericActivity{
	private MTTextField statsField;
	private Buzzer buzzer;
	private ReactionButton reactionButton;
	private ReactionStatistics stats;
	private MTApplication mtapp;
	public ReactionActivity(MTApplication mtapp) {
		super(mtapp, "Reaction Activity", MainMenuScene.getSessionManager());
		useGradientBackground();
		this.mtapp = mtapp;
	}

	@Override
	public void load() {
		buzzer = new Buzzer();
		stats = new ReactionStatistics();
		IFont font = FontManager.getInstance().createFont(mtapp,"arial.ttf", 20,  MTColor.WHITE, MTColor.WHITE);
		MTTextField instructionsField = new MTTextField(100, 0, 800, 50, font, mtapp);
		instructionsField.setNoFill(true);
		instructionsField.setNoStroke(true);
		instructionsField.setPickable(false);
		instructionsField.setText("Touch the red circle when it appears");
		getCanvas().addChild(instructionsField);
		
		
		statsField = new MTTextField(100, 560, 800, 50, font, mtapp);
		statsField.setNoFill(true);
		statsField.setNoStroke(true);
		statsField.setPickable(false);
		getCanvas().addChild(statsField);
		
		reactionButton = new ReactionButton(getMTApplication(), new Vector3D(50, 50),50) {
			
			@Override
			public void buttonPressed(long reactionTime) {
				stats.addStatistic(reactionTime);
				reactionButton.setVisible(false);
				reactionButton.setPickable(false);
				Timer timer = new Timer();
				RelocateTask relocateTask = new RelocateTask();
				long t = (long)(Math.random() * 6000);
				timer.schedule(relocateTask, t);
				
				int x = (int)reactionButton.getCenterPointGlobal().x;
				int y = (int)reactionButton.getCenterPointGlobal().y;
				logInteraction(x, y, "h");
			}
		};		
		reactionButton.setVisible(false);
		reactionButton.setPickable(false);
		reactionButton.setFillColor(MTColor.RED);
		Timer timer = new Timer();
		RelocateTask relocateTask = new RelocateTask();
		long t = (long)(Math.random() * 6000);
		timer.schedule(relocateTask, t);
		addChild(reactionButton);
		homeButton.sendToFront();
infoButton.sendToFront();
		
		MTRectangle info = new MTRectangle(0, 0, 400, 300, mtapp);
		font = FontManager.getInstance().createFont(mtapp,"arial.ttf", 30,  MTColor.WHITE, MTColor.WHITE);
		MTTextArea infoArea = new MTTextArea(0, 300, 400, 300, font, mtapp);
		infoArea.setText("Touch the red circle when it appears on the screen\n");
		infoArea.setNoFill(true);
		infoArea.setNoStroke(true);
		PImage texture = mtapp.loadImage(imagePath +"reaction.png");
		texture.resize(400, 300);
		MTImage image = new MTImage(texture, mtapp);
		info.addChild(image);
		info.addChild(infoArea);
		setInformation(info);
	}
	
	@Override
	public void sceneChange(){
		buzzer.disable();
		logInteraction(0, 0, "best_"+stats.getBestTime()+
				",worst_"+stats.getWorstTime()+",average_"+stats.getAverageReactionTime());
	}
	@Override
	public void preDrawAction(){
		long best = stats.getBestTime();
		if(best == 100000)best = 0; 
		long worst = stats.getWorstTime();
		int average = (int)stats.getAverageReactionTime();
		statsField.setText("Best = "+ best+"msec \tWorst = "+
				worst+"msec \tAverage = "+average+"msec");
	}
	
	class RelocateTask extends TimerTask{
		@Override
		public void run(){
			MTApplication mtapp = getMTApplication();
			float x = (float)(mtapp.width * Math.random());
			float y = (float)(mtapp.height * Math.random());
			reactionButton.setPositionGlobal(new Vector3D(x, y));
			reactionButton.setVisible(true);
			buzzer.playSound();
			reactionButton.setPickable(true);
			reactionButton.resetTime();
		}
	}

}
