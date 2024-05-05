package view.OutGame;

import view.OutGame.MenuView;

import javax.swing.*;
import java.awt.*;

public class VersusEnterView extends MenuView {

    public VersusEnterView() {
        super();
        setLocationRelativeTo(null);
    }

    @Override
    public void initTitle() {
        // Title as buildType
        String fullTitle = "진입 모드 선택";
        titleLabel = new JLabel(fullTitle);
        titleLabel.setFont(new Font("malgun gothic", Font.BOLD, 20));
    }

    @Override
    public void initWindow(int resX, int resY) {
        setTitle("Tetris");
        setSize(resX, resY);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // 여기서부터 함수 코드 끝까지는 해상도에 따른 프로퍼티를 구현함

        titleLabel.setBounds(resX / 2 - titleLabel.getText().length() * 7 , resY / 20, resX / 2, resY / 6);
        this.add(titleLabel);

        for (int i = 0; i < menuButton.length; i++) {
            menuButton[i].setBounds(resX / 3,  resY / 5 + (resY / 8) * (i + 1), resX / 3, resY / 10);
        }

        this.add(mainPanel);
    }

}
