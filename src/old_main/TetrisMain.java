package old_main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TetrisMain extends JFrame {
    // 0 : Release, 1 : Development, 2 : Feature, 3: FeatureTest
    private static final int buildType = 2;
    private String[] buildString = { "Release", "Development", "Feature", "InProgress"};

    private JPanel mainPanel;

    private static int resX;
    private static int resY;

    private int curGameMode = 0;

    private int curFocus = 0;

    // Only for featureTest.
    private JLabel[] arrowLabel = { new JLabel("<"), new JLabel(">") };

    private JLabel[] gameModeLabel = new JLabel[2];

    private String[] menuString = { "Start Game", "Settings", "ScoreBoard", "Quit" };
    private JButton[] menuButton = new JButton[menuString.length];

    public TetrisMain() {
        resX = 400;
        resY = 600;
        setLocationRelativeTo(null);
        if (buildType == 3) {
            initLable();
        }
        initPanel();
        initTitle();
        initButtons();
        initFocus();
    }

    public TetrisMain(int resX, int resY) {
        this.resX = resX;
        this.resY = resY;
        setLocationRelativeTo(null);
        if (buildType == 3) {
            initLable();
        }
        initPanel();
        initTitle();
        initButtons();
        initFocus();
    }

    // for final Submission
    private void initLable() {
        gameModeLabel[0] = new JLabel("Single Play");
        gameModeLabel[1] = new JLabel("Duo Play");

        for(int i = 0; i < 2; i++) {
            gameModeLabel[i].setFont(new Font("Arial", Font.BOLD, 15));
            gameModeLabel[i].setBounds(resX / 3, resY / 6, resX / 3, resY / 15);
            gameModeLabel[i].setHorizontalAlignment(JLabel.CENTER);
            this.add(gameModeLabel[i]);
        }
        gameModeLabel[1].setVisible(false); // Duo Play 구현 시 메서드로 프로퍼티 수정하기

        arrowLabel[0].setBounds(resX / 3 + 10, resY / 6, 30, 30);
        arrowLabel[1].setBounds(resX - (resX / 3 + 20), resY / 6, 30, 30);
        this.add(arrowLabel[0]);
        this.add(arrowLabel[1]);
    }

    private void initPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
    }

    private void initTitle() {
        setTitle("Tetris");
        setSize(resX, resY);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Title as buildType
        String fullTitle = "Tetris " + buildString[buildType];
        JLabel titleLabel = new JLabel(fullTitle);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setBounds(resX / 2 - fullTitle.length() * 5 , resY / 20, resX / 2, resY / 6);
        mainPanel.add(titleLabel);

        this.add(mainPanel);
    }

    private void initButtons() {
        for (int i = 0; i < menuButton.length; i++) {
            menuButton[i] = new JButton(menuString[i]);
            menuButton[i].setBounds(resX / 3,  resY / 5 + (resY / 8) * (i + 1), resX / 3, resY / 10);
            menuButton[i].setFocusPainted(false);
            menuButton[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            mainPanel.add(menuButton[i]);
            this.add(menuButton[i]);
        }
        if (menuButton.length > 0) {
            menuButton[0].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3)); // 첫 번째 버튼 강조
        }
        this.add(mainPanel);

    }

    private void initFocus() {
        InputMap im = this.getRootPane().getInputMap();
        ActionMap am = this.getRootPane().getActionMap();

        im.put(KeyStroke.getKeyStroke("UP"), "up");
        im.put(KeyStroke.getKeyStroke("DOWN"), "down");
        im.put(KeyStroke.getKeyStroke("ENTER"), "enter");

        am.put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveFocus(-1);
            }
        });
        am.put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                moveFocus(+1);
            }
        });
        am.put("enter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enterMenu(curFocus);
            }
        });
    }
    // 버그 찾을 때 GPT 4.0 사용
    private void moveFocus(int x) {
        if ((x > 0 && (curFocus + x) < menuButton.length) || (x < 0 && curFocus + x >= 0)) {
            menuButton[curFocus].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            curFocus += x;
            menuButton[curFocus].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        }
    }

    private void enterMenu(int curFocus) {
        switch (curFocus) {
            case 0:
                // 테트리스 게임 진입
                Tetris.startGame();
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

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            TetrisMain ex = new TetrisMain(400, 500);
            ex.setVisible(true);
        });
    }
}