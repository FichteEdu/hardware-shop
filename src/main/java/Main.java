import shop.ControllerShop;
import shop.ModelShop;
import shop.ViewShop;
import customer.ControllerCustomer;
import customer.ModelCustomer;
import customer.ViewCustomer;


public class Main {

	public static void main(String[] args) {
		ModelShop ms = new ModelShop();
		ViewShop vs = new ViewShop();
		ControllerShop cs = new ControllerShop();
		cs.link(ms, vs);

		vs.setVisible(true);

		customer(ms);
	}

	private static void customer(ModelShop ms) {

		ModelCustomer mc = new ModelCustomer();
		ViewCustomer vc = new ViewCustomer();
		ControllerCustomer cc = new ControllerCustomer();
		// link manually
		cc.link(ms, mc, vc);

		vc.setVisible(true);
	}

}
