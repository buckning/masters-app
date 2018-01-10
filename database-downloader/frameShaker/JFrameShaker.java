package frameShaker;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.Timer;

public class JFrameShaker {
	private static final int SHAKE_DISTANCE = 10;
	private static final double SHAKE_CYCLE = 50;
	private static final int SHAKE_DURATION = 1000;
	private static final int SHAKE_UPDATE = 5;
	
	private JFrame frame;
	private Point naturalLocation;
	private long startTime;
	private Timer shakeTimer;
	private boolean shaking = false;
	private final double TWO_PI = Math.PI*2.0;
	
	public JFrameShaker(JFrame frame){
		this.frame = frame;
	}
	
	public void startShake(){
		if(!shaking){
			naturalLocation = frame.getLocation();
			startTime = System.currentTimeMillis();
			shakeTimer = new Timer(SHAKE_UPDATE, new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					long elapsed = System.currentTimeMillis() - startTime;
					double waveOffset = (elapsed % SHAKE_CYCLE)/SHAKE_CYCLE;
					double angle = waveOffset * TWO_PI;
					
					int shakenX = (int)((Math.sin(angle)*SHAKE_DISTANCE)+naturalLocation.x);
					frame.setLocation(shakenX,naturalLocation.y);
					frame.repaint();
					if(elapsed >= SHAKE_DURATION)stopShake();
				}
			});
			shakeTimer.start();
			shaking = true;
		}
	}
	
	public void stopShake(){
		shakeTimer.stop();
		frame.setLocation(naturalLocation);
		frame.repaint();
		shaking = false;
	}
}
