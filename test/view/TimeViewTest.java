package view;

import org.junit.jupiter.api.*;
import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeViewTest {
    @Test
    void testSetTimeText() {
        // Arrange
        TimeView timeView = new TimeView();

        // Act
        timeView.setTimeText(System.currentTimeMillis());

        // Assert
        JTextPane timeTextPane = (JTextPane) timeView.getComponent(0);
        String displayedText = timeTextPane.getText();
        // 예상된 텍스트와 실제 텍스트를 비교하여 시간이 올바르게 표시되는지 확인
        assertEquals("Time: 10", displayedText);
    }

}