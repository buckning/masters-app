package occupationalTherapies.activities.paint;

import java.util.ArrayList;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

public class ColorPallet extends MTRectangle{
	private MTApplication mtapp;
	private ColorChangeListener colorChangelistener;
	private float width, height;
	private ArrayList<MTRectangle> colours;
	private MTRectangle activeColour;
	
	public ColorPallet(MTApplication mtapp){
		super(0, 0, 480, 100, mtapp);
		width = 480;
		height = 100;
		this.mtapp = mtapp;
		colours = new ArrayList<MTRectangle>();
		
		addColor(MTColor.GREY, 75, height/2);
		addColor(new MTColor(165, 42, 42), 115, height/2);
		addColor(MTColor.RED, 155, height/2);
		addColor(new MTColor(255, 122, 0), 195, height/2);
		addColor(MTColor.YELLOW, 235, height/2);
		addColor(MTColor.GREEN, 275, height/2);
		addColor(new MTColor(0,255,255), 315, height/2);
		addColor(MTColor.BLUE, 355, height/2);
		addColor(MTColor.PURPLE, 395, height/2);
		addColor(MTColor.WHITE, 435, height/2);
		addColor(MTColor.BLACK,35, height/2);
		activeColour.setStrokeWeight(5.0f);
		activeColour.setStrokeColor(new MTColor(0, 255, 0));
		colorChangelistener = null;        
		rotateZ(getCenterPointGlobal(), 90);
	}
	public MTColor getCurrentColor(){
		return new MTColor(activeColour.getFillColor().getR(),activeColour.getFillColor().getG(),activeColour.getFillColor().getB(), 255);
	}
	private void addColor(MTColor colour, float x, float y){
		MTRectangle colorWidget = new MTRectangle(0, 0, 30, height*4/5, mtapp);
		colorWidget.setNoStroke(false);
		colorWidget.setStrokeColor(MTColor.BLACK);
		colorWidget.setFillColor(colour);
		colorWidget.setPositionRelativeToParent(new Vector3D(x,y));
		addChild(colorWidget);
		setUpColorChangeListener(colorWidget);
		colours.add(colorWidget);
		activeColour = colorWidget;
	}
	
	
	private void setUpColorChangeListener(MTRectangle color){
		final MTRectangle colour = color;
		colour.unregisterAllInputProcessors();
		
        colour.registerInputProcessor(new TapProcessor(mtapp));
		colour.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				int x = (int)te.getCursor().getCurrentEvtPosX();
				int y = (int)te.getCursor().getCurrentEvtPosY();
				MTRectangle rect = (MTRectangle)te.getTargetComponent();
				activeColour.setStrokeWeight(1.0f);
				activeColour.setStrokeColor(MTColor.BLACK);
				rect.setStrokeWeight(5.0f);
				rect.setStrokeColor(new MTColor(0, 255, 0));
				activeColour = rect;
				if(te.getId() == MTGestureEvent.GESTURE_ENDED){
					if(colorChangelistener != null){
						colorChangelistener.colorChanged(x,y,
							new MTColor(colour.getFillColor().getR(),colour.getFillColor().getG(),colour.getFillColor().getB(), 255));
					}
				}
				
				return false;
			}
		});
	}
	
	public void addColorChangeListener(ColorChangeListener listener){
		this.colorChangelistener = listener;
	}
}
