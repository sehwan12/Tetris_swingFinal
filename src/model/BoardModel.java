package model;

import java.util.*;
import java.awt.Color;
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

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.CompoundBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import IO.ExportSettings;
import controller.BoardController;
import model.blocks.*;
import model.ModelStateChangeListener;
import model.OutGameModel;


public class BoardModel {
    // board
    protected int[][] board;

    protected String[][] board_text;

    protected List<Integer> linesToClear = new LinkedList<>();

    //색맹모드와 무늬모드를 위한 color_blind 와 pattern 선언
    protected boolean color_blind;
    protected int pattern;
    protected  String gamemode;
    private boolean isClearBlock=false;

    public static final int HEIGHT = 20;
    public static final int WIDTH = 10;

    //Default Block Position. (3,0)
    int x = 3;
    int y = 0;

    // Side board
    public static ArrayList<Block> BlockQueue;
    static int x1 = 2; //Default Position.
    static int y1= 0;

    protected Color[] board_color; //보드 색깔 저장 배열

    protected Timer timer;

    protected Block curr;

    protected Block nextBlock;



    int blockCount;
    int linesCleared;
    boolean isDowned;


    protected static int initInterval = 1000;
    long beforeTime;
    long afterTime;

    private int totalscore;


    // 게임 일시 정지 확인용 isPaused by chatGPT3.5
    // 피드백 : 멤버 메서드와 변수는 따로 분리해서 써주세요
    private boolean isPaused = false;
    // Observer pattern을 위한 리스트
    private List<ModelStateChangeListener> listeners = new ArrayList<>();

    public int getWhat_item(){
        return what_item;
    }

    public void setWhat_item(){
        this.what_item=what_item;
    }

