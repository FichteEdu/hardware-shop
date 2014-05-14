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
			try {
				fis = new FileInputStream("products.ser");
				ois = new ObjectInputStream(fis);
			} catch (IOException e) {
					e.printStackTrace();
			}
		}
		try {
			return (Product) ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void writeObject(Product obj) throws IOException {
		if (fis == null) {
			try {
				fos = new FileOutputStream("products.ser");
				oos = new ObjectOutputStream(fos);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try (FileOutputStream fos = new FileOutputStream("products.ser"); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
			oos.writeObject(obj); // write Object
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void close() throws IOException {
		if (fis != null)
			fis.close();
		if (fos != null)
			fos.close();		
	}
}
