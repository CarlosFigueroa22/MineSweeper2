import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Random;

import javax.swing.JPanel;

public class MyPanel extends JPanel {
	private static final long serialVersionUID = 3426940946811133635L;
	private static final int GRID_X = 25;
	private static final int GRID_Y = 25;
	private static final int INNER_CELL_SIZE = 29;
	private static final int TOTAL_COLUMNS = 9;
	private static final int TOTAL_ROWS = 10;  
	public int x = -1;
	public int y = -1;
	public int mouseDownGridX = 0;
	public int mouseDownGridY = 0;
	
//	public Color[][] colorArray = new Color[TOTAL_COLUMNS][TOTAL_ROWS];
	public Color[][] colorCoveredSquare = new Color[TOTAL_COLUMNS][TOTAL_ROWS+1];  //Add 1 to avoid an error
	public Color[][] colorUncoveredSquare = new Color[TOTAL_COLUMNS][TOTAL_ROWS];  //Will determine if the square is a mine or not
	
	public static int mineCount = 10; //Number of mines
	public int squareCountAvailable = (TOTAL_COLUMNS-1)*(TOTAL_COLUMNS-1) - mineCount;

	
//	//Used to display the numbers inside squares
//	public int[][] closeMines= new int[TOTAL_COLUMNS + 1][TOTAL_ROWS + 1];
//	public String[][] squareCount = new String[TOTAL_COLUMNS + 1][TOTAL_ROWS + 1];
	
	public MyPanel() {   //This is the constructor... this code runs first to initialize
		
		if (INNER_CELL_SIZE + (new Random()).nextInt(1) < 1) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("INNER_CELL_SIZE must be positive!");
		}
		if (TOTAL_COLUMNS + (new Random()).nextInt(1) < 2) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_COLUMNS must be at least 2!");
		}
		if (TOTAL_ROWS + (new Random()).nextInt(1) < 3) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_ROWS must be at least 3!");
		}
		for (int x = 0; x < TOTAL_COLUMNS; x++) {   //Grid 9x9
			for (int y = 0; y < TOTAL_ROWS; y++) {
				colorCoveredSquare[x][y] = Color.WHITE;
			}
		}
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		//Compute interior coordinates
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		
		int x2 = getWidth() - myInsets.right - 1;
		int y2 = getHeight() - myInsets.bottom - 1;
		int width = x2 - x1;
		int height = y2 - y1;

		//Paint the background
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x1, y1, width + 1, height + 1);

		//By default, the grid will be 9x9 (see above: TOTAL_COLUMNS and TOTAL_ROWS) 
		g.setColor(Color.BLACK);
		for (int y = 0; y <= TOTAL_ROWS - 1; y++)
		{
			g.drawLine(x1 + GRID_X, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)), x1 + GRID_X + ((INNER_CELL_SIZE + 1) * TOTAL_COLUMNS), y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)));
		}
		for (int x = 0; x <= TOTAL_COLUMNS; x++)
		{
			g.drawLine(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y, x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS - 1)));
		}

		//Paint cell/square colors
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS - 1; y++) {
				if ((x == 0) || (y != TOTAL_ROWS - 1)) {
					Color c = colorCoveredSquare[x][y];
					g.setColor(c);
					g.fillRect(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);
				}
			}
		}
		
//		//Displays the numbers
//		for (int x=1; x<TOTAL_COLUMNS;x++)
//		{
//			for (int y=1; y<TOTAL_ROWS-1; y++)
//			{
//				g.setColor(Color.GREEN);
//				g.drawString(squareCount[x][y], GRID_X + x*(INNER_CELL_SIZE+1) + 10, GRID_Y + y*(INNER_CELL_SIZE+1) + 20);
//			}
//		}
	
	}
	
	public int getGridX(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
			return x;
		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 2) {   //Outside the rest of the grid
			return -1;
		}
		return x;
	}
	public int getGridY(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
			return y;
		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 2) {   //Outside the rest of the grid
			return -1;
		}
		return y;
	}
}	

//  //IDEAS (ERRORS AT THE MOMENT)
//	public void displayAdjacentSquares(int x, int y)
//	{ 
//		for (int a = -1; a<=1; a++)
//		{
//			for (int b = -1; b<=1; b++)
//			{
//				if (colorCoveredSquare[x + a][y + b].equals(Color.RED))
//				{
//					//If the square has a flag(RED)
//					//Do nothing
//				}
//				else if (x + a < 1 || y + b < 1 || x + a == TOTAL_COLUMNS || y + b == TOTAL_COLUMNS || x==TOTAL_COLUMNS || y==TOTAL_COLUMNS || x < 1 || y < 1)
//				{
//					//If it is outside the grid
//					//Do nothing
//				}
//				else if (b==0 && a==0)
//				{
//					//Do nothing
//	
//				}			
//				else if (colorCoveredSquare[x + a][y + b].equals(colorUncoveredSquare[x + a][y + b]))
//				{
//					//If the square is already light gray
//					//Do nothing
//				}
//				else if (closeMines[x + a][y + b] != 0)
//				{
//					//If the square has mines around it
//					squareCount[x + a][y + b] = String.valueOf(closeMines[x + a][y + b]);
//					squareCountAvailable--;
//					colorCoveredSquare[x + a][y + b] = colorUncoveredSquare[x + a][y + b];
//				}
//				else
//				{
//					//If the square doesn't have mines around
//					squareCountAvailable--;
//					colorCoveredSquare[x + a][y + b] = colorUncoveredSquare[x + a][y + b];
//					displayAdjacentSquares(x + a, y + b);	
//				}				
//			}
//		}
//	}
//	
//	public void randomMineLocation(int x, int y)
//	{
//		//FreeSquare is the first square you click, there shouldn't be a mine
//		Boolean[][] freeSquare = new Boolean[TOTAL_COLUMNS+1][TOTAL_ROWS+1];
//
//		//asign all values to false
//		for (int a = 0; a<=TOTAL_COLUMNS; a++)
//		{
//			for (int b = 0; b<=TOTAL_ROWS; b++)
//			{
//				freeSquare[a][b] = false;
//			}
//		}
//		for (int a = -1; a<=1; a++)
//		{     
//			for (int b = -1; b<=1; b++)
//			{
//				freeSquare[x + a][y + b] = true;
//			}
//		}
//
//
//		Random random = new Random();    
//		for(int a = 1 ; a <= mineCount; a++)
//		{ 
//			//Assigns the squares that are mines (X and Y are random)
//			int X = random.nextInt(TOTAL_COLUMNS) + 1; //Add 1 so the x can never be 0 and will be 9
//			int Y = random.nextInt(TOTAL_COLUMNS) + 1;
//
//			if (colorUncoveredSquare[X][Y] == Color.BLACK)
//				a--;
//			else if (freeSquare[X][Y])
//				a--;
//			else
//			{
//				colorUncoveredSquare[X][Y] = Color.BLACK;
//
//				for (int b = -1; b<=1; b++)
//				{
//					for (int c = -1; c<=1; c++)
//					{
//						this.closeMines[X + b][Y + c]++;
//					}
//				}
//			}
//		}
//	}
//}