import java.io.*;



public class Readinput {

	/**
	 * @param args
	 */
	//private BufferedReader in;
	private Board b;
	public Readinput(String inputfile, int row, int col){
		int numrows = 0;
		int numcols = 0;
		BufferedReader in = null;
		try{
			in = new BufferedReader(new InputStreamReader (new FileInputStream (inputfile)));
		}catch(Exception e){
			System.err.println ("Couldn't access file!"); //if there is an error getting the file
			System.exit (1);
		}
		if (row == 0 && col == 0){
			try{
				String inputdimension = in.readLine();
				String[] dimension = inputdimension.split(" ");
				if (dimension.length != 2){
					System.err.println("There must be two number for the dimension of the board"); //catches error in tray file
					System.exit (1);
				}
				numrows = Integer.parseInt(dimension[0]);
				numcols = Integer.parseInt(dimension[1]);

			}catch (IOException e){
				System.err.println ("Empty file");
				System.exit (1);
			}catch (NumberFormatException e1){
				System.err.println("The dimension of the board is not a number");
				System.exit(1);
			}
		}else{
			numrows = row;
			numcols = col;
		}
		b = new Board(numrows, numcols);
		while (true){ //all of this code makes the original starting board given to us in the init file
			try{
				String nextline = in.readLine();
				if (nextline == null){
					break;
				}else{
					String[] point = nextline.split(" ");
					if (point.length != 4){
						System.err.println("Wrong number of input in " + nextline);
					}
					Point p1 = new Point(Integer.parseInt(point[1]), Integer.parseInt(point[0]));
					Point p2 = new Point(Integer.parseInt(point[3]), Integer.parseInt(point[2]));
					Block a = new Block(p1, p2);
					b.addBlock(a);

				}
			}catch(IOException e){
				System.err.println(e);
				System.exit(1);
			}catch(NumberFormatException e1){
				System.err.println("The row or column of the block is not a number");
				System.exit(1);
			}
		}
	}
	public Board board(){
		return b;

	}

}
