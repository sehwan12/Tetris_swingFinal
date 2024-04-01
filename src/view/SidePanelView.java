package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


// MVC에서 View와 Controller의 상호작용
import controller.BoardController;
import model.blocks.*;

public class SidePanelView extends JPanel {

    private int [][] board;

    private static final int WIDTH = 6;
    private static final int HEIGHT = 6;

    public static final char BORDER_CHAR = ' ';

    private int x1 = 2; //Default Position.
    private int y1= 0;

    private SimpleAttributeSet styleSet;
    private JTextPane nextPiece;
    private JPanel scorePanel;
    private JTextPane scoreText;

    private String scoreString;

    public SidePanelView() {
        nextPiece = new JTextPane();
        scorePanel = new JPanel();
        scoreText = new JTextPane();
        scoreString = "Score: 0";
        board = new int[HEIGHT][WIDTH];

        //sidepanel에 다음블록패널 추가
        this.setLayout(new BorderLayout());
        nextPiece.setLayout(new BorderLayout());
        nextPiece.setBorder(new LineBorder(Color.BLACK));
        nextPiece.setPreferredSize(new Dimension(80,100));
        this.add(nextPiece, BorderLayout.NORTH);
        //점수패널
        scorePanel.setLayout(new BorderLayout());
        scorePanel.setBackground(Color.BLACK);
        // scorePanel.setPreferredSize(new Dimension(20,80));
        this.add(scorePanel,BorderLayout.CENTER);
        // 점수 Text
        scoreText.setLayout(new BorderLayout());
        scoreText.setBorder(new LineBorder(Color.CYAN));
        scoreText.setBackground(Color.BLACK);
        scoreText.setForeground(Color.YELLOW);
        scoreText.setFont(new Font("Courier New", Font.PLAIN,20));
        scoreText.setText(scoreString);
        scoreText.setPreferredSize(new Dimension(10,50));
        scorePanel.add(scoreText,BorderLayout.NORTH);
        //next텍스트 표시
        JLabel nexttext=new JLabel("Next");
        nexttext.setForeground(Color.WHITE);
        nexttext.setFont(new Font("Courier New", Font.PLAIN,20));
        nexttext.setBorder(new EmptyBorder(0,0,15,0));
        nextPiece.add(nexttext, BorderLayout.NORTH);
        nextPiece.setBorder(new EmptyBorder(5, 5, 5, 5));

    }

    public void setScoreText(int score) {
        scoreText.setText("Score: "+score);
    }

    public void paintNextPiece(Block nextBlock){
        nextPiece.removeAll();
        nextPiece.setBackground(Color.BLACK);
        // 왜 2 * 6?
        // board=new int[2][6];
        board=new int[HEIGHT][WIDTH];
        placeblock(nextBlock);
        JLabel nexttext=new JLabel("Next");
        nexttext.setForeground(Color.WHITE);
        nexttext.setFont(new Font("Courier New", Font.PLAIN,20));
        nexttext.setBorder(new EmptyBorder(0,0,10,0));
        nextPiece.add(nexttext, BorderLayout.NORTH);
    }
    // ArrayIndexOutOfBoundsException 오류 발생
    public void placeblock(Block nextBlock){
        StyledDocument doc = nextPiece.getStyledDocument();
        SimpleAttributeSet styles = new SimpleAttributeSet();
        StyleConstants.setForeground(styles, nextBlock.getColor());
        for(int j=0; j<nextBlock.height(); j++) {
            int rows = y1+j == 0 ? 0 : y1+j-1;
            int offset = rows * (WIDTH+3) + x1 + 1;
            doc.setCharacterAttributes(offset, nextBlock.width(), styles, true);
            for(int i=0; i<nextBlock.width(); i++) {
                if (y1+j < board.length && x1+i < board[y1+j].length) {
                    board[y1+j][x1+i] = nextBlock.getShape(i, j);
                }
            }
        }
        nextPiece.setStyledDocument(doc);
    }

    public void drawBoard(Block nextBlock) {
        paintNextPiece(nextBlock);
        placeblock(nextBlock);
        StringBuffer sb = new StringBuffer();
        for(int t=0; t<WIDTH+2; t++) sb.append(BORDER_CHAR);
        sb.append("\n");
        for(int t=0; t<WIDTH+2; t++) sb.append(BORDER_CHAR);
        sb.append("\n");
        for(int i=0; i < board.length; i++) {
            sb.append(BORDER_CHAR);
            for(int j=0; j < board[i].length; j++) {
                if(board[i][j] == 1) {
                    sb.append("O");
                } else {
                    sb.append(" ");
                }
            }
            sb.append(BORDER_CHAR);
            sb.append("\n");
        }

        for(int t=0; t<WIDTH+2; t++) sb.append(BORDER_CHAR);
        nextPiece.setText(sb.toString());
        StyledDocument doc = nextPiece.getStyledDocument();


        SimpleAttributeSet styles = new SimpleAttributeSet();
        StyleConstants.setForeground(styles, nextBlock.getColor()); // 다음 블록 색상 설정
        StyleConstants.setFontFamily(styles, "Courier New");
        doc.setCharacterAttributes(0, doc.getLength(), styles, false); // 모든 문자의 속성을 새로 설정
        nextPiece.setStyledDocument(doc);

    }

}
