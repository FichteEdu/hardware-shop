

import main.ControllerShop;
import main.ModelShop;
import main.ViewShop;

public class Main {

    public static void main(String[] args) {
        ModelShop m = new ModelShop();
        ViewShop v = new ViewShop();
        ControllerShop c = new ControllerShop();
        c.link(m, v);
        v.setVisible(true);
    }

}
