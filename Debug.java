
public class Debug {

	static boolean numM = false;
	static boolean showM = false;
	static boolean possM = false;
	static boolean seen = false;
	static boolean printB = false;
	static boolean time = false;
	static boolean mem = false;
	static boolean empty = false;
	static boolean hash = false;
	static boolean man = false;
	static boolean printBd = false;
	static boolean printSGF = false;
	static boolean numBoard = false;
	static boolean all = false;

	public static void print(){
		System.out.println("Listed below are all of the possible debug options that can accompany '-o'. Also a call to Board's isOk method will automatically be made after every move and an IllegalStateException will be thrown if isOk fails.");
		System.out.println("numM = shows number of moves taken (number does not include moves made in boards that were never taken out of the fringe)");
		System.out.println("showM = prints the new board after every move made before it is pushed onto the fringe");
		System.out.println("possM = prints out the number of possible moves each board can make");
		System.out.println("seen = lets you know when a previously seen board configuration was not added to the fringe");
		System.out.println("printB = prints the location of the blocks of each board as it is being taken out of the fringe");
		System.out.println("time = tells you how much time has passed after each move made");
		System.out.println("mem = shows how much memory is free still after every move a board can make is pushed into the fringe");
		System.out.println("empty = prints the empty spaces in each board in (xy) format");
		System.out.println("hash = prints the hashcode of each new board made");
		System.out.println("man = prints the manhattan distance for each board popped out of the fringe");
		System.out.println("printBd = prints boards as they are being taken out of the fringe");
		System.out.println("printSGF = prints the starting board with the goal board, and then prints the final ending board (should be the same as the goal board)");
		System.out.println("numBoard = prints number of boards added to the fringe total before solution found");
		System.out.println("all = shows all of the options specified above");
	}

	public static void debugSet(String s){
		if (s.equals("numM"))  
			numM = true;
		if (s.equals("showM"))   
			showM = true;
		if (s.equals("possM"))   
			possM = true;
		if (s.equals("seen")) 
			seen = true;
		if (s.equals("printB"))   
			printB = true;
		if (s.equals("time"))   
			time = true;
		if (s.equals("mem"))  
			mem = true;
		if (s.equals("empty")) 
			empty = true;
		if (s.equals("hash")) 
			hash = true;
		if (s.equals("man")) 
			man = true;
		if (s.equals("printBd")) 
			printBd = true;
		if (s.equals("printSGF"))  
			printSGF = true;
		if (s.equals("numBoard "))  
			numBoard = true;
		if (s.equals("all"))  
			all = true;
	}

}
