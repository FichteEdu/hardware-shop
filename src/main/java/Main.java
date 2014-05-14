
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
		//ModelCustomer cc = new ModelCustomer();
		ViewCustomer cv = new ViewCustomer(m);
		
		v.setVisible(true);
		cv.setVisible(true);
	}
	
	

}