    int what_item;// 아이템 모드에서 아이템인지를 표현
    int how_many_items=3;// 아이템 모드에서 총 아이템 개수
    public BoardModel() {
        timer = new Timer(initInterval, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                notifyStateChanged();
                if (blockCount > 100 || linesCleared >= 20) {
                    // Decrease timer interval for faster movement
                    if(OutGameModel.getDifficulty()=="Easy"){
                        updateTimer(1);
                        System.out.println("현재속도:1.38배");
                    }else if(OutGameModel.getDifficulty()=="Hard"){
                        updateTimer(2);
                        System.out.println("현재속도:2.08배");
                    }else{
                        updateTimer(0);
                        System.out.println("현재속도:1.66배");
                    }
                    timer.setDelay(initInterval);

                    // You can adjust this factor according to your needs
                }

            }
        });
        color_blind = OutGameModel.isBlindMode();
        gamemode="normal";
        ExportSettings.saveSettings("mode",gamemode);
        pattern = 0;
        //Initialize board for the game.
        board = new int[HEIGHT][WIDTH];
        //보드 색깔 저장 배열
        //setCharacterAttributes이 offset 기준으로 글자의 색을 정하므로
        //board의 offset 크기를 배열 크기로 정의
        //WIDTH에 좌우경계와 '\n'을 더한 WIDTH+2+1
        //HEIGHT에 상하경계를 더한 HEIGHT+2
        board_color = new Color[(HEIGHT+2)*(WIDTH+2+1)];
        //블럭 무늬를 표현할 보드 텍스트 저장 배열
        board_text = new String[HEIGHT][WIDTH];

        totalscore = 0;

        //Create the first block and draw.
        isDowned=false;
        curr = getRandomBlock();
        nextBlock = getRandomBlock();
        blockCount=0;
        placeBlock();
        timer.start();
        beforeTime= System.currentTimeMillis();
    }


    public void addModelStateChangeListener(ModelStateChangeListener listener) {
        listeners.add(listener);
    }

    protected void notifyStateChanged() {
        for (ModelStateChangeListener listener : listeners) {
            listener.onModelStateChanged();
        }
    }

    protected void notifyUpdateBoard() {
        for (ModelStateChangeListener listener : listeners) {
            listener.notifyUpdateBoard();
        }
    }

    protected void notifyGameOver() {
        for (ModelStateChangeListener listener : listeners) {
            listener.notifyGameOver();
        }
    }

    public int[][] getBoard() {
        return board;
    }

    public Color[] getBoard_color() {
        return board_color;
    }

    public String[][] getBoard_text() {
        return board_text;
    }

    protected Block getRandomBlock() {
        // Random rnd = new Random(System.currentTimeMillis());
        // int block = rnd.nextInt(7);
        int block = rws_select();
        // Block 객체 생성시 color_blind, pattern 전달 추가

        switch (block) {
            case 0:
                return new IBlock(color_blind, pattern);
            case 1:
                return new JBlock(color_blind, pattern);
            case 2:
                return new LBlock(color_blind, pattern);
            case 3:
                return new ZBlock(color_blind, pattern);
            case 4:
                return new SBlock(color_blind, pattern);
            case 5:
                return new TBlock(color_blind, pattern);
            case 6:
                return new OBlock(color_blind, pattern);
        }
        return new LBlock(color_blind, pattern);


    }

    public int rws_select() { //확률에 따른 블럭 생성
        // 블럭들의 적합도(가중치)
        double I = 10, J = 10, L = 10, Z = 10, S = 10, T = 10, O = 10;
        if (OutGameModel.getDifficultyInt() == 0) {
            I = 12;
            //System.out.println("Easy");
        }
        else if (OutGameModel.getDifficultyInt() == 1) {
            I = 10;
            //System.out.println("Normal");
        }
        else if (OutGameModel.getDifficultyInt() == 2) {
            I = 8;
            //System.out.println("Hard");
        }

        // 블럭들의 적합도(가중치) 배열
        double[] fitness = {I, J, L, Z, S, T, O};

        Random random = new Random();
        //Random random = new Random(System.currentTimeMillis()); // 이것을 쓰면 오차범위가 5%를 넘음

        // 최대 적합도 찾기
        double maxFitness = 0;
        for (double fit : fitness) {
            if (fit > maxFitness) {
                maxFitness = fit;
            }
        }
        // 확률적 수락을 통한 개체 선택
        while (true) {
            // 임의의 개체 선택
            int index = random.nextInt(fitness.length);
            // 선택된 개체의 적합도가 확률적으로 수락되는지 확인
            if (random.nextDouble() < fitness[index] / maxFitness) {
                return index; // 선택된 개체의 인덱스 반환
            }
        }
    }

    public Block getNextBlock() {
        return nextBlock;
    }

    // 피드백 : 함수 명을 보고 단순 getter라고 착각할 수 있음
    public double getTime() {
        //1=0.1초
        double secDiffTime=(afterTime - beforeTime)/100;

        System.out.println("걸린 시간: " + secDiffTime);

        // 시간을 측정한 후 변수 초기화
        beforeTime = 0;
        afterTime = 0;
        return secDiffTime;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        this.isPaused = paused;
        if (paused) {
            timer.stop();
        }
        else {
            timer.start();
        }
    }

    public boolean isDowned() {
        return isDowned;
    }

    public void setDowned(boolean downed) {
        isDowned = downed;
    }

    public int getTotalscore() {
        return totalscore;
    }

    public void setTotalscore(int totalscore) {
        this.totalscore = totalscore;
    }

    public void updateScore(int a){
        switch(a){
            //디폴트
            case 0:
                totalscore+=1;
                break;
            //빠른 시간내에 블록을 떨어트린 경우 or 타이머가 빨라진 경우
            case 1:
                totalscore+=2;
                break;
            //한번도 다운 or 엔터키를 누르지 않은 경우(머뭇거린 경우)
            case 2:
                totalscore-=5;
                break;
            //빠른 시간내에 블록을 떨어트린 경우
            case 3:
                totalscore+=20;
                break;
            default:
                totalscore++;
        }
    }

    public void updateTimer(int a){
        switch(a){
            //normalmode에서 타이머가 빨라질 때
            case 0:
                initInterval=600;
                break;
            //easymode에서 타이머가 빨라질때
            case 1:
                initInterval=720;
                break;
            //hardmode에서 타이머가 빨라질때
            case 2:
                initInterval=480;
                break;
            //타이머 아이템이 있는 줄이 지워졌을 때
            case 3:
                initInterval=1500;
                break;
            default:
                initInterval=1000;
        }
    }

    protected void placeBlock() {
        for(int j=0; j<curr.height(); j++) {
            int rows = y+j+1;
            int offset = (rows) * (WIDTH+3) + x + 1;
            for(int i=0; i<curr.width(); i++) {
                // curr.getShape(i, j)가 1일 때만 board 값을 업데이트
                if (curr.getShape(i, j) == 1) {
                    board[y+j][x+i] = 1; // 현재 블록의 부분이 1일 경우에만 board를 업데이트
                    board_color[offset + i] = curr.getColor();
                    board_text[y+j][x+i] = curr.getText(); //블럭이 있는 위치에 블럭 텍스트 지정
                    //블럭이 있는 위치에 블럭 색깔 지정

                }
            }
        }
    }

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

                    // 다른 블록과 충돌하는지 검사
                    if (board[boardY][boardX] != 0) {
                        System.out.println("다른 블록과 충돌");
                        return true;
                    }
                }
            }
        }
        return false; // 충돌 없음
    }

    public void eraseCurr() {
        for(int j=0; j<curr.height(); j++) {
            // int rows = j + 1;
            int rows = j + y + 1;
            int offset = rows * (WIDTH + 3) + x + 1;
            for(int i=0; i<curr.width(); i++) {
                // 현재 블록의 shape가 1인 부분만 0으로 지워야함 이걸 못찾았다니..
                // if (board[j][i] == 1)
                if (curr.getShape(i, j) >= 1) {
                    board[y+j][x+i] = 0;
                    // board_color[offset + (i - x)] = null; // 이전 블록의 색상 초기화

                    board_color[offset + i] = null; // 이전 블록의 색상 초기화
                    board_text[y+j][x+i] = null; // 이전 블록으 텍스트 초기화
                }
            }
        }
    }

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

    protected void lineClear() {
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
                System.out.println("삭제된 라인 수"+linesCleared);
                row++;

            }
        }
    }

    public void reset() {
        this.board = new int[HEIGHT][WIDTH]; // 리터럴 사용은 자제해주세요
        this.board_color = new Color[(HEIGHT+2)*(WIDTH+2+1)]; // 보드 색상 배열도 리셋
        this.board_text = new String[HEIGHT][WIDTH]; // 보드 텍스트 배열도 리셋
        x = 3;
        y = 0;
    }

    protected void checkForScore(){
        if(isDowned==false){
            updateScore(2);
            System.out.println("한번도 다운키를 누르지 않으셨습니다.");
        }
        isDowned=false;
        if(getTime()<20){
            updateScore(3);
        }
    }

    public void generateBlock() {
        if (y == 0) { // 블록이 맨 위에 도달했을 때
            notifyGameOver();
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

    // 게임이 종료되면 false를 반환
    // feedback 1 : SidePanel.setScore();는 함수 마지막에 한 번만 쓰면 됨
    public void moveDown() {
        eraseCurr();
        if(!collisionCheck(0, 1)) {
            y++;
            System.out.println(y + '\n');
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
        }
    }

    public void moveBottom() {
        eraseCurr();
        // 바닥에 이동
        while (!collisionCheck(0, 1)) { y++; }
        placeBlock();
        afterTime=System.currentTimeMillis();
        checkForScore();
        // LineClear 과정
        startLineClearAnimation();
    }

    public void moveRight() {
        eraseCurr();
        if(!collisionCheck(1, 0)) x++;
        placeBlock();
    }

    public void moveLeft() {
        eraseCurr();
        if(!collisionCheck(-1, 0)) {
            x--;
        }
        placeBlock();
    }

    public void moveRotate() {

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
    public String getGamemode(){
        return gamemode;
    }

}
