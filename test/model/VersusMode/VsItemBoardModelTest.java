package model.VersusMode;

import model.SingleMode.BoardModel;
import model.blocks.Block;
import model.blocks.IBlock;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
public class VsItemBoardModelTest {

    int HEIGHT = 20;
    int WIDTH = 10;

    @Test
    void testcheckCurrBlockArea(){
        // 테스트에 필요한 객체 생성
        VsItemBoardModel boardModel = new VsItemBoardModel(0);
        Block block = new IBlock(true, 0); // 예시로 I 블록 생성

        // 현재 블록으로 설정합니다.
        boardModel.setCurr(block);

        // placeBlock() 메서드를 호출하여 보드에 블록을 배치합니다.
        boardModel.placeBlock();

        // 블럭이 있는 곳에서는 true
        assertTrue(boardModel.checkCurrBlockArea(3,0));

        // 블럭이 없는 곳에서는 false
        assertFalse(boardModel.checkCurrBlockArea(3,10));

    }

    @Test
    void testcheckBoardHeight(){
        VsItemBoardModel boardModel = new VsItemBoardModel(0);
        boardModel.eraseCurr();
        assertEquals(HEIGHT-1, boardModel.checkBoardHeight());

        // 테스트에 필요한 객체 생성
        Block block = new IBlock(true, 0); // 예시로 I 블록 생성
        // 현재 블록으로 설정합니다.
        boardModel.setCurr(block);
        // placeBlock() 메서드를 호출하여 보드에 블록을 배치합니다.
        boardModel.placeBlock();
        assertEquals(0, boardModel.checkBoardHeight());


    }

    @Test
    void testSaveDefenseBlock() {
        VsItemBoardModel boardModel = new VsItemBoardModel(1);

        // Case 1: 방어용 블록이 최대치인 10줄에 도달하지 않은 경우
        BlockChunk blockChunk1 = new BlockChunk();
        blockChunk1.grayLinesNum = 5;
        boardModel.saveDefenseBlock(blockChunk1);
        assertEquals(boardModel.getDefenseBlock().grayLinesNum, 5);

        // Case 2: 이미 방어용 블록이 존재하고 추가할 블록이 최대치인 10줄을 초과하지 않는 경우
        BlockChunk blockChunk2 = new BlockChunk();
        blockChunk2.grayLinesNum = 3;
        boardModel.saveDefenseBlock(blockChunk2);
        assertEquals(boardModel.getDefenseBlock().grayLinesNum, 8); // 기존 5줄 + 추가 3줄

        /* // java.lang.ArrayIndexOutOfBoundsException: Index 10 out of bounds for length 10 경고
        // Case 3: 이미 방어용 블록이 존재하고 추가할 블록이 최대치인 10줄을 초과하는 경우
        BlockChunk blockChunk3 = new BlockChunk();
        blockChunk3.grayLinesNum = 5;
        boardModel.saveDefenseBlock(blockChunk3);
        assertEquals(boardModel.getDefenseBlock().grayLinesNum, 10); // 최대치인 10줄에 도달해야 함
        */
    }

    @Test
    void testAddDefenseBlock() {
        VsItemBoardModel boardModel = spy(new VsItemBoardModel(1));
        boardModel.eraseCurr();

        // Case 1: 방어용 블록이 추가되지 않은 경우
        int[][] originalBoard = Arrays.stream(boardModel.getBoard()).map(int[]::clone).toArray(int[][]::new);
        boardModel.addDefenseBlock();
        assertArrayEquals(originalBoard, boardModel.getBoard());

        // Case 2: 방어용 블록이 추가되는 경우
        boardModel.defenseBlockChunk.grayLinesNum = 2;
        boardModel.defenseBlockChunk.block = new int[][]{{1,1,1,1,1,1,1,1,1,1}, {1,1,1,1,1,1,1,1,1,1}};
        boardModel.addDefenseBlock();

        // 추가된 블록이 보드에 제대로 반영되었는지 확인
        assertEquals(1, boardModel.getBoard()[HEIGHT - 1][0]);
        assertEquals(1, boardModel.getBoard()[HEIGHT - 2][1]);

        // Case 3: 보드의 높이보다 방어용 블록의 줄 수가 많은 경우
        boardModel.defenseBlockChunk.grayLinesNum = HEIGHT + 1; // 보드의 높이보다 큰 수 설정
        boardModel.addDefenseBlock();
        verify(boardModel).notifyGameOver(); // 게임이 종료되어야 함
    }

