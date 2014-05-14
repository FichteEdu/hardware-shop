package model.serialization;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import fpt.com.Product;

public class XMLStrategy implements fpt.com.SerializableStrategy {

	@Override
	public Product readObject() throws IOException {
		try (FileInputStream fi = new FileInputStream("products.xml"); XMLDecoder decoder = new XMLDecoder(fi)) {
			return (Product) decoder.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void writeObject(Product obj) throws IOException {
		try (FileOutputStream fo = new FileOutputStream("products.xml"); XMLEncoder encoder = new XMLEncoder(fo)) {
			encoder.writeObject(obj);
			encoder.flush();
		} catch ( IOException e) {
			e.printStackTrace() ;
		}
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		
	}

}
