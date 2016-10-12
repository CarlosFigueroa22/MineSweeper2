import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
//import java.util.Random;

import javax.swing.JFrame;

public class MyMouseAdapter extends MouseAdapter {
	
//	private Random generator = new Random();
	
	
	public void mousePressed(MouseEvent e) {
		switch (e.getButton()) {
		
		case 1:		//Left mouse button
			Component c = e.getComponent();
			while (!(c instanceof JFrame)) {
				c = c.getParent();
				if (c == null) {
					return;
				}
			}
			JFrame myFrame = (JFrame) c;
			MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);
			Insets myInsets = myFrame.getInsets();
			int x1 = myInsets.left;
			int y1 = myInsets.top;
			e.translatePoint(-x1, -y1);
			int x = e.getX();
			int y = e.getY();
			myPanel.x = x;
			myPanel.y = y;
			myPanel.mouseDownGridX = myPanel.getGridX(x, y);
			myPanel.mouseDownGridY = myPanel.getGridY(x, y);
			myPanel.repaint();
			break;
			
		case 3: //Right Mouse Button working
			Component d = e.getComponent();
			while (!(d instanceof JFrame)) {
				d = d.getParent();
				if (d == null) {
					return;
				}
			}
			JFrame myFrame2 = (JFrame) d;
			MyPanel myPanel2 = (MyPanel) myFrame2.getContentPane().getComponent(0);
			Insets myInsets2 = myFrame2.getInsets();
			int x2 = myInsets2.left;
			int y2 = myInsets2.top;
			e.translatePoint(-x2, -y2);
			int x3 = e.getX();
			int y3 = e.getY();
			myPanel2.x = x3;
			myPanel2.y = y3;
			myPanel2.mouseDownGridX = myPanel2.getGridX(x3, y3);
			myPanel2.mouseDownGridY = myPanel2.getGridY(x3, y3);
			myPanel2.repaint();
			break;
			
		default:    //Some other button (2 = Middle mouse button, etc.)
			//Do nothing
			break;
		}
	}
	public void mouseReleased(MouseEvent e) {
		switch (e.getButton()) {
		
		case 1:		//Left mouse button
			Component c = e.getComponent();
			while (!(c instanceof JFrame)) {
				c = c.getParent();
				if (c == null) {
					return;
				}
			}
			JFrame myFrame = (JFrame)c;
			MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);  //Can also loop among components to find MyPanel
			Insets myInsets = myFrame.getInsets();
			int x1 = myInsets.left;
			int y1 = myInsets.top;
			e.translatePoint(-x1, -y1);
			int x = e.getX();
			int y = e.getY();
			myPanel.x = x;
			myPanel.y = y;
			int gridX = myPanel.getGridX(x, y);
			int gridY = myPanel.getGridY(x, y);
			
			if ((myPanel.mouseDownGridX == -1) || (myPanel.mouseDownGridY == -1)) 
			{
				//Had pressed outside
				//Do nothing
			} 
			else 
			{
				if ((gridX == -1) || (gridY == -1)) 
				{
					//Is releasing outside
					//Do nothing
				} 
				else
				{
					if ((myPanel.mouseDownGridX != gridX) || (myPanel.mouseDownGridY != gridY)) 
					{
						//Released the mouse button on a different cell where it was pressed
						//Do nothing
					}
					
					if (myPanel.colorCoveredSquare[myPanel.mouseDownGridX][myPanel.mouseDownGridY].equals(Color.RED))
					{
						//IF the square has a flag(RED)
						//Do nothing
					}
					
					if(gridX >= 0 && gridX <= 9 && gridY >= 0 && gridY <= 8) {
						//On the left column and on the top row... do nothing
					}
										
//					//If the square has mines around it, display the number of adjacent mines (JUST AN IDEA)
//					if (myPanel.closeMines[myPanel.mouseDownGridX][myPanel.mouseDownGridY] != 0)
//					{
//						myPanel.squareCount[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = String.valueOf(myPanel.closeMines[myPanel.mouseDownGridX][myPanel.mouseDownGridY]);
//					}
										
				}
			}
			myPanel.repaint();
			break;
			
		case 3:		//Right mouse button
			Component d = e.getComponent();
			while (!(d instanceof JFrame)) {
				d = d.getParent();
				if (d == null) {
					return;
				}
			}
			JFrame myFrame2 = (JFrame) d;
			MyPanel myPanel2 = (MyPanel) myFrame2.getContentPane().getComponent(0);
			Insets myInsets2 = myFrame2.getInsets();
			int x2 = myInsets2.left;
			int y2 = myInsets2.top;
			e.translatePoint(-x2, -y2);
			int x3 = e.getX();
			int y3 = e.getY();
			myPanel2.x = x3;
			myPanel2.y = y3;
			int gridX2 = myPanel2.getGridX(x3, y3);
			int gridY2 = myPanel2.getGridY(x3, y3);
			
			if(gridX2 >= 0 && gridX2 <= 9 && gridY2 >= 0 && gridY2 <= 9) 
			{   //Checks if square is NOT red
				if(!myPanel2.colorCoveredSquare[gridX2][gridY2].equals(Color.RED) && !myPanel2.Squares[gridX2][gridY2].isVisible())
				{
					myPanel2.colorCoveredSquare[gridX2][gridY2] = Color.RED;
					myPanel2.repaint();
				}
				//Toggle RED off
				else if(myPanel2.colorCoveredSquare[gridX2][gridY2].equals(Color.RED))
				{
					myPanel2.colorCoveredSquare[gridX2][gridY2] = Color.WHITE;
					myPanel2.repaint();
				}
        	}

			break;
			
		default:    //Some other button (2 = Middle mouse button, etc.)
			//Do nothing
			break;
			}

		}
}