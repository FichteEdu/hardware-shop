package model;

public class IDGenerator {

	private int	lastID	= 0;
	private static final int MAX_ID = 999999;

	public int generate() throws Exception {
		if (lastID == MAX_ID)
			throw new Exception("Reached maximum ID, no further generations possible.");
		return lastID++;
	}

}
