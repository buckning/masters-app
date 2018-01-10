package databaseDownloader;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import frameShaker.JFrameShaker;

import net.miginfocom.swing.MigLayout;

public class StartDownloader extends JFrame implements GUIUpdater{
	private String savePath = "C:\\";
	private Database db;
	
	private String defaultDbName = "hospitalData";
	private String defaultDbUser = "root";
	private String defaultDbPassword = "gmitresearch";
	private String defaultTable = "sessionData";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch(Exception e){
			e.printStackTrace();
		}
		new StartDownloader();
	}

	
	public StartDownloader(){
		super("Database Downloader");
		setSize(new Dimension(500, 220));
		frame = this;
		int textFieldLength = 50;
		
		tfDB = new JTextField(textFieldLength);
		tfDB.setText(defaultDbName);
		tfPortNum = new JTextField(textFieldLength);
		tfPortNum.setText("3306");
		tfUserName = new JTextField(textFieldLength);
		tfUserName.setText(defaultDbUser);
		pfPassword = new JPasswordField(textFieldLength);
		pfPassword.setText(defaultDbPassword);
		tfOutput = new JTextField(textFieldLength);
		tfOutput.setText(savePath);
		tfTableName = new JTextField();
		tfTableName.setText(defaultTable);
		JLabel lblDB = new JLabel("Database");
		JLabel lblPort = new JLabel("Port");
		JLabel lblUserName = new JLabel("User name");
		JLabel lblPassword = new JLabel("password");
		JLabel lblOutput = new JLabel("Output file");
		JButton btnBrowse = new JButton("Browse");
		btnSave = new JButton("Save");
		
		connectionPanel = new JPanel();
		connectionPanel.setLayout(new MigLayout());
		connectionPanel.add(lblDB);
		connectionPanel.add(tfDB,"growx,wrap");
		connectionPanel.add(lblPort);
		connectionPanel.add(tfPortNum,"wrap");
		connectionPanel.add(lblUserName);
		connectionPanel.add(tfUserName,"wrap");
		connectionPanel.add(lblPassword);
		connectionPanel.add(pfPassword,"wrap");
		btnConnect = new JButton("Connect to database");
		progressBar = new JProgressBar();
		connectionPanel.add(btnConnect,"split,spanx, growx, wrap");
		
		savePanel = new JPanel(new MigLayout());
		savePanel.add(new JLabel("Table"));
		savePanel.add(tfTableName,"span, growx,wrap");
		savePanel.add(lblOutput);
		savePanel.add(tfOutput, "split, span, growx");
		savePanel.add(btnBrowse, "wrap");
		savePanel.add(btnSave,"span, growx");
		setResizable(false);
		
		btnBrowse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				if( chooser.showDialog(frame, "Choose") == JFileChooser.APPROVE_OPTION ){
					savePath = chooser.getSelectedFile().toString();
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							tfOutput.setText(savePath);
						}
					});
				}
			}
		});
		
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				indeterminate(false);
				File f = new File(tfOutput.getText());
				if(f.isDirectory())	db.downloadAllSessions("sessionData",tfOutput.getText()+File.separator + "test.zip");
			}
		});
		
		btnConnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				connectToDatabase(tfDB.getText(), Integer.parseInt(tfPortNum.getText()),tfUserName.getText(), new String(pfPassword.getPassword()));
			}
		});
		
		add(connectionPanel);
		add(progressBar,BorderLayout.SOUTH);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void connectToDatabase(String databaseName, int port, String user, String password){
		if(databaseName.equals("")){
			JOptionPane.showMessageDialog(frame, "Database name must be entered", "Error!" , JOptionPane.ERROR_MESSAGE);
			return;
		}
		indeterminate(true);
		db = Database.connectToDatabase(port, databaseName, user, password);

		if(db != null){		
			db.setGUIUpdater(this);
			btnConnect.setEnabled(false);
			JOptionPane.showMessageDialog(frame, "You are successfully connected to the "+databaseName+" database", "Connection Successful!" , JOptionPane.INFORMATION_MESSAGE);
			changeGUISetup();
			indeterminate(false);
		}
		else{
			JFrameShaker shaker = new JFrameShaker(this);
			shaker.startShake();
			JOptionPane.showMessageDialog(frame, "Error occured while connecting to database", "Connection Unsuccessful!" , JOptionPane.ERROR_MESSAGE);
			indeterminate(false);
		}
	}
	
	private void changeGUISetup(){
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				remove(connectionPanel);
				setResizable(true);
				setSize(new Dimension(500, 150));
				setResizable(false);
				add(savePanel);
				tfTableName.requestFocusInWindow();
			}
		});
	}
	
	public void indeterminate(boolean b){
		final boolean bool = b;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				progressBar.setIndeterminate(bool);
		
			}
		});
		
		
	}
	
	
	/**
	 * Receive updated information about the database download task and update the progress bar
	 * displaying feedback to the user
	 */
	@Override
	public void fireProgressUpdate(int start, int end, int current) {
		final int s = start;
		final int e = end;
		final int c = current;
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				progressBar.setMinimum(s);
				progressBar.setMaximum(e);
				progressBar.setValue(c);
			}
		});
		
	} 
	
	/**
	 * Database download has completed.
	 */
	@Override
	public void fireTaskComplete(){
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				progressBar.setMinimum(0);
				progressBar.setMaximum(0);
				progressBar.setValue(0);
				JOptionPane.showMessageDialog(frame, "Database was successfully downloaded to \n"+savePath, "Download Complete!" , JOptionPane.INFORMATION_MESSAGE);
				frame.dispose();
				openFileExplorer(new File(savePath));
			}
		});
	}
	
	private void openFileExplorer(File path){
		Desktop desktop = null;
	    // on Windows, retrieve the path of the "Program Files" folder
//	    File file = new File(path);
	    if (Desktop.isDesktopSupported()) {
	      desktop = Desktop.getDesktop();
	    }
	    try {
	      desktop.open(path);
	    }
	    catch (IOException e){  }
	}
	
	private JProgressBar progressBar;
	private JTextField tfDB;
	private JTextField tfPortNum;
	private JTextField tfUserName; 
	private JTextField tfOutput;
	private JTextField tfTableName;
	private JPasswordField pfPassword;
	private JButton btnConnect;
	private JPanel savePanel;
	private JPanel connectionPanel;
	private JButton btnSave;
	
	private JFrame frame;
}
