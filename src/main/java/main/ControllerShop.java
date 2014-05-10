package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ControllerShop implements ActionListener {

    private ModelShop model;
    private ViewShop  view;

    public ControllerShop() {
    }

    public ControllerShop(ModelShop m, ViewShop v) {
        link(m, v);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    /**
     * Link a model and a view component.
     * 
     * @param m
     * @param v
     */
    public void link(ModelShop m, ViewShop v) {
        model = m;
        model.addObserver(v);

        view = v;
        view.addActionListener(this);
    }
}
