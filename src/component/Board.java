package component;

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
	public static ArrayList<Block> BlockQueue;

	private JTextPane pane;

	private int[][] board;
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
		this.getContentPane().setBackground(Color.BLACK);
		//Board display setting.
		pane = new JTextPane();
		pane.setEditable(false);
		pane.setBackground(Color.BLACK);
		CompoundBorder border = BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.GRAY, 10),
				BorderFactory.createLineBorder(Color.DARK_GRAY, 5));
		pane.setBorder(border);
		this.getContentPane().add(pane, BorderLayout.CENTER);

		SidePanel sidePanel=new SidePanel();
		this.getContentPane().add(sidePanel,BorderLayout.EAST);


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
		int block = rnd.nextInt(6);
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
		StyledDocument doc = pane.getStyledDocument();
		SimpleAttributeSet styles = new SimpleAttributeSet();
		StyleConstants.setForeground(styles, curr.getColor());
		for(int j=0; j<curr.height(); j++) {
			int rows = y+j == 0 ? 0 : y+j-1;
			int offset = rows * (WIDTH+3) + x + 1;
			doc.setCharacterAttributes(offset, curr.width(), styles, true);
			for(int i=0; i<curr.width(); i++) {
				// curr.getShape(i, j)가 1일 때만 board 값을 업데이트
				if (curr.getShape(i, j) == 1) {
					board[y+j][x+i] = 1; // 현재 블록의 부분이 1일 경우에만 board를 업데이트
				}
			}
		}
	}
	private boolean collisionCheck(int horizon, int vertical) { // 블록, 벽 충돌을 체크하는 메서드
		int nextX = x + horizon;
		int nextY = y + vertical;

		// 경계 및 블록과의 충돌 체크
		for (int j = 0; j < curr.height(); j++) {
			for (int i = 0; i < curr.width(); i++) {
				if (curr.getShape(i, j) != 0) { // 현재 블록의 해당 부분이 실제로 블록을 구성하는지 확인 (1인지 0인지)
					int boardX = nextX + i;
					int boardY = nextY + j;

					// 벽 충돌 체크
					if (boardX < 0 || boardX >= WIDTH || boardY < 0 || boardY >= HEIGHT) {
						return true;
					}

					// 다른 블록과 충돌하는지 검사
					if (board[boardY][boardX] != 0) {
						return true;
					}
				}
			}
		}
		return false; // 충돌 없음
	}


	private void eraseCurr() {
		for(int j=0; j<curr.height(); j++) {
			for(int i=0; i<curr.width(); i++) {
				// 현재 블록의 shape가 1인 부분만 0으로 지워야함 이걸 못찾았다니..
				if (curr.getShape(i, j) == 1) {
					board[y+j][x+i] = 0;
				}
			}
		}
	}

	private void lineClear() {
		for (int row = HEIGHT - 1; row >= 0; row--) {
			boolean fullLine = true;

			for (int col = 0; col < WIDTH; col++) {
				if (board[row][col] == 0) {
					fullLine = false;
					break;
				}
			}

			if (fullLine) {
				for (int moveRow = row; moveRow > 0; moveRow--) {
					for (int col = 0; col < WIDTH; col++) {
						board[moveRow][col] = board[moveRow - 1][col];
					}
				}

				for (int col = 0; col < WIDTH; col++) {
					board[0][col] = 0;
				}

				row++;
			}
		}
	}



	protected void moveDown() {
		eraseCurr();
		if(!collisionCheck(0, 1)) y++;
		else {
			placeBlock();
      // LineClear 과정
			lineClear();
			curr = SidePanel.getNextBlock();
			SidePanel.paintNextPiece();
			// curr = getRandomBlock();
			x = 3;
			y = 0;
		}
		placeBlock();
	}


	protected void moveBottom() {
		eraseCurr();
		while (!collisionCheck(0, 1)) { y++; }
		placeBlock();
	}

	protected void moveRight() {
		eraseCurr();
		if(!collisionCheck(1, 0)) x++;
		placeBlock();
	}

	protected void moveLeft() {
		eraseCurr();
		if(!collisionCheck(-1, 0)) {
			x--;
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
				eraseCurr();
				curr.rotate();
				drawBoard();
				break;
			case KeyEvent.VK_ENTER:
				eraseCurr();
				// 위치 이동 메서드
				moveBottom();
				drawBoard();
				break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {

		}
	}

}