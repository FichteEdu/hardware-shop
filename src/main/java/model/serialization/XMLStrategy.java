package model.serialization;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import fpt.com.Product;

public class XMLStrategy implements fpt.com.SerializableStrategy {
	private XMLDecoder decoder;
	private XMLEncoder encoder;

	@Override
	public Product readObject() throws IOException {
		if (this.decoder == null) {
			try (FileInputStream fi = new FileInputStream("products.xml"); XMLDecoder d = new XMLDecoder(fi)) {
				this.decoder = d;
				return (Product) decoder.readObject();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				return (Product) decoder.readObject();
			} catch (ArrayIndexOutOfBoundsException e) {
				return null;
			}
		}
		return null;
	}

	@Override
	public void writeObject(Product obj) throws IOException {
		if (this.encoder == null) {
			try (FileOutputStream fo = new FileOutputStream("products.xml"); XMLEncoder e = new XMLEncoder(fo)) {
				this.encoder = e;
				encoder.writeObject(obj);
				encoder.flush();
			} catch ( IOException e) {
				e.printStackTrace() ;
			}
		} else {
			this.encoder.writeObject(obj);
			this.encoder.flush();
		}
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		this.decoder.close();
		this.encoder.close();
	}

}
