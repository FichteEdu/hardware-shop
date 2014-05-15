package model;

public class IDGenerator {

	private long				nextID	= 0;
	private static final long	MAX_ID	= 999999;

	public long generate() throws Exception {
		if (nextID == MAX_ID)
			throw new Exception("Reached maximum ID, no further generations possible.");
		return nextID++;
	}

	public void resetID() {
		this.nextID = 0;
	}

	public long getNextID() {
		return nextID;
	}

	public void setNextID(long id) {
		nextID = id;
	}

}
