package model.serialization;

import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import fpt.com.Product;

public class BinaryStrategy implements fpt.com.SerializableStrategy {
	
	private FileInputStream fis;
	private FileOutputStream fos;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;

	@Override
	public Product readObject() throws IOException {
		if (ois == null) {
			fis = new FileInputStream("products.ser");
			ois = new ObjectInputStream(fis);
		}
		try {
			return (Product) ois.readObject();
		} catch (ClassNotFoundException e) {
			System.out.println("Datei enthält keine Objekte der gesuchten Klasse.");
		}
		return null;
	}

	@Override
	public void writeObject(Product obj) throws IOException {
		if (fos == null) {
			fos = new FileOutputStream("products.ser");
			oos = new ObjectOutputStream(fos);
		}
		oos.writeObject(obj); // write Object
	}

	@Override
	public void close() throws IOException {
		if (fis != null)
			fis.close();
		if (ois != null)
			ois.close();
		if (fos != null)
			fos.close();
		if (oos != null)
			oos.close();
	}
}
