package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;

import controller.MenuController;
import model.MenuModel;

public class MenuView extends JFrame {

    private JPanel mainPanel;

    // Only for featureTest.
    private JLabel[] arrowLabel = { new JLabel("<"), new JLabel(">") };

    private JLabel[] gameModeLabel;

    private JButton[] menuButton;

    private JLabel titleLabel;

    private int curFocus;


    public MenuView() {
        setLocationRelativeTo(null);
    }

    public void initPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
    }

    public void initLable(String[] gameModeString) {
        if (gameModeString.length != 0) {
            gameModeLabel = new JLabel[gameModeString.length];
        }
        for (int i = 0; i < gameModeString.length; i++) {
            gameModeLabel[i] = new JLabel(gameModeString[i]);
        }
    }

    public void initTitle(String[] buildString, int buildType) {
        // Title as buildType
        String fullTitle = "Tetris ";
        if (buildString.length != 0) {
            fullTitle += buildString[buildType];
        }
        titleLabel = new JLabel(fullTitle);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
    }

    public void initButtons(String[] menuString) {
        if (menuString.length > 0) {
            menuButton = new JButton[menuString.length];
        }
        for (int i = 0; i < menuString.length; i++) {
            menuButton[i] = new JButton(menuString[i]);
            menuButton[i].setFocusPainted(false);
            menuButton[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            mainPanel.add(menuButton[i]);
            this.add(menuButton[i]);
        }
        if (menuString.length > 0) {
            menuButton[0].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3)); // 첫 번째 버튼 강조
        }
        this.add(mainPanel);
    }


    public void initWindow(int resX, int resY) {
        setTitle("Tetris");
        setSize(resX, resY);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // 여기서부터 함수 코드 끝까지는 해상도에 따른 프로퍼티를 구현함
        if (gameModeLabel != null) {
            for (int i = 0; i < gameModeLabel.length; i++) {
                gameModeLabel[i].setFont(new Font("Arial", Font.BOLD, 15));
                gameModeLabel[i].setBounds(resX / 3, resY / 6, resX / 3, resY / 15);
                gameModeLabel[i].setHorizontalAlignment(JLabel.CENTER);
                this.add(gameModeLabel[i]);
                gameModeLabel[i].setVisible(false); // Duo Play 구현 시 메서드로 프로퍼티 수정하기
            }
            if (gameModeLabel.length != 0) {
                gameModeLabel[0].setVisible(true); // Duo Play 구현 시 메서드로 프로퍼티 수정하기
            }
            arrowLabel[0].setBounds(resX / 3 + 10, resY / 6, 30, 30);
            arrowLabel[1].setBounds(resX - (resX / 3 + 20), resY / 6, 30, 30);
            this.add(arrowLabel[0]);
            this.add(arrowLabel[1]);
        }

        titleLabel.setBounds(resX / 2 - titleLabel.getText().length() * 5 , resY / 20, resX / 2, resY / 6);
        this.add(titleLabel);

        for (int i = 0; i < menuButton.length; i++) {
            menuButton[i].setBounds(resX / 3,  resY / 5 + (resY / 8) * (i + 1), resX / 3, resY / 10);
        }

        this.add(mainPanel);
    }


    public void paintFocus (int x) {
        if (menuButton.length != 0 && curFocus != x) {
            menuButton[curFocus].setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            curFocus = x;
            menuButton[curFocus].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        }
    }
}
