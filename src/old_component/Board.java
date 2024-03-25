package old_component;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import model.blocks.Block;
import model.blocks.IBlock;
import model.blocks.JBlock;
import model.blocks.LBlock;
import model.blocks.OBlock;
import model.blocks.SBlock;
import model.blocks.TBlock;
import model.blocks.ZBlock;

import static old_main.Tetris.LevelMain;

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
  private JPanel glassPane; //게임 정지화면을 나타낼 glassPane
	private int selectedOption = 1;
	//게임 정지화면에서 재시작, 메인메뉴, 게임종료를 선택하는 selectedOption
	int x = 3; //Default Position.
	int y = 0;

	int blockCount;
	int linesCleared;
	boolean isDowned;
	private static int initInterval = 1000;
	long beforeTime;
	long afterTime;
	public double getTime(){
		//1=0.1초
		double secDiffTime=(afterTime - beforeTime)/100;

		System.out.println("걸린 시간: " + secDiffTime);

		// 시간을 측정한 후 변수 초기화
		beforeTime = 0;
		afterTime = 0;
		return secDiffTime;
	}

	public Board() {
		super("SeoulTech SE Tetris");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.getContentPane().setBackground(Color.BLACK);

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

				String[] menuItems = {"일시정지", "메인메뉴", " 재시작 ", "게임종료"};
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

					if (i == selectedOption) g2d.setColor(Color.YELLOW);
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
				if (blockCount > 100 || linesCleared >= 20) {
					// Decrease timer interval for faster movement
					initInterval=700;
					timer.setDelay(initInterval);
					System.out.println("빨라졌습니다");
					// You can adjust this factor according to your needs
				}

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
		isDowned=false;
		curr = getRandomBlock();
		blockCount=1;
		placeBlock();
		drawBoard();
		timer.start();
		beforeTime= System.currentTimeMillis();
	}

	// 게임 일시 정지 확인용 isPaused by chatGPT3.5
	private boolean isPaused = false;
	public void pauseGame() { // 게임 일시 정지 by chatGPT3.5
		if (!isPaused) {
			timer.stop(); // 타이머 일시 정지
			isPaused = true;
			glassPane.setVisible(!glassPane.isVisible());
		}
	}
	public void resumeGame() { // 게임 다시 시작 by chatGPT3.5
		if (isPaused) {
			timer.start(); // 타이머 다시 시작
			isPaused = false;
			glassPane.setVisible(!glassPane.isVisible());
		}
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
				linesCleared++;
				System.out.println("삭제된 라인 수"+linesCleared);
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

	private void gameExit() { // 게임 종료
		//Yes No버튼 선택할 때 마우스입력만 되는건가?
		//만약 마우스입력만 된다면 이후에 키보드로도 선택할 수 있도록 수정
		timer.stop();
		// Option 패널 이용하여 Question
		int response = JOptionPane.showConfirmDialog(this, "게임을 종료하시겠습니까?",
				"Game Exit", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			System.exit(0); // 예를 선택한 경우 게임 종료
		}
	}

	protected void moveDown() {
		// eraseCurr()을 if 안에 넣을지
		eraseCurr();
		if(!collisionCheck(0, 1)) {
			y++;
			if(initInterval==1000){
				SidePanel.updateScore(0);
			}else{
				SidePanel.updateScore(1);
			}
			SidePanel.setScore();
		}
		else {
			placeBlock();
			//블럭을 내려놓은 시간을 계산 후 빨리 내려놓았으면 추가점수 획득
			afterTime=System.currentTimeMillis();
			if(isDowned==false){
				SidePanel.updateScore(2);
				SidePanel.setScore();
				System.out.println("한번도 다운키를 누르지 않으셨습니다.");
			}
			isDowned=false;
			if(getTime()<20){
				SidePanel.updateScore(1);
				SidePanel.setScore();
			}
      // LineClear 과정
			lineClear();
			if (y == 0) { // 블록이 맨 위에 도달했을 때
				gameOver(); // 게임 종료 메서드 호출
				return; // 추가적인 동작을 방지
			}
			curr = SidePanel.getNextBlock();
			beforeTime=System.currentTimeMillis();
			blockCount++;
			System.out.println("추가 된 블록 수: "+blockCount);
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

		if(collisionCheck(0, 0)){
			//DawnGlow님의 collisionCheck적용
			//기존에 collisionCheck 쓸 때는 미래 위치에 있는
			//블럭의 충돌체크를 위해 horizon, vertical을 받지만
			//moveRotate에서는 미리 회전시키고 충돌체크하므로 0,0을 전달

			//회전한 블럭이 벽이나 기존 블럭과 충돌하면
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
			if (!isPaused) {
				switch (e.getKeyCode()) {
					case KeyEvent.VK_DOWN:
				    if(isDowned==false){
					    isDowned=true;
				    }
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
				    if(isDowned==false){
					    isDowned=true;
				    }
						eraseCurr();
						// 위치 이동 메서드
						moveBottom();
						drawBoard();
						break;
					case KeyEvent.VK_P:
						pauseGame();
						break;
				}

			}
			else {
				// 일시 정지 상태인 경우, 스위치 문을 사용하여 추가 키를 처리합니다.
				// KeyEvent.VK_P까지 by chatGPT3.5 이후는 추가 작성
				switch (e.getKeyCode()) {
					case KeyEvent.VK_P:
						resumeGame();
						break;
					case KeyEvent.VK_DOWN:
						selectedOption++;
						if(selectedOption>3) selectedOption=1;
						glassPane.repaint();
						break;
					case KeyEvent.VK_UP:
						selectedOption--;
						if(selectedOption<1) selectedOption=3;
						glassPane.repaint();
						break;
					case KeyEvent.VK_ENTER:
						switch(selectedOption){
							case 1: //메인메뉴
								resumeGame(); //이후에 메인메뉴 추가
								break;
							case 2: //재시작
								resumeGame();
								break;
							case 3: //게임종료
								gameExit();
								break;
						}
				}
			}

		}

		@Override
		public void keyReleased(KeyEvent e) {

		}
	}

}