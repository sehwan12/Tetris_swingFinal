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

import static main.Tetris.LevelMain;

public class Board extends JFrame {

	private static final long serialVersionUID = 2434035659171694595L;


	public static final int HEIGHT = 20;
	public static final int WIDTH = 10;
	public static final char BORDER_CHAR = 'X';
	public static ArrayList<Block> BlockQueue;

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
		setLocationRelativeTo(null);
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
				// curr.getShape(i, j)가 1일 때만 board 값을 업데이트
				if (curr.getShape(i, j) == 1) {
					board[y+j][x+i] = 1; // 현재 블록의 부분이 1일 경우에만 board를 업데이트
          board_color[offset + i] = curr.getColor();
					//블럭이 있는 위치에 블럭 색깔 지정
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
      // int rows = j + 1;
      int rows = j + y + 1;
      int offset = rows * (WIDTH + 3) + x + 1;
			for(int i=0; i<curr.width(); i++) {
				// 현재 블록의 shape가 1인 부분만 0으로 지워야함 이걸 못찾았다니..
        // if (board[j][i] == 1)
				if (curr.getShape(i, j) == 1) {
					board[y+j][x+i] = 0;
          // board_color[offset + (i - x)] = null; // 이전 블록의 색상 초기화
          board_color[offset + i] = null; // 이전 블록의 색상 초기화
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

	private void gameOver() { // 종료 / 새로운 게임 시작 여부 확인
		timer.stop(); // 게임 타이머를 정지
		// Option 패널 이용하여 Question
		int response = JOptionPane.showConfirmDialog(this, "Game Over. 시작 메뉴로 돌아가시겠습니까?\n (No : 게임 종료)", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			// 추후 메인 메뉴로 돌아갈 수 있도록 코드를 작성해야함
			LevelMain();
			/* 아래 코드는 새로운 게임을 진행하도록 만든 코드
			reset(); // 보드 및 색 배열 초기화
			curr = getRandomBlock(); // 새로운 블록 생성
			placeBlock();
			drawBoard(); // 보드 다시 그리기
			timer.start();
			*/
		} else {
			System.exit(0); // 아니오를 선택한 경우 게임 종료
		}
	}

	protected void moveDown() {
		// eraseCurr()을 if 안에 넣을지
		eraseCurr();
		if(!collisionCheck(0, 1)) y++;
		else {
			placeBlock();
			// LineClear 과정
			lineClear();
			if (y == 0) { // 블록이 맨 위에 도달했을 때
				gameOver(); // 게임 종료 메서드 호출
				return; // 추가적인 동작을 방지
			}
			curr = SidePanel.getNextBlock();
			SidePanel.paintNextPiece();
			// curr = getRandomBlock();
			x = 3;
			y = 0;
		}
		placeBlock();
		drawBoard();
	}


	protected void moveBottom() {
		eraseCurr();
		// 바닥에 이동
		while (!collisionCheck(0, 1)) { y++; }
		placeBlock();
		// LineClear 과정
		lineClear();
		if (y == 0) { // 블록이 맨 위에 도달했을 때
			gameOver(); // 게임 종료 메서드 호출
			return; // 추가적인 동작을 방지
		}
		// 새로운 블럭 생성
		curr = SidePanel.getNextBlock();
		SidePanel.paintNextPiece();
		// curr = getRandomBlock();
		x = 3;
		y = 0;
		placeBlock();
		drawBoard();
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
		this.board = new int[HEIGHT][WIDTH]; // 리터럴 사용은 자제해주세요
		this.board_color = new Color[(HEIGHT+2)*(WIDTH+2+1)]; // 보드 색상 배열도 리셋
		x = 3;
		y = 0;
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