package main;

import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import model.ProductList;
import fpt.com.Product;


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
        Product p = plist.get(row);
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

    private static final long serialVersionUID = -9216593040924621252L;
    private JTable            productTable;

    public ViewShop() {
        setTitle("ViewShop");

        productTable = new JTable();
        productTable.setModel(new ProductListTableModel());
        getContentPane().add(productTable, BorderLayout.CENTER);
    }

    @Override
    public void update(Observable arg0, Object arg1) {
        productTable.repaint();
    }

}
