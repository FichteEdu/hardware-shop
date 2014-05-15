package shop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import model.ProductList;
import model.serialization.BinaryStrategy;
import model.serialization.XMLStrategy;
import model.serialization.XStreamStrategy;
import fpt.com.Product;
import fpt.com.SerializableStrategy;


public class ControllerShop implements ActionListener {

	private ModelShop	model;
	private ViewShop	view;
	private byte		strat;	// 0=bin, 1=beans, 2=xstream

	public ControllerShop() {
	}

	public ControllerShop(	ModelShop m, ViewShop v) {
		link(m, v);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() instanceof JButton) {
			JButton btn = (JButton) e.getSource();

			switch (btn.getText()) {
				case "Add":
					Product p = view.getNewProduct();
					if (p != null)
						model.add(p);
					break;
				case "Delete (selected)":
					model.delete(view.getSelected());
					break;
				default:
					System.out.println("Unknown Action for button: " + btn.getText());
			}
		} else if (e.getSource() instanceof JMenuItem) {
			JMenuItem itm = (JMenuItem) e.getSource();
			switch (itm.getText()) {
				case "Bin":
					strat = (byte) 0;
					break;
				case "beans":
					strat = (byte) 1;
					break;
				case "xstream":
					strat = (byte) 2;
					break;
				case "save":
					save();
					break;
				case "load":
					load();
					break;
				default:
					System.out.println("Unknown Action for button: " + itm.getText());
					break;
			}
		} else {
			System.out.println("Error: Input of unknown source.");
		}

	}

	private SerializableStrategy getStrat() {
		SerializableStrategy binaryStrat;
		switch (strat) { // 0=bin, 1=beans, 2=xstream
			case 0:
				binaryStrat = new BinaryStrategy();
				break;
			case 1:
				binaryStrat = new XMLStrategy();
				break;
			default:
				binaryStrat = new XStreamStrategy();
				break;
		}

		return binaryStrat;
	}

	private void load() {
		model.setProductList(new ProductList());

		SerializableStrategy binaryStrat = getStrat();

		try {
			Product p;
			while (true) {
				p = binaryStrat.readObject();
				if (p == null)
					break;
				else
					model.add(p);
			}
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error while loading from file.");
		} finally {
			try {
				binaryStrat.close();
			} catch (IOException e) {
			}
		}
	}

	private void save() {
		SerializableStrategy binaryStrat = getStrat();

		try {
			for (Product p : model)
				binaryStrat.writeObject(p);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error while saving to file.");
		} finally {
			try {
				binaryStrat.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * Link a model and a view component.
	 * 
	 * @param m
	 * @param v
	 */
	public void link(ModelShop m, ViewShop v) {
		model = m;
		view = v;

		model.addObserver(v);
		view.setModel(m);
		view.addActionListener(this);
	}

}
