package controller.OutGame;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.OutGame.VersusEnterView;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VersusEnterControllerTest {

    private VersusEnterController versusEnterController;

    @BeforeEach
    public void setUp() {
        versusEnterController = VersusEnterController.getInstance();
        versusEnterController.initFrame();
    }

    @Test
    public void testGetInstance() {
        assertNotNull(versusEnterController);
        VersusEnterController secondInstance = VersusEnterController.getInstance();
        assertEquals(versusEnterController, secondInstance, "Instances should be the same");
    }

    @Test
    public void testInitFrame() {
        assertNotNull(versusEnterController.view, "VersusEnterView should be initialized");
    }

    @Test
    public void testInitFocus() {
        assertNotNull(versusEnterController.view.getRootPane().getInputMap(), "InputMap should not be null");
        assertNotNull(versusEnterController.view.getRootPane().getActionMap(), "ActionMap should not be null");
    }

    @Test
    public void testEnterMenuCase0() {
        // Mock the view
        VersusEnterView mockedView = mock(VersusEnterView.class);
        versusEnterController.view = mockedView;

        versusEnterController.enterMenu(0);

        // Verify that the view methods are called appropriately
        verify(mockedView).setVisible(false);
        verify(mockedView).dispose();
        assertNull(versusEnterController.view);
    }

    @Test
    public void testEnterMenuCase1() {
        // Mock the view
        VersusEnterView mockedView = mock(VersusEnterView.class);
        versusEnterController.view = mockedView;

        versusEnterController.enterMenu(1);

        // Verify that the view methods are called appropriately
        verify(mockedView).setVisible(false);
        verify(mockedView).dispose();
        assertNull(versusEnterController.view);
    }

    @Test
    public void testEnterMenuCase2() {
        // Mock the view
        VersusEnterView mockedView = mock(VersusEnterView.class);
        versusEnterController.view = mockedView;

        versusEnterController.enterMenu(2);

        // Verify that the view methods are called appropriately
        verify(mockedView).setVisible(false);
        verify(mockedView).dispose();
        assertNull(versusEnterController.view);
    }
}