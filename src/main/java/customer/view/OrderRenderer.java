package customer.view;

import java.awt.Component;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import fpt.com.Order;
import fpt.com.Product;


public class OrderRenderer implements ListCellRenderer<Order> {

	// Mainly from received Example

	// verwendbar mit JList<Product>.setCellRenderer(new OrderRenderer());
	// methode

	@Override
	public Component getListCellRendererComponent(JList<? extends Order> list, Order order,
			int index, boolean isSelected, boolean cellHasFocus) {

		// nur anzeigen

		// also nichts besonderes
		Box box = Box.createVerticalBox();
		JLabel label = new JLabel(String.format("Order with %d Item%s for %5.2f€",
				order.getQuantity(), (order.getQuantity() == 1 ? "" : "s"), order.getSum()));
		box.add(label);
		StringBuilder bb = new StringBuilder();
		Font font = label.getFont().deriveFont(Font.ITALIC, label.getFont().getSize() * 0.9f);
		// TODO: show in multiple lines
		for (Product p : order) {
			int l = bb.length();
			if (l > 0) {
				if (l > 100) {
					JLabel ll = new JLabel(bb.toString());
					bb = new StringBuilder();
					ll.setFont(font);
					box.add(ll);
				} else {
					bb.append(", ");
				}
			}
			bb.append(p.getQuantity()).append(" x ");
			bb.append(p.getName());
			bb.append(" (");
			bb.append(String.format("%5.2f€", p.getQuantity() * p.getPrice()));
			bb.append(" )");
		}
		if (bb.length() > 0) {
			JLabel ll = new JLabel(bb.toString());
			bb = new StringBuilder();
			ll.setFont(font);
			box.add(ll);
		}

		return box;
	}

}