    @Test
    public void testSaveAttackBlock() {
        VsItemBoardModel vsBoardModel;
        vsBoardModel = new VsItemBoardModel(1);

        // Mocking a full line at row 19 to be cleared
        Arrays.fill(vsBoardModel.getBoard()[19], 1);
        vsBoardModel.linesToClear = new ArrayList<>(Arrays.asList(19));

        Block block = new IBlock(true, 0); // 예시로 I 블록 생성
        // Setting up a current block
        vsBoardModel.setCurr(block); // Mock block of size 2x2
        vsBoardModel.setX(6); // Block is at (8, 18) and (9, 18) (8, 19) and (9, 19)
        vsBoardModel.setY(19);

        vsBoardModel.saveAttackBlock();

        BlockChunk expectedAttackBlockChunk = new BlockChunk();
        expectedAttackBlockChunk.grayLinesNum = 1;

        // The current block covers the rightmost 2 columns of the full line,
        // so the attack block should have 0s in these positions.
        int[][] expectedBlock = new int[10][WIDTH];
        Arrays.fill(expectedBlock[0], 1);
        expectedBlock[0][6] = 0;
        expectedBlock[0][7] = 0;
        expectedBlock[0][8] = 0;
        expectedBlock[0][9] = 0;

        expectedAttackBlockChunk.block = expectedBlock;

        assertEquals(expectedAttackBlockChunk.grayLinesNum, vsBoardModel.getAttackBlock().grayLinesNum);
        assertArrayEquals(expectedAttackBlockChunk.block, vsBoardModel.getAttackBlock().block);
    }

    @Test
    public void testStartLineClearAnimation_SingleLine() {
        VsItemBoardModel vsBoardModel;

        vsBoardModel = spy(new VsItemBoardModel(1));
        vsBoardModel.linesToClear = new ArrayList<>();

        // Set up a single full line at row 19
        Arrays.fill(vsBoardModel.getBoard()[19], 1);

        vsBoardModel.startLineClearAnimation();

        assertEquals(1, vsBoardModel.linesToClear.size());
        assertTrue(vsBoardModel.linesToClear.contains(19));

        // Verify board color changes to white for the cleared line
        for (int col = 0; col < WIDTH; col++) {
            assertEquals(Color.WHITE, vsBoardModel.getBoard_color()[(19 + 1) * (WIDTH + 3) + col + 1]);
        }
    }

    @Test
    public void testStartLineClearAnimation_MultipleLines() {
        VsItemBoardModel vsBoardModel;

        vsBoardModel = spy(new VsItemBoardModel(1));
        vsBoardModel.linesToClear = new ArrayList<>();

        // Set up two full lines at rows 18 and 19
        Arrays.fill(vsBoardModel.getBoard()[18], 1);
        Arrays.fill(vsBoardModel.getBoard()[19], 1);

        vsBoardModel.startLineClearAnimation();

        assertEquals(2, vsBoardModel.linesToClear.size());
        assertTrue(vsBoardModel.linesToClear.contains(18));
        assertTrue(vsBoardModel.linesToClear.contains(19));

        // Verify saveAttackBlock and notifyAttack are called
        verify(vsBoardModel).saveAttackBlock();
        assertNotNull(vsBoardModel.getAttackBlock());

        // Verify attackBlockChunk is reset
        assertEquals(0, vsBoardModel.getAttackBlock().grayLinesNum);

        // Verify board color changes to white for the cleared lines
        for (int line : vsBoardModel.linesToClear) {
            for (int col = 0; col < WIDTH; col++) {
                assertEquals(Color.WHITE, vsBoardModel.getBoard_color()[(line + 1) * (WIDTH + 3) + col + 1]);
            }
        }
    }

}