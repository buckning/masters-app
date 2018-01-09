package occupationalTherapies.activities;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.IPreDrawAction;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

public class LoadingScene extends AbstractScene{

	public LoadingScene(MTApplication mtapp, ILoadable scene) {
		super(mtapp, "Loading Scene");
		final ILoadable mainScene = scene; 

		IFont font = FontManager.getInstance().createFont(mtapp, "arial.ttf", 60, MTColor.WHITE, MTColor.WHITE);
		MTTextArea textArea = new MTTextArea(mtapp, font);
		textArea.setPositionGlobal(new Vector3D(mtapp.width/2, mtapp.height/2));
		textArea.setText("Please Wait ...");
		textArea.removeAllGestureEventListeners();
		textArea.unregisterAllInputProcessors();
		textArea.setNoFill(true);
		textArea.setNoStroke(true);
		this.setClearColor(new MTColor(146, 180, 158));
		getCanvas().addChild(textArea);
		//before this scene is drawn, start loading the main scene in a thread
		registerPreDrawAction(new IPreDrawAction() {
			@Override
			public void processAction() {
				getMTApplication().invokeLater(new Runnable() {
					@Override
					public void run() {
						mainScene.load();
						AbstractScene theScene = (AbstractScene)mainScene;
						getMTApplication().changeScene(theScene);
					}
				});
			}
			
			@Override
			public boolean isLoop() {
				// the scene is only to be loaded once
				return false;
			}
		});
	}
	@Override
	public void init() {}
	@Override
	public void shutDown() {}

}
