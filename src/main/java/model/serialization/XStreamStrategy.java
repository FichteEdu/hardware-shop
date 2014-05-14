package model.serialization;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.io.xml.DomDriver;

import fpt.com.Product;

public class XStreamStrategy implements fpt.com.SerializableStrategy {
	
	static final XStream xstream = new XStream(new DomDriver());
	private FileWriter fw;
	private FileReader fr;

	@Override
	public Product readObject() throws IOException {
		if (fr == null) {
			try {
				fr = new FileReader("productsx.xml");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			return (Product) xstream.fromXML(fr);
		} catch (XStreamException e) {
			return null;
		}
	}

	@Override
	public void writeObject(Product obj) throws IOException {
		if (fw == null) {
			try {
				fw = new FileWriter("productsx.xml");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			xstream.toXML(obj, fw);
		} catch (XStreamException e) {
			
		}
	}

	@Override
	public void close() throws IOException {
		if (fr != null)
			fr.close();
		if (fw != null)
			fw.close();
	}

}
