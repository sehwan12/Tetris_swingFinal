package controller;

import model.SingleMode.BoardModel;
import model.VersusMode.VsBoardModel;
import model.VersusMode.VsItemBoardModel;
import model.VersusMode.VsTimeBoardModel;
import model.blocks.Block;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import view.BoardView;
import view.SidePanelView;
import view.VsBoardView;

import java.awt.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class VersusBoardControllerTest {

    private VsBoardModel model_0;
    private VsBoardModel model_1;
    private VersusBoardController controller;

    @Before
    public void setUp() {
        model_0 = Mockito.spy(new VsBoardModel(0));
        model_1 = Mockito.spy(new VsBoardModel(1));
        controller = Mockito.spy(new VersusBoardController(model_0, model_1));
    }

    @Test
    public void testPauseAndResumeGame() {
        // Initially not paused
        assertFalse(model_0.isPaused());
        assertFalse(model_1.isPaused());

        // Pause the game
        controller.pauseGame();
        assertTrue(model_0.isPaused());
        assertTrue(model_1.isPaused());

        // Resume the game
        controller.resumeGame();
        assertFalse(model_0.isPaused());
        assertFalse(model_1.isPaused());
    }

    @Test
    public void notifyAttack_Player0_VsBoardModel() {
        // Given
        VsBoardModel p1Model = new VsBoardModel(0);
        VsBoardModel p2Model = new VsBoardModel(1);
        VersusBoardController controller = new VersusBoardController(p1Model, p2Model);

        controller.notifyAttck(0);
        assertEquals(p1Model.getAttackBlock(), p2Model.getDefenseBlock());

        controller.notifyAttck(1);
        assertEquals(p2Model.getAttackBlock(), p1Model.getDefenseBlock());
    }

    @Test
    public void notifyAttack_Player0_VsItemBoardModel() {
        // Given
        VsItemBoardModel p1Model = new VsItemBoardModel(0);
        VsItemBoardModel p2Model = new VsItemBoardModel(1);
        VersusBoardController controller = new VersusBoardController(p1Model, p2Model);

        controller.notifyAttck(0);
        assertEquals(p1Model.getAttackBlock(), p2Model.getDefenseBlock());

        controller.notifyAttck(1);
        assertEquals(p2Model.getAttackBlock(), p1Model.getDefenseBlock());
    }

    @Test
    public void testNotifyUpdateBoard_PlayerTypeZero() {
        // Arrange
        VersusBoardController controller = new VersusBoardController();
        VersusBoardController spyController = Mockito.spy(controller);

        // Act
        spyController.notifyUpdateBoard(0);

        // Assert
        verify(spyController, times(1)).updateBoard();
        verify(spyController, never()).updateP2Board();
    }

    @Test
    public void testNotifyUpdateBoard_PlayerTypeNonZero() {
        // Act
        controller.notifyUpdateBoard(1);

        // Assert
        verify(controller, never()).updateBoard();
        verify(controller, times(1)).updateP2Board();
    }

    @Test
    public void testNotifyUpdateBoard_PlayerTypeNonZero_() {
        // Act
        controller.notifyUpdateBoard(0);

        // Assert
        verify(controller, never()).updateP2Board();
        verify(controller, times(1)).updateBoard();
    }

}