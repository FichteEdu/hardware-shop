package model.serialization;

import java.io.EOFException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;

import fpt.com.Product;

public class XStreamStrategy implements fpt.com.SerializableStrategy {
	
	static final XStream xstream = new XStream(new DomDriver());
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

	@Override
	public Product readObject() throws IOException {
		if (ois == null) {
			try {
				ois = xstream.createObjectInputStream(new FileReader("productsx.xml"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			return (Product) ois.readObject();
		} catch (ClassNotFoundException e) {
			return null;
		} catch (EOFException e) {
			return null;
		}
	}

	@Override
	public void writeObject(Product obj) throws IOException {
		if (oos == null) {
			xstream.useAttributeFor(model.Product.class, "id");
			xstream.alias("ware", model.Product.class);
			xstream.aliasField("preis", model.Product.class, "price");
			xstream.aliasField("anzahl", model.Product.class, "quantity");
			try {
				oos = xstream.createObjectOutputStream(new FileWriter("productsx.xml"), "waren");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		oos.writeObject(obj);
	}

	@Override
	public void close() throws IOException {
		if (ois != null)
			ois.close();
		if (oos != null)
			oos.close();
	}

}
