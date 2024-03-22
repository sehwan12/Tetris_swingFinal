package InProgress;

import javax.swing.*;
import java.awt.*;

public class SetDifficulty extends JFrame {
    public SetDifficulty(JFrame parent) {
        setTitle("난이도 선택");
        setSize(300, 200);
        setLocationRelativeTo(parent);
        setLayout(new GridLayout(3, 1));

        JButton easyButton = new JButton("Easy");
        JButton normalButton = new JButton("Normal");
        JButton hardButton = new JButton("Hard");

        easyButton.addActionListener(e -> {
            // 추후 코드 추가
            this.dispose(); // 창 닫기
        });

        normalButton.addActionListener(e -> {
            // 추후 테스트 추가
            this.dispose(); // 창 닫기
        });

        hardButton.addActionListener(e -> {
            // 여기에 Hard 난이도로 게임을 시작하는 코드를 추가하세요.
            this.dispose(); // 창 닫기
        });

        add(easyButton);
        add(normalButton);
        add(hardButton);
    }
}
