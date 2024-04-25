package model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import model.blocks.Block;
import model.blocks.IBlock;
import model.blocks.OBlock;
import org.junit.jupiter.api.Test;

public class BoardModelTest_simple {
    // setPaused(boolean paused)
    // updateScore(int a)
    // updateTimer(int a)
    // checkForScore()
    // generateBlock()
    // moveDown()
    // moveBottom()
    // moveRight()
    // moveLeft()
    // moveRotate()
    // 비교적 간단한 메소드 테스트
    @Test
    public void testSetPaused() {
        // Given
        BoardModel boardModel = new BoardModel();

        // When
        boardModel.setPaused(true);

        // Then
        assertTrue(boardModel.isPaused()); // 일시 정지되어 있어야 함

        // When
        boardModel.setPaused(false);

        // Then
        assertFalse(boardModel.isPaused()); // 일시 정지되어 있지 않아야 함
    }

    @Test
    void testUpdateScore() {
        // Given
        BoardModel boardModel = new BoardModel();
        int initialScore = boardModel.getTotalscore();

        // When & Then
        for (int i = 0; i <= 3; i++) {
            switch (i) {
                case 0:
                    boardModel.updateScore(i);
                    assertEquals(initialScore + 1, boardModel.getTotalscore());
                    initialScore=initialScore + 1;
                    break;
                case 1:
                    boardModel.updateScore(i);
                    assertEquals(initialScore + 2, boardModel.getTotalscore());
                    initialScore=initialScore + 2;
                    break;
                case 2:
                    boardModel.updateScore(i);
                    assertEquals(initialScore - 5, boardModel.getTotalscore());
                    initialScore=initialScore - 5;
                    break;
                case 3:
                    boardModel.updateScore(i);
                    assertEquals(initialScore + 20, boardModel.getTotalscore());
                    initialScore=initialScore - 20;
                    break;
            }
        }
    }

    @Test
    public void testUpdateTimer() {
        // Given
        BoardModel boardModel = new BoardModel();
        // When
        boardModel.updateTimer(1); // 예제에서는 1을 넣었으므로, 해당 값을 테스트
        // Then
        assertEquals(720, boardModel.initInterval); // 타이머가 예상대로 업데이트되었는지 확인
    }

    @Test
    public void testCheckForScore_isDowned_true() {
        // Given
        BoardModel boardModel = new BoardModel();
        int initialScore;

        // When
        initialScore = boardModel.getTotalscore();
        boardModel.setDowned(true);
        boardModel.checkForScore(); // checkForScore 메서드 호출

        // Then
        assertEquals(initialScore + 20, boardModel.getTotalscore()); // 점수가 20 증가했는지 확인
    }

    @Test
    public void testCheckForScore_isDowned_false() {
        // Given
        BoardModel boardModel = new BoardModel();
        int initialScore;

        // When
        initialScore = boardModel.getTotalscore();
        boardModel.setDowned(false);
        boardModel.checkForScore(); // checkForScore 메서드 호출

        // Then
        assertEquals(initialScore - 5 + 20, boardModel.getTotalscore()); // 점수가 5 감소했는지 확인
    }

    @Test
    void testGenerateBlock() {
        // Given
        BoardModel boardModel = new BoardModel();
        int initialBlockCount = boardModel.blockCount;

        // When
        boardModel.setY(1);
        boardModel.generateBlock();

        // Then
        assertEquals(initialBlockCount + 1, boardModel.blockCount); // 블록 수가 1 증가했는지 확인
        assertNotNull(boardModel.curr); // 현재 블록이 null이 아닌지 확인
        assertNotNull(boardModel.getNextBlock()); // 다음 블록이 null이 아닌지 확인
        assertNotNull(boardModel.getNextBlock().getText()); // 다음 블록에 텍스트가 할당되었는지 확인
    }

    @Test
    void testMoveDown_BlockCanMoveDown_ShouldIncreaseY() {
        // Given
        BoardModel boardModel = new BoardModel();
        int initialY = boardModel.y;

        // When
        boardModel.moveDown();

        // Then
        // 아래로 이동 가능한 상황에서는 Y 좌표가 1 증가해야 합니다.
        assertEquals(initialY + 1, boardModel.y);
    }

    @Test
    void testMoveDown_BlockCannotMoveDown_ShouldPlaceBlockAndPerformOtherActions() {
        // Given
        BoardModel boardModel = spy(new BoardModel());

        //!collisionCheck(0, 1)을 0으로 대체하기
        when(boardModel.collisionCheck(0, 1)).thenReturn(true);

        int initialY = boardModel.y;

        // When
        boardModel.moveDown();

        // Then
        // 아래로 이동 불가능한 상황에서는 Y 좌표가 그대로여야 합니다.
        assertEquals(initialY, boardModel.y);
        // 블록이 배치되고 다른 동작들이 수행되었는지 확인합니다.
        verify(boardModel).placeBlock();
        verify(boardModel).checkForScore();
        verify(boardModel).startLineClearAnimation();
    }

