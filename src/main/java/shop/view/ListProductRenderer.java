package shop.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

import fpt.com.Product;


public class ListProductRenderer implements ListCellRenderer<Product> {

	// Precompose border objects because we need them over and over again
	private static final Border	border	= BorderFactory.createLineBorder(Color.BLACK),
			selectedBorder = BorderFactory.createLineBorder(Color.RED);

	@Override
	public Component getListCellRendererComponent(JList<? extends Product> list, Product product,
			int index, boolean isSelected, boolean cellHasFocus) {

		String name = product.getName();
		// JComponent panel = Box.createHorizontalBox();
		JComponent panel = new JPanel(new GridLayout(0, 3));

		JLabel id = new JLabel("ID: " + product.getId());
		JLabel p = new JLabel("Price: " + product.getPrice());
		JLabel q = new JLabel("Quantity: " + product.getQuantity());

		// Adjust font
		Font f = p.getFont();
		f = f.deriveFont(Font.ITALIC, f.getSize() * 0.8f);
		id.setFont(f);
		p.setFont(f);
		q.setFont(f);

		// Add labels
		panel.add(id);
		panel.add(p);
		panel.add(q);

		// Create titled border (with different color)
		Border lineBorder = isSelected ? selectedBorder : border;
		panel.setBorder(BorderFactory.createTitledBorder(lineBorder, name));
		return panel;
	}
}
