package analysisTool;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.apache.commons.io.IOUtils;

import net.miginfocom.swing.MigLayout;

public class AnalysisFrame extends JFrame{
	private BufferedImage watermark = null;
	private ZipFile zf;
	private Enumeration enumeration;
	private BufferedImage userImage = null;
	private SessionXMLDecoder decoder;
	private static String title;
	static{
		title = "Data Analysis Tool V0.3";
	}
	
	public AnalysisFrame(String zipFileName){
		super(title);
		
//		try{
//			watermark = ImageIO.read(new File("watermark.png"));
//			
//		}
//		catch(Exception e){
//			e.printStackTrace();
//		}
		
		JPanel upperPanel = new JPanel(new BorderLayout());
		
		upperPanel.setPreferredSize(new Dimension(600,300));
		upperPanel.setMinimumSize(new Dimension(600,300));
		upperPanel.setMaximumSize(new Dimension(600,300));
		
		JPanel photoPanel = new JPanel(){
			@Override
			public void paintComponent(Graphics g){
				Graphics2D g2 = (Graphics2D)g;
				if(userImage != null){
					g2.drawImage(userImage,0,0, null);
				}
			}
		};
//		ImageIcon icon = new ImageIcon("session35.jpg");
//		photoPanel.add(new JLabel(icon));
		
		JPanel infoPanel = new JPanel(new MigLayout());
		
		int textFieldSize = 10;
		application = new JTextField(textFieldSize);
		id = new JTextField(textFieldSize);
		startTimeField = new JTextField(textFieldSize);
		endTimeField = new JTextField(textFieldSize);
		startDateField = new JTextField(textFieldSize);
		endDateField = new JTextField(textFieldSize);
		sessionLengthField = new JTextField(textFieldSize);
		application.setEditable(false);
		application.setEnabled(false);
		id.setEditable(false);
		id.setEnabled(false);
		startTimeField.setEditable(false);
		startTimeField.setEnabled(false);
		endTimeField.setEditable(false);
		endTimeField.setEnabled(false);
		startDateField.setEditable(false);
		startDateField.setEnabled(false);
		endDateField.setEditable(false);
		endDateField.setEnabled(false);
		sessionLengthField.setEditable(false);
		sessionLengthField.setEnabled(false);
		infoPanel.add(new JLabel("Application Used: "));
		infoPanel.add(application,"growx, wrap");
		infoPanel.add(new JLabel("ID: "));
		infoPanel.add(id, "growx, wrap");
		infoPanel.add(new JLabel("Start Time: "));
		infoPanel.add(startTimeField,"growx, wrap");
		infoPanel.add(new JLabel("End Time: "));
		infoPanel.add(endTimeField,"growx, wrap");
		infoPanel.add(new JLabel("Start Date: "));
		infoPanel.add(startDateField, "growx, wrap");
		infoPanel.add(new JLabel("End Date: "));
		infoPanel.add(endDateField, "growx, wrap");
		infoPanel.add(new JLabel("Session Length: "));
		infoPanel.add(sessionLengthField, "growx, wrap");
		
		upperPanel.add(infoPanel, BorderLayout.CENTER);
		photoPanel.setBackground(Color.black);
		photoPanel.setMinimumSize(new Dimension(400,400));
		photoPanel.setPreferredSize(new Dimension(400,400));
		photoPanel.setMaximumSize(new Dimension(400,400));
		upperPanel.add(photoPanel, BorderLayout.EAST);
		
		interactionData = new JTextArea();
		interactionData.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(interactionData);
		scrollPane.setMinimumSize(new Dimension(400,300));
		scrollPane.setPreferredSize(new Dimension(400,300));
		scrollPane.setMaximumSize(new Dimension(400,300));
		
		JPanel navigationPanel = new JPanel(new MigLayout());
		nextBtn = new JButton("Next");
		nextBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//parse new file
				try{
					next();
					next();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
//		nextBtn.setEnabled(false);//TODO
		try{
			zf = new ZipFile(new File(zipFileName));
		
		}
		catch(Exception e){
			e.printStackTrace();
		}
		enumeration = zf.entries();
		
		
		navigationPanel.add(nextBtn,"growx, wrap");
		
		JButton openBtn = new JButton("Open");
		openBtn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.showOpenDialog(null);
				File f = fileChooser.getSelectedFile();
				if(f != null){String fileName = f.toString();
					if(fileName.endsWith(".zip")){
						setTitle(title + " - " + f.getName());
						try{
							zf = new ZipFile(new File(fileName));
							enumeration = zf.entries();
							nextBtn.setEnabled(true);
							next();
							next();
							
						}
						catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			}
		});
		navigationPanel.add(openBtn);
		
		add(upperPanel);
		add(scrollPane, BorderLayout.SOUTH);
		add(navigationPanel, BorderLayout.EAST);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu toolsMenu = new JMenu("Tools");
		JMenuItem heatMap = new JMenuItem("Generate Heat Map");
		
		JMenu helpMenu = new JMenu("Help");
		JMenuItem aboutItem = new JMenuItem("Help");
		toolsMenu.add(heatMap);
		helpMenu.add(aboutItem);
		
		menuBar.add(toolsMenu);
		menuBar.add(helpMenu);
		setJMenuBar(menuBar);
		
		aboutItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AboutFrame frame = new AboutFrame(null);
				frame.setResizable(true);
				frame.setVisible(true);
			}
		});
		
		heatMap.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
