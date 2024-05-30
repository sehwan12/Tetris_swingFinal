package model.SingleMode;

import model.blocks.*;
import model.blocks.item.Alias.LineClearBlock;
import model.blocks.item.Alias.LineFillBlock;
import model.blocks.item.Alias.TimerBlock;
import model.blocks.item.ClearBlock;
import model.blocks.item.WeightBlock;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.Random;

//아이템모드를 위한 모델
public class ItemBoardModel extends BoardModel {
    private int beforeLineCount;

    private final static int ci = 10;

    @Override
    public int getbeforeLineCount() {return beforeLineCount;}

    @Override
    public int getci() {return ci;}

    boolean horizonLock =false;
    public ItemBoardModel(){
        gamemode="Item";
        beforeLineCount = 0;
    }
    @Override
    protected Block getRandomBlock() {
        int maxThreshold = (beforeLineCount / ci) * ci;
        if ((linesCleared >= (maxThreshold + ci))) {
            beforeLineCount = linesCleared;
            Random random = new Random();
            int item = random.nextInt(5);
            switch (item) {
                case 0:
                    return new ClearBlock();
                case 1:
                    return new WeightBlock();
                case 2:
                    return new LineClearBlock();
                case 3:
                    return new LineFillBlock();
                case 4:
                    return new TimerBlock();
                default:
                    return new TimerBlock();
            }
        }
        else {
            return super.getRandomBlock();
        }
    }
    @Override
    public void placeBlock() {
        for(int j=0; j<curr.height(); j++) {
            int rows = y+j+1;
            int offset = (rows) * (WIDTH+3) + x + 1;
            for(int i=0; i<curr.width(); i++) {
                // curr.getShape(i, j)가 1일 때만 board 값을 업데이트
                if (curr.getShape(i, j) >= 1) {
                    board[y+j][x+i] = 1; // 현재 블록의 부분이 1일 경우에만 board를 업데이트
                    board_color[offset + i] = curr.getColor();
                    board_text[y+j][x+i] = curr.getText(); //블럭이 있는 위치에 블럭 텍스트 지정
                    //블럭이 있는 위치에 블럭 색깔 지정
                    if(curr.getShape(i, j) == 2) {
                        //블럭의 shape가 2인곳에서
                        //what_item에 따라서 L과 F를 설정
                        if (curr instanceof LineClearBlock) {
                            board_text[y+j][x+i] = "L";
                        }
                        else if (curr instanceof LineFillBlock) {
                            board_text[y+j][x+i] = "F";
                        }
                        else {
                            board_text[y+j][x+i] = "S";
                        }
                        board_color[offset + i]=Color.WHITE;
                    }
                }
            }
        }
    }

    @Override
    protected boolean collisionCheck(int horizon, int vertical) { // 블록, 벽 충돌을 체크하는 메서드
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
                        System.out.println("벽과 충돌");
                        return true;
                    }

                    if ((boardY >= HEIGHT - 1) && curr instanceof WeightBlock) {
                        System.out.println("바닥과 충돌");
                        horizonLock = true;
                    }

