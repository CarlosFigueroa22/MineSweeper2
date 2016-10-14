import java.awt.Color;
import java.awt.Font;
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
	public Color[][] colorCoveredSquare = new Color[TOTAL_COLUMNS][TOTAL_ROWS+1];  //Add 1 to avoid an error
	public Color[][] colorUncoveredSquare = new Color[TOTAL_COLUMNS][TOTAL_ROWS];  //Will determine if the square is a mine or not
	
	public static int mineCount = 10; //Number of mines
	public int squareCountAvailable = (TOTAL_COLUMNS-1)*(TOTAL_COLUMNS-1) - mineCount;

	public GridSquare[][] Squares = new GridSquare[TOTAL_COLUMNS][TOTAL_ROWS];
	public boolean revealAllNumbers = false;
	public boolean gameOver = false;
	public int revealedSquares;
	public boolean won = false;
	

	
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
				Squares[x][y] = new GridSquare(x, y);
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
		Font arial = new Font("Arial", Font.BOLD, 20);
		g.setFont(arial);
		
		if(revealedSquares == (81 - mineCount))
		{
			win(g);
			repaint();
		}
		
		if(gameOver == true)
		{
			revealAllNumbers = true;
			revealAllMines();
			lose(g);
			repaint();
		}
		
		for (int x = 0; x < TOTAL_COLUMNS; x++)
		{
			for (int y = 0; y < TOTAL_ROWS - 1; y++)
			{
				if(Squares[x][y].isVisible() && Squares[x][y].getNearbyMines() > 0)
				{
					revealNumbers(g, x, y);
					repaint();
				}
				
				else if(Squares[x][y].isVisible() && Squares[x][y].getNearbyMines() <= 0 && !Squares[x][y].isMine())
				{
					if (x!=0)
					{
						if(y!=8){Squares[x - 1][y + 1].setVisible(true); colorCoveredSquare[x - 1][y + 1] = Color.GRAY;}
						if(y!=0){Squares[x - 1][y - 1].setVisible(true); colorCoveredSquare[x - 1][y - 1] = Color.GRAY;}
						Squares[x - 1][y].setVisible(true); colorCoveredSquare[x - 1][y] = Color.GRAY;
					}
					if(x!=8)
					{
						if(y!=8){Squares[x + 1][y + 1].setVisible(true); colorCoveredSquare[x + 1][y + 1] = Color.GRAY;}
						if(y!=0){Squares[x + 1][y - 1].setVisible(true); colorCoveredSquare[x + 1][y - 1] = Color.GRAY;}
						Squares[x + 1][y].setVisible(true); colorCoveredSquare[x + 1][y] = Color.GRAY;
					}
					if(y!=8){Squares[x][y + 1].setVisible(true); colorCoveredSquare[x][y + 1] = Color.GRAY;}
					if(y!=0){Squares[x][y - 1].setVisible(true); colorCoveredSquare[x][y - 1] = Color.GRAY;}
				}
			}
		}	
		
		
		if (revealAllNumbers)
		{
			revealAllNumbers(g);
		}
		
		for (int x = 0; x < TOTAL_COLUMNS; x++)
		{
			for (int y = 0; y < TOTAL_ROWS - 1; y++)
			{
				if(Squares[x][y].isVisible() && !Squares[x][y].isMine() && Squares[x][y].getNearbyMines() > 0)
				{
					g.setColor(Squares[x][y].getNumberColor());
					g.drawString(Integer.toString(Squares[x][y].getNearbyMines()), GRID_X + x*(INNER_CELL_SIZE+1) + 10, GRID_Y + y*(INNER_CELL_SIZE+1) + 20);
				}
			}
		}
		
		revealedSquares = 0;
		for (int x = 0; x < TOTAL_COLUMNS; x++)
		{
			for (int y = 0; y < TOTAL_ROWS - 1; y++)
			{
				if(Squares[x][y].isVisible() && !Squares[x][y].isMine())
				{
					revealedSquares += 1;
				}
			}
		}
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
	
	public void generateNumbers()
	{
		for (int x = 0; x < TOTAL_COLUMNS; x++)
		{
			for (int y = 0; y < TOTAL_ROWS; y++)
			{
				if(Squares[x][y].isMine())
				{
					if (x!=0)
					{
						if(y!=8){Squares[x - 1][y + 1].bumpMines();}
						if(y!=0){Squares[x - 1][y - 1].bumpMines();}
						Squares[x - 1][y].bumpMines();
					}
					if(x!=8)
					{
						if(y!=8){Squares[x + 1][y + 1].bumpMines();}
						if(y!=0){Squares[x + 1][y - 1].bumpMines();}
						Squares[x + 1][y].bumpMines();
					}
					if(y!=8){Squares[x][y + 1].bumpMines();}
					if(y!=0){Squares[x][y - 1].bumpMines();}
				}
			}
		}
	}
	
	public void generateMines()
	{
		Random randX = new Random();
		Random randY = new Random();
		int generatedMines = 0;
		while (generatedMines < mineCount)
		{
			int x = randX.nextInt(9);
			int y = randY.nextInt(9);
			if (!Squares[x][y].isMine())
			{
				Squares[x][y].setMine(true);
				generatedMines++;
			}
		}
	}
	
	public void revealAllMines()
	{
		for (int x = 0; x < TOTAL_COLUMNS; x++)
		{
			for (int y = 0; y < TOTAL_ROWS; y++) 
			{
				if(Squares[x][y].isMine())
				{
					Squares[x][y].setVisible(true);
					colorCoveredSquare[x][y] = Color.BLACK;
				}
			}
		}
	}
	
	public void revealAllNumbers(Graphics g)
	{
		for (int x = 0; x < TOTAL_COLUMNS; x++)
		{
			for (int y = 0; y < TOTAL_ROWS; y++) 
			{
				if(!Squares[x][y].isMine() && Squares[x][y].getNearbyMines() > 0)
				{
					g.setColor(Squares[x][y].getNumberColor());
					g.drawString(Integer.toString(Squares[x][y].getNearbyMines()), GRID_X + x*(INNER_CELL_SIZE+1) + 10, GRID_Y + y*(INNER_CELL_SIZE+1) + 20);
				}
			}
		}
	}
	
	public void revealNumbers(Graphics g, int xPos, int yPos)
	{
		g.setColor(Squares[xPos][yPos].getNumberColor());
		g.drawString(Integer.toString(Squares[xPos][yPos].getNearbyMines()), GRID_X + x*(INNER_CELL_SIZE+1) + 10, GRID_Y + y*(INNER_CELL_SIZE+1) + 20);
		return;
	}
	
	public void lose(Graphics g)
	{
			g.setColor(Color.RED);
			g.drawString("GAME OVER!", GRID_X + INNER_CELL_SIZE+1 + ((INNER_CELL_SIZE+1)*TOTAL_COLUMNS-100)/2, GRID_Y + ((INNER_CELL_SIZE+1)*TOTAL_COLUMNS) + INNER_CELL_SIZE);
	}
	
	public void win(Graphics g)
	{
			g.setColor(Color.GREEN);
			g.drawString("YOU WIN!", GRID_X + INNER_CELL_SIZE+1 + ((INNER_CELL_SIZE+1)*TOTAL_COLUMNS-100)/2, GRID_Y + ((INNER_CELL_SIZE+1)*TOTAL_COLUMNS) + INNER_CELL_SIZE);
			repaint();
	}
}	