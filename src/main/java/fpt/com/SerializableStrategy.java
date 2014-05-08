package fpt.com;

import java.io.IOException;

public interface SerializableStrategy   {

	/**
	 * read product form io-stream. ensure stream is open.
	 * 
	 * @return the serialized object from the io-stream
	 * @throws IOException
	 *             if stream is closed
	 * @throws ClassNotFoundException
	 *             if the class of the object is not present
	 */
	Product readObject() throws IOException;

	/**
	 * write product to the io-stream
	 * 
	 * @param obj
	 *            the object for serialization
	 * @throws IOException
	 *             if stream is closed or we can't write
	 */
	void writeObject(Product obj) throws IOException;

	/**
	 * close the io-stream
	 * 
	 * @throws IOException
	 *             if stream can't be closed.
	 */
	void close() throws IOException;
}
