package demos;
 
import java.util.ArrayList;
 
 
import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.inputData.AbstractCursorInputEvt;
import org.mt4j.input.inputData.InputCursor;
import org.mt4j.input.inputData.MTInputEvent;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
 
import processing.core.PGraphics;
 
public class MusicGrid extends MTRectangle{
    private PianoKey[] whiteKeys = new PianoKey[24];
    private float width, height;
    private MidiSynthesizer synth;
    private GridListener gridListener;
    private long lastUpdate = 0;
     
    public MusicGrid(MTApplication mtapp){
        super(0, 0, mtapp.width/10*4, mtapp.height/10*6, mtapp);
        width = mtapp.width;
        height = mtapp.height;
        synth = new MidiSynthesizer();
         
        setFillColor(MTColor.GRAY);
         
        MTRectangle interactionLayer = new MTRectangle(0,0,width,height,mtapp);
        interactionLayer.removeAllGestureEventListeners();
        interactionLayer.setNoFill(true);
        interactionLayer.setNoStroke(true);
        interactionLayer.addInputListener(new IMTInputEventListener() {
            public boolean processInputEvent(MTInputEvent inEvt){
                AbstractCursorInputEvt posEvt = (AbstractCursorInputEvt)inEvt;
                InputCursor m = posEvt.getCursor();
                float x = (int)m.getCurrentEvtPosX();
                float y = (int)m.getCurrentEvtPosY();
     
                // if the key was pressed, keep it active for 100 msec
                for(int i=0; i < whiteKeys.length; i++){
                    if(whiteKeys[i].containsPointGlobal(new Vector3D(x,y))){
                        whiteKeys[i].activate(50);
                    }
                }
                return false;
            }
        });
         
         
        //E = 28, 40, 52, 64, 76, 88
        //G = 31, 43, 55, 67, 79, 91
        //B = 35, 47, 59, 71, 83, 95
        //C# = 25, 37, 49, 61, 73, 85
        int note = 25;
        int[] inc = {0,3,3,4}; 
        float whiteKeyWidth = width/6;
        float whiteKeyHeight = height/4;
        for(int i = 0; i<whiteKeys.length; i++){
            int r = i%4;
            int c = i/6;
            note += inc[r];
            if(i %4 == 0){
                if(i != 0){
                    note +=2;
                }
            }
//          System.out.println(note);
            whiteKeys[i] = new PianoKey(mtapp, r*whiteKeyWidth, c*whiteKeyHeight, whiteKeyWidth, whiteKeyHeight, note, MTColor.WHITE);
            addChild(whiteKeys[i]);         
        }
         
        addChild(interactionLayer);
    }
     
    public void addGridListener(GridListener listener){
        this.gridListener = listener;
    }
     
    @Override
    public void drawComponent(PGraphics g){
         
        long now = System.currentTimeMillis();
        for(PianoKey key: whiteKeys){
            key.update(now - lastUpdate);
        }
        super.drawComponent(g);
        lastUpdate = System.currentTimeMillis();
    }
     
    private class PianoKey extends MTRectangle{
        private boolean on;
        private int note;
        private long timeToLive = 0;
        private MTColor keyColor;
        public PianoKey(MTApplication mtapp, float x, float y, float w, float h, int note, MTColor keyColor){
            super(x, y, w, h, mtapp);
            this.keyColor = keyColor;
            this.on = false;
            this.note = note;
            setPickable(false);
            setStrokeColor(MTColor.BLACK);
            setFillColor(keyColor);
        }
         
        public void on(){
            if(!on){
                synth.playSound(note);
                int x = (int)getCenterPointGlobal().x;
                int y = (int)getCenterPointGlobal().y;
                if(gridListener != null)gridListener.gridPressed(x, y, note);
            }
            this.on = true;
            setFillColor(MTColor.BLUE);
        }
         
        public void off(){
            this.on = false;
            synth.stopSound(note);
            setFillColor(keyColor);
        }
         
        public void update(long time){
             
            if(timeToLive <= 0){
                if(on){
                    off();
                    on = false;
                }
            }
            else{
                timeToLive -= time;
            }
        }
         
        public void activate(long time){
            timeToLive = time;
            on();
        }
    }
}