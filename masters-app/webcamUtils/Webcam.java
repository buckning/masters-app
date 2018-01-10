package webcamUtils;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.imageio.ImageIO;
import javax.media.Buffer;
import javax.media.CaptureDeviceInfo;
import javax.media.CaptureDeviceManager;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.control.FrameGrabbingControl;
import javax.media.format.VideoFormat;
import javax.media.util.BufferToImage;

/**
 * Uses JMF to access the webcam
 * @author amcglynn
 *
 */
public class Webcam {
	private MediaLocator locator;
	private Player player;
	private static Webcam webcam = null;
	
	private Webcam()throws Exception{
//		Vector<CaptureDeviceInfo> cams = CaptureDeviceManager.getDeviceList(null);	
//		CaptureDeviceInfo webcam = null;
//		for(CaptureDeviceInfo source: cams){
//			//webcam comes in the format: "vfw:Microsoft WDM Image Capture (Win32):0"
//			if(source.getName().startsWith("vfw:")){
//				webcam = source;
//			}
//		}
//		locator = webcam.getLocator();
//		player = Manager.createRealizedPlayer(locator);
//		player.start();
//		//allow webcam to start up
//		Thread.sleep(3000);
		Webcam.webcam = this;
	}
	
	/**
	 * Get an instance of the webcam object. Only one webcam per system. 
	 * @return
	 * @throws Exception
	 */
	public static Webcam getInstance()throws Exception{
		if(webcam == null){
			new Webcam();
		}
		return webcam;
	}
	
	/**
	 * Get the video feedback to play in a UI
	 * @return the video feed as an AWT component
	 */
	public Component getVisualComponent(){
		return player.getVisualComponent();
	}
	
	/**
	 * Save a frame to a JPEG image file.
	 * @param fileName
	 */
	public void saveFrame(String fileName) throws Exception, IOException{
//		FrameGrabbingControl frameGrabber = (FrameGrabbingControl)player.getControl("javax.media.control.FrameGrabbingControl");
//		Buffer buf = frameGrabber.grabFrame();	
//		Image img = (new BufferToImage((VideoFormat)buf.getFormat()).createImage(buf));
//		BufferedImage buffImg = new BufferedImage(img.getWidth(null),
//				img.getHeight(null), BufferedImage.TYPE_INT_RGB);
//		Graphics2D g = buffImg.createGraphics();
//		g.drawImage(img, null, null);
//		ImageIO.write(buffImg, "jpg", new File(fileName));
	}
}
/******************************************************************
	Backup
	the backup can create many webcam objects which is not desireable
*******************************************************************/
//package webcamUtils;
//
//import java.awt.Component;
//import java.awt.Graphics2D;
//import java.awt.Image;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import java.util.Vector;
//import javax.imageio.ImageIO;
//import javax.media.Buffer;
//import javax.media.CaptureDeviceInfo;
//import javax.media.CaptureDeviceManager;
//import javax.media.Manager;
//import javax.media.MediaLocator;
//import javax.media.Player;
//import javax.media.control.FrameGrabbingControl;
//import javax.media.format.VideoFormat;
//import javax.media.util.BufferToImage;
//
//public class Webcam {
//	private MediaLocator locator;
//	private Player player;
//	
//	public Webcam()throws Exception{
//		Vector<CaptureDeviceInfo> cams = CaptureDeviceManager.getDeviceList(null);	
//		CaptureDeviceInfo webcam = null;
//		for(CaptureDeviceInfo source: cams){
//			//webcam comes in the format: "vfw:Microsoft WDM Image Capture (Win32):0"
//			if(source.getName().startsWith("vfw:")){
//				webcam = source;
//			}
//		}
//		locator = webcam.getLocator();
//		player = Manager.createRealizedPlayer(locator);
//		player.start();
//		//allow webcam to start up
//		Thread.sleep(3000);
//	}
//	
//	/**
//	 * Get the video feedback to play in a UI
//	 * @return the video feed as an AWT component
//	 */
//	public Component getVisualComponent(){
//		return player.getVisualComponent();
//	}
//	
//	/**
//	 * Save a frame to a JPEG image file.
//	 * @param fileName
//	 */
//	public void saveFrame(String fileName) throws Exception, IOException{
//		FrameGrabbingControl frameGrabber = (FrameGrabbingControl)player.getControl("javax.media.control.FrameGrabbingControl");
//		Buffer buf = frameGrabber.grabFrame();	
//		Image img = (new BufferToImage((VideoFormat)buf.getFormat()).createImage(buf));
//		BufferedImage buffImg = new BufferedImage(img.getWidth(null),
//				img.getHeight(null), BufferedImage.TYPE_INT_RGB);
//		Graphics2D g = buffImg.createGraphics();
//		g.drawImage(img, null, null);
//		ImageIO.write(buffImg, "jpg", new File(fileName));
//	}
//}
