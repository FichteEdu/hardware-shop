package shop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;

import fpt.com.Product;


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
		JButton btn = (JButton) e.getSource();

		switch (btn.getText()) {
			case "Add":
				new AddProductDialog(new Observer() {

					@Override
					public void update(Observable o, Object arg) {
						if (arg != null)
							model.add((Product) arg);
					}
				});
				break;
			case "Delete (selected)":
				model.delete(view.getSelected());
				break;
			default:
				System.out.println("Unknown Action for button: " + btn.getText());
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
