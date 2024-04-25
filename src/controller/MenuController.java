package controller;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;

import model.ItemBoardModel;
import model.OutGameModel;
import view.MenuView;

public class MenuController {

    private static MenuController instance;

    private SettingsController settingsController;

    private ScoreBoardController scoreBoardController;

    private BoardController boardController;

    private OutGameModel model;
    public MenuView view;



    private MenuController() {
        this.model = OutGameModel.getInstance();
        initFrame();
    }
    // 싱글턴
    public static synchronized MenuController getInstance() {
        if (instance == null) {
            instance = new MenuController();
        }
        return instance;
    }

    public void initFrame() {
        this.view = new MenuView();
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
        view.setLocationRelativeTo(null);
    }




    private void initFocus() {
        HashSet<String> keys = new HashSet<>();
        keys.add("↑");
        keys.add("↓");
        keys.add("⏎");
        keys.add("⎋");
        keys.add("Up");
        keys.add("Down");
        keys.add("Enter");
        keys.add("Escape");
        // JComponent.WHEN_IN_FOCUSED_WINDOW
        InputMap im = view.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        // InputMap im = view.getRootPane().getInputMap();
        ActionMap am = view.getRootPane().getActionMap();

        im.put(KeyStroke.getKeyStroke("UP"), "up");
        im.put(KeyStroke.getKeyStroke("↑"), "up");
        im.put(KeyStroke.getKeyStroke("DOWN"), "down");
        im.put(KeyStroke.getKeyStroke("↓"), "down");
        im.put(KeyStroke.getKeyStroke("ENTER"), "enter");
        im.put(KeyStroke.getKeyStroke("⏎"), "enter");
        im.put(KeyStroke.getKeyStroke("ESC"), "esc");
        im.put(KeyStroke.getKeyStroke("⎋"), "esc");



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

        view.addKeyListener(new KeyAdapter() {
            String currentKey;
            @Override
            public void keyPressed(KeyEvent e) {
                // 키 매핑 모드에서만 키 입력 처리
                currentKey = KeyEvent.getKeyText(e.getKeyCode());
                if (!keys.contains(currentKey)) {
                    view.showWarning("↑, ↓, ENTER, ESC로만 조작할 수 있습니다");
                }

            }
        });

        view.setFocusable(true);
        view.requestFocusInWindow();

    }
    private void enterMenu(int curFocus) {
        switch (curFocus) {
            case 0:
                view.setVisible(false);
                view.dispose();
                view = null;
                // 추후 대전모드를 위해서 Singleton으로 적용하지 않았음
                boardController = new BoardController();
                break;
            case 1:
                view.setVisible(false);
                view.dispose();
                view = null;
                // 아이템 모드 진입하도록 수정해야 함
                boardController = new BoardController(new ItemBoardModel());
                break;
            case 2:
                view.setVisible(false);
                view.dispose();
                view = null;
                // Option 창 진입
                settingsController = SettingsController.getInstance();
                if (settingsController.view == null) {
                    settingsController.initFrame();
                }
                // 추후 메서드 추가
                break;
            case 3:
                view.setVisible(false);
                view.dispose();
                view = null;
                scoreBoardController = ScoreBoardController.getInstance();
                scoreBoardController.initFrame();

                // ScoreBoard 진입
                // 추후 메서드 추가
                break;
            case 4:
                System.exit(0); // 정상 종료
                break;
        }
    }

}
