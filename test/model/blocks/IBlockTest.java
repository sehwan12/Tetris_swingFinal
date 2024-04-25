package model.blocks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.awt.Color;
import static org.junit.jupiter.api.Assertions.*;

public class IBlockTest {

    private IBlock block;

    @BeforeEach
    void setUp() {
        block = new IBlock();
    }

    @Test
    void constructorTest() {
        // Given, When - setUp() 메소드에서 이미 생성한 객체 사용

        // Then
        // 블록의 모양과 색상이 예상한대로 설정되었는지 확인합니다.
        assertEquals(1, block.getShape(0, 0));
        assertEquals(1, block.getShape(1, 0));
        assertEquals(1, block.getShape(2, 0));
        assertEquals(1, block.getShape(3, 0));
        assertEquals(Color.CYAN, block.getColor());
        assertEquals("O", block.getText());
    }

    @Test
    void constructorTest_colorblind() {
        block = new IBlock(true,0);

        // Then
        // 블록의 모양과 색상이 예상한대로 설정되었는지 확인합니다.
        assertEquals(1, block.getShape(0, 0));
        assertEquals(1, block.getShape(1, 0));
        assertEquals(1, block.getShape(2, 0));
        assertEquals(1, block.getShape(3, 0));
        assertEquals(new Color(6,158,115), block.getColor());
        assertEquals("O", block.getText());
    }

    @Test
    void rotate_xTest() {
        // Given, When - setUp() 메소드에서 이미 생성한 객체 사용

        // Then
        // 회전 축의 x 좌표가 각 회전 상태에 따라 정확히 반환되는지 확인합니다.
        assertEquals(2, block.rotate_x()); // rotate_status가 1인 경우
        block.rotate();
        assertEquals(-2, block.rotate_x()); // rotate_status가 2인 경우
        block.rotate();
        assertEquals(1, block.rotate_x()); // rotate_status가 3인 경우
        block.rotate();
        assertEquals(-1, block.rotate_x()); // rotate_status가 4인 경우
    }

    @Test
    void rotate_yTest() {
        // Given, When - setUp() 메소드에서 이미 생성한 객체 사용

        // Then
        // 회전 축의 y 좌표가 각 회전 상태에 따라 정확히 반환되는지 확인합니다.
        assertEquals(-1, block.rotate_y()); // rotate_status가 1인 경우
        block.rotate();
        assertEquals(2, block.rotate_y()); // rotate_status가 2인 경우
        block.rotate();
        assertEquals(-2, block.rotate_y()); // rotate_status가 3인 경우
        block.rotate();
        assertEquals(1, block.rotate_y()); // rotate_status가 4인 경우
    }
}