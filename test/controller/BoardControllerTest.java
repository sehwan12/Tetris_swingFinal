package controller;

import model.SingleMode.BoardModel;
import model.blocks.Block;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import view.BoardView;
import view.SidePanelView;

import java.awt.*;

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
}

