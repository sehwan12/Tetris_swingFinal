package view;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Color;


import model.blocks.item.Alias.LineClearBlock;
import model.blocks.item.Alias.LineFillBlock;
import model.blocks.item.Alias.TimerBlock;
import org.junit.jupiter.api.*;

import model.blocks.Block;
import view.SidePanelView;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import model.blocks.IBlock;

public class SidePanelViewTest {
    private SidePanelView sidePanelView;

    @BeforeEach
    public void setUp() {
        sidePanelView = new SidePanelView();
    }

    @Test
    public void testSetScoreText() {
        int score = 100;
        sidePanelView.setScoreText(score);
        assertEquals("Score: " + score, sidePanelView.getScoreText().getText());
    }

    @Test
    public void testPaintNextPiece() {
        Block nextBlock = new IBlock(true, 0);
        sidePanelView.paintNextPiece(nextBlock);
        assertNotNull(sidePanelView.getNextPiece());
    }


    @Test
    public void testDrawBoard() {
        // Given
        Block nextBlock = new IBlock(true, 0);

        // When
        sidePanelView.drawBoard(nextBlock);
        JTextPane nextPiece = sidePanelView.getNextPiece();
        StyledDocument doc = nextPiece.getStyledDocument();
        SimpleAttributeSet styles = new SimpleAttributeSet();

        // Then
        assertNotNull(nextPiece);
        String text = nextPiece.getText();

        // Top border 확인
        assertTrue(text.startsWith(SidePanelView.BORDER_CHAR + " "));
        assertTrue(text.contains("\n" + SidePanelView.BORDER_CHAR + " "));
        assertTrue(text.endsWith(SidePanelView.BORDER_CHAR + ""));

        // Bottom border 확인
        assertTrue(text.endsWith(SidePanelView.BORDER_CHAR + ""));

        // 블록이 올바르게 그려졌는지 확인
        assertTrue(text.contains(" ")); // 보드 내부에 공백이 있는지 확인

        // 블록 종류에 따라 올바른 문자가 표시되는지 확인
        nextBlock = new LineClearBlock();
        sidePanelView.drawBoard(nextBlock);
        text = nextPiece.getText();
        assertTrue(text.contains("L")); // LineClearBlock일 때 'L' 문자가 있는지 확인

        nextBlock = new LineFillBlock();
        sidePanelView.drawBoard(nextBlock);
        text = nextPiece.getText();
        assertTrue(text.contains("F")); // LineFillBlock일 때 'F' 문자가 있는지 확인

        nextBlock = new TimerBlock();
        sidePanelView.drawBoard(nextBlock);
        text = nextPiece.getText();
        assertTrue(text.contains("S")); // TimerBlock일 때 'S' 문자가 있는지 확인

        // 텍스트 스타일 확인
        StyleConstants.setForeground(styles, nextBlock.getColor());
        StyleConstants.setFontFamily(styles, "Courier New");
        assertTrue(doc.getLength() > 0); // 스타일이 적용된 문자가 있는지 확인
    }
}