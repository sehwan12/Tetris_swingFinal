package view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import model.OutGame.OutGameModel;
import org.junit.Before;
import org.junit.jupiter.api.*;
import model.VersusMode.BlockChunk;
import view.DefenseBlockView;
public class DefenseBlockViewTest {
    int WIDTH = 10;
    int HEIGHT = 11;
    @Test
    public void testConstructor() {
        DefenseBlockView defenseBlockView = new DefenseBlockView();

        // Check if DefenseBlockPiece is initialized
        assertNotNull(defenseBlockView.DefenseBlockPiece);

        // Check if defense_board and defense_board_text arrays are initialized with correct dimensions
        assertEquals(HEIGHT, defenseBlockView.defense_board.length);
        assertEquals(WIDTH, defenseBlockView.defense_board[0].length);
        assertEquals(HEIGHT, defenseBlockView.defense_board_text.length);
        assertEquals(WIDTH, defenseBlockView.defense_board_text[0].length);

        // Check if defense_board_color array is initialized with correct length
        assertEquals((HEIGHT + 2) * (WIDTH + 2 + 1), defenseBlockView.defense_board_color.length);

        // Check if layout and border are set correctly
        assertNotNull(defenseBlockView.getLayout());
        assertNotNull(defenseBlockView.DefenseBlockPiece.getLayout());
        assertEquals(LineBorder.class, defenseBlockView.DefenseBlockPiece.getBorder().getClass());

        // Check if preferred size and background color are set correctly
        assertEquals(OutGameModel.getResX() / 5, defenseBlockView.DefenseBlockPiece.getPreferredSize().width);
        assertEquals(120, defenseBlockView.DefenseBlockPiece.getPreferredSize().height);
        assertEquals(Color.BLACK, defenseBlockView.DefenseBlockPiece.getBackground());

        // Check if styleSet attributes are set correctly
        assertEquals("Courier New", StyleConstants.getFontFamily(defenseBlockView.styleSet));
        assertTrue(StyleConstants.isBold(defenseBlockView.styleSet));
        assertEquals(Color.WHITE, StyleConstants.getForeground(defenseBlockView.styleSet));
        assertEquals(StyleConstants.ALIGN_CENTER, StyleConstants.getAlignment(defenseBlockView.styleSet));
    }

    @Test
    public void testPlaceBlock() {
        DefenseBlockView defenseBlockView = new DefenseBlockView();
        BlockChunk defenseBlockChunk = new BlockChunk();
        defenseBlockChunk.grayLinesNum = 2; // Adjust as needed
        // Initialize defenseBlockChunk.block array with values for testing
        defenseBlockChunk.block = new int[][]{{1, 0, 1, 0, 1, 0, 1, 0, 1, 0}, {0, 1, 0, 1, 0, 1, 0, 1, 0, 1}};

        defenseBlockView.placeblock(defenseBlockChunk);

        for (int j = 0; j < HEIGHT; j++) {
            for (int i = 0; i < WIDTH; i++) {
                System.out.print(defenseBlockView.defense_board[j][i]);
            }
            System.out.println();
        }

        // Check if defense_board and defense_board_text are updated correctly
        assertEquals(1, defenseBlockView.defense_board[10][0]);
        assertEquals(1, defenseBlockView.defense_board[10][2]);
        assertEquals("O", defenseBlockView.defense_board_text[10][0]);
        assertEquals("O", defenseBlockView.defense_board_text[10][2]);
    }

    @Test
    public void testDrawBoard() {
        DefenseBlockView defenseBlockView = new DefenseBlockView();
        BlockChunk defenseBlockChunk = new BlockChunk();
        defenseBlockChunk.grayLinesNum = 2; // Adjust as needed
        // Initialize defenseBlockChunk.block array with values for testing
        defenseBlockChunk.block = new int[][] {{1, 0, 1, 0, 1, 0, 1, 0, 1, 0}, {0, 1, 0, 1, 0, 1, 0, 1, 0, 1}};

        defenseBlockView.drawBoard(defenseBlockChunk);

        JTextPane textPane = defenseBlockView.DefenseBlockPiece;
        StyledDocument doc = textPane.getStyledDocument();
        SimpleAttributeSet[] attributeSets = new SimpleAttributeSet[doc.getLength()];

        for (int i = 0; i < doc.getLength(); i++) {
            attributeSets[i] = new SimpleAttributeSet(doc.getCharacterElement(i).getAttributes());
        }

        // Check if top border is drawn correctly
        for (int t = 0; t < WIDTH + 2; t++) {
            assertEquals(DefenseBlockView.BORDER_CHAR, textPane.getText().charAt(t));
            assertEquals(new Color(80,80,80), attributeSets[t].getAttribute(javax.swing.text.StyleConstants.Foreground));
        }
    }
}