package view;

import static org.junit.jupiter.api.Assertions.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import IO.ImportSettings;
import controller.BoardController;
import model.OutGame.OutGameModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import view.OutGame.OutGameView;
import static org.mockito.Mockito.*;

import java.awt.*;

public class BoardViewTest {
    @Test
    void testShowPauseScreen() {
        BoardView boardView = new BoardView();

        // Initially, glassPane should be invisible
        assertFalse(boardView.getGlassPane().isVisible());

        // Show the pause screen
        boardView.showPauseScreen(true);
        assertTrue(boardView.getGlassPane().isVisible());

        // Hide the pause screen again
        boardView.showPauseScreen(false);
        assertFalse(boardView.getGlassPane().isVisible());
    }

    @Test
    void testGlassRepaint() {
        BoardView boardView = new BoardView();

        // Mocking glassPane repaint
        JPanel glassPane = Mockito.spy(boardView.glassPane);
        boardView.glassPane = glassPane;

        // Call glassRepaint method
        boardView.glassRepaint();

        // Verify that repaint method is called
        Mockito.verify(glassPane, Mockito.times(1)).repaint();
    }
    @Test
    void testDrawBoard() {
        BoardView boardView = new BoardView();
        int[][] board = {
                {1, 0, 1},
                {0, 1, 0},
                {1, 1, 1}
        };

        Color[] boardColor = {
                Color.RED, Color.GREEN, Color.BLUE,
                null, Color.YELLOW, null,
                Color.WHITE, Color.ORANGE, Color.PINK
        };

        String[][] boardText = {
                {"A", "B", "C"},
                {"D", "E", "F"},
                {"G", "H", "I"}
        };

        boardView.drawBoard(board, boardColor, boardText);

        JTextPane pane = boardView.pane;

        StyledDocument doc = pane.getStyledDocument();

        // Test if text pane content matches the expected string
        assertEquals("XXXXXXXXXXXX\n" +
                "XA CX\n" +
                "X E X\n" +
                "XGHIX\n" +
                "XXXXXXXXXXXX", pane.getText());

        // Test if styles are correctly applied
        for (int i = 0; i < 9; i++) {
            AttributeSet attributes = doc.getCharacterElement(i).getAttributes();
            if (boardColor[i] != null) {
                Color foreground = StyleConstants.getForeground(attributes);
                assertEquals(boardColor[i], foreground);
            }
            assertEquals("Courier New", StyleConstants.getFontFamily(attributes));
        }
    }

    @Test
    void testConstructor() {
        BoardView boardView = new BoardView();
        // Check JFrame properties
        assertEquals("SeoulTech SE Tetris", boardView.getTitle());
        assertEquals(JFrame.EXIT_ON_CLOSE, boardView.getDefaultCloseOperation());

        // Check GlassPane properties
        assertFalse(boardView.glassPane.isVisible());

        // Check JTextPane properties
        JTextPane pane = boardView.pane;
        assertNotNull(pane);
        assertEquals(Color.BLACK, pane.getBackground());

        // Check if JTextPane has a border
        Border border = pane.getBorder();
        assertNotNull(border);
        assertInstanceOf(CompoundBorder.class, border);
        CompoundBorder compoundBorder = (CompoundBorder) border;
        assertNotNull(compoundBorder.getOutsideBorder());
        assertNotNull(compoundBorder.getInsideBorder());

        // Check StyledDocument default style
        SimpleAttributeSet styleSet = boardView.styleSet;
        assertNotNull(styleSet);
        assertEquals(18 * OutGameModel.getResX() / 400, StyleConstants.getFontSize(styleSet));
        assertEquals("Courier New", StyleConstants.getFontFamily(styleSet));
        assertTrue(StyleConstants.isBold(styleSet));
        assertEquals(Color.WHITE, StyleConstants.getForeground(styleSet));
        assertEquals(StyleConstants.ALIGN_CENTER, StyleConstants.getAlignment(styleSet));
    }
}