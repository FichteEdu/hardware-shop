package model.serialization;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import fpt.com.Product;


public class XMLStrategy implements fpt.com.SerializableStrategy {

	private FileInputStream		fi;
	private FileOutputStream	fo;
	private XMLDecoder			decoder;
	private XMLEncoder			encoder;

	@Override
	public Product readObject() throws IOException {
		if (this.decoder == null) {
			fi = new FileInputStream("products.xml");
			decoder = new XMLDecoder(fi);
		}
		try {
			return (Product) decoder.readObject();
		} catch (ArrayIndexOutOfBoundsException e) {
			return null;
		}
	}

	@Override
	public void writeObject(Product obj) throws IOException {
		if (this.encoder == null) {
			fo = new FileOutputStream("products.xml");
			encoder = new XMLEncoder(fo);
		}
		encoder.writeObject(obj);
		encoder.flush();
	}

	@Override
	public void close() throws IOException {
		if (decoder != null)
			decoder.close();
		if (encoder != null)
			encoder.close();
		if (fi != null)
			fi.close();
		if (fo != null)
			fo.close();
	}

}
