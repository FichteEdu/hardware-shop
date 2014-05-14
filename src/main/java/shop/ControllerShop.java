package shop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JMenuItem;


public class ControllerShop implements ActionListener {

	private ModelShop	model;
	private ViewShop	view;

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
			switch (((JMenuItem) e.getSource()).getText()) {
				case "Bin": model.setStrat((byte) 0);
					break;
				case "beans": model.setStrat((byte) 1);
					break;
				case "xstream": model.setStrat((byte) 2);
					break;
				default: //Comming
					break;				
			}
		} else {
			System.out.println("Error: Input of unknown source.");
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
