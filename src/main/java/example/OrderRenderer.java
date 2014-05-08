package example;


import java.awt.Component;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import fpt.com.Order;
import fpt.com.Product;

public class OrderRenderer implements ListCellRenderer<Order> {
	// verwendbar mit JList<Product>.setCellRenderer(new OrderRenderer());
	// methode

	public Component getListCellRendererComponent(JList<? extends Order> list,
			Order value, int index, boolean isSelected, boolean cellHasFocus) {

		// nur anzeigen

		// also nichts besonderes
		Box bv = Box.createVerticalBox();
		JLabel jl =new JLabel("Order with " + value.getQuantity() + " Items for "
				+ String.format("%5.2f", value.getSum())); 
		bv.add(jl);
		StringBuilder bb = new StringBuilder();
		Font font = jl.getFont().deriveFont(Font.ITALIC,jl.getFont().getSize() * 0.9f);
		for (Product p : value) {
			int l = bb.length();
			if (l > 0) {
				if (l > 100) {
					JLabel ll = new JLabel(bb.toString());
					bb = new StringBuilder();
					ll.setFont(font);
					bv.add(ll);
				} else {
					bb.append(", ");
				}
			}
			bb.append(p.getQuantity()).append(" x ");
			bb.append(p.getName());
			bb.append(" (");
			bb.append(String.format("%5.2f",p.getQuantity() * p.getPrice()));
			bb.append(" )");
		}
		if (bb.length() > 0) {
			JLabel ll = new JLabel(bb.toString());
			bb = new StringBuilder();
			ll.setFont(font);
			bv.add(ll);
		}

		return bv;
	}

}
