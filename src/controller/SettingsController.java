package controller;

import model.OutGameModel;
import view.SettingsView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SettingsController {
    private static SettingsController instance;
    private OutGameModel model;
    public SettingsView view;

    private String currentKey;
    private boolean isKeyMappingMode = false; // 키 매핑 모드 상태를 추적하는 변수

    private SettingsController() {
        model = OutGameModel.getInstance();
        initFrame();
    }


    public static synchronized SettingsController getInstance() {
        if (instance == null) {
            instance = new SettingsController();
        }
        return instance;
    }

    public void initFrame() {
        if (view == null) {
            this.view = new SettingsView();
        }
        initView();
        initFocus();
        view.setVisible(true);
    }


    private void initView() {
        view.initPanel();
        view.initTitle();
        view.initLabel(OutGameModel.getOptionString());
        view.initResPanel(OutGameModel.getRecommendResolution());
        view.initKeyMapPanel(OutGameModel.getKeyString(), OutGameModel.getKeyMap());
        view.initCBlindPanel(OutGameModel.getBlindString());
        view.initResetPanel(OutGameModel.getResetString());
        view.initDifficultyPanel(OutGameModel.getDifficultyString());
        view.initWindow(model.getResX(), model.getResY());
    }

    protected void initFocus() {
        InputMap im = view.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = view.getRootPane().getActionMap();

        im.put(KeyStroke.getKeyStroke("UP"), "up");
        im.put(KeyStroke.getKeyStroke("DOWN"), "down");
        im.put(KeyStroke.getKeyStroke("OPEN_BRACKET"), "open_bracket");
        im.put(KeyStroke.getKeyStroke("CLOSE_BRACKET"), "close_bracket");
        im.put(KeyStroke.getKeyStroke("ENTER"), "enter");
        im.put(KeyStroke.getKeyStroke("ESCAPE"), "esc");

        am.put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.moveButtonFocus(-1);
                view.moveButton(model.getOptionFocus(), model.getyCursor());
            }
        });
        am.put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.moveButtonFocus(1);
                view.moveButton(model.getOptionFocus(), model.getyCursor());
            }
        });
        am.put("open_bracket", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // view.clearOption(model.getOptionFocus());
                model.setyCursor(0);
                view.moveButton(model.getOptionFocus(), model.getyCursor());
                model.moveOptionFocus(-1);
                view.moveOption(model.getOptionFocus());
            }
        });
        am.put("close_bracket", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // view.clearOption(model.getOptionFocus());
                model.setyCursor(0);
                view.moveButton(model.getOptionFocus(), model.getyCursor());
                model.moveOptionFocus(1);
                view.moveOption(model.getOptionFocus());
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
                switch (model.getOptionFocus()) {
                    case 0:
                        view.paintButton(model.getOptionFocus(), model.getyCursor());
                        model.setResolution();
                        view.initWindow(model.getResX(), model.getResY());
                        break;
                    case 1:
                        if (!isKeyMappingMode) {
                            isKeyMappingMode = true;
                            view.paintButton(model.getOptionFocus(), model.getyCursor());
                            // 키 매핑 모드 진입
                            // 여기서 키 입력 대기, 아래 KeyAdapter에서 처리
                        }
                        break;
                    case 2:
                        view.paintButton(model.getOptionFocus(), model.getyCursor());
                        model.setBlindMode();
                        if (model.isBlindMode()) {
                            SwingUtilities.invokeLater(() -> {
                                view.showWarning("색맹모드가 켜졌습니다");
                            });
                        }
                        else {
                            SwingUtilities.invokeLater(() -> {
                                view.showWarning("색맹모드가 꺼졌습니다");
                            });
                        }
                        break;
                    case 3:
                        view.paintButton(model.getOptionFocus(), model.getyCursor());
                        if (model.getyCursor() == 0) {
                            if (JOptionPane.YES_OPTION == view.showQuestion("스코어 보드 기록을 초기화하시겠습니까?")) {
                                model.resetScoreBoard();
                            }
                        }
                        else {
                            if (JOptionPane.YES_OPTION == view.showQuestion("모든 옵션이 기본값으로 초기화됩니다")) {
                                model.resetDefault();
                                view.initWindow(model.getResX(), model.getResY());
                                view.updateKeyMap(model.getKeyString(), model.getKeyMap());
                            }
                        }
                        break;
                    case 4:
                        view.paintButton(model.getOptionFocus(), model.getyCursor());
                        model.setDifficulty();
                        view.showWarning(OutGameModel.getDifficultyString()[model.getyCursor()] + " 난이도로 설정되었습니다.");
                        break;
                    default:
                        break;
                }
            }
        });

        view.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // 키 매핑 모드에서만 키 입력 처리
                currentKey = KeyEvent.getKeyText(e.getKeyCode());
                System.out.println("Pressed key: " + currentKey);

                // 설정 모델에 키 업데이트
                if (isKeyMappingMode) {
                    isKeyMappingMode = false;
                    if (currentKey.equals("⏎") || currentKey.equals("Enter")) {
                        SwingUtilities.invokeLater(() -> {
                            view.showWarning("Enter 키는 설정하실 수 없습니다.");
                            // 경고 메시지 후 다음 명령 실행
                            view.releaseButton(model.getOptionFocus(), model.getyCursor());
                            isKeyMappingMode = false;
                        });
                    }
                    else {
                        OutGameModel.setKeyMap(currentKey);
                        view.updateKeyMap(model.getKeyString(), model.getKeyMap());
                        view.releaseButton(model.getOptionFocus(), model.getyCursor());
                    }
                }
            }
        });

        view.setFocusable(true);
        view.requestFocusInWindow();

    }
}
