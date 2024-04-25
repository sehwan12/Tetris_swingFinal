package controller;

import IO.ScoreIO;
import model.BoardModel;
import model.blocks.Block;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import view.BoardView;
import view.SidePanelView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

//by chatGPT3.5
public class BoardControllerTest {

    private BoardModel model;
    private BoardView view;
    private SidePanelView sidePanelView;
    private BoardController controller;


    @Before
    public void setUp() {
        model = Mockito.spy(new BoardModel());
        view = Mockito.spy(new BoardView());
        sidePanelView = Mockito.spy(new SidePanelView());
        controller = new BoardController(model, view, sidePanelView);
    }

    @Test
    public void testPauseAndResumeGame() {
        // Initially not paused
        assertFalse(model.isPaused());

        // Pause the game
        controller.pauseGame();
        assertTrue(model.isPaused());

        // Resume the game
        controller.resumeGame();
        assertFalse(model.isPaused());
    }

    @Test
    public void testUpdateBoard() {
        int HEIGHT = 20;
        int WIDTH = 10;

        // 게임 보드 모델에서 필요한 데이터 설정
        int[][] board = new int[HEIGHT][WIDTH]; // 예시로 10x20 크기의 빈 게임 보드 생성
        Color[] board_color = new Color[(HEIGHT+2)*(WIDTH+2+1)]; // 색상 정보가 필요한 경우에 대한 예시
        String[][] board_text = new String[HEIGHT][WIDTH]; // 텍스트 정보가 필요한 경우에 대한 예시
        when(model.getBoard()).thenReturn(board);
        when(model.getBoard_color()).thenReturn(board_color);
        when(model.getBoard_text()).thenReturn(board_text);

        // updateBoard() 메소드 호출
        controller.updateBoard();

        // 뷰에 대한 호출 확인
        verify(view).drawBoard(eq(board), eq(board_color), eq(board_text));

        // SidePanelView에 대한 호출 확인
        verify(sidePanelView).drawBoard(any(Block.class), any(int.class));
        verify(sidePanelView).setScoreText(any(int.class));
    }

}

