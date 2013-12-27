import java.util.ArrayList; 
import java.util.HashMap; 
import java.util.HashSet; 
import java.util.Iterator; 

public class Board {  

	private HashMap<Point, Block> myTray;   //our tray of points that have a block at that location
	private int boardWidth;  
	private int boardHeight;  
	private ArrayList<String> myMoves; //all the moves made to get the board to the configuration it is currently at from the starting board
	private HashSet<Block> Blocks; //all the blocks this board contains


	public Board(int height, int width, Board parent) {  //our constructor for making a board that isn't the initial configuration board

		if (height > 256 || width > 256)   
			throw new IllegalArgumentException("Exceeded maximum allowed configuration for tray size.");  
		if (height < 0 || width < 0)   
			throw new IllegalArgumentException("Size must be a nonnegative number.");  
		boardWidth = width;  
		boardHeight = height;  

		//takes in a parent board because it must have all of the blocks it's parent save for the one block that was moved to get it to this new configuration
		//also it must have all of the moves it's parent had in its arraylist plus one extra move
		myTray = new HashMap<Point, Block>(); 
		myMoves = new ArrayList<String>(); 
		Blocks = new HashSet<Block>(); 

		Blocks.addAll(parent.Blocks); 
		myMoves.addAll(parent.myMoves); 

		if (parent.myTray != null) { 
			myTray.putAll(parent.myTray);    
		} 
	}  

	public Board(int height, int width) { //initial board config constructor
		boardWidth = width;  
		boardHeight = height;  
		myTray = new HashMap<Point, Block>(); 
		myMoves = new ArrayList<String>(); 
		Blocks = new HashSet<Block>(); 
	}  

	private void clearBlock(Block block) {  //we made seperate methods for clearBlock and fillBlock in our tray because we use the code so often it seemed extraneous to keep typing it out

		for (int j = block.UpperLeft().x; j <= block.LowerRight().x; j++) {  
			for (int k = block.UpperLeft().y; k <= block.LowerRight().y; k++) {  
				myTray.remove(new Point(j,k)); 
				Blocks.remove(block); 
			}  
		}  
	}  

	private void fillBlock(Block block) {  

		for (int j = block.UpperLeft().x; j <= block.LowerRight().x; j++) {  
			for (int k = block.UpperLeft().y; k <= block.LowerRight().y; k++) {  
				myTray.put(new Point(j,k), block);  
				Blocks.add(block); 
			}  
		}  
	}  

	private boolean validMove(Block block, Point point) {  //checks to see if the block will fit in it's new location by checking to see if any of the points the block will occupy in its new position are already in the hashmap myTray

		if (point.x < 0 || point.y < 0) {  
			return false;  
		}  

		if (point.x> boardWidth || point.y > boardHeight) {  
			return false;  
		}  

		clearBlock(block);  

		for (int j = point.x; j < point.x + block.getWidth(); j++) {  
			for (int k = point.y; k < point.y + block.getHeight(); k++) {  
				if (myTray.containsKey(new Point(j,k))) {  
					fillBlock(block);  
					return false;  
				}  
			}  
		}  
		return true;  
	}  

	public Board makeMove(Block block, Point point) throws IllegalStateException {  

		Block tempBlock = new Block(block.UpperLeft(), block.LowerRight()); //copy of the block being moved  

		if (!validMove(block, point)) {  
			throw new IllegalStateException("not a valid move!");  
		}  
		else{  
			fillBlock(block); //refill the block in because we are creating a new Board with the updated block position  
			tempBlock.changeOrientation(point, new Point(point.x + block.getWidth()-1, point.y + block.getHeight()-1));  
		}  

		Board board = new Board(boardHeight, boardWidth, this);  
		board.clearBlock(block); //clear the original position of the block in the new board  
		board.fillBlock(tempBlock); //fill the new board with the updated block position  

		String moveMade = Integer.toString(block.UpperLeft().y) + " " +  Integer.toString(block.UpperLeft().x) + " " + Integer.toString(point.y) + " " + Integer.toString(point.x);  
		board.myMoves.add(moveMade); 
		return board;  
	}  

