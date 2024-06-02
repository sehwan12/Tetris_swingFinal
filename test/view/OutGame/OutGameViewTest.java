package view.OutGame;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;

import IO.ImportSettings;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import view.OutGame.OutGameView;

import java.awt.*;

public class OutGameViewTest {

    private OutGameView outGameView;
    @Mock
    private ImportSettings importSettingsMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);  // Initialize mocks annotated with @Mock
        outGameView = new OutGameView();
    }

    @Test
    public void testInitPanel() {
        outGameView.initPanel();
        assertNotNull(outGameView.mainPanel);
        assertTrue(outGameView.mainPanel.getLayout() instanceof BorderLayout);
    }

    @Test
    public void testInitTitle() {
        outGameView.initTitle();
        assertNotNull(outGameView.titleLabel);
        assertEquals("", outGameView.titleLabel.getText());
        assertEquals(new Font("malgun gothic", Font.BOLD, 20), outGameView.titleLabel.getFont());
    }

    @Test
    public void testInitTitle2() {
        String[] buildString = {"Test"};
        outGameView.initTitle(buildString, 0);
        assertNotNull(outGameView.titleLabel);
        assertEquals("Test", outGameView.titleLabel.getText());
        assertEquals(new Font("malgun gothic", Font.BOLD, 20), outGameView.titleLabel.getFont());
    }

    /*@Test
    public void testInitWindow() {
        // Correct use of Mockito's when method
        when(importSettingsMock.getSetting("ResolutionSizeX")).thenReturn("800");
        when(importSettingsMock.getSetting("ResolutionSizeY")).thenReturn("600");

        outGameView.setImportSettings(importSettingsMock);  // Ensure the settings mock is used in outGameView
        outGameView.initWindow();

        assertEquals(800, outGameView.getSize().width);
        assertEquals(600, outGameView.getSize().height);
        assertFalse(outGameView.isResizable());
        assertEquals(JFrame.EXIT_ON_CLOSE, outGameView.getDefaultCloseOperation());
    }*/

    @Test
    public void testShowWarning() {
        // Testing GUI elements like JOptionPane should be done with caution in unit tests
        // For demonstration purposes only, not a best practice
        String message = "Test Warning";
        outGameView.showWarning(message);
        // Verify interactions or use a GUI testing framework in practice
    }

    @Test
    public void testShowQuestion() {
        // Similar handling as `testShowWarning`
        String message = "Test Question";
        outGameView.showQuestion(message);
        // Verification of GUI components should be done with proper GUI testing tools
    }
}
