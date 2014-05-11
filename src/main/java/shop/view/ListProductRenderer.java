package shop.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;

import fpt.com.Product;


public class ListProductRenderer implements ListCellRenderer<Product> {

    // verwendbar mit JList<Product>.setCellRenderer(new ListProductRenderer());
    // methode

    @Override
    public Component getListCellRendererComponent(JList<? extends Product> list, Product product,
            int index, boolean isSelected, boolean cellHasFocus) {

        String name = product.getName();
        Box box = Box.createVerticalBox();
        JLabel l = new JLabel(name);
        JLabel p = new JLabel("Price: " + product.getPrice());
        JLabel q = new JLabel("Quantity: " + product.getQuantity());
        
        Font f = l.getFont();
        f = f.deriveFont(Font.ITALIC, f.getSize() * 0.8f);
        p.setFont(f);
        q.setFont(f);
        
        box.add(l);
        box.add(p);
        box.add(q);
        
        // Create titled border (with different color)
        Color lineColor = isSelected ? Color.BLUE : Color.BLACK;
        Border border = BorderFactory.createLineBorder(lineColor);
        box.setBorder(BorderFactory.createTitledBorder(border, name));
        return box;
    }
}
