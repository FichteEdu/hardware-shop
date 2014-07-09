package customer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Order;
import shop.ModelShop;
import customer.view.QuantityEvent;
import customer.view.QuantityListener;
import fpt.com.Product;


public class ControllerCustomer implements ActionListener, QuantityListener {

	@SuppressWarnings("unused")
	private ViewCustomer	v;
	private ModelCustomer	m;
	private Order			currentOrder;

	public ControllerCustomer() {
	}

	private void newOrder() {
		currentOrder = new Order();
		m.getOrders().add(0, currentOrder);
		m.changed();
	}

	public ControllerCustomer(	ModelShop ms, ModelCustomer m, ViewCustomer v) {
		link(ms, m, v);
	}

	public void link(ModelShop ms, ModelCustomer m, ViewCustomer v) {
		this.v = v;
		this.m = m;
		v.setModels(m, ms);
		// When a cell was edited, this is called
		v.addActionListener(this);
		v.addQuantityListener(this);

		m.newOrders();
		newOrder();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO network stuff with currentOrder
		newOrder();
	}

	@Override
	public void quantityChanged(QuantityEvent e) {

		Product p = e.getProduct();
		Product op = currentOrder.findProductById(p.getId());

		// Limit to maximum available
		int quantity = Math.min(e.getNewQuantity(), p.getQuantity());

		// Remove product from order if below 1 (and in the order)
		if (quantity < 1) {
			if (op != null)
				currentOrder.remove(op);
			return;
		}
		// Add product to order if necessary
		if (op == null) {
			op = model.Product.clone(p);
			currentOrder.add(op);
		}
		op.setQuantity(quantity);
		m.changed();
	}

}
