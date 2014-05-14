package shop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JMenuItem;


public class ControllerShop implements ActionListener {

	private ModelShop	model;
	private ViewShop	view;
	private byte		strat;		//0=bin, 1=beans, 2=xstream

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
					model.add(view.getNewProduct());
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
				case "Bin": setStrat((byte) 0);
					break;
				case "beans": setStrat((byte) 1);
					break;
				case "xstream": setStrat((byte) 2);
					break;
				case "save": save();
					break;
				case "load": load();
					break;
				default: //Comming
					System.out.println("Unknown Action for button: " + itm.getText());				
			}
		} else {
			System.out.println("Error: Input of unknown source.");
		}
		
	}
	
	public byte getStrat() {
		return this.strat;
	}
	
	public void setStrat(byte strat) {
		this.strat = strat;
	}

	private void load() {
		// TODO Auto-generated method stub
		
	}

	private void save() {
		// TODO Auto-generated method stub
		
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
