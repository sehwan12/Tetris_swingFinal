package view;

import view.OutGameView;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class SettingsView extends OutGameView {

    JPanel[] optionPanel;

    private JLabel[] optionLabel;

    private JButton[] resButton;

    private JButton[] keyButton;

    private JLabel[] keyLabel;

    private JButton[] blindButton;

    private JButton[] resetButton;

    public SettingsView() { }

    // public void initPanel() {}

    public void initTitle() {
        // Title as buildType
        String fullTitle = "Options";
        titleLabel = new JLabel(fullTitle);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
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
            keyLabel[i] = new JLabel(keyMap.get(keyString[i]));
            keyButton[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            keyButton[i].setBackground(Color.WHITE);
            keyLabel[i].setFont(new Font("Arial", Font.BOLD, 20));
            optionPanel[1].add(keyButton[i]);
            optionPanel[1].add(keyLabel[i]);
            keyButton[i].setVisible(true);
            keyLabel[i].setVisible(true);
        }
        optionPanel[1].setVisible(false);
        keyButton[0].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        this.add(optionPanel[1]);
    }

    public void initCBlindPanel(String[] patternString) {
        optionPanel[2] = new JPanel();
        optionPanel[2].setLayout(null);
        if (patternString.length > 0) {
            blindButton = new JButton[patternString.length];
        }
        for (int i = 0; i < patternString.length; i++) {
            blindButton[i] = new JButton(patternString[i]);
            blindButton[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            optionPanel[2].add(blindButton[i]);
            blindButton[i].setVisible(true);
        }
        optionPanel[2].setVisible(false);
        blindButton[0].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        this.add(optionPanel[2]);
    }

    public void initResetPanel(String[] resetString) {
        optionPanel[3] = new JPanel();
        optionPanel[3].setLayout(null);
        if (resetString.length > 0) {
            resetButton = new JButton[resetString.length];
        }
        for (int i = 0; i < resetString.length; i++) {
            resetButton[i] = new JButton(resetString[i]);
            resetButton[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
            optionPanel[3].add(resetButton[i]);
            resetButton[i].setVisible(true);
        }
        optionPanel[3].setVisible(false);
        resetButton[0].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        this.add(optionPanel[3]);
    }

    public void initWindow(int resX, int resY) {
        setTitle("Option");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        if (optionLabel != null) {
            for (int i = 0; i < optionLabel.length; i++) {
                optionLabel[i].setFont(new Font("Arial", Font.BOLD, 15));
                optionLabel[i].setBounds(resX * (i + 1) / (optionLabel.length + 1), resY / 6, resX / optionLabel.length , resY / 15);
                // optionLabel[i].setHorizontalAlignment(JLabel.CENTER);
                this.add(optionLabel[i]);
                optionLabel[0].setForeground(Color.YELLOW);
                // optionLabel[i].setVisible(false);
            }
//            if (optionLabel.length != 0) {
//                optionLabel[0].setVisible(true);
//            }

            arrowLabel[0].setBounds(resX / 10, resY / 6, resX / 3, resY / 15);
            arrowLabel[0].setForeground(Color.GREEN);
            arrowLabel[1].setBounds(resX * 9 / 10, resY / 6, resX / 3, resY / 15);
            arrowLabel[1].setForeground(Color.GREEN);
            this.add(arrowLabel[0]);
            this.add(arrowLabel[1]);

        }

        titleLabel.setBounds(resX / 2 - titleLabel.getText().length() * 5 , resY / 20, resX / 2, resY / 6);
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

        for (int i = 0; i < blindButton.length; i++) {
            blindButton[i].setBounds(resX / 4,  i * (resY / 20 + resY / 30), resX / 2, resY / 20);
        }

        for (int i = 0; i < resetButton.length; i++) {
            resetButton[i].setBounds(resX / 4,  i * (resY / 20 + resY / 30), resX / 2, resY / 20);
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

    public void updateKeyMap(String[] keyString, HashMap<String, String> keyMap) {
        for (int i = 0; i < keyString.length; i++) {
            keyLabel[i].setText(keyMap.get(keyString[i]));
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
                releaseButton(blindButton, yCursor);
                break;
            case 3:
                releaseButton(resetButton, yCursor);
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
                paintButton(blindButton, yCursor);
                break;
            case 3:
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
                highlightButton(blindButton, yCursor);
                break;
            case 3:
                highlightButton(resetButton, yCursor);
                break;
        }
    }



}
