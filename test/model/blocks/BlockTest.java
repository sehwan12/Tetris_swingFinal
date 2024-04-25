package model.blocks;

import org.junit.jupiter.api.Test;
import java.awt.Color;
import static org.junit.jupiter.api.Assertions.*;

public class BlockTest {

    @Test
    void getShapeTest() {
        // Given
        Block block = new Block() {
            {
                shape = new int[][]{{1, 2}, {3, 4}};
            }
        };

        // Then
        // 주어진 위치의 블록 모양이 정확히 반환되는지 확인합니다.
        assertEquals(1, block.getShape(0, 0));
        assertEquals(2, block.getShape(1, 0));
        assertEquals(3, block.getShape(0, 1));
        assertEquals(4, block.getShape(1, 1));
    }

    @Test
    void setShapeTest() {
        // Given
        Block block = new Block() {
            {
                shape = new int[2][2];
            }
        };

        // When
        block.setShape(0, 0, 1);
        block.setShape(1, 0, 2);
        block.setShape(0, 1, 3);
        block.setShape(1, 1, 4);

        // Then
        // 주어진 위치에 블록 모양이 올바르게 설정되었는지 확인합니다.
        assertEquals(1, block.getShape(0, 0));
        assertEquals(2, block.getShape(1, 0));
        assertEquals(3, block.getShape(0, 1));
        assertEquals(4, block.getShape(1, 1));
    }

    @Test
    void getColorTest() {
        // Given
        Block block = new Block() {
            {
                color = Color.RED;
            }
        };

        // Then
        // 블록의 색상이 정확히 반환되는지 확인합니다.
        assertEquals(Color.RED, block.getColor());
    }

    @Test
    void getTextTest() {
        // Given
        Block block = new Block() {
            {
                text = "Test";
            }
        };

        // Then
        // 블록의 텍스트가 정확히 반환되는지 확인합니다.
        assertEquals("Test", block.getText());
    }

    @Test
    void rotateTest() {
        // Given
        Block block = new Block() {
            {
                shape = new int[][]{{1, 2}, {3, 4}};
            }
        };

        // When
        block.rotate();

        // Then
        // 블록이 90도 회전되었는지 확인합니다.
        assertEquals(3, block.getShape(0, 0));
        assertEquals(1, block.getShape(1, 0));
        assertEquals(4, block.getShape(0, 1));
        assertEquals(2, block.getShape(1, 1));

        // 회전 상태가 90도로 업데이트되었는지 확인합니다.
        assertEquals(2, block.rotate_status);
    }

    @Test
    void rotate_xTest() {
        // Given
        Block block = new Block() {
            {
                shape = new int[][]{{1, 2}, {3, 4}};
            }
        };

        // Then
        // 회전 축의 x 좌표가 항상 0을 반환하는지 확인합니다.
        assertEquals(0, block.rotate_x());
    }

    @Test
    void rotate_yTest() {
        // Given
        Block block = new Block() {
            {
                shape = new int[][]{{1, 2}, {3, 4}};
            }
        };

        // Then
        // 회전 축의 y 좌표가 항상 0을 반환하는지 확인합니다.
        assertEquals(0, block.rotate_y());
    }

    @Test
    void heightTest() {
        // Given
        Block block = new Block() {
            {
                shape = new int[][]{{1, 2}, {3, 4}};
            }
        };

        // Then
        // 블록의 높이가 정확히 반환되는지 확인합니다.
        assertEquals(2, block.height());
    }

    @Test
    void widthTest() {
        // Given
        Block block = new Block() {
            {
                shape = new int[][]{{1, 2}, {3, 4}};
            }
        };

        // Then
        // 블록의 너비가 정확히 반환되는지 확인합니다.
        assertEquals(2, block.width());
    }
}
