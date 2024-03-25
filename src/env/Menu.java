package env;

import model.MenuModel;
import view.MenuView;
import controller.MenuController;

public class Menu {
    private MenuModel model;
    private MenuView view;
    private MenuController controller;

    public Menu() {
        model = new MenuModel();
        view = new MenuView();
        controller = new MenuController(model, view);
    }

    public static void main(String[] args)  {
        Menu a = new Menu();
    }
}
