package controller;

import javax.swing.*;
import java.awt.event.ActionEvent;

import model.ScoreBoardModel;
import view.ScoreBoardView;
import model.OutGameModel;

public class ScoreBoardController {

    private ScoreBoardModel model;
    private ScoreBoardView view;

    private static ScoreBoardController instance;


    // inputMap으로 변경(마우스 클릭시 키입력 먹통되는 현상 해결을 위해서)
    // private KeyListener playerKeyListener;

    private ScoreBoardController() {
        model = new ScoreBoardModel();
        view = new ScoreBoardView();
        view.setVisible(true);
        initFrame();
    }

    public static synchronized ScoreBoardController getInstance() {
        if (instance == null) {
            instance = new ScoreBoardController();
        }
        return instance;
    }

    public void initFrame() {
        if (view == null) {
            refreshData();
            this.view = new ScoreBoardView();
        }
        view.setSize(OutGameModel.getInstance().getResX(), OutGameModel.getInstance().getResY());
        initFocus();
        view.setVisible(true);
    }

    public void initFrame(String name, int score) {
        if (view == null) {
            refreshData();
            this.view = new ScoreBoardView(name, score);
        }
        view.setSize(OutGameModel.getInstance().getResX(), OutGameModel.getInstance().getResY());
        initFocus();
        view.setVisible(true);
    }

    public void destroyView() {
        view.dispose();
        view = null;
    }

    public void refreshData() {
        model.initData();
    }

    private void initFocus() {
        // JComponent.WHEN_IN_FOCUSED_WINDOW
        InputMap im = view.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        // InputMap im = view.getRootPane().getInputMap();
        ActionMap am = view.getRootPane().getActionMap();

        im.put(KeyStroke.getKeyStroke("ESCAPE"), "esc");


        am.put("esc", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                destroyView();
                MenuController.getInstance().initFrame();
            }
        });


    }

}
