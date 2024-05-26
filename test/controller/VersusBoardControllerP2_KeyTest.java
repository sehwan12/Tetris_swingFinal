package controller;

import model.SingleMode.BoardModel;
import model.VersusMode.VsBoardModel;

import static org.mockito.Mockito.*;

import java.awt.event.KeyEvent;

import IO.ExportSettings;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;
import view.BoardView;
import view.SidePanelView;
public class VersusBoardControllerP2_KeyTest {

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
    public void testKeyPressed_MoveDown_NotPaused_P2() {
        ExportSettings.saveSettings("moveDown2P", "S");

        // Arrange
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_S);
        when(model_1.isPaused()).thenReturn(false);

        // Act
        keyListener.keyPressed(mockKeyEvent);

        // Assert
        verify(model_1, atLeast(1)).moveDown();
        verify(VScontroller).updateP2Board();
    }

    @Test
    public void testKeyPressed_MoveRight_NotPaused_P2() {
        ExportSettings.saveSettings("moveRight2P", "D");

        // Arrange
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_D);
        when(model_1.isPaused()).thenReturn(false);

        // Act
        keyListener.keyPressed(mockKeyEvent);

        // Assert
        verify(model_1).moveRight();
        verify(VScontroller).updateP2Board();
    }

    @Test
    public void testKeyPressed_MoveLeft_NotPaused_P2() {
        ExportSettings.saveSettings("moveLeft1P", "A");

        // Arrange
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_A);
        when(model_1.isPaused()).thenReturn(false);

        // Act
        keyListener.keyPressed(mockKeyEvent);

        // Assert
        verify(model_1).moveLeft();
        verify(VScontroller).updateP2Board();
    }

    @Test
    public void testKeyPressed_Rotate_NotPaused_P2() {
        ExportSettings.saveSettings("rotate1P", "W");
        // Arrange
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_W);
        when(model_1.isPaused()).thenReturn(false);

        // Act
        keyListener.keyPressed(mockKeyEvent);

        // Assert
        verify(model_1).moveRotate();
        verify(VScontroller).updateP2Board();
    }

}