                    // 다른 블록과 충돌하는지 검사
                    if (board[boardY][boardX] != 0) {
                        System.out.println("다른 블록과 충돌");
                        if (curr instanceof WeightBlock) {
                            horizonLock = true;
                        }
                        return true;
                    }
                }
            }
        }
        return false; // 충돌 없음
    }

    @Override
    protected void lineClear() {
        boolean isClearBlock=false;
        boolean isTimerBlock=false;
        int Crow=0;
        int Srow=0;
        for (int row = HEIGHT - 1; row >= 0; row--) {
            boolean fullLine = true;
            for (int col = 0; col < WIDTH; col++) {
                if(board_text[row][col]=="C"){
                    isClearBlock=true;
                    Crow=row;
                }
                if(board_text[row][col]=="S"){
                    isTimerBlock=true;
                    Srow=row;
                }
                if (board[row][col] == 0) {
                    fullLine = false;
                    break;
                }

            }
            for (int col = 0; col < WIDTH; col++) {
                if (Objects.equals(board_text[row][col], "L")) {
                    //L이 있는지 검사하고 L이 있으면 lineclear
                    fullLine = true;
                    break;
                }
            }
            if(linesToClear.contains(Crow) && isClearBlock==true){
                for (int c = 0; c < WIDTH; c++) {
                    for(int r=0; r<HEIGHT;r++){
                        board[r][c] = 0;
                        board_text[r][c] = null;
                    }
                }
                for (int col = 0; col < WIDTH+3; col++){
                    board_color[col] = null;
                }
            }
            if(linesToClear.contains(Srow)&& isTimerBlock==true){
                updateTimer(3);
                System.out.println("아이템으로 인해 타이머가 변경되었습니다");
            }

            if (fullLine) {
                for (int moveRow = row; moveRow > 0; moveRow--) {
                    int rows = moveRow+1;
                    int offset = (rows) * (WIDTH+3) + 1;
                    for (int col = 0; col < WIDTH; col++) {
                        board[moveRow][col] = board[moveRow - 1][col];
                        // 색상 추가한 코드
                        board_color[offset + col] = board_color[offset + col - (WIDTH+3)];
                        board_text[moveRow][col] = board_text[moveRow - 1][col];
                        //블럭의 color 와 text 도 lineClear 될 수 있도록 추가
                    }
                }

                for (int col = 0; col < WIDTH; col++) {
                    board[0][col] = 0;
                    board_text[0][col] = null;
                }

                for (int col = 0; col < WIDTH+3; col++){
                    board_color[col] = null;
                }
                linesCleared++;
                row++;

            }
        }
    }
    protected void lineFill() {
        for (int col = 0; col < WIDTH; col++) {
            boolean is_F=false;
            int F_row_position=HEIGHT - 1;

            for (int row = 0; row <= HEIGHT - 1; row++) {
                if (Objects.equals(board_text[row][col], "F")) {
                    is_F = true;
                    F_row_position=row;
                    break;
                }
            }
            if (is_F) {
                for (int fill_row = F_row_position; fill_row <= HEIGHT - 1; fill_row++) {
                    int offset = (fill_row+1) * (WIDTH+3) + 1;
                    if(board[fill_row][col]==0){
                        board[fill_row][col]=1;
                        board_color[offset + col] = new Color(180,180,180);
                        board_text[fill_row][col] = "O";
                    }
                }
            }
        }
    }

    @Override
    public void generateBlock() {
        horizonLock = false;
        if (y == 0) { // 블록이 맨 위에 도달했을 때
            notifyGameOver();
            System.out.println("게임오버");
            return;
        }
        // curr = SidePanel.getNextBlock();
        curr = nextBlock;
        nextBlock = getRandomBlock();
        beforeTime=System.currentTimeMillis();
        blockCount++;
        System.out.println("추가 된 블록 수: "+blockCount);
        x = 3;
        y = 0;
        if (collisionCheck(0, 0)) {
            notifyGameOver();
            return;
        }
        placeBlock();
    }
    @Override
    public void startLineClearAnimation() {
        linesToClear.clear(); // 이전 애니메이션 정보 초기화
        for (int row = HEIGHT - 1; row >= 0; row--) {
            boolean fullLine = true;
            for (int col = 0; col < WIDTH; col++) {
                if (board[row][col] == 0) {
                    fullLine = false;
                    break;
                }
            }
            for (int col = 0; col < WIDTH; col++) {
                if (Objects.equals(board_text[row][col], "L")) {
                    //L이 있는지 검사하고 L이 있으면 lineclear
                    fullLine = true;
                    break;
                }
            }
            if (fullLine) {
                linesToClear.add(row); // 지워질 줄을 linesToClear에 추가
            }
        }

        if (!linesToClear.isEmpty()) {
            // 강조할 줄을 노란색으로 변경하는 로직
            for (int line : linesToClear) {
                for (int col = 0; col < WIDTH; col++) {
                    // 노란색으로 변경하는 부분은 UI에 따라 다르게 구현될 수 있습니다.
                    // 예시로, 각 줄의 색상을 변경하는 방식을 사용할 수 있습니다.
                    board_color[(line + 1) * (WIDTH + 3) + col + 1] = Color.WHITE;
                }
            }
            notifyUpdateBoard();
            timer.stop();
            // 애니메이션을 위한 타이머 시작
            Timer timer2 = new Timer(200, e -> {
                lineClear(); // 실제로 줄을 지우는 메서드
                generateBlock();
                timer.start();
            });
            timer2.setRepeats(false); // Timer가 한 번만 실행되도록 설정
            timer2.start(); // Timer 시작
        }
        else {
            generateBlock();
        }
    }
    @Override
    public void moveRight() {
        if (horizonLock) {
            return;
        }
        eraseCurr();
        if(!collisionCheck(1, 0)) x++;
        placeBlock();
    }
    @Override
    public void moveLeft() {
        if (horizonLock) {
            return;
        }
        eraseCurr();
        if(!collisionCheck(-1, 0)) {
            x--;
        }
        placeBlock();
    }
    @Override
    public void moveDown() {
        eraseCurr();
        if (curr instanceof WeightBlock) {
            if (!collisionCheck(0, 1) || y < HEIGHT - curr.height()) {
                y++;
                System.out.println(y + '\n');
                placeBlock();
            } else {
                placeBlock();
                afterTime = System.currentTimeMillis();
                generateBlock();
            }
        }
        else {
            if (!collisionCheck(0, 1)) {
                y++;
                System.out.println(y + '\n');
                if (initInterval == 1000) {
                    updateScore(0);
                } else {
                    updateScore(1);
                }
                placeBlock();
            } else {
                placeBlock();
                afterTime = System.currentTimeMillis();
                checkForScore();
                lineFill();
                // LineClear 과정
                startLineClearAnimation();
            }
        }
    }
    @Override
    public void moveBottom() {
        eraseCurr();
        // 바닥에 이동
        if (curr instanceof WeightBlock) {
            while (y < HEIGHT - curr.height()) {
                moveDown();
            }
            startLineClearAnimation();
        }
        else {
            while (!collisionCheck(0, 1)) {
                y++;
            }
            placeBlock();
            afterTime = System.currentTimeMillis();
            checkForScore();
            lineFill();
            // LineClear 과정
            startLineClearAnimation();
        }
    }

}
