package model.VersusMode;

import controller.VersusBoardController;
import model.SingleMode.BoardModel;
import model.SingleMode.ModelStateChangeListener;
import model.blocks.Block;

import javax.swing.*;
import java.awt.*;
import java.text.MessageFormat;
import java.util.Arrays;

public class VsBoardModel extends BoardModel {

    // 대전모드용 변수들
    public int[][] opp_board;// 상대의 배경
    public String [][] opp_text;
    public int opp_Num;
    //public Color [] opp_color;
    private int[][] pre_background;// 블럭이 배경으로 이동하기 전 배경
    private String[][] pre_text;

    public int[][] getOpp_board() {
        return opp_board;
    }

    public String[][] getOpp_text() {
        return opp_text;
    }

    public Color[] getOpp_color() {
        return opp_color;
    }

    public int[][] getAttackLines() {
        return attackLines;
    }

    public String[][] getAttackString() {
        return attackString;
    }

    public int getAttackLinesNum() {
        return attackLinesNum;
    }

    private int[][] attackLines; 		// 공격에 사용될 줄들
    private String[][] attackString;
    private int attackLinesNum; 		// 공격할 줄 수
    private int grayLinesNum; 			// 이미 공격한 줄 수

    public int getPlayerType() {
        return playerType;
    }

    public int playerType;
    public VsBoardModel(int playerType) {
        super();
        init_pvp();
        grayLinesNum = 0;
        this.playerType = playerType;
    }
    public void init_pvp(){
        this.attackLines = new int[HEIGHT][WIDTH];
        this.attackString= new String[HEIGHT][WIDTH];
        this.opp_board=new int[HEIGHT][WIDTH];
        this.opp_text=new String[HEIGHT][WIDTH];
        this.opp_Num=0;
        //this.opp_color=new Color[(HEIGHT+2)*(WIDTH+2+1)];
    }


    @Override
    protected void notifyStateChanged() {
        for (ModelStateChangeListener listener : listeners) {
            listener.onModelStateChanged(playerType);
        }
    }
    @Override
    protected void notifyUpdateBoard() {
        for (ModelStateChangeListener listener : listeners) {
            listener.notifyUpdateBoard(playerType);
        }
    }
    @Override
    protected void notifyGameOver() {
        for (ModelStateChangeListener listener : listeners) {
            listener.notifyGameOver(playerType);
            JOptionPane.showMessageDialog(null
                    , MessageFormat.format("{0} 번 플레이어 패배\n (No : 게임 종료)", playerType+1),"message",JOptionPane.INFORMATION_MESSAGE
            );
            System.out.println(playerType + "번 플레이어 패배");
        }
    }

    protected void notifyVersusUpdateBoard(){
        for (ModelStateChangeListener listener : listeners) {
            listener.onVersusUpdateBoard(playerType);
        }
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
            if (fullLine) {
                linesToClear.add(row);// 지워질 줄을 linesToClear에 추가
                //setGrayLinesNum();
//                if ( grayLinesNum < 10) {
//
//                    // 공격에 사용될 줄 생성!
//                    makeAttackLine(row - num);
//                }
//                num++;
            }
        }
        notifyVersusUpdateBoard();
        if (!linesToClear.isEmpty()) {
            if(linesToClear.size()>=2 && grayLinesNum<10){
                int num=0;
                for(int line: linesToClear){
                    makeAttackLine(line);
                    num++;
                }
            }
            notifyVersusUpdateBoard();
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
                saveBackground();
                if(opp_Num>=2){
                    attackEd(opp_Num,opp_board,opp_text);
                }
                init_pvp();
                generateBlock();
                timer.start();
            });
            timer2.setRepeats(false); // Timer가 한 번만 실행되도록 설정
            timer2.start(); // Timer 시작
        }
        else {
            saveBackground();
            if(opp_Num>=2){
                attackEd(opp_Num,opp_board,opp_text);
            }
            init_pvp();
            generateBlock();

        }
    }
    public void saveBackground() {

        pre_background = new int[HEIGHT][WIDTH];
        pre_text=new String[HEIGHT][WIDTH];
        int[][] cur_background = getBoard();
        String[][] cur_text= getBoard_text();

        for (int row = 0; row <HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                pre_background[row][col] = cur_background[row][col];
                pre_text[row][col]=cur_text[row][col];
            }
        }
    }
    public void makeAttackLine(int r){
//        attackLines[attackLinesNum]=pre_background[r];
//        attackString[attackLinesNum]=pre_text[r];
        if (attackLinesNum < 10) {  // 공간이 있을 경우에만 새 공격 줄을 추가
            attackLines[attackLinesNum] = Arrays.copyOf(pre_background[r], WIDTH);
            attackString[attackLinesNum] = Arrays.copyOf(pre_text[r], WIDTH);
            System.out.println(r + "번째라인 공격 예정");
            attackLinesNum++;
            grayLinesNum+=attackLinesNum;
        }
    }
    public void shiftUp(int attackNum) {
        for (int row = 0; row < HEIGHT - attackNum; row++) {
            for (int col = 0; col < WIDTH; col++) {
                board[row][col] = board[row + attackNum][col];
                board_text[row][col] = board_text[row + attackNum][col];
                board_color[(row * WIDTH) + col] = board_color[((row + attackNum) * WIDTH) + col];
            }
        }
        System.out.println("보드 위로 올리기 완료");
        for (int r = HEIGHT - attackNum; r < HEIGHT; r++) {
            for (int col = 0; col < WIDTH; col++) {
                board[r][col] = 0;
                board_text[r][col] = null;
                board_color[r * WIDTH + col] = null;
            }
        }
        notifyUpdateBoard();  // 변경사항을 뷰에 알림
    }

    @Override
    public void attackEd(int attackNum, int[][] opp_Lines, String[][] opp_String) {
//
        // 두 줄 이상이 삭제되었을 때만
        shiftUp(attackNum);
        for (int row = 0; row < attackNum; row++) {
            for (int col = 0; col < WIDTH; col++) {
                board[HEIGHT - attackNum + row][col] = opp_Lines[row][col];
                board_text[HEIGHT - attackNum + row][col] = opp_String[row][col];
                board_color[(HEIGHT - attackNum + row) * WIDTH + col] = Color.GRAY;
            }
        }
        notifyUpdateBoard();
    }
    @Override
    public void moveDown() {
        eraseCurr();

        if(!collisionCheck(0, 1)) {
            y++;
            //System.out.println(y + '\n');
            if(initInterval==1000){
                updateScore(0);
            }else{
                updateScore(1);
            }
            placeBlock();

        }
        else {
            placeBlock();
            afterTime=System.currentTimeMillis();
            checkForScore();

            // LineClear 과정
            startLineClearAnimation();
            //공격받은 줄 가져오기
        }
    }
    @Override
    public void moveBottom() {
        eraseCurr();
        // 바닥에 이동
        while (!collisionCheck(0, 1)) { y++; }
        placeBlock();
        afterTime=System.currentTimeMillis();
        checkForScore();
        // LineClear 과정
        startLineClearAnimation();
        //공격받은 줄 가져오기
    }


}
