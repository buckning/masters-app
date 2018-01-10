package analysisTool;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

public class StartAnalysis {
	public static void main(String[] args){
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(Exception e){
			
		}
		
		
		JOptionPane.showMessageDialog(null, "Select file for analysis.");
		
		AnalysisFrame analysisFrame = new AnalysisFrame(selectZipFileForAnalysis());
		analysisFrame.setSize(800,800);
		analysisFrame.setDefaultCloseOperation(AnalysisFrame.EXIT_ON_CLOSE);
		analysisFrame.pack();
		analysisFrame.setVisible(true);
	}
	
	private static String selectZipFileForAnalysis() {
		JFileChooser chooser = new JFileChooser();
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		if(chooser.showDialog(null, "Choose") == JFileChooser.APPROVE_OPTION ) {
			return chooser.getSelectedFile().toString();
		}
		return null;
	}
}