//				int lineCount = interactionData.getLineCount();
//				double[][] data = new double[1024][768];
//				
//				String text = interactionData.getText();
//				String[] tokens = text.split("\n");
//				for(String s: tokens){
////					System.out.println("**********"+s);
//					String[] coords = s.split(" ");
//					if(coords.length > 2){
////						System.out.println("("+coords[0]+","+coords[1]+")");
//						try{
//							int x = Integer.parseInt(coords[0]);
//							int y = Integer.parseInt(coords[1]);
////							System.out.println(x+" "+ y);
//							data[x][y] += 100;
//						}catch(Exception e){
//							//JOptionPane.showMessageDialog(null, "Unexpected file input", "Error", JOptionPane.ERROR_MESSAGE);
//							e.printStackTrace();
//						}
//					}
//				}				
//	
				
				int lineCount = interactionData.getLineCount();
				double[][] data = new double[1024][768];
				
				String text = interactionData.getText();
				String[] tokens = text.split("\n");
				for(String s: tokens){
					String[] coords = s.split(" ");
					if(coords.length > 2){
						try{
							int x = Integer.parseInt(coords[0]);
							int y = Integer.parseInt(coords[1]);
							 
							data[x][y] += 100;
						}catch(Exception e){
							//JOptionPane.showMessageDialog(null, "Unexpected file input", "Error", JOptionPane.ERROR_MESSAGE);
							e.printStackTrace();
						}
					}
				}				
	
		
				for(int i = 0; i < 1024; i++){
					for(int j = 0; j < 768; j++){						
						if(data[i][j]!=0.0)System.out.println("("+i+", "+j+") = "+data[i][j]);
					}
				}
					HeatMapGenerator generator = new HeatMapGenerator(data);
					generator.setSize(new Dimension(1024, 768));
					generator.setVisible(true);
			}
		});
	}
	
	private void setInteractions(SessionXMLDecoder decoder){
		ArrayList<String> interactions = decoder.getInteractionData(); 
		interactionData.setText("");
		for(String s: interactions)interactionData.append(s);
	}
	
	private void setInfoPanel(SessionXMLDecoder decoder){
		String applicationName = decoder.getValue("APP");
		String userID = decoder.getValue("ID");
		String startTime = decoder.getValue("STARTTIME");
		String endTime = decoder.getValue("ENDTIME");
		String startDate = startTime.substring(0, 6);
		String endDate = endTime.substring(0, 6);
		startTime = startTime.substring(7,13);
		endTime = endTime.substring(7, 13);
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < startTime.length(); i++){
			sb.append(startTime.charAt(i));
		}
		sb.insert(4, ":");
		sb.insert(2, ":");
		startTime = sb.toString();
		
		sb = new StringBuilder();
		for(int i = 0; i < endTime.length(); i++){
			sb.append(endTime.charAt(i));
		}
		sb.insert(4, ":");
		sb.insert(2, ":");
		endTime = sb.toString();
		
		sessionLengthField.setText(calculateSessionLength(startTime, endTime));
		
		sb = new StringBuilder();
		for(int i = 0; i < startDate.length(); i++){
			sb.append(startDate.charAt(i));
		}
		sb.insert(4, "/");
		sb.insert(2, "/");
		startDate = sb.toString();
		
		sb = new StringBuilder();
		for(int i = 0; i < endDate.length(); i++){
			sb.append(endDate.charAt(i));
		}
		sb.insert(4, "/");
		sb.insert(2, "/");
		endDate = sb.toString();
		
		
		application.setText(applicationName);
		id.setText(userID);
		startTimeField.setText(startTime);
		endTimeField.setText(endTime);
		startDateField.setText(startDate);
		endDateField.setText(endDate);
	}
	
	public void next()throws IOException{
		if(enumeration.hasMoreElements()){
			ZipEntry s = (ZipEntry)enumeration.nextElement();
			if(s.toString().endsWith(".jpg")){
				System.out.println("reading image file: "+s.toString());
				try{
					userImage = ImageIO.read(zf.getInputStream(s));
					repaint();	//needed to display the correct image
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
			if(s.toString().endsWith(".xml")){
				//convert input stream to string
				final File tempXMLFile = File.createTempFile("temp", "xml");
				System.out.println(tempXMLFile.getAbsolutePath());
				tempXMLFile.deleteOnExit();
				
				InputStream in = zf.getInputStream(s);
				StringWriter writer = new StringWriter();
				IOUtils.copy(in, writer);
				FileWriter fw = new FileWriter(tempXMLFile);
				fw.write(writer.toString());
				fw.flush();
				fw.close();
				decoder = new SessionXMLDecoder(tempXMLFile.getAbsolutePath());
				try{
					decoder.decode();
					
					SwingUtilities.invokeLater(new Runnable() {
						
						@Override
						public void run() {
							setInteractions(decoder);
							setInfoPanel(decoder);
							tempXMLFile.delete();
						}
					});
				}
				catch(Exception e){
					e.printStackTrace();
				}
//				System.out.println(writer.toString());
			}
		}
		else{
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					nextBtn.setEnabled(false);
				}
			});
		}
	
	}

	
	@Override
	public void paint(Graphics g){
		super.paintComponents(g);
		//display a watermark
		Graphics2D g2 = (Graphics2D)g;
		AffineTransform oldTrans = g2.getTransform();
		g2.translate(50, 0);
		g2.rotate(Math.toRadians(32));
		g2.scale(0.6f, 0.5f);
		if(watermark != null)g2.drawImage(watermark, 0, 0, null);
		g2.setTransform(oldTrans);		
	}
	
	private String calculateSessionLength(String start, String end){
		//convert to seconds 
		int hour = 10*Integer.parseInt((""+start.charAt(0)));
		hour+= Integer.parseInt((""+start.charAt(1)));
		hour *= (60*60);
		int min = 10*Integer.parseInt((""+start.charAt(3)));
		min+= Integer.parseInt((""+start.charAt(4)));
		min*=60;
		int sec = 10*Integer.parseInt((""+start.charAt(6)));
		sec+= Integer.parseInt((""+start.charAt(7)));
		
		int startTime = hour+min+sec;
		
		hour = 10*Integer.parseInt((""+end.charAt(0)));
		hour+= Integer.parseInt((""+end.charAt(1)));
		hour *= (60*60);
		min = 10*Integer.parseInt((""+end.charAt(3)));
		min+= Integer.parseInt((""+end.charAt(4)));
		min*=60;
		sec = 10*Integer.parseInt((""+end.charAt(6)));
		sec+= Integer.parseInt((""+end.charAt(7)));
//		long startTime = Integer.getInteger(""+start.charAt(0))+(Integer.getInteger(""+start.charAt(1)));
		System.out.println("long = "+hour+" min = "+min+"sec = "+sec+ "time = "+startTime);
		int endTime = hour+min+sec;
		
//		System.out.println("end time = "+endTime);
//		System.out.println("session length = "+ (endTime - startTime));
		int sessionLength =  endTime - startTime;
		if(sessionLength <0)return "????????";
		sec = sessionLength%60;
		hour = sessionLength/(60*60);
		min = (sessionLength%(60*60))/(60);
		String length = "";
		if(hour < 10)length+="0";
		length += ""+hour+":";
		if(min < 10)length+="0";
		length += min+":";
		if(sec < 10)length += "0";
		length += sec;
		
		return length;//TODO;
	}
	
	private JTextField application;
	private JTextField id;
	private JTextField startTimeField;
	private JTextField endTimeField;
	private JTextField startDateField;
	private JTextField sessionLengthField;
	private JTextField endDateField;
	private JTextArea interactionData;
	private JButton nextBtn;
}
