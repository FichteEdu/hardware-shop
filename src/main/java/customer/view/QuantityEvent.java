package customer.view;

import java.awt.AWTEvent;

import fpt.com.Product;


@SuppressWarnings("serial")
public class QuantityEvent extends AWTEvent {

	private Product	product;
	private int		newQuantity;

	public QuantityEvent(	Object source, Product p, int quantity) {
		super(source, 0);
		this.product = p;
		this.newQuantity = quantity;
	}

	public Product getProduct() {
		return product;
	}

	public int getNewQuantity() {
		return newQuantity;
	}

}
