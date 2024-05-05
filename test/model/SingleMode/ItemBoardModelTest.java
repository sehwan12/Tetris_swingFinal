package model.SingleMode;

import model.SingleMode.BoardModel;
import model.SingleMode.ItemBoardModel;
import model.blocks.Block;
import model.blocks.*;
import model.blocks.item.*;
import model.blocks.item.Alias.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemBoardModelTest {

    ItemBoardModel boardModel;

    @BeforeEach
    void setUP() {
        boardModel = spy(new ItemBoardModel());
    }

    @Test
    void getRandomBlock() {
        ItemBoardModel boardModel = spy(new ItemBoardModel());
        boardModel.linesCleared = 9;
        assertFalse(boardModel.getRandomBlock() instanceof ItemBlock);

        // given
        boardModel.linesCleared = 11;
        // when, then
        assertTrue(boardModel.getRandomBlock() instanceof ItemBlock);


        /*
        boardModel.linesCleared_10=1;
        when(boardModel.rws_selectItem()).thenReturn(0);
        assertTrue(boardModel.getRandomBlock() instanceof ClearBlock);

        boardModel.linesCleared_10=1;
        when(boardModel.rws_select()).thenReturn(0);
        when(boardModel.rws_selectItem()).thenReturn(1);
        assertTrue(boardModel.getRandomBlock() instanceof IBlock);

        boardModel.linesCleared_10=1;
        when(boardModel.rws_selectItem()).thenReturn(2);
        assertTrue(boardModel.getRandomBlock() instanceof WeightBlock);

        boardModel.linesCleared_10=0;
        when(boardModel.rws_select()).thenReturn(0);
        assertTrue(boardModel.getRandomBlock() instanceof IBlock);
         */
    }
    @Test
    void testPlaceBlock_Shape_1() {
        // 테스트에 필요한 객체 생성
        ItemBoardModel boardModel = new ItemBoardModel();
        Block block = new IBlock(true, 0); // 예시로 I 블록 생성

        // 현재 블록으로 설정합니다.
        boardModel.setCurr(block);

        // placeBlock() 메서드를 호출하여 보드에 블록을 배치합니다.
        boardModel.placeBlock();

        // 블록이 보드에 적절히 배치되었는지 확인합니다.
        int[][] board = boardModel.getBoard();
        String[][] boardText = boardModel.getBoard_text();
        Color[] boardColor = boardModel.getBoard_color();

        // 블록이 존재하는 위치에는 1로 설정되어야 합니다.
        for (int i = 0; i < block.width(); i++) {
            for (int j = 0; j < block.height(); j++) {
                if (block.getShape(i, j) == 1) {
                    assertTrue(board[boardModel.getY() + j][boardModel.getX() + i] == 1);
                }
            }
        }

        // 블록이 존재하는 위치에는 적절한 텍스트와 색상이 지정되어야 합니다.
        for (int i = 0; i < block.width(); i++) {
            for (int j = 0; j < block.height(); j++) {
                if (block.getShape(i, j) == 1) {
                    // 적절한 텍스트 확인
                    assertEquals(boardText[boardModel.getY() + j][boardModel.getX() + i], block.getText());
                    // 적절한 색상 확인
                    assertEquals(boardColor[((boardModel.getY() + j + 1) * (BoardModel.WIDTH + 3)) + boardModel.getX() + i + 1], block.getColor());
                }
            }
        }
    }

    @Test
    void testPlaceBlock_Shape_2() {

        ItemBlock[] block = new ItemBlock[3];
        // 테스트에 필요한 객체 생성
        block[0] = new LineClearBlock(); // 예시로 I 블록 생성
        block[1] = new LineFillBlock(); // 예시로 I 블록 생성
        block[2] = new TimerBlock(); // 예시로 I 블록 생성

        for(int k=0; k<=2; k++) {
            // 현재 블록으로 설정합니다.
            boardModel.setCurr(block[k]);

            boardModel.curr.setShape(1,0,2);
            // placeBlock() 메서드를 호출하여 보드에 블록을 배치합니다.
            boardModel.placeBlock();

            // 블록이 보드에 적절히 배치되었는지 확인합니다.
            int[][] board = boardModel.getBoard();
            String[][] boardText = boardModel.getBoard_text();
            Color[] boardColor = boardModel.getBoard_color();

            // 블록이 존재하는 위치에는 1로 설정되어야 합니다.
            for (int i = 0; i < block[k].width(); i++) {
                for (int j = 0; j < block[k].height(); j++) {
                    if (block[k].getShape(i, j) == 2) {
                        System.out.println("2");
                        assertTrue(board[boardModel.getY() + j][boardModel.getX() + i] == 1);
                    }
                }
            }

            // 블록이 존재하는 위치에는 적절한 텍스트와 색상이 지정되어야 합니다.
            for (int i = 0; i < block[k].width(); i++) {
                for (int j = 0; j < block[k].height(); j++) {
                    if (block[k].getShape(i, j) == 2) {
                        switch (k){
                            case 0:
                                // 적절한 텍스트 확인
                                assertEquals(boardText[boardModel.getY() + j][boardModel.getX() + i], "L");
                                break;
                            case 1:
                                // 적절한 텍스트 확인
                                assertEquals(boardText[boardModel.getY() + j][boardModel.getX() + i], "F");
                                break;
                            case 2:
                                // 적절한 텍스트 확인
                                assertEquals(boardText[boardModel.getY() + j][boardModel.getX() + i], "S");
                                break;
                        }
                        // 적절한 색상 확인
                        assertEquals(boardColor[((boardModel.getY() + j + 1) * (BoardModel.WIDTH + 3)) + boardModel.getX() + i + 1], Color.WHITE);
                    }
                }
            }
        }

    }

    boolean testCollision(BoardModel boardModel, int setX, int setY, int horizon, int vertical) {
        boardModel.setX(setX);
        boardModel.setY(setY);

        return boardModel.collisionCheck(horizon, vertical);
    }

    @Test
    void testBoundaryCollision() {
        ItemBoardModel boardModel = new ItemBoardModel();
        //y가 0일때 위쪽 경계 테스트
        assertTrue(testCollision(boardModel, 0,0,0,-1));
        //y가 boardModel.HEIGHT-1일때 아래쪽 경계 테스트
        assertTrue(testCollision(boardModel, 0,boardModel.HEIGHT-1,0,+1));
        //x가 0일때 왼쪽 경계 테스트
        assertTrue(testCollision(boardModel, 0,0,-1,0));
        //x가 boardModel.WIDTH-1일때 오른쪽 경계 테스트
        assertTrue(testCollision(boardModel, boardModel.WIDTH-1,0,+1,0));
    }

    @Test
    void testBlockCollision() {
        // 초기 보드 모델 생성
        ItemBoardModel boardModel = new ItemBoardModel();

        // 충돌이 발생할 위치에 블록 배치
        int collisionX = 5; // 충돌이 발생할 x 좌표
        int collisionY = 15; // 충돌이 발생할 y 좌표
        boardModel.getBoard()[collisionY][collisionX] = 1; // 충돌이 발생할 위치에 블록 배치

        // 충돌을 테스트할 블록 생성
        Block testBlock = new IBlock(false, 0); // 충돌을 테스트할 블록 생성 (예시로 I 블록 사용)
        boardModel.setCurr(testBlock);

        // 테스트할 블록의 위치 설정 (이동하지 않고 고정된 위치에 배치)
        int testBlockX = 4; // 테스트할 블록의 x 좌표
        int testBlockY = 14; // 테스트할 블록의 y 좌표
        boardModel.setX(testBlockX);
        boardModel.setY(testBlockY);

        // 충돌 테스트 수행
        boolean collisionDetected = boardModel.collisionCheck(1, 1);

        // 충돌이 발생해야 함
        assertTrue(collisionDetected);
    }

    @Test
    void testeraseCurr() {
        ItemBoardModel boardModel = new ItemBoardModel();
        // Arrange
        boardModel.placeBlock();

        // Act
        boardModel.eraseCurr();

        // Assert
        // Ensure all elements in the board array are reset to 0
        for (int[] row : boardModel.getBoard()) {
            for (int cell : row) {
                assertEquals(0, cell);
            }
        }
        // Ensure all elements in the color array are reset to null
        for (Color color : boardModel.getBoard_color()) {
            assertNull(color);
        }
        // Ensure all elements in the text array are reset to null
        for (String[] row : boardModel.getBoard_text()) {
            for (String text : row) {
                assertNull(text);
            }
        }
    }

    @Test
    void testlineClear() {
        // 초기 보드 모델 생성
        ItemBoardModel boardModel = new ItemBoardModel();

        int Y = 15;
        //한줄로 블록 배치
        for(int X=0; X<boardModel.WIDTH; X++) {
            boardModel.getBoard()[Y][X] = 1;
            boardModel.getBoard_text()[Y][X] = "O";
        }

        int Y_up = 13;
        for(int X=5; X<boardModel.WIDTH; X++) {
            boardModel.getBoard()[Y_up][X] = 1;
            boardModel.getBoard_text()[Y_up][X] = "O";
        }

        boardModel.lineClear();

        //Y = 15인 줄이 지워졌는지 확인
        for(int X=0; X<boardModel.WIDTH; X++) {
            assertEquals(0,boardModel.getBoard()[Y][X]);
            assertNull(boardModel.getBoard_text()[Y][X]);
        }

        //Y_up = 13의 다음 줄인 Y_up+1에 윗즐의 내용이 있는지 확인
        for(int X=0; X<5; X++) {
            assertEquals(0,boardModel.getBoard()[Y][X]);
            assertNull(boardModel.getBoard_text()[Y][X]);
        }
        for(int X=5; X<boardModel.WIDTH; X++) {
            assertEquals(1,boardModel.getBoard()[Y_up+1][X]);
            assertEquals("O",boardModel.getBoard_text()[Y_up+1][X]);
        }
    }

    @Test
    void testlineClear_L_text() {
        // 초기 보드 모델 생성
        ItemBoardModel boardModel = new ItemBoardModel();

        int Y = 15;
        //한줄로 블록 배치
        int X_L = 5;
        boardModel.getBoard()[Y][X_L] = 1;
        boardModel.getBoard_text()[Y][X_L] = "L";

        int Y_up = 13;
        for(int X=5; X<boardModel.WIDTH; X++) {
            boardModel.getBoard()[Y_up][X] = 1;
            boardModel.getBoard_text()[Y_up][X] = "O";
        }

        boardModel.lineClear();

        //L 텍스트가 있는 줄이 지워였는지 확인
        for(int X=0; X<boardModel.WIDTH; X++) {
            assertEquals(0,boardModel.getBoard()[Y][X]);
            assertNull(boardModel.getBoard_text()[Y][X]);
        }

        //Y_up = 13의 다음 줄인 Y_up+1에 윗즐의 내용이 있는지 확인
        for(int X=0; X<5; X++) {
            assertEquals(0,boardModel.getBoard()[Y_up+1][X]);
            assertNull(boardModel.getBoard_text()[Y_up+1][X]);
        }
        for(int X=5; X<boardModel.WIDTH; X++) {
            assertEquals(1,boardModel.getBoard()[Y_up+1][X]);
            assertEquals("O",boardModel.getBoard_text()[Y_up+1][X]);
        }
    }

    @Test
    void testlineClear_S_text() {
        // 초기 보드 모델 생성
        ItemBoardModel boardModel = spy(new ItemBoardModel());
        int Y = 15;
        //한줄로 블록 배치
        for(int X=0; X<boardModel.WIDTH; X++) {
            boardModel.getBoard()[Y][X] = 1;
            boardModel.getBoard_text()[Y][X] = "O";
        }
        int X_S = 5;
        boardModel.getBoard()[Y][X_S] = 1;
        boardModel.getBoard_text()[Y][X_S] = "S";

        boardModel.lineClear();

        // assertTrue(boardModel.isTimerBlock);

        //Y = 15인 줄이 지워졌는지 확인
        for(int X=0; X<boardModel.WIDTH; X++) {
            assertEquals(0,boardModel.getBoard()[Y][X]);
            assertNull(boardModel.getBoard_text()[Y][X]);
        }
    }

    @Test
    void testlineClear_C_text() {
        // 초기 보드 모델 생성
        ItemBoardModel boardModel = new ItemBoardModel();
        int Y = 15;
        //한줄로 블록 배치
        for(int X=0; X<boardModel.WIDTH; X++) {
            boardModel.getBoard()[Y][X] = 1;
            boardModel.getBoard_text()[Y][X] = "O";
        }
        int X_C = 5;
        boardModel.getBoard()[Y][X_C] = 1;
        boardModel.getBoard_text()[Y][X_C] = "C";

        boardModel.lineClear();

        // assertTrue(boardModel.isClearBlock);

        //Y = 15인 줄이 지워졌는지 확인
        for(int X=0; X<boardModel.WIDTH; X++) {
            assertEquals(0,boardModel.getBoard()[Y][X]);
            assertNull(boardModel.getBoard_text()[Y][X]);
        }

    }

    @Test
    void testlineFill() {
        // 초기 보드 모델 생성
        ItemBoardModel boardModel = new ItemBoardModel();

        int Y_F = 15;
        //한줄로 블록 배치
        int X_F = 1;
        boardModel.getBoard()[Y_F][X_F] = 1;
        boardModel.getBoard_text()[Y_F][X_F] = "F";

        boardModel.lineFill();

        //Y = 15의 윗줄은 비어있고, 아랫줄을 내용이 있는지 확인
        for(int Y=0; Y<Y_F; Y++) {
            assertEquals(0,boardModel.getBoard()[Y][X_F]);
            assertNull(boardModel.getBoard_text()[Y][X_F]);
        }
        for(int Y=Y_F; Y<boardModel.HEIGHT; Y++) {
            assertEquals(1,boardModel.getBoard()[Y][X_F]);
            if(Y==Y_F) assertEquals("F",boardModel.getBoard_text()[Y][X_F]);
            else assertEquals("O",boardModel.getBoard_text()[Y][X_F]);
        }


    }

    @Test
    void generateBlock() {
        // given
        Block watingBlock = boardModel.getRandomBlock();
        boardModel.setY(1);
        int beforeBlockCount = boardModel.blockCount;
        boardModel.setNextBlock(watingBlock);

        // when
        boardModel.generateBlock();

        //then
        assertFalse(boardModel.horizonLock);
        assertTrue(boardModel.getCurr() == watingBlock);
        assertTrue(boardModel.getX() == 3);
        assertTrue(boardModel.getY() == 0);
    }

    @Test
    void moveRight() {
        // given
        Block block = new IBlock();
        boardModel.setCurr(block);
        boardModel.setX(3);
        boardModel.setY(0);
        // when
        boardModel.moveRight();
        // then
        assertTrue(boardModel.getX() == 4);
    }

    @Test
    void moveLeft() {
        // given
        Block block = new IBlock();
        boardModel.setCurr(block);
        boardModel.setX(6);
        boardModel.setY(3);
        // when
        boardModel.moveLeft();
        // then
        assertTrue(boardModel.getX() == 5);
    }

    @Test
    void moveDown() {
        // given
        Block block = new WeightBlock();
        boardModel.setCurr(block);
        boardModel.setX(6);
        boardModel.setY(1);
        // when
        boardModel.moveDown();
        // then
        assertTrue(boardModel.getY() == 2);
    }

    @Test
    void moveBottom() {
        Block block = new WeightBlock();
        boardModel.setCurr(block);
        boardModel.setX(6);
        boardModel.setY(1);
        // when
        boardModel.moveBottom();

        // then
        assertTrue(boardModel.getY() == 0);
    }


}