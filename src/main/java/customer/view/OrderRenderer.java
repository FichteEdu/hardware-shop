package customer.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import fpt.com.Order;
import fpt.com.Product;


// Mainly from received Example
public class OrderRenderer implements ListCellRenderer<Order> {
	
	private static final Font  SECONDARY_FONT;
	private static final Color ACTIVE_COLOR = Color.BLUE;

	static {
		Font baseFont = new JLabel().getFont();
		SECONDARY_FONT = baseFont.deriveFont(Font.ITALIC, baseFont.getSize() * 0.9f);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends Order> list, Order order,
			int index, boolean isSelected, boolean cellHasFocus) {

		Box vbox = Box.createVerticalBox();
		JLabel label = new JLabel(String.format("Order with %d Item%s for %5.2f€",
				order.getQuantity(), (order.getQuantity() == 1 ? "" : "s"), order.getSum()));
		vbox.add(label);
		if (index == 0)
			label.setForeground(ACTIVE_COLOR);

		// Add label for each product
		for (Product p : order) {
			String text = String.format("%dx %s (%5.2f€)", p.getQuantity(), p.getName(),
					p.getQuantity() * p.getPrice());
			JLabel ll = new JLabel(text);
			ll.setFont(SECONDARY_FONT);
			vbox.add(ll);
		}

		return vbox;
	}

}
