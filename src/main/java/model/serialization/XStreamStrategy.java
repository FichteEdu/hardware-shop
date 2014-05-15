package model.serialization;

import java.io.EOFException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Locale;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;

import fpt.com.Product;


public class XStreamStrategy implements fpt.com.SerializableStrategy {

	private static final XStream	xstream	= new XStream(new DomDriver());
	private FileReader				fr;
	private ObjectInputStream		ois;
	private ObjectOutputStream		oos;
	private FileWriter				fw;

	// Set defaults for xstream
	static {
		xstream.useAttributeFor(model.Product.class, "id");
		xstream.aliasField("preis", model.Product.class, "price");
		xstream.aliasField("anzahl", model.Product.class, "quantity");
		xstream.registerLocalConverter(model.Product.class, "id", new IDConverter());
		xstream.registerLocalConverter(model.Product.class, "price", new PriceConverter());
		xstream.alias("ware", model.Product.class);
	}

	@Override
	public Product readObject() throws IOException {
		if (ois == null) {
			fr = new FileReader("productsx.xml");
			ois = xstream.createObjectInputStream(fr);
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
			fw = new FileWriter("productsx.xml");
			oos = xstream.createObjectOutputStream(fw, "waren");
		}
		oos.writeObject(obj);
	}

	@Override
	public void close() throws IOException {
		if (ois != null)
			ois.close();
		if (oos != null)
			oos.close();
		if (fr != null)
			fr.close();
		if (fw != null)
			fw.close();
	}

}


class IDConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(String.class);
	}

	@Override
	public Object fromString(String arg0) {
		return Long.parseLong(arg0);
	}

	@Override
	public String toString(Object arg0) {
		return String.format("%06d", arg0);
	}

}


class PriceConverter implements SingleValueConverter {

	@SuppressWarnings("rawtypes")
	@Override
	public boolean canConvert(Class type) {
		return type.equals(double.class) || type.equals(Double.class);
	}

	@Override
	public Object fromString(String arg0) {
		return Double.parseDouble(arg0);
	}

	@Override
	public String toString(Object arg0) {
		return String.format(Locale.ENGLISH, "%.2f", arg0);
	}

}
