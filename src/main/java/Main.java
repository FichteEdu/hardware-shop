import shop.ControllerShop;
import shop.ModelShop;
import shop.ViewShop;
//import customer.ModelCustomer;
import customer.ViewCustomer;


public class Main {

	public static void main(String[] args) {
		ModelShop m = new ModelShop();
		ViewShop v = new ViewShop();
		ControllerShop c = new ControllerShop();
		c.link(m, v);

		// TODO: there is no ControllerCustomer yet, so do it quick and dirty
		// The view should not know about its model at creation time, but this
		// will be taken care of at a later point in time.
		//
//		ModelCustomer mc = new ModelCustomer();
		ViewCustomer vc = new ViewCustomer();
		// link manually
		vc.setModel(m);
		m.addObserver(vc);

		v.setVisible(true);
		vc.setVisible(true);
	}

}
