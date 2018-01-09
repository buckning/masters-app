package webcamUtils;

import java.io.File;

import org.mt4j.sceneManagement.IPreDrawAction;

public class FrameGrabberAction implements IPreDrawAction{

	private Webcam webcam;
	private long timeBetweenFrames;
	private long lastFrameGrab;
	private int frameNum = 0;
	private File saveDirectory;
	
	public FrameGrabberAction(File saveDirectory,int timeBetweenFrames){
		this.timeBetweenFrames = timeBetweenFrames;
		lastFrameGrab = System.currentTimeMillis();
		try{
			webcam = Webcam.getInstance();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		
		saveDirectory.mkdir();
//		System.out.println(saveDirectory.toString()+" is a directory = "+ saveDirectory.isDirectory());
		this.saveDirectory = new File(saveDirectory.getAbsolutePath());
	}
	
	@Override
	public void processAction() {
		long now = System.currentTimeMillis();
		if(now - lastFrameGrab > timeBetweenFrames){
			lastFrameGrab = now;
			Thread t = new Thread(){
				public void run(){
					try{
//						System.out.println("saving frame"+frameNum);
						File f = new File(saveDirectory.getAbsolutePath()+"/"+frameNum+".jpg");
						
//						System.out.println(f.toString());
						webcam.saveFrame(f.toString());
						frameNum++;
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
			};
			t.start();
		}
	}

	@Override
	public boolean isLoop() {
		return true;
	}

}
