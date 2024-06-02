package controller.OutGame;

import controller.BoardController;
import controller.VersusBoardController;
import model.VersusMode.VsBoardModel;
import model.VersusMode.VsItemBoardModel;
import model.VersusMode.VsTimeBoardModel;
import view.OutGame.VersusEnterView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;

public class VersusEnterController extends MenuController {
    private static VersusEnterController instance;

    private VersusEnterController() {
        super();
        // initFrame();
    }

    public static synchronized VersusEnterController getInstance() {
        if (instance == null) {
            instance = new VersusEnterController();
        }
        return instance;
    }

    public void initFrame() {
        this.view = new VersusEnterView();
        initView();
        initFocus();
        view.setVisible(true);
    }

    @Override
    protected void initView() {
        view.initPanel();
        view.initTitle();
        view.initButtons(model.getVersusMenuString());
        view.initWindow(model.getResX(), model.getResY());
        view.paintFocus(model.getCurFocus());
        view.setLocationRelativeTo(null);
    }

    @Override
    protected void initFocus() {
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
        im.put(KeyStroke.getKeyStroke("ESCAPE"), "esc");
        im.put(KeyStroke.getKeyStroke("⎋"), "esc");



        am.put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.moveVersusFocus(-1);
                view.paintFocus(model.getCurFocus());
            }
        });
        am.put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.moveVersusFocus(+1);
                view.paintFocus(model.getCurFocus());
            }
        });
        am.put("esc", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.setyCursor(0);
                view.dispose();
                view = null;
                MenuController.getInstance().initFrame();
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

    @Override
    protected void enterMenu(int curFocus) {
        switch (curFocus) {
            case 0:
                view.setVisible(false);
                view.dispose();
                view = null;
                // 추후 대전모드를 위해서 Singleton으로 적용하지 않았음
                boardController = new VersusBoardController(new VsBoardModel(0), new VsBoardModel(1));
                System.out.println("기본모드 진입");
                break;
            case 1:
                view.setVisible(false);
                view.dispose();
                view = null;
                // 아이템 모드 진입하도록 수정해야 함
                boardController = new VersusBoardController(new VsItemBoardModel(0), new VsItemBoardModel(1));
                System.out.println("아이템모드 진입");
                break;
            case 2:
                view.setVisible(false);
                view.dispose();
                view = null;
                // 아이템 모드 진입하도록 수정해야 함
                boardController = new VersusBoardController(new VsTimeBoardModel(0), new VsTimeBoardModel(1));
                System.out.println("시간제한 모드 진입");
                break;

        }
    }



}
