package example;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import fpt.com.Product;

public class ListProductRenderer implements ListCellRenderer<Product> {
	// verwendbar mit JList<Product>.setCellRenderer(new ListProductRenderer());
	// methode

	public Component getListCellRendererComponent(
			JList<? extends Product> list, Product value, int index,
			boolean isSelected, boolean cellHasFocus) {

		String namex = value.getName();
		Box box = Box.createVerticalBox();
		JLabel l = new JLabel(namex);
		JLabel p = new JLabel("Price:" + value.getPrice());
		JLabel q = new JLabel("Quantity:" + value.getQuantity());
		Font f = l.getFont();
		f = f.deriveFont(Font.ITALIC, f.getSize() * 0.8f);
		p.setFont(f);
		q.setFont(f);
		box.add(l);
		box.add(p);
		box.add(q);
		if (isSelected) {
			box.setBorder(BorderFactory.createLineBorder(Color.blue));
		}
		return box;
	}
}