	public void addBlock(Block block) throws IllegalStateException {  

		if (myTray.containsValue(block))  
			throw new IllegalStateException("block already in board!");  

		if (!validMove(block, block.UpperLeft())) {  
			throw new IllegalStateException("not a valid block position!");  
		}  

		fillBlock(block);  
	}  

	public boolean isOK() throws IllegalStateException {  
		ArrayList<Point> occupied = new ArrayList<Point>();
		if (Blocks != null) {
			for (Block current : Blocks) { 
				ArrayList<Point> tempPoints = new ArrayList<Point>();  

				//This checks to make sure the blocks are within the actual board dimensions.
				if (current.UpperLeft().x < 0 || current.UpperLeft().y < 0 || current.LowerRight().x > boardWidth || current.LowerRight().y > boardHeight)  
					throw new IllegalStateException("isOK Error: Blocks go out of dimensions given.");  

				//This checks to make sure the upper left point values are smaller or equal to the lower right point values.
				if (current.UpperLeft().x > current.LowerRight().x || current.UpperLeft().y > current.LowerRight().y) 
					throw new IllegalStateException("isOK Error: The blocks' point values are not correctly inputted as upper left and lower right coordinates."); 

				//This adds blocks into an array list by converting them into points, 
				//so the values of blocks that occupy more than two spaces can be checked for all of the blocks they occupy.
				for (int j=current.UpperLeft().x; j <= current.LowerRight().x; j++) {
					for (int k=current.UpperLeft().y; k <= current.LowerRight().y; k++) {
						tempPoints.add(new Point(j, k));
					}
				}

				//This checks for overlapping blocks.
				for (Point checkPoint : tempPoints) {
					if (occupied.contains(checkPoint)) {
						throw new IllegalStateException("isOK Error: Overlapping block space at (" + checkPoint.x + "," + checkPoint.y + ")");
					} else {
						occupied.add(checkPoint);
					}            	
				} //end overlapping block check
			} //end for loop for hash set of blocks  
		} //end null check
		return true;        
	}  //end isOK


	public int hashCode(){ //new hashCode 

		int rtn = 0; 
		for (Block block: myTray.values()) { 
			rtn += Math.pow(block.UpperLeft().x, 2) + Math.pow(block.UpperLeft().y, 3) + Math.pow(block.LowerRight().x, 4) + Math.pow(block.LowerRight().y, 5); 
		} 
		return rtn; 
	}  

	public boolean equals(Object o) {  
		Board board = (Board) o;  
		if (board.boardHeight != boardHeight || board.boardWidth != boardWidth)  
			return false;  
		else if (!this.myTray.equals(board.myTray)) {  //same board if trays are equal
			return false;  
		}  
		else {  
			return true;  
		}  
	}  

	public HashMap<Point, Block> getTray() { 
		return myTray; 
	} 

	public String toString() {  //prints out the size of the block at each location for example a 1x1 block at 0,0 would just be the number 1 at the location 0,0
		String board = "";  

		for (int j = 0; j < boardHeight; j++) {  
			for (int k = 0; k < boardWidth; k++) {  
				if (myTray.containsKey(new Point(k,j))) {  
					Block block = myTray.get(new Point(k,j)); 
					int size = block.getHeight() * block.getWidth(); 
					board += Integer.toString(size);  
				}  
				else{  
					board += "O";  
				}  
			}  
			board += "\n";  
		}  
		return board;  
	}  

	public String displayMoves() { //prints all the moves made previously to get to this board in myMoves

		String allMoves = ""; 
		for (int i = 0; i < myMoves.size(); i++) { 
			allMoves += myMoves.get(i); 
			allMoves += "\n"; 
		} 
		return allMoves; 
	} 

	public int getHeight() { 
		return boardHeight; 
	} 

	public int getWidth() { 
		return boardWidth; 
	} 

	public HashSet<Block> getBlocks() { 
		return Blocks; 
	} 
}