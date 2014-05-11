

import shop.ControllerShop;
import shop.ModelShop;
import shop.ViewShop;

public class Main {

    public static void main(String[] args) {
        ModelShop m = new ModelShop();
        ViewShop v = new ViewShop();
        ControllerShop c = new ControllerShop();
        c.link(m, v);
        v.setVisible(true);
    }

}
