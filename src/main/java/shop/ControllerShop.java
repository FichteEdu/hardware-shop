package shop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import model.IDGenerator;
import model.serialization.db.DatabaseStrategy;
import fpt.com.Product;
import fpt.com.SerializableStrategy;


public class ControllerShop implements ActionListener {

	private static final int			LAST_N_PRODUCTS	= 20;
	private static final IDGenerator	idgen			= new IDGenerator();

	private ModelShop					m;
	private ViewShop					v;

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
					if (p == null)
						break;
					// Generate id if serialize selected
					if (v.getStratType() == ViewShop.StratType.SERIALIZE) {
						try {
							p.setId(idgen.generate());
						} catch (Exception ex) {
							ex.printStackTrace(); // Not expected to happen
						}
					}
					// Finally add product
					m.add(p);
					break;

				case "Delete (selected)":
					boolean database = v.getStratType() == ViewShop.StratType.DATABASE;
					boolean prompted = false; // Only prompt once
					for (Product pr : v.getSelected()) {
						if (database && pr.getId() != -1) {
							if (!prompted) {
								JOptionPane.showMessageDialog(null,
										"Unable to delete remote products.");
								prompted = true;
							}
						} else {
							m.delete(pr);
						}
					}
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
					// For simplicity, empty the current product list when
					// switching strategy and try to load previous data
					load();
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
		m.reset();

		switch (v.getStratType()) {
			case SERIALIZE:
				SerializableStrategy serStrat = v.getSelectedSerStrat();
				long maxID = idgen.getNextID() - 1;
				try {
					Product p;
					while (true) {
						p = serStrat.readObject();
						if (p == null)
							break;
						m.add(p);
						// Get the highest ID used in the entire list, but only
						// if it's higher than the current next ID
						maxID = Math.max(maxID, p.getId());
					}
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error while reading file.");
				} finally {
					try {
						serStrat.close();
					} catch (IOException e) {
					}

					// And use it to tell the generator which ID to generate
					// next
					idgen.setNextID(maxID + 1);
				}
				break;

			case DATABASE:
				DatabaseStrategy dataStrat = v.getSelectedDataStrat();
				try {
					for (Product p : dataStrat.read(LAST_N_PRODUCTS))
						m.add(p);
				} catch (SQLException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error while reading from database.");
				}
				break;
		}
	}

	private void save() {
		switch (v.getStratType()) {
			case SERIALIZE:
				SerializableStrategy serStrat = v.getSelectedSerStrat();
				try {
					for (Product p : m)
						serStrat.writeObject(p);
				} catch (IOException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error while saving to file.");
				} finally {
					try {
						serStrat.close();
					} catch (IOException e) {
					}
				}

			case DATABASE:
				DatabaseStrategy dataStrat = v.getSelectedDataStrat();
				try {
					dataStrat.write(v.toArray());
				} catch (SQLException e) {
					e.printStackTrace();
					JOptionPane.showMessageDialog(null, "Error while reading from database.");
				}

				// Call the model's changed method (which we made public)
				// because we changed the products' ids within the containing
				// ProductList that m provides (and we render that).
				// This is the easiest way to do this.
				m.changed();
				break;
		}
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
