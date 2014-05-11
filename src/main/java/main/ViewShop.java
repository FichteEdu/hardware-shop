package main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import fpt.com.Product;
import fpt.com.ProductList;

import javax.swing.JPanel;

import java.awt.FlowLayout;


@SuppressWarnings("serial")
class ProductListTableModel extends AbstractTableModel {

    private ProductList       plist;

    private static Object[][] columns = { { "ID", Long.class }, { "Name", String.class },
            { "Price", Double.class }, { "Quantity", Integer.class } };

    public ProductListTableModel() {
    }

    public ProductListTableModel(ProductList plist) {
        this.plist = plist;
    }

    /**
     * Set the table model's ProductList to fetch values from.
     * 
     * @param plist
     */
    public void setPlist(ProductList plist) {
        this.plist = plist;
    }

    public ProductList getPlist() {
        return plist;
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int i) {
        return (String) columns[i][0];
    }

    @Override
    public Class<?> getColumnClass(int i) {
        return (Class<?>) columns[i][1];
    }

    @Override
    public int getRowCount() {
        return plist != null ? plist.size() : 0;
    }

    @Override
    public Object getValueAt(int row, int col) {
        Product p = getProduct(row);

        switch (col) {
            case 1:
                return p.getId();
            case 2:
                return p.getName();
            case 3:
                return p.getPrice();
            case 4:
                return p.getQuantity();
            default:
                System.out.println("Invalid column number");
        }
        return null;
    }

    /**
     * Get the product at position i in {@code plist} using an iterator.
     * 
     * @param i
     * @return The Product at that position or null.
     */
    Product getProduct(int i) {
        Iterator<Product> it = plist.iterator();

        // Get the product at position X
        // TOCHECK
        for (int j = 0; it.hasNext(); it.next(), j++)
            if (j == i)
                return it.next();

        return null;
    }

}


public class ViewShop extends JFrame implements Observer {

    private static final long     serialVersionUID = -9216593040924621252L;
    private JTable                productTable;
    private ProductListTableModel tableModel;

    public ViewShop() {
        setTitle("ViewShop");
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setSize(450, 300);

        productTable = new JTable();
        tableModel = new ProductListTableModel();
        productTable.setModel(tableModel);
        getContentPane().add(productTable, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        getContentPane().add(panel, BorderLayout.SOUTH);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JButton btnNewButton = new JButton("Add");
        panel.add(btnNewButton);

        JButton btnNewButton_1 = new JButton("Delete (selected)");
        panel.add(btnNewButton_1);
    }

    @Override
    public void update(Observable arg0, Object arg1) {
        productTable.repaint();
    }

    /**
     * Recursively iterate through all containers and add `action` as action
     * listener to all buttons.
     * 
     * @param action
     *            The listener to add
     */
    public void addActionListener(ActionListener al) {
        addActionListener(this, al);
    }

    /**
     * Recursively iterate through all containers and add `action` as action
     * listener to all buttons.
     * 
     * @param c
     *            Container to iterate through
     * @param al
     *            The listener to add
     */
    private void addActionListener(Container c, ActionListener al) {
        for (Component child : c.getComponents()) {

            if (child instanceof Container)
                addActionListener((Container) child, al);
            if (child instanceof JButton) {
                ((JButton) child).addActionListener(al);
            }
        }
    }

    public void setModel(ProductList m) {
        tableModel.setPlist(m);
    }

    public int getSelectedIndex() {
        return productTable.getSelectedRow();
    }

    /**
     * Return the currently selected product in the table.
     * @return ^
     */
    public Product getSelected() {
        return tableModel.getProduct(productTable.getSelectedRow());
    }
}
