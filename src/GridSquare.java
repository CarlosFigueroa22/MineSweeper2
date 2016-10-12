
public class GridSquare
{
	private int xPos;
	private int yPos;
	private int nearbyMines = 0;
	private boolean Mine;
	private boolean Visible = false;
	
	GridSquare(int x, int y)
	{
		this.xPos = x;
		this.yPos = y;	
	}
	
	public int getXPos()
	{
		return this.xPos;
	}
	
	public int getYPos()
	{
		return this.yPos;
	}
	
	public int getNearbyMines()
	{
		return this.nearbyMines;
	}
	
	public void setXPos(int x)
	{
		this.xPos = x;
	}
	
	public void setYPos(int y)
	{
		this.yPos = y;
	}
	
	public boolean isMine()
	{
		return this.Mine;
	}
	
	public boolean isVisible()
	{
		return this.Visible;
	}
	
	public void setMine(boolean value)
	{
		this.Mine = value;
	}
	
	public void setVisible(boolean value)

	{
		this.Visible = value;
	}	
	
	public void bumpMines()
	{
			this.nearbyMines += 1;
	}
}
