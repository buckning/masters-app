package analysisTool.heatmap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * <p>This class shows the various options of the HeatMap.</p>
 *
 * <hr />
 * <p><strong>Copyright:</strong> Copyright (c) 2007, 2008</p>
 *
 * <p>HeatMap is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.</p>
 *
 * <p>HeatMap is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.</p>
 *
 * <p>You should have received a copy of the GNU General Public License
 * along with HeatMap; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA</p>
 *
 * @author Matthew Beckler (matthew@mbeckler.org)
 * @author Josh Hayes-Sheen (grey@grevian.org), Converted to use BufferedImage.
 * @author J. Keller (jpaulkeller@gmail.com), Added transparency (alpha) support, data ordering bug fix.
 * @version 1.6
 */

class HeatMapDemo extends JFrame
{
    HeatMap panel;
   
    public HeatMapDemo() throws Exception
    {
        super("Heat Map Demo");
        
        // gui stuff to demonstrate options
       
            
        double[][] data = HeatMap.generateSinCosData(200);
//        double[][] data = HeatMap.generatePyramidData(200);
//        double[][] data = new double[1024][768];
        
//        for(int x = 0; x < 1024; x++){
//        	for(int y = 0; y < 768; y++){
//        		data[x][y] = Math.random()*100;
//        	}
//        }
//        for(int x = 1000; x < 1024; x++){
//        	for(int y = 700; y < 768; y++){
//        		data[x][y] = Math.random()*100 + 100;
//        	}
//        }
//        data[1000][700] = 1;
        
        boolean useGraphicsYAxis = true;
        
        // you can use a pre-defined gradient:
        Color[] grad = Gradient.createMultiGradient(new Color[]{new Color(0, 0, 0, 70), new Color(105, 0, 0, 70), new Color(192, 23, 0, 70), new Color(255, 150, 38, 70), new Color(255, 255, 255, 70)}, 500);
        panel = new HeatMap(data, useGraphicsYAxis, grad);
        panel.setBackground( new Color(1.0f, 1.0f, 1.0f, 0.5f) );
        panel.setCoordinateBounds(0, 6.28, 0, 6.28);
 
        panel.setColorForeground(Color.black);
        panel.setColorBackground(Color.white);
        
        this.getContentPane().add(panel, BorderLayout.CENTER);
    }
     
    // this function will be run from the EDT
    private static void createAndShowGUI() throws Exception
    {
        HeatMapDemo hmd = new HeatMapDemo();
        hmd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        hmd.setSize(800, 600);
        hmd.setVisible(true);
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                try
                {
                    createAndShowGUI();
                }
                catch (Exception e)
                {
                    System.err.println(e);
                    e.printStackTrace();
                }
            }
        });
    }
    
}
