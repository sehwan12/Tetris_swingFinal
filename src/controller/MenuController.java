package controller;

import javax.swing.*;
import java.awt.event.ActionEvent;

import env.Board;
import model.MenuModel;
import view.MenuView;

public class MenuController {
    private MenuModel model;
    private MenuView view;

    public MenuController(MenuModel model, MenuView view) {
        this.model = model;
        this.view = view;
        initView();
        initFocus();
        view.setVisible(true);
    }

    private void initView() {
        view.initPanel();
        if (model.getBuildType() == 3) {
            view.initLable(model.getGameModeString());
        }
        view.initTitle(model.getBuildString(), model.getBuildType());
        view.initButtons(model.getMenuString());
        view.initWindow(model.getResX(), model.getResY());
        view.paintFocus(model.getCurFocus());
    }




    private void initFocus() {
        InputMap im = view.getRootPane().getInputMap();
        ActionMap am = view.getRootPane().getActionMap();

        im.put(KeyStroke.getKeyStroke("UP"), "up");
        im.put(KeyStroke.getKeyStroke("DOWN"), "down");
        im.put(KeyStroke.getKeyStroke("ENTER"), "enter");

        am.put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.moveFocus(-1);
                view.paintFocus(model.getCurFocus());
            }
        });
        am.put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.moveFocus(+1);
                view.paintFocus(model.getCurFocus());
            }
        });
        am.put("enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enterMenu(model.getCurFocus());
            }
        });

    }
    private void enterMenu(int curFocus) {
        switch (curFocus) {
            case 0:
                // 테트리스 게임 진입
                Board.startGame();
                break;
            case 1:
                // Option 창 진입
                // 추후 메서드 추가
                break;
            case 2:
                // ScoreBoard 진입
                // 추후 메서드 추가
                break;
            case 3:
                System.exit(0); // 정상 종료
                break;
        }
    }

}
