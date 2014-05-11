package model;

import fpt.com.Product;


public class Order extends ProductList implements fpt.com.Order {

	private static final long	serialVersionUID	= 3001L;

	@Override
	public double getSum() {
		int sum = 0;
		for (Product p : this)
			sum += p.getQuantity() * p.getPrice();

		return sum;
	}

	@Override
	public int getQuantity() {
		int sum = 0;
		for (Product p : this)
			sum += p.getQuantity();

		return sum;
	}

}
