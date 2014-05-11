package model;

public class IDGenerator {

	private long				lastID	= 0;
	private static final long	MAX_ID	= 999999;

	public long generate() throws Exception {
		if (lastID == MAX_ID)
			throw new Exception("Reached maximum ID, no further generations possible.");
		return lastID++;
	}

}