    @Test
    void testMoveBottom() {
        // Mock 객체 생성
        BoardModel boardModel = spy(new BoardModel());

        // collisionCheck() 메서드가 처음에는 false를 반환하고, 이후 true를 반환하도록 설정
        when(boardModel.collisionCheck(0, 1)).thenReturn(true).thenReturn(true);

        // moveBottom() 호출
        boardModel.moveBottom();

        verify(boardModel).placeBlock();
        verify(boardModel).checkForScore();
        verify(boardModel).startLineClearAnimation();
    }

    @Test
    void testMoveRight() {
        // Mock 객체 생성
        BoardModel boardModel = spy(new BoardModel());
        int initial_x=boardModel.getX();

        // collisionCheck() 메서드가 false를 반환하도록 설정
        when(boardModel.collisionCheck(1, 0)).thenReturn(false);

        // moveRight() 호출
        boardModel.moveRight();

        verify(boardModel).placeBlock();
        assertEquals(initial_x+1,boardModel.getX());
    }

    @Test
    void testMoveLeft() {
        // Mock 객체 생성
        BoardModel boardModel = spy(new BoardModel());
        int initial_x=boardModel.getX();

        // collisionCheck() 메서드가 false를 반환하도록 설정
        when(boardModel.collisionCheck(1, 0)).thenReturn(false);

        // moveRight() 호출
        boardModel.moveLeft();

        verify(boardModel).placeBlock();
        assertEquals(initial_x-1,boardModel.getX());
    }

    @Test
    void testMoveRotate() {
        // 초기 상태 설정, IBlock 으로 테스트
        BoardModel boardModel = spy(new BoardModel());
        boardModel.setCurr(new IBlock(true,0));
        boardModel.setY(10); // 맨위에서 블럭 돌리면 충돌 일어나므로 위치를 가운데로 옮기기

        // 현재 블록의 실제 인스턴스 가져오기
        Block realBlock = boardModel.curr;

        // 충돌이 발생하지 않도록 설정
        when(boardModel.collisionCheck(0, 0)).thenReturn(false);

        // 현재 위치 및 회전 상태 저장
        int initialX = boardModel.getX();
        int initialY = boardModel.getY();
        int initialRotation = realBlock.getRotate_status();

        // moveRotate() 호출
        boardModel.moveRotate();

        // 회전 후에 블록의 좌표가 변경되었는지 확인
        assertNotEquals(initialX, boardModel.getX());
        assertNotEquals(initialY, boardModel.getY());

        // 블록의 회전 상태가 변경되었는지 확인
        assertNotEquals(initialRotation, realBlock.getRotate_status());

        //////////////////////////////////////////////////////////////////////////

        // 초기 상태 설정, 이번엔 OBlock 으로 테스트
        boardModel.setCurr(new OBlock(true,0));
        boardModel.setY(10);// 맨위에서 블럭 돌리면 충돌 일어나므로 위치를 가운데로 옮기기

        // 현재 블록의 실제 인스턴스 가져오기
        realBlock = boardModel.curr;

        // 충돌이 발생하지 않도록 설정
        when(boardModel.collisionCheck(0, 0)).thenReturn(false);

        // 현재 위치 및 회전 상태 저장
        initialX = boardModel.getX();
        initialY = boardModel.getY();
        initialRotation = realBlock.getRotate_status();

        // moveRotate() 호출
        boardModel.moveRotate();

        // O블럭은 회전 후에 블록의 좌표가 변경되지 않아야함
        assertEquals(initialX, boardModel.getX());
        assertEquals(initialY, boardModel.getY());

        // 블록의 회전 상태가 변경되었는지 확인
        assertNotEquals(initialRotation, realBlock.getRotate_status());
    }

    @Test
    void testMoveRotate_collisionCheck_true() {
        // 초기 상태 설정, IBlock 으로 테스트
        BoardModel boardModel = spy(new BoardModel());
        boardModel.setCurr(new IBlock(true, 0));
        boardModel.setY(10);// 맨위에서 블럭 돌리면 충돌 일어나므로 위치를 가운데로 옮기기

        // 현재 블록의 실제 인스턴스 가져오기
        Block realBlock = boardModel.curr;

        System.out.println(realBlock);

        // 충돌이 발생하도록 설정
        when(boardModel.collisionCheck(0, 0)).thenReturn(true);

        // 현재 위치 및 회전 상태 저장
        int initialX = boardModel.getX();
        int initialY = boardModel.getY();
        int initialRotation = realBlock.getRotate_status();

        // moveRotate() 호출
        boardModel.moveRotate();

        // 회전 후에 블록의 좌표가 변경되지 않아야함
        assertEquals(initialX, boardModel.getX());
        assertEquals(initialY, boardModel.getY());

        // 블록의 회전 상태가 변경되지 않는지 확인
        assertEquals(initialRotation, realBlock.getRotate_status());
    }


}