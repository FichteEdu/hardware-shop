package customer;

import java.util.ArrayList;
import java.util.Observable;

import model.Order;


public class ModelCustomer extends Observable {

	private ArrayList<Order>	orders	= new ArrayList<>();

	public ArrayList<Order> getOrders() {
		return orders;
	}

	public void newOrders() {
		orders = new ArrayList<Order>();
	}

	public void changed() {
		setChanged();
		notifyObservers();
	}
}
