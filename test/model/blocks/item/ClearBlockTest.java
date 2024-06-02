package model.blocks.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.Color;
import static org.junit.jupiter.api.Assertions.*;

public class ClearBlockTest {
    private ClearBlock block;

    @BeforeEach
    void setUp() {
        block = new ClearBlock();
    }

    @Test
    void constructorTest() {
        // Given, When - setUp() 메소드에서 이미 생성한 객체 사용

        // Then
        // 블록의 모양과 색상이 예상한대로 설정되었는지 확인합니다.
        assertEquals(1, block.getShape(0, 0));
        assertEquals(Color.WHITE, block.getColor());
        assertEquals("C", block.getText());
    }


}
