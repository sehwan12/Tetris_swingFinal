package model.blocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.Color;
import static org.junit.jupiter.api.Assertions.*;

public class TBlockTest {

    private TBlock block;

    @BeforeEach
    void setUp() {
        block = new TBlock();
    }

    @Test
    void constructorTest() {
        // Given, When - setUp() 메소드에서 이미 생성한 객체 사용

        // Then
        // 블록의 모양과 색상이 예상한대로 설정되었는지 확인합니다.
        assertEquals(0, block.getShape(0, 0));
        assertEquals(1, block.getShape(1, 0));
        assertEquals(0, block.getShape(2, 0));
        assertEquals(1, block.getShape(0, 1));
        assertEquals(1, block.getShape(1, 1));
        assertEquals(1, block.getShape(2, 1));
        assertEquals(Color.MAGENTA, block.getColor());
        assertEquals("O", block.getText());
    }

    @Test
    void constructorTest_colorblind() {
        block = new TBlock(true,0);

        // Then
        // 블록의 모양과 색상이 예상한대로 설정되었는지 확인합니다.
        assertEquals(0, block.getShape(0, 0));
        assertEquals(1, block.getShape(1, 0));
        assertEquals(0, block.getShape(2, 0));
        assertEquals(1, block.getShape(0, 1));
        assertEquals(1, block.getShape(1, 1));
        assertEquals(1, block.getShape(2, 1));
        assertEquals(new Color(204,121,167), block.getColor());
        assertEquals("O", block.getText());
    }

    @Test
    void rotate_xTest() {
        // Given, When - setUp() 메소드에서 이미 생성한 객체 사용

        // Then
        // 회전 축의 x 좌표가 각 회전 상태에 따라 정확히 반환되는지 확인합니다.
        assertEquals(1, block.rotate_x()); // rotate_status가 1인 경우
        block.rotate();
        assertEquals(-1, block.rotate_x()); // rotate_status가 2인 경우
        block.rotate();
        assertEquals(0, block.rotate_x()); // rotate_status가 3인 경우
        block.rotate();
        assertEquals(0, block.rotate_x()); // rotate_status가 4인 경우
    }

    @Test
    void rotate_yTest() {
        // Given, When - setUp() 메소드에서 이미 생성한 객체 사용

        // Then
        // 회전 축의 y 좌표가 각 회전 상태에 따라 정확히 반환되는지 확인합니다.
        assertEquals(0, block.rotate_y()); // rotate_status가 1인 경우
        block.rotate();
        assertEquals(1, block.rotate_y()); // rotate_status가 2인 경우
        block.rotate();
        assertEquals(-1, block.rotate_y()); // rotate_status가 3인 경우
        block.rotate();
        assertEquals(0, block.rotate_y()); // rotate_status가 4인 경우
    }
}