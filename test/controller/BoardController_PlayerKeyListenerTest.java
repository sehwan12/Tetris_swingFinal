package controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.KeyEvent;

import IO.ExportSettings;
import model.OutGame.OutGameModel;
import org.junit.Before;
import org.junit.Test;

import model.SingleMode.BoardModel;
import org.mockito.Mockito;
import view.BoardView;
import view.SidePanelView;

public class BoardController_PlayerKeyListenerTest {

    //기본값으로 설정된 키를 기준으로 테스트

    private BoardModel model;
    private BoardView view;
    private SidePanelView sidePanelView;
    private BoardController controller;
    private BoardController.PlayerKeyListener keyListener;

    @Before
    public void setUp() {
        model = Mockito.spy(BoardModel.class);
        view = Mockito.spy(BoardView.class);
        sidePanelView = Mockito.spy(SidePanelView.class);
        controller = spy(new BoardController(model, view, sidePanelView));
        keyListener = controller.new PlayerKeyListener();
    }

    //NotPaused
    @Test
    public void testKeyPressed_MoveDown_NotPaused() {
        ExportSettings.saveSettings("moveDown1P", "Down");

        // Arrange
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_DOWN);
        when(model.isPaused()).thenReturn(false);

        // Act
        keyListener.keyPressed(mockKeyEvent);

        // Assert
        verify(model).moveDown();
        verify(controller).updateBoard();
    }
    @Test
    public void testKeyPressed_MoveRight_NotPaused() {
        ExportSettings.saveSettings("moveRight1P", "Right");

        // Arrange
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_RIGHT);
        when(model.isPaused()).thenReturn(false);

        // Act
        keyListener.keyPressed(mockKeyEvent);

        // Assert
        verify(model).moveRight();
        verify(controller).updateBoard();
    }

    @Test
    public void testKeyPressed_MoveLeft_NotPaused() {
        ExportSettings.saveSettings("moveLeft1P", "Left");

        // Arrange
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_LEFT);
        when(model.isPaused()).thenReturn(false);

        // Act
        keyListener.keyPressed(mockKeyEvent);

        // Assert
        verify(model).moveLeft();
        verify(controller).updateBoard();
    }

    @Test
    public void testKeyPressed_Rotate_NotPaused() {
        ExportSettings.saveSettings("rotate1P", "Up");
        // Arrange
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_UP);
        when(model.isPaused()).thenReturn(false);

        // Act
        keyListener.keyPressed(mockKeyEvent);

        // Assert
        verify(model).moveRotate();
        verify(controller).updateBoard();
    }

    @Test
    public void testKeyPressed_Drop_NotPaused() {
        ExportSettings.saveSettings("drop1P", "Space");
        // Arrange
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_SPACE);
        when(model.isPaused()).thenReturn(false);

        // Act
        keyListener.keyPressed(mockKeyEvent);

        // Assert
        verify(model).moveBottom();
        verify(controller).updateBoard();
    }
    @Test
    public void testKeyPressed_Pause_NotPaused() {
        // Arrange
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_P);
        when(model.isPaused()).thenReturn(false);

        // Act
        keyListener.keyPressed(mockKeyEvent);

        // Assert
        verify(controller).pauseGame();
    }

    //Paused
    @Test
    public void testKeyPressed_DOWN_Paused() {
        // Arrange
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_DOWN);
        when(model.isPaused()).thenReturn(true);

        controller.setSelectedOption(2);
        int selectedOption = controller.getSelectedOption();

        //기본 테스트
        // Act
        keyListener.keyPressed(mockKeyEvent);
        // Assert
        verify(view).glassRepaint();
        assertEquals(selectedOption + 1, controller.getSelectedOption());

        //특수한 상황 테스트
        controller.setSelectedOption(3);
        // Act
        keyListener.keyPressed(mockKeyEvent);
        // Assert
        assertEquals(1, controller.getSelectedOption());
    }
    @Test
    public void testKeyPressed_UP_Paused() {
        // Arrange
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_UP);
        when(model.isPaused()).thenReturn(true);

        controller.setSelectedOption(2);
        int selectedOption = controller.getSelectedOption();

        //기본 테스트
        // Act
        keyListener.keyPressed(mockKeyEvent);
        // Assert
        verify(view).glassRepaint();
        assertEquals(selectedOption - 1, controller.getSelectedOption());

        //특수한 상황 테스트
        controller.setSelectedOption(1);
        // Act
        keyListener.keyPressed(mockKeyEvent);
        // Assert
        assertEquals(3, controller.getSelectedOption());
    }
    @Test
    public void testKeyPressed_ESCAPE_Paused() {
        // Arrange
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_ESCAPE);
        when(model.isPaused()).thenReturn(true);

        // Act
        keyListener.keyPressed(mockKeyEvent);

        // Assert
        verify(controller).resumeGame();
    }
    @Test
    public void testKeyPressed_ENTER_Paused() {

        // Arrange
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_ENTER);
        when(model.isPaused()).thenReturn(true);

        //controller.selectedOption이 1인 경우
        controller.setSelectedOption(1);
        keyListener.keyPressed(mockKeyEvent);
        verify(view).setVisible(false);

        //controller.selectedOption이 2인 경우
        controller.setSelectedOption(2);
        keyListener.keyPressed(mockKeyEvent);
        verify(controller).resumeGame();

        //controller.selectedOption이 2인 경우
        controller.setSelectedOption(3);
        keyListener.keyPressed(mockKeyEvent);
        verify(controller).gameExit();
    }


}
