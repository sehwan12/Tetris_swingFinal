package view.OutGame;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class SettingsView extends OutGameView {

    JPanel[] optionPanel;

    private JLabel[] optionLabel;

    private JButton[] resButton;

    private JButton[] keyButton;
    private JButton[] keyButton2;

    private JLabel[] keyLabel;
    private JLabel[] keyLabel2;

    private JButton[] blindButton;

    private JButton[] resetButton;

    private JButton[] difficultyButton;

    public SettingsView() { }

    // public void initPanel() {}

    @Override
    public void initTitle() {
        // Title as buildType
        String fullTitle = "Options";
        titleLabel = new JLabel(fullTitle);

        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 20));

    }
    public void initLabel(String[] optionString) {
        if (optionString.length > 0) {
            optionLabel = new JLabel[optionString.length];
            optionPanel = new JPanel[optionString.length];
        }
        for (int i = 0; i < optionString.length; i++) {
            optionLabel[i] = new JLabel(optionString[i]);
        }
    }


    public void initResPanel(String[] recommendRes) {
        optionPanel[0] = new JPanel();
        optionPanel[0].setLayout(null);
        if (recommendRes.length > 0) {
            resButton = new JButton[recommendRes.length];
        }
        for (int i = 0; i < recommendRes.length; i++) {
            resButton[i] = new JButton(recommendRes[i]);
            resButton[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            optionPanel[0].add(resButton[i]);
            resButton[i].setVisible(true);
        }
        optionPanel[0].setVisible(true);
        resButton[0].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        this.add(optionPanel[0]);
    }

    public void initKeyMapPanel(String[] keyString, HashMap<String, String> keyMap) {
        optionPanel[1] = new JPanel();
        optionPanel[1].setLayout(null);
        if (keyString.length > 0) {
            keyButton = new JButton[keyString.length];
            keyLabel = new JLabel[keyString.length];
        }
        for (int i = 0; i < keyString.length; i++) {
            keyButton[i] = new JButton(keyString[i]);
            keyLabel[i] = new JLabel(keyMap.get(keyString[i] + "1P"));
            keyButton[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            keyButton[i].setBackground(Color.WHITE);

            keyLabel[i].setFont(new Font("Malgun Gothic", Font.BOLD, 20));



            optionPanel[1].add(keyButton[i]);
            optionPanel[1].add(keyLabel[i]);
            keyButton[i].setVisible(true);
            keyLabel[i].setVisible(true);
        }
        optionPanel[1].setVisible(false);
        keyButton[0].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        this.add(optionPanel[1]);
    }
    public void initKeyMapPanel2(String[] keyString, HashMap<String, String> keyMap) {
        optionPanel[2] = new JPanel();
        optionPanel[2].setLayout(null);
        if (keyString.length > 0) {
            keyButton2 = new JButton[keyString.length];
            keyLabel2 = new JLabel[keyString.length];
        }
        for (int i = 0; i < keyString.length; i++) {
            keyButton2[i] = new JButton(keyString[i]);
            keyLabel2[i] = new JLabel(keyMap.get(keyString[i] + "2P"));
            keyButton2[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            keyButton2[i].setBackground(Color.WHITE);

            keyLabel2[i].setFont(new Font("Malgun Gothic", Font.BOLD, 20));

            optionPanel[2].add(keyButton2[i]);
            optionPanel[2].add(keyLabel2[i]);
            keyButton2[i].setVisible(true);
            keyLabel2[i].setVisible(true);
        }
        optionPanel[2].setVisible(false);
        keyButton2[0].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        this.add(optionPanel[2]);
    }

    public void initCBlindPanel(String[] patternString) {
        optionPanel[3] = new JPanel();
        optionPanel[3].setLayout(null);
        if (patternString.length > 0) {
            blindButton = new JButton[patternString.length];
        }
        for (int i = 0; i < patternString.length; i++) {
            blindButton[i] = new JButton(patternString[i]);
            blindButton[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            optionPanel[3].add(blindButton[i]);
            blindButton[i].setVisible(true);
        }
        optionPanel[3].setVisible(false);
        blindButton[0].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        this.add(optionPanel[3]);
    }

    public void initResetPanel(String[] resetString) {
        optionPanel[4] = new JPanel();
        optionPanel[4].setLayout(null);
        if (resetString.length > 0) {
            resetButton = new JButton[resetString.length];
        }
        for (int i = 0; i < resetString.length; i++) {
            resetButton[i] = new JButton(resetString[i]);
            resetButton[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            optionPanel[4].add(resetButton[i]);
            resetButton[i].setVisible(true);
        }
        optionPanel[4].setVisible(false);
        resetButton[0].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        this.add(optionPanel[4]);
    }

    public void initDifficultyPanel(String[] difficultyString) {
        optionPanel[5] = new JPanel();
        optionPanel[5].setLayout(null);
        if (difficultyString.length > 0) {
            difficultyButton = new JButton[difficultyString.length];
        }
        for (int i = 0; i < difficultyString.length; i++) {
            difficultyButton[i] = new JButton(difficultyString[i]);
            difficultyButton[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            optionPanel[5].add(difficultyButton[i]);
            difficultyButton[i].setVisible(true);
        }
        optionPanel[5].setVisible(false);
        difficultyButton[0].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        this.add(optionPanel[5]);
    }

    public void initWindow(int resX, int resY) {
        setTitle("Option");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        if (optionLabel != null) {
            for (int i = 0; i < optionLabel.length; i++) {

                optionLabel[i].setFont(new Font("Malgun Gothic", Font.BOLD, 15));

                optionLabel[i].setBounds(20 + (i) * resX / optionLabel.length, resY / 6, 5 * resX / ( 6 * optionLabel.length), resY / 15);
                // optionLabel[i].setHorizontalAlignment(JLabel.CENTER);
                this.add(optionLabel[i]);
                optionLabel[0].setForeground(Color.YELLOW);
                // optionLabel[i].setVisible(false);
            }
//            if (optionLabel.length != 0) {
//                optionLabel[0].setVisible(true);
//            }

            arrowLabel[0].setBounds(10, resY / 6, resX / 3, resY / 15);
            arrowLabel[0].setForeground(Color.GREEN);
            arrowLabel[1].setBounds(resX - 10, resY / 6, resX / 3, resY / 15);
            arrowLabel[1].setForeground(Color.GREEN);
            this.add(arrowLabel[0]);
            this.add(arrowLabel[1]);

        }

        titleLabel.setBounds(resX / 2 - titleLabel.getText().length() * 5 , resY / 50, resX / 2, resY / 6);
        this.add(titleLabel);

        for (int i = 0; i < optionLabel.length; i++) {
            optionPanel[i].setBounds(0, resY / 3, resX, resY / 2);
        }

        for (int i = 0; i < resButton.length; i++) {
            resButton[i].setBounds(resX / 4,  i * (resY / 20 + resY / 30), resX / 2, resY / 20);
        }

        for (int i = 0; i < keyButton.length; i++) {
            keyButton[i].setBounds(resX / 6,  i * (resY / 20 + resY / 30), resX / 3, resY / 20);
        }

        for (int i = 0; i < keyLabel.length; i++) {
            keyLabel[i].setBounds(2 * resX / 3,  i * (resY / 20 + resY / 30), resX / 3, resY / 20);
        }

        for (int i = 0; i < keyButton2.length; i++) {
            keyButton2[i].setBounds(resX / 6,  i * (resY / 20 + resY / 30), resX / 3, resY / 20);
        }

        for (int i = 0; i < keyLabel2.length; i++) {
            keyLabel2[i].setBounds(2 * resX / 3,  i * (resY / 20 + resY / 30), resX / 3, resY / 20);
        }

        for (int i = 0; i < blindButton.length; i++) {
            blindButton[i].setBounds(resX / 4,  i * (resY / 20 + resY / 30), resX / 2, resY / 20);
        }

        for (int i = 0; i < resetButton.length; i++) {
            resetButton[i].setBounds(resX / 4,  i * (resY / 20 + resY / 30), resX / 2, resY / 20);
        }
        for (int i = 0; i < difficultyButton.length; i++) {
            difficultyButton[i].setBounds(resX / 4,  i * (resY / 20 + resY / 30), resX / 2, resY / 20);
        }



        this.add(mainPanel);

        setSize(resX, resY);
        setLocationRelativeTo(null);

        repaint();

        setVisible(true);
    }

    public void moveOption(int section) {
        for (int i = 0; i < optionLabel.length; i++) {
            optionLabel[i].setForeground(Color.black);
            optionPanel[i].setVisible(false);
        }
        optionLabel[section].setForeground(Color.YELLOW);
        optionPanel[section].setVisible(true);
    }

    public void updateKeyMap(String[] keyString, HashMap<String, String> keyMap, String player) {
        if (player.equals("1P")) {
            for (int i = 0; i < keyString.length; i++) {
                keyLabel[i].setText(keyMap.get(keyString[i] + player));
            }
        }
        else {
            for (int i = 0; i < keyString.length; i++) {
                keyLabel2[i].setText(keyMap.get(keyString[i] + player));
            }
        }
    }

    public void highlightButton(JButton[] button, int yCursor) {
        for (int i = 0; i < button.length; i++) {
            button[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        }
        button[yCursor].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
    }

    public void releaseButton(int section, int yCursor) {
        switch(section) {
            case 0:
                releaseButton(resButton, yCursor);
                break;
            case 1:
                releaseButton(keyButton, yCursor);
                break;
            case 2:
                releaseButton(keyButton2, yCursor);
                break;
            case 3:
                releaseButton(blindButton, yCursor);
                break;
            case 4:
                releaseButton(resetButton, yCursor);
                break;
            case 5:
                releaseButton(difficultyButton, yCursor);
                break;
        }
    }

    public void releaseButton(JButton[] button, int yCursor) {
        button[yCursor].setBackground(Color.WHITE);
        button[yCursor].setOpaque(true);
    }

    public void paintButton(int section, int yCursor) {
        switch(section) {
            case 0:
                paintButton(resButton, yCursor);
                break;
            case 1:
                paintButton(keyButton, yCursor);
                break;
            case 2:
                paintButton(keyButton2, yCursor);
                break;
            case 3:
                paintButton(blindButton, yCursor);
                break;
            case 4:
                paintButton(resetButton, yCursor);
                break;
        }
    }

    public void paintButton(JButton[] button, int yCursor) {
        for (int i = 0; i < button.length; i++) {
            button[i].setBackground(Color.WHITE);
            button[i].setOpaque(true);
        }
        button[yCursor].setBackground(Color.YELLOW);
        button[yCursor].setOpaque(true);
    }

    public void moveButton(int section, int yCursor) {
        switch(section) {
            case 0:
                highlightButton(resButton, yCursor);
                break;
            case 1:
                highlightButton(keyButton, yCursor);
                break;
            case 2:
                highlightButton(keyButton2, yCursor);
                break;
            case 3:
                highlightButton(blindButton, yCursor);
                break;
            case 4:
                highlightButton(resetButton, yCursor);
                break;
            case 5:
                highlightButton(difficultyButton, yCursor);
                break;
        }
    }



}
