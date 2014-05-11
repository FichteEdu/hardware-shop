package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observer;

import javax.swing.JFrame;

import model.Product;
import net.miginfocom.swing.MigLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;


@SuppressWarnings("serial")
public class AddProductDialog extends JFrame {

    private JTextField tfName;
    private JTextField tfPrice;
    private JTextField tfQuantity;

    public AddProductDialog(final Observer observer) {
        setTitle("Add Product");
        getContentPane().setLayout(new MigLayout("", "[right][grow]", "[][][][]"));

        JLabel lblName = new JLabel("Name:");
        getContentPane().add(lblName, "cell 0 0,alignx trailing");

        tfName = new JTextField();
        getContentPane().add(tfName, "cell 1 0,growx");
        tfName.setColumns(10);

        JLabel lblPrice = new JLabel("Price:");
        getContentPane().add(lblPrice, "cell 0 1,alignx trailing");

        tfPrice = new JTextField();
        getContentPane().add(tfPrice, "cell 1 1,growx");
        tfPrice.setColumns(10);

        JLabel lblQuantity = new JLabel("Quantity:");
        getContentPane().add(lblQuantity, "cell 0 2,alignx trailing");

        tfQuantity = new JTextField();
        getContentPane().add(tfQuantity, "cell 1 2,growx");
        tfQuantity.setColumns(10);

        JButton btnAdd = new JButton("Add");
        getContentPane().add(btnAdd, "flowx,cell 1 3");
        btnAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ev) {
                try {
                    double price = Double.valueOf(tfPrice.getText());
                    int quantity = Integer.valueOf(tfQuantity.getText());

                    // Notify the observer with our newly created product
                    observer.update(null, new Product(0, tfName.getText(), price, quantity));
                    // We're done now, so just dispose
                    dispose();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(AddProductDialog.this,
                            "Yout number is not valid.", "Invalid input", JOptionPane.OK_OPTION);
                    return;
                }

            }
        });

        JButton btnCancel = new JButton("Cancel");
        getContentPane().add(btnCancel, "cell 1 3");
        btnAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

}
