package controller;

import model.SingleMode.BoardModel;
import model.VersusMode.VsBoardModel;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.awt.event.KeyEvent;

import IO.ExportSettings;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;
import view.BoardView;
import view.SidePanelView;

public class VersusBoardControllerP1_KeyTest {

    private BoardModel model;
    private BoardView view;
    private SidePanelView sidePanelView;
    private BoardController controller;
    private VersusBoardController.PlayerKeyListener keyListener;

    private VsBoardModel model_0;
    private VsBoardModel model_1;
    private VersusBoardController VScontroller;

    @Before
    public void setUp() {
        model_0 = Mockito.spy(new VsBoardModel(0));
        model_1 = Mockito.spy(new VsBoardModel(1));
        VScontroller = spy(new VersusBoardController(model_0, model_1));

        model = Mockito.spy(BoardModel.class);
        view = Mockito.spy(BoardView.class);
        sidePanelView = Mockito.spy(SidePanelView.class);
        controller = spy(new BoardController(model, view, sidePanelView));

        keyListener = VScontroller.new PlayerKeyListener();
    }

    @Test
    public void testKeyPressed_MoveDown_NotPaused() {
        ExportSettings.saveSettings("moveDown1P", "Down");

        // Arrange
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_DOWN);
        when(model_0.isPaused()).thenReturn(false);

        // Act
        keyListener.keyPressed(mockKeyEvent);

        // Assert
        verify(model_0, atLeast(1)).moveDown();
        verify(VScontroller).updateBoard();
    }

    @Test
    public void testKeyPressed_MoveRight_NotPaused() {
        ExportSettings.saveSettings("moveRight1P", "Right");

        // Arrange
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_RIGHT);
        when(model_0.isPaused()).thenReturn(false);

        // Act
        keyListener.keyPressed(mockKeyEvent);

        // Assert
        verify(model_0).moveRight();
        verify(VScontroller).updateBoard();
    }

    @Test
    public void testKeyPressed_MoveLeft_NotPaused() {
        ExportSettings.saveSettings("moveLeft1P", "Left");

        // Arrange
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_LEFT);
        when(model_0.isPaused()).thenReturn(false);

        // Act
        keyListener.keyPressed(mockKeyEvent);

        // Assert
        verify(model_0).moveLeft();
        verify(VScontroller).updateBoard();
    }

    @Test
    public void testKeyPressed_Rotate_NotPaused() {
        ExportSettings.saveSettings("rotate1P", "Up");
        // Arrange
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_UP);
        when(model_0.isPaused()).thenReturn(false);

        // Act
        keyListener.keyPressed(mockKeyEvent);

        // Assert
        verify(model_0).moveRotate();
        verify(VScontroller).updateBoard();
    }

    @Test
    public void testKeyPressed_Drop_NotPaused() {
        ExportSettings.saveSettings("drop1P", "Space");
        // Arrange
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_SPACE);
        when(model_0.isPaused()).thenReturn(false);

        // Act
        keyListener.keyPressed(mockKeyEvent);

        // Assert
        verify(model_0).moveBottom();
        verify(VScontroller).updateBoard();
    }

    @Test
    public void testKeyPressed_Drop_NotPaused_P2() {
        ExportSettings.saveSettings("drop2P", "Z");
        // Arrange
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_Z);
        when(model_1.isPaused()).thenReturn(false);

        // Act
        keyListener.keyPressed(mockKeyEvent);

        // Assert
        verify(model_1).moveBottom();
        verify(VScontroller).updateP2Board();
    }

    @Test
    public void testKeyPressed_Pause_NotPaused() {
        // Arrange
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_P);
        when(model_0.isPaused()).thenReturn(false);

        // Act
        keyListener.keyPressed(mockKeyEvent);

        // Assert
        verify(VScontroller).pauseGame();
    }

    //Paused
    @Test
    public void testKeyPressed_DOWN_Paused() {
        // Arrange
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_DOWN);
        when(model_0.isPaused()).thenReturn(true);

        VScontroller.setSelectedOption(2);
        int selectedOption = VScontroller.getSelectedOption();

        //기본 테스트
        // Act
        keyListener.keyPressed(mockKeyEvent);
        // Assert
        assertEquals(selectedOption + 1, VScontroller.getSelectedOption());

        //특수한 상황 테스트
        VScontroller.setSelectedOption(3);
        // Act
        keyListener.keyPressed(mockKeyEvent);
        // Assert
        assertEquals(1, VScontroller.getSelectedOption());
    }
    @Test
    public void testKeyPressed_UP_Paused() {
        // Arrange
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_UP);
        when(model_0.isPaused()).thenReturn(true);

        VScontroller.setSelectedOption(2);
        int selectedOption = VScontroller.getSelectedOption();

        //기본 테스트
        // Act
        keyListener.keyPressed(mockKeyEvent);
        // Assert
        assertEquals(selectedOption - 1, VScontroller.getSelectedOption());

        //특수한 상황 테스트
        VScontroller.setSelectedOption(1);
        // Act
        keyListener.keyPressed(mockKeyEvent);
        // Assert
        assertEquals(3, VScontroller.getSelectedOption());
    }
    @Test
    public void testKeyPressed_ESCAPE_Paused() {
        // Arrange
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_ESCAPE);
        when(model_0.isPaused()).thenReturn(true);

        // Act
        keyListener.keyPressed(mockKeyEvent);

        // Assert
        verify(VScontroller).resumeGame();
    }
    @Test
    public void testKeyPressed_ENTER_Paused() {

        // Arrange
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_ENTER);
        when(model_0.isPaused()).thenReturn(true);

        //controller.selectedOption이 1인 경우
        VScontroller.setSelectedOption(1);
        keyListener.keyPressed(mockKeyEvent);

        //controller.selectedOption이 2인 경우
        VScontroller.setSelectedOption(2);
        keyListener.keyPressed(mockKeyEvent);
        verify(VScontroller).resumeGame();

        //controller.selectedOption이 2인 경우
        VScontroller.setSelectedOption(3);
        keyListener.keyPressed(mockKeyEvent);
        verify(VScontroller).gameExit();
    }

}