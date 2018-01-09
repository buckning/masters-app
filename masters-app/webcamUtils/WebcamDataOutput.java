package webcamUtils;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

//import javax.media.CaptureDeviceInfo;
//import javax.media.CaptureDeviceManager;
//import javax.media.DataSink;
//import javax.media.Format;
//import javax.media.Manager;
//import javax.media.MediaLocator;
//import javax.media.Processor;
//import javax.media.ProcessorModel;
//import javax.media.format.AudioFormat;
//import javax.media.format.VideoFormat;
//import javax.media.protocol.DataSource;
//import javax.media.protocol.FileTypeDescriptor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class WebcamDataOutput {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new WebcamDataOutput();
	}
	
	public WebcamDataOutput(){
		setupGUI();
		try{
			initWebcam();
			startRecording();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void setupGUI(){
		frame = new JFrame("Webcam data save");
		stopButton = new JButton("Stop Recording");
		stopButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("button");
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						stopButton.setEnabled(false);
						stopRecording();
					}
				});
			}
		});
		
		outputArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(outputArea);
		frame.add(scrollPane);
		frame.add(stopButton, BorderLayout.SOUTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(new Dimension(500,500));
		frame.setVisible(true);
	}
	
	public void initWebcam()throws Exception{
//		Vector<CaptureDeviceInfo> cams = CaptureDeviceManager.getDeviceList(null);
//		CaptureDeviceInfo webcam = null;
//		
//		for(CaptureDeviceInfo source: cams){
//			if(source.getName().startsWith("vfw")){
//				webcam = source;
//				outputArea.append("Found webcam\n");
//			}
//		}
//		
//		MediaLocator locator = webcam.getLocator();
////		processor = Manager.createProcessor(locator);
//		Format formats[] = new Format[2];
//		formats[0] = new AudioFormat(AudioFormat.IMA4);
//		formats[1] = new VideoFormat(VideoFormat.CINEPAK);
//		FileTypeDescriptor outputType = new FileTypeDescriptor(FileTypeDescriptor.QUICKTIME);
//		processor = null;
//		processor = Manager.createRealizedProcessor(new ProcessorModel(formats, outputType));
	}
	
	public void startRecording(){
//		DataSource source = processor.getDataOutput();
//		MediaLocator dest = new MediaLocator("file://foo.mov");
//		filewriter = null;
//		
//		try{
//			filewriter = Manager.createDataSink(source, dest);
//			filewriter.open();
//			
//			filewriter.start();
//			processor.start();
//			outputArea.append("Starting processor\n");
//			outputArea.append("recording . . .\n");
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
		
	}
	
	public void stopRecording(){
//		processor.stop();
//		try{
//			filewriter.stop();
//			filewriter.close();
//		}
//		catch(Exception e){
//			outputArea.append("problem stopping writer/processor");
//		}
	}
	
//	//JMF components
//	private Processor processor;
//	private DataSink filewriter = null;
	
	//GUI components
	private JFrame frame;
	private JTextArea outputArea;
	private JButton stopButton;
}
