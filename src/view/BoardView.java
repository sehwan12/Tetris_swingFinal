package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


// MVC에서 View와 Controller의 상호작용
import controller.BoardController;
import IO.ImportSettings;
import model.OutGameModel;

public class BoardView extends JFrame {
    private BoardController controller; // BoardController 참조 추가

    // board array size
    private static final int WIDTH = 10;
    private static final int HEIGHT = 20;

    public static final char BORDER_CHAR = 'X';

    private JTextPane pane;
    private SimpleAttributeSet styleSet;


    private JPanel glassPane; //게임 정지화면을 나타낼 glassPane

    public BoardView() {
        super("SeoulTech SE Tetris");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setBackground(Color.BLACK);
        setSize(OutGameModel.getResX(),
                OutGameModel.getResY());

        // GlassPane 초기화 by chatGPT
        glassPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();

                // Set the transparency (alpha) value
                float alpha = 0.5f;
                g2d.setColor(new Color(0, 0, 0, (int) (255 * alpha))); // Black color with transparency
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.setFont(new Font("SansSerif", Font.BOLD, 40));

                String[] menuItems = {"일시정지", "메인메뉴", "게임재개", "게임종료"}; //재시작을 게임재개로 수정 3/23
                Color[] menuColors = {new Color(200,200,200), Color.WHITE, Color.WHITE, Color.WHITE};

                for (int i = 0; i < menuItems.length; i++) {
                    // "일시정지" 메뉴 이후의 메뉴들에 대해 폰트 크기를 작게 설정
                    if (i > 0) g2d.setFont(new Font("SansSerif", Font.BOLD, 30));
                    FontMetrics fm = g2d.getFontMetrics();
                    int menuHeight = fm.getHeight();
                    int startY = (getHeight() - menuHeight * 4) / 4; //화면 상단에서 1/4부분부터 시작
                    String text = menuItems[i];
                    int textWidth = fm.stringWidth(text);
                    int x = (getWidth() - textWidth) / 2; //화면 가운데로
                    int interval_space = 20; //서로 20만큼 간격 넣기 추후 화면크기 조정할 때 수정 필요
                    int y = startY + i * (menuHeight+interval_space);

                    if (i == controller.getSelectedOption()) g2d.setColor(Color.YELLOW);
                    else g2d.setColor(menuColors[i]);
                    g2d.fillRect(x, y, textWidth, menuHeight);

                    g2d.setColor(Color.BLACK);
                    g2d.drawString(text, x, y + fm.getAscent());
                }
                g2d.dispose();
            }
        };
        glassPane.setOpaque(false);
        // JRootPane의 GlassPane 설정 by chatGPT
        JRootPane rootPane = this.getRootPane();
        rootPane.setGlassPane(glassPane);
        glassPane.setVisible(false); // 초기에는 보이지 않도록 설정

        //Board display setting.
        pane = new JTextPane();
        pane.setEditable(false);
        pane.setBackground(Color.BLACK);
        CompoundBorder border = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 10),
                BorderFactory.createLineBorder(Color.DARK_GRAY, 5));
        pane.setBorder(border);
        this.getContentPane().add(pane, BorderLayout.CENTER);


        //Document default style.
        styleSet = new SimpleAttributeSet();
        StyleConstants.setFontSize(styleSet, 18 * Integer.parseInt(ImportSettings.getSetting(
                "ResolutionSizeX"
        )) / 400); // 추후 defaultsetting 값을 import 하는 것을 구현 한 후 리터럴을 수정해야함
        StyleConstants.setFontFamily(styleSet, "Courier New");
        StyleConstants.setBold(styleSet, true);
        StyleConstants.setForeground(styleSet, Color.WHITE);
        StyleConstants.setAlignment(styleSet, StyleConstants.ALIGN_CENTER);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                requestFocus();
            }
        });
    }

    public void addKeyListenerToFrame(KeyListener listener) {
        addKeyListener(listener);
        setFocusable(true);
        requestFocusInWindow();
    }



    public void setController(BoardController controller) {
        this.controller = controller;
    }

    public void showPauseScreen(boolean show) {
        glassPane.setVisible(show);
    }

    public String showInputDialog(String message) {
        return JOptionPane.showInputDialog(message);
    }

    public int showConfirmDialog(String message, String title) {
        return JOptionPane.showConfirmDialog(this, message,
                title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    public void drawBoard(int[][] board, Color[] board_color, String[][] board_text) {
        StringBuffer sb = new StringBuffer();
        for(int t=0; t<WIDTH+2; t++) sb.append(BORDER_CHAR);
        sb.append("\n");
        for(int i=0; i < board.length; i++) {
            sb.append(BORDER_CHAR);
            for(int j=0; j < board[i].length; j++) {
                if(board[i][j] == 1) {
                    // sb.append("O");
                    sb.append(board_text[i][j]);
                    //블럭이 가진 텍스트(무늬)로 보드를 그린다
                } else {
                    sb.append(" ");
                }
            }
            sb.append(BORDER_CHAR);
            sb.append("\n");
        }
        for(int t=0; t<WIDTH+2; t++) sb.append(BORDER_CHAR);
        pane.setText(sb.toString());
        StyledDocument doc = pane.getStyledDocument();

        doc.setParagraphAttributes(0, doc.getLength(), styleSet, false);

        //board_color에 저장된 값을 바탕으로 글자 색 칠하기
        for(int i=0; i<board_color.length; i++) {
            if (board_color[i] != null) {
                SimpleAttributeSet styles = new SimpleAttributeSet();
                StyleConstants.setForeground(styles, board_color[i]);
                doc.setCharacterAttributes(i, 1, styles, false);
            }
        }
        pane.setStyledDocument(doc);
    }

    public void glassRepaint() {
        glassPane.repaint();
    }

}
