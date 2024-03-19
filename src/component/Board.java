package component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.border.CompoundBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import blocks.Block;
import blocks.IBlock;
import blocks.JBlock;
import blocks.LBlock;
import blocks.OBlock;
import blocks.SBlock;
import blocks.TBlock;
import blocks.ZBlock;

public class Board extends JFrame {

	private static final long serialVersionUID = 2434035659171694595L;
	
	public static final int HEIGHT = 20;
	public static final int WIDTH = 10;
	public static final char BORDER_CHAR = 'X';
	
	private JTextPane pane;
	private int[][] board;
	private Color[] board_color; //보드 색깔 저장 배열
	private KeyListener playerKeyListener;
	private SimpleAttributeSet styleSet;
	private Timer timer;
	private Block curr;
	int x = 3; //Default Position.
	int y = 0;
	
	private static final int initInterval = 1000;
	
	public Board() {
		super("SeoulTech SE Tetris");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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
		StyleConstants.setFontSize(styleSet, 18);
		StyleConstants.setFontFamily(styleSet, "Courier New");
		StyleConstants.setBold(styleSet, true);
		StyleConstants.setForeground(styleSet, Color.WHITE);
		StyleConstants.setAlignment(styleSet, StyleConstants.ALIGN_CENTER);
		
		//Set timer for block drops.
		timer = new Timer(initInterval, new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				moveDown();
				drawBoard();
			}
		});
		
		//Initialize board for the game.
		board = new int[HEIGHT][WIDTH];
		//보드 색깔 저장 배열
		//setCharacterAttributes이 offset 기준으로 글자의 색을 정하므로
		//board의 offset 크기를 배열 크기로 정의
		//WIDTH에 좌우경계와 '\n'을 더한 WIDTH+2+1
		//HEIGHT에 상하경계를 더한 HEIGHT+2
		board_color = new Color[(HEIGHT+2)*(WIDTH+2+1)];

		playerKeyListener = new PlayerKeyListener();
		addKeyListener(playerKeyListener);
		setFocusable(true);
		requestFocus();
		
		//Create the first block and draw.
		curr = getRandomBlock();
		placeBlock();
		drawBoard();
		timer.start();
	}

	private Block getRandomBlock() {
		Random rnd = new Random(System.currentTimeMillis());
		int block = rnd.nextInt(7);
		switch(block) {
		case 0:
			return new IBlock();
		case 1:
			return new JBlock();
		case 2:
			return new LBlock();
		case 3:
			return new ZBlock();
		case 4:
			return new SBlock();
		case 5:
			return new TBlock();
		case 6:
			return new OBlock();			
		}
		return new LBlock();
	}
	
	private void placeBlock() {
		for(int j=0; j<curr.height(); j++) {
			int rows = y+j+1;
			int offset = (rows) * (WIDTH+3) + x + 1;
			for(int i=0; i<curr.width(); i++) {
				if (curr.getShape(i, j) == 1) {
					board[y + j][x + i] = curr.getShape(i, j);
					board_color[offset + i] = curr.getColor();
					//블럭이 있는 위치에 블럭 색깔 지정
				}
			}
		}

	}
	
	private void eraseCurr() {
		for(int j=y; j<y+curr.height(); j++) {
			int rows = j + 1;
			int offset = rows * (WIDTH + 3) + x + 1;
			for(int i=x; i<x+curr.width(); i++) {
				if (curr.getShape(i-x, j-y) == 1){ // DawnGlow님의 eraseCurr() 적용
					board[j][i] = 0;
					board_color[offset + (i - x)] = null; // 이전 블록의 색상 초기화
				}
			}
		}
		System.out.println();
	}

	protected void moveDown() {

		if(y < HEIGHT - curr.height())
		{
			eraseCurr();
			y++;
		}
		else {
			placeBlock();
			curr = getRandomBlock();
			x = 3;
			y = 0;
		}
		placeBlock();
	}
	
	protected void moveRight() {
		eraseCurr();
		if(x < WIDTH - curr.width()) x++;
		placeBlock();
	}

	protected void moveLeft() {
		eraseCurr();
		if(x > 0) {
			x--;
		}
		placeBlock();
	}

	protected void moveRotate() {

		eraseCurr();

		//회전 시 x, y위치 변경
		x=x+curr.rotate_x();
		y=y+curr.rotate_y();
		curr.rotate();

		if(x > WIDTH - curr.width() || x<0 || y > HEIGHT - curr.height()|| y<0){
			//회전한 블럭이 벽에 충돌하면
			//3번 더 회전 시켜서 원상태로 복귀
			x=x+curr.rotate_x();
			y=y+curr.rotate_y();
			curr.rotate();

			x=x+curr.rotate_x();
			y=y+curr.rotate_y();
			curr.rotate();

			x=x+curr.rotate_x();
			y=y+curr.rotate_y();
			curr.rotate();

		}

		placeBlock();
	}

	public void drawBoard() {
		StringBuffer sb = new StringBuffer();
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
	
	public void reset() {
		this.board = new int[20][10];
	}

	public class PlayerKeyListener implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {
				
		}

		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_DOWN:
				moveDown();
				drawBoard();
				break;
			case KeyEvent.VK_RIGHT:
				moveRight();
				drawBoard();
				break;
			case KeyEvent.VK_LEFT:
				moveLeft();
				drawBoard();
				break;
			case KeyEvent.VK_UP:
				moveRotate();
				drawBoard();
				break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			
		}
	}
	
}
