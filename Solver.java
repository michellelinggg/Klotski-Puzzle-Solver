import java.io.*;
import java.util.*;

public class Solver {

	/**
	 * @param args
	 */
	private Board myRoot; //original starting board
	private Board myGoal; //what we want to end up at
	private Board currentBoard; //the board we have just taken out of the fringe
	private PriorityQueue<Node> fringe;
	private HashSet<Board> boardSeen; //hashset of all the boards configurations we have already seen so we don't readd them to the fringe
	private Node temp;
	private static boolean debug; //all of the instance variables here and below are only instantiated and used for debugging purposes
	private int moveCount; 
	private int boardMove;
	private int numBoard;
	private long startTime;
	private long stopTime;

	public Solver(Board root, Board goal){
		myRoot = root;
		myGoal = goal;
		currentBoard = root;
		boardSeen = new HashSet<Board>();
		boardSeen.add(root);
		fringe = new PriorityQueue<Node>(1000, new NodeComparator()); 
		fringe.add(new Node(root, 0)); //adding the root with arbituary priority because it will be the first thing taken out regardless of the fringe
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			if (args.length == 1) {
				if(args[0].startsWith("-o")){
					debug = true;
					String debugger = args[0].substring(2);
					if (debugger.equals("options")){ //checks to see if it's -ooptions
						Debug.print();
						System.exit(0);
					}
				}
				else{
					System.err.println("Can not have only one argument unless it is -ooptions");
				}
			}
			if (args.length < 1) {
				System.err.println ("Wrong number arguments, got " + args.length + " number of arguments.");
				System.exit (1);
			}
			String inputfile;
			String outputfile;
			if (args.length > 2){
				int i;
				for (i = 0; i < args.length - 2; i++){
					if (args[i].startsWith("-o")){ //checks for all the debugging options selected by user and turns them on
						String debugger = args[i].substring(2);
						Debug.debugSet(debugger);
					}else
						throw new IllegalStateException("invalid input before intial and goal files"); //catches error in format
				}
				debug = true;
				inputfile = args[i];
				outputfile = args[i+1];
			}
			else{
				debug = false; //assumes debug is off and program runs as normal
				inputfile = args[0];
				outputfile = args[1];
			}
			Readinput in = new Readinput(inputfile, 0, 0);
			Readinput goal = new Readinput(outputfile, in.board().getHeight(), in.board().getWidth());
			Solver s = new Solver(in.board(), goal.board());
			s.solve();
		}catch (OutOfMemoryError e){
			System.out.println("Ran out of memory");
		}
	}
	private class Node{ //node we made for our comparator
		private int manhatdis;
		private Board myBoard;
		public Node(Board b, int mdis){
			myBoard = b;
			manhatdis = mdis;
		}
	}
	private class NodeComparator implements Comparator<Node>{
		@Override //compares the manhattan distance between two objects for our priority queue
		public int compare(Node obj1, Node obj2){

			if (obj1.manhatdis > obj2.manhatdis){
				return 1;
			}else if (obj1.manhatdis < obj2.manhatdis){
				return -1;
			}else{
				return 0;
			}
		}

	}
	private int calculateManhattan(Board board){
		int sumdistance = 0; //calculates distance between blocks in current board and goal board
		for (Block b: myGoal.getBlocks()){
			for (Block i: board.getBlocks()){
				if (b.getType().equals(i.getType())){
					sumdistance += Math.abs((i.UpperLeft().x - b.UpperLeft().x));
					sumdistance += Math.abs((i.UpperLeft().y - b.UpperLeft().y));
				}
			}
		}

		return sumdistance;
	}
	public void solve(){
		if(debug && (Debug.time || Debug.all)) //lots of debugging
			startTime = System.currentTimeMillis();
		if (debug && (Debug.numBoard || Debug.all))
			numBoard = 1;
		if (debug && (Debug.printSGF || Debug.all)){
			System.out.println("Starting board:" + "\n" + myRoot);
			System.out.println("Goal board:" + "\n" + myGoal);
		}
		if (debug && (Debug.numM || Debug.all))
			moveCount = 0;
		while (!this.isSolved()){
			if (debug && (Debug.possM || Debug.all))
				boardMove = 0;
			this.findPossibleMoves(); //see below code
			if(fringe.isEmpty()){ //all boards have been seen and no solution found
				//System.out.println("No solution found");
				System.exit(1);
			}
			temp = fringe.remove(); //takes out board from fringe
			currentBoard = temp.myBoard;
			if (debug && (Debug.time || Debug.all)){
				stopTime = System.currentTimeMillis();
				System.out.println("time elapsed: " + (stopTime - startTime));
			}
			if (debug && (Debug.man || Debug.all))
				System.out.println("current board's manhattan distance (priority) in queue: " + temp.manhatdis);
			if (debug && (Debug.printBd || Debug.all))
				System.out.println("current board:" + "\n" + currentBoard);
			if (debug && (Debug.printB || Debug.all))
				System.out.println("Blocks in current board: " + currentBoard.getBlocks());
			if (debug && (Debug.numM || Debug.all))
				moveCount++;
		}
		if (debug && (Debug.printSGF || Debug.all)) //assuming that the solution has been found here
			System.out.println("final board:" + "\n" + currentBoard);
		if (debug && (Debug.numM || Debug.all))
			System.out.println("move count: " + moveCount);
		if (debug && (Debug.numBoard || Debug.all))
			System.out.println("number of boards added to fringe:" + numBoard);
		if (debug && (Debug.time || Debug.all)){
			stopTime = System.currentTimeMillis();
			System.out.println("final time elapsed: " + (stopTime - startTime));
		}
		System.out.print(currentBoard.displayMoves()); //print out all moves taken to get there

	}
	public boolean isSolved(){

		for (Block i: myGoal.getBlocks()){ //checks to see if all blocks in goal board are in same position in current board
			if (!currentBoard.getBlocks().contains(i)) 
				return false; 
		} 
		return true; 
	}
	private void findPossibleMoves(){
		HashMap<Point, Block> currTray = currentBoard.getTray(); //our board implementation
		for (int i=0; i < currentBoard.getWidth(); i++){
			for(int j=0; j < currentBoard.getHeight(); j++){
				if (!currTray.containsKey(new Point(i,j))){ //looks for empty spaces until it finds one
					if (debug && (Debug.empty || Debug.all))
						System.out.println("empty space: " + new Point(i, j));
					if (i-1 >= 0){ //tries moving blocks from the left right down and up of that empty space
						this.tryMove(new Point(i-1, j), "right");}
					if(i+1 < currentBoard.getWidth()){
						this.tryMove(new Point(i+1, j), "left");}
					if(j-1 >= 0){
						this.tryMove(new Point(i, j-1), "down");}
					if(j+1 < currentBoard.getHeight()){
						this.tryMove(new Point(i, j+1), "up");}
				}
			}
		}
		if (debug && (Debug.possM || Debug.all))
			System.out.println("number of possible moves this board can make: " + boardMove);
		if (debug && (Debug.mem || Debug.all))
			System.out.println("Free memory: " + Runtime.getRuntime().freeMemory());

	}
	private void tryMove(Point p, String direction){
		HashMap<Point, Block> currTray = currentBoard.getTray();
		if (currTray.containsKey(p)){ //picks a point to moveTo depending on direction specified
			Point upperl = currTray.get(p).UpperLeft();
			Point moveto = new Point(-1,-1);
			if (direction.equals("right")){
				moveto = new Point(upperl.x+1, upperl.y);
			}else if (direction.equals("left")){
				moveto = new Point(upperl.x-1, upperl.y);
			}else if (direction.equals("up")){
				moveto = new Point(upperl.x, upperl.y-1);
			}else if (direction.equals("down")){
				moveto = new Point(upperl.x, upperl.y+1);
			}

			try{
				Board possibleBoard = currentBoard.makeMove(currTray.get(p), moveto); //makes move. if it's not seen adds to fringe and hashset board seen, if it has been seen do nothing
				if (debug)
					try{
						possibleBoard.isOK(); //debug includes a call to isOK after every move made
					}catch (IllegalStateException e){
						System.out.println(e.getMessage()); 
					}
				if (debug && (Debug.hash || Debug.all))
					System.out.println("new board's hashcode: " + currentBoard.hashCode());
				if (!boardSeen.contains(possibleBoard)){
					if (debug && (Debug.numBoard || Debug.all))
						numBoard++;
					if (debug && (Debug.showM || Debug.all))
						System.out.println("new block positions in board: " + "\n" + possibleBoard);
					Node n = new Node(possibleBoard, this.calculateManhattan(possibleBoard));
					fringe.add(n);
					boardSeen.add(possibleBoard);
					if (debug && (Debug.possM || Debug.all))
						boardMove++;
				}else{
					if (debug && (Debug.seen || Debug.all))
						System.out.println("A previously seen board type was reached and therefore not added to the fringe");
				}
			}catch(IllegalStateException e){
				return;
			}

		}


	}


}
