package model.serialization;

import java.io.EOFException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
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
			xstream.aliasField("preis", model.Product.class, "price");
			xstream.aliasField("anzahl", model.Product.class, "quantity");
			xstream.registerLocalConverter(model.Product.class, "id", new IDConverter());
			xstream.registerLocalConverter(model.Product.class, "price", new PriceConverter());
			xstream.alias("ware", model.Product.class);
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

class IDConverter implements Converter {

	@Override
	public boolean canConvert(Class arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void marshal(Object arg0, HierarchicalStreamWriter arg1, MarshallingContext arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader arg0, UnmarshallingContext arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

class PriceConverter implements SingleValueConverter {

	@Override
	public boolean canConvert(Class type) {
		// TODO Auto-generated method stub
		return type.equals(double.class) || type.equals(Double.class);
	}

	@Override
	public Object fromString(String arg0) {
		return Double.parseDouble(arg0);
	}

	@Override
	public String toString(Object arg0) {
		// TODO Auto-generated method stub
		return String.valueOf(arg0);
	}
	
}