package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import model.OutGame.OutGameModel;
import model.VersusMode.BlockChunk;

public class DefenseBlockView extends JPanel {

    private int[][] defense_board;
    private String[][] defense_board_text;
    protected Color[] defense_board_color;
    private static final int WIDTH = 10;
    private static final int HEIGHT = 11;

    public static final char BORDER_CHAR = 'X';
    private SimpleAttributeSet styleSet;
    private JTextPane DefenseBlockPiece;

    private Color DefenseBlockColor = new Color(180, 180, 180);

    public DefenseBlockView() {
        DefenseBlockPiece = new JTextPane();
        defense_board = new int[HEIGHT][WIDTH];
        defense_board_text = new String[HEIGHT][WIDTH];
        defense_board_color = new Color[(HEIGHT+2)*(WIDTH+2+1)];

        this.setLayout(new BorderLayout());
        DefenseBlockPiece.setLayout(new BorderLayout());
        DefenseBlockPiece.setBorder(new LineBorder(Color.BLACK));
        //해상도에 따라 크기 변경
        DefenseBlockPiece.setPreferredSize(new Dimension(OutGameModel.getResX()/5, 120));
        this.add(DefenseBlockPiece);
        DefenseBlockPiece.setBackground(Color.BLACK);

        styleSet = new SimpleAttributeSet();
        //해상도에 따라 폰트 변경
        StyleConstants.setFontSize(styleSet, 10*(OutGameModel.getResX()/5)/80);
        StyleConstants.setFontFamily(styleSet, "Courier New");
        StyleConstants.setBold(styleSet, true);
        StyleConstants.setForeground(styleSet, Color.WHITE);
        StyleConstants.setAlignment(styleSet, StyleConstants.ALIGN_CENTER);
    }

    public void placeblock(BlockChunk defenseBlockChunk) {
        defense_board = new int[HEIGHT][WIDTH];
        defense_board_text = new String[HEIGHT][WIDTH];
        defense_board_color = new Color[(HEIGHT+2)*(WIDTH+2+1)];

        for (int j = 0; j < defenseBlockChunk.grayLinesNum; j++) {
            for (int i = 0; i < WIDTH; i++) {
                if (defenseBlockChunk.block[j][i] >= 1) {
                    defense_board[HEIGHT - 1 - j][i] = defenseBlockChunk.block[j][i];
                    defense_board_color[(HEIGHT - j) * (WIDTH + 1) + i + 1] = DefenseBlockColor;
                    defense_board_text[HEIGHT - 1 - j][i] = "O";
                }
            }
        }
    }

    public void drawBoard(BlockChunk defenseBlockChunk) {
        placeblock(defenseBlockChunk);

        StringBuffer sb = new StringBuffer();

        // Draw top border
        for (int t = 0; t < WIDTH + 2; t++) sb.append(BORDER_CHAR);
        sb.append("\n");

        // Iterate over the board
        for (int i = 0; i < defense_board.length; i++) {
            sb.append(BORDER_CHAR);
            for (int j = 0; j < defense_board[i].length; j++) {
                if (defense_board[i][j] == 1) {
                    sb.append(defense_board_text[i][j]);
                }
                else {
                    sb.append(" ");
                }
            }
            sb.append(BORDER_CHAR);
            sb.append("\n");
        }

        // Draw bottom border
        for (int t = 0; t < WIDTH + 2; t++) sb.append(BORDER_CHAR);

        // Update the text of the JTextPane with the buffer content
        DefenseBlockPiece.setText(sb.toString());

        StyledDocument doc = DefenseBlockPiece.getStyledDocument();

        doc.setParagraphAttributes(0, doc.getLength(), styleSet, false);

        //defense_board_color 에 저장된 값을 바탕으로 글자 색 칠하기
        for(int i = 0; i< defense_board_color.length; i++) {
            if (defense_board_color[i] != null) {
                SimpleAttributeSet styles1 = new SimpleAttributeSet();
                StyleConstants.setForeground(styles1, defense_board_color[i]);
                doc.setCharacterAttributes(i, 1, styles1, false);

            }
        }

        // BORDER_CHAR 를 짙은 회색으로 그외는 DefenseBlockColor 인 밝은 회색으로
        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            if (c == BORDER_CHAR) {
                SimpleAttributeSet styles2 = new SimpleAttributeSet();
                StyleConstants.setForeground(styles2, new Color(80,80,80));
                doc.setCharacterAttributes(i, 1, styles2, false);
            }
            else{
                SimpleAttributeSet styles3 = new SimpleAttributeSet();
                StyleConstants.setForeground(styles3, DefenseBlockColor);
                doc.setCharacterAttributes(i, 1, styles3, false);
            }
        }

        // 문자열의 모든 위치에 같은 폰트를 적용해서 모든 크기를 균일하게
        for (int i = 0; i < sb.length(); i++) {
            SimpleAttributeSet styles2 = new SimpleAttributeSet();
            StyleConstants.setFontFamily(styles2, "Courier New");
            doc.setCharacterAttributes(i, 1, styles2, false);
        }

        DefenseBlockPiece.setStyledDocument(doc);
    }

}