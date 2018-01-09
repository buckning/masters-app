package occupationalTherapies.activities.reaction;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.math.Vector3D;

public abstract class ReactionButton extends MTEllipse{

	private long startTime;
	private long reactionTime;
	
	public ReactionButton(MTApplication mtapp, Vector3D centerPoint, float rad){
		super(mtapp, centerPoint, rad, rad);
		this.unregisterAllInputProcessors();
		this.removeAllGestureEventListeners();
		this.registerInputProcessor(new TapProcessor(mtapp));
		this.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;

				if(te.getId() == TapEvent.GESTURE_DETECTED){
					reactionTime = System.currentTimeMillis() - startTime;
					buttonPressed(reactionTime);
				}
				return false;
			}
		});
		this.startTime = System.currentTimeMillis();
	}
	
	public void resetTime(){
		this.startTime = System.currentTimeMillis();
	}
	
	public abstract void buttonPressed(long reactionTime);
}
