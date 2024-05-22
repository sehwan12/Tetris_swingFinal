package model.OutGame;

import static org.junit.jupiter.api.Assertions.*;

import IO.ImportSettings;
import model.OutGame.OutGameModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OutGameModelTest {

    private OutGameModel outGameModel;

    @BeforeEach
    void setUp() {
        outGameModel = OutGameModel.getInstance();
    }

    @Test
    void moveOptionFocusTest() {
        outGameModel.setOptionFocus(0);

        outGameModel.moveOptionFocus(1);
        assertEquals(1, outGameModel.getOptionFocus());

        outGameModel.moveOptionFocus(-1);
        assertEquals(0, outGameModel.getOptionFocus());

        // Test wrapping around
        outGameModel.moveOptionFocus(-1);
        assertEquals(5, outGameModel.getOptionFocus());

        outGameModel.moveOptionFocus(1);
        assertEquals(0, outGameModel.getOptionFocus());
    }

    @Test
    void moveButtonFocusTest() {
        outGameModel.setOptionFocus(0);

        for (int i = 0; i < OutGameModel.getOptionString().length; i++) {
            int yCursorBefore = outGameModel.getyCursor();
            int limit = outGameModel.getStringType()[i].length;
            int expectedCursor = (yCursorBefore) % limit;
            //System.out.println(i+" "+limit+" "+yCursorBefore+" "+expectedCursor);
            for (int j = 0; j < limit * 2; j++) {

                yCursorBefore = outGameModel.getyCursor();
                outGameModel.moveButtonFocus(1);

                if (yCursorBefore + 1 < 0) {
                    expectedCursor = limit - 1;
                } else if (yCursorBefore + 1 >= limit) {
                    expectedCursor = 0;
                } else {
                    expectedCursor += 1;
                }
                //System.out.println(outGameModel.getyCursor()+" "+expectedCursor);
                assertEquals(expectedCursor, outGameModel.getyCursor());
            }
            outGameModel.moveOptionFocus(1);
        }
    }

    @Test
    void resetDefaultTest() {
        // Given
        OutGameModel outGameModel = OutGameModel.getInstance();

        // When
        // 설정을 변경하고 resetDefault를 호출하기 전에 저장된 설정값을 백업합니다.
        int previousResX = outGameModel.getResX();
        int previousResY = outGameModel.getResY();
        boolean BlindMode = ImportSettings.getSetting("blindMode").equals("true"); //OutGameModel.isBlindMode();

        // 설정을 변경합니다.
        outGameModel.setResX(8000);
        outGameModel.setResY(6000);
        OutGameModel.setBlindMode();

        // resetDefault를 호출하여 기본값으로 초기화합니다.
        outGameModel.resetDefault();

        // Then
        // 설정이 기본값으로 초기화되었는지 확인합니다.
        assertEquals(previousResX, outGameModel.getResX());
        assertEquals(previousResY, outGameModel.getResY());
        assertEquals(BlindMode, OutGameModel.isBlindMode());

    }
}