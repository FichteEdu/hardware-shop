package shop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JMenuItem;

import model.ProductList;
import model.serialization.db.JDBCConnector;
import fpt.com.Product;


public class ControllerShop implements ActionListener {

	private ModelShop	m;
	private ViewShop	v;

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
					Product p = v.getNewProduct();
					if (p != null)
						m.add(p);
					break;
				case "Delete (selected)":
					for (Product pr : v.getSelected())
						m.delete(pr);
					break;
				default:
					System.out.println("Unknown Action for button: " + btn.getText());
			}
		} else if (e.getSource() instanceof JMenuItem) {
			JMenuItem itm = (JMenuItem) e.getSource();
			switch (itm.getText()) {
				case "Bin":
				case "beans":
				case "xstream":
				case "JDBC upload":
				case "OpenJPA Serialization 10":
					break;
				case "save":
					save();
					break;
				case "load":
					load();
					break;
				default:
					System.out.println("Unknown Action for menu item: " + itm.getText());
					break;
			}
		} else {
			System.out.println("Error: Input of unknown source.");
		}

	}

	private void load() {
		// Empty the current product list
		m.setProductList(new ProductList());

		// TODO move this to JBDCStrategy
		JDBCConnector dbconn = new JDBCConnector();
		try {
			// Only read the last 10 entries in the database
			for (Product p : dbconn.readSomeLast(10)) {
				m.add(p);
			}
		} catch (SQLException e) {
			System.out.println("Unable to connect to database");
			e.printStackTrace();
		} finally {
			dbconn.close();
		}
		
		/*
		SerializableStrategy binaryStrat = v.getSelectedStrat();
		long maxID = model.Product.getIdgen().getNextID() - 1;

		try {
			Product p;
			while (true) {
				p = binaryStrat.readObject();
				if (p == null)
					break;
				m.add(p);
				// Get the highest ID used in the entire list, but only if it's
				// higher than the current next ID
				maxID = Math.max(maxID, p.getId());
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

		// And use it to tell the generator which ID to generate next
		model.Product.getIdgen().setNextID(maxID + 1);		
		*/
	}

	private void save() {

		JDBCConnector dbconn = new JDBCConnector();

		try {
			dbconn.writeSome(m.getProductList().toArray(new Product[m.getProductList().size()]));
		} catch (SQLException e) {
			System.out.println("Fehler beim Verbinden mit Datenbank");
			e.printStackTrace();
		} finally {
			dbconn.close();
		}

		// Call the model's changed method (which we made public) because we
		// changed the products' ids within the containing ProductList that m
		// provides (and we render that). This is the easiest way to do this.
		m.changed();
		
		/*
		SerializableStrategy binaryStrat = v.getSelectedStrat();

		try {
			for (Product p : m)
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
		*/
	}

	/**
	 * Link a model and a view component.
	 * 
	 * @param m
	 * @param v
	 */
	public void link(ModelShop m, ViewShop v) {
		this.m = m;
		this.v = v;

		m.addObserver(v);
		v.setModel(m);
		v.addActionListener(this);
	}

}
