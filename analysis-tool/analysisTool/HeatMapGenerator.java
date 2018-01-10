package analysisTool;

import analysisTool.heatmap.Gradient;
import analysisTool.heatmap.HeatMap;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

public class HeatMapGenerator extends JFrame{
	public HeatMapGenerator(double[][] data){
		boolean useGraphicsYAxis = true;
        
        // you can use a pre-defined gradient:
        Color[] grad = Gradient.createMultiGradient(new Color[]{new Color(Color.white.getRed(), Color.white.getGreen(), Color.white.getBlue(), 70), 
        		new Color(Color.blue.getRed(), Color.blue.getGreen(), Color.blue.getBlue(), 70),
        		new Color(Color.cyan.getRed(), Color.cyan.getGreen(), Color.cyan.getBlue(), 70), 
        		new Color(Color.green.getRed(), Color.green.getGreen(), Color.green.getBlue(), 70), 
        		new Color(Color.yellow.getRed(), Color.yellow.getGreen(), Color.yellow.getBlue(), 70), 
        		new Color(Color.red.getRed(), Color.red.getGreen(), Color.red.getBlue(), 70)}, 500);
        
        //white, blue, cyan, green, yellow, red
        panel = new HeatMap(data, useGraphicsYAxis, grad);
        panel.setBackground( new Color(1.0f, 1.0f, 1.0f, 0.5f) );
        panel.setCoordinateBounds(0, 6.28, 0, 6.28);
 
        panel.setColorForeground(Color.black);
        panel.setColorBackground(Color.white);
        
        this.getContentPane().add(panel, BorderLayout.CENTER);
	}
	HeatMap panel;
}
