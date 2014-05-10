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
        Iterator<Product> it = plist.iterator();
        Product p = null;

        // Get the product at position X
        // TOCHECK all rows covered?
        for (int i = 0; it.hasNext(); it.next(), i++)
            if (i == row)
                p = it.next();

        // This should not happen, so we let it fail knowingly
        // if (p == null) return null;

        switch (col) {
            case 1:
                return p.getId();
            case 2:
                return p.getName();
            case 3:
                return p.getPrice();
            case 4:
                return p.getQuantity();
        }
        return null;
    }

}


public class ViewShop extends JFrame implements Observer {

    private static final long     serialVersionUID = -9216593040924621252L;
    private JTable                productTable;
    private ProductListTableModel tableModel;

    public ViewShop() {
        setTitle("ViewShop");
        setSize(450, 300);

        productTable = new JTable();
        tableModel = new ProductListTableModel();
        productTable.setModel(tableModel);
        getContentPane().add(productTable, BorderLayout.CENTER);
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
}
