public class Point {
	//our overridden point class with new hashCode
	public int x;
	public int y;

	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}


	public boolean equals(Object other) {
		if (this == other)
			return true;

		Point p2 = (Point) other;
		return p2.x == x && p2.y == y;
	}
	
	public String toString(){
		return " (" + x + y + ") ";
	}

	//override
	public int hashCode(){
		return (Integer.toString(x) + "," + Integer.toString(y)).hashCode();
	}

}