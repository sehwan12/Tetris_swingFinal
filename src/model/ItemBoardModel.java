package model;

import IO.ExportSettings;
import model.blocks.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.Random;

//아이템모드를 위한 모델
public class ItemBoardModel extends BoardModel {
    boolean isClearBlock=false;
    boolean isTimerBlock=false;
    //기존블럭을 변형한 블럭인지 체크
    boolean isPlusItem=false;
    int linesCleared_10=0;
    public ItemBoardModel(){
        gamemode="Item";
        ExportSettings.saveSettings("mode", gamemode);
    }
    @Override
    protected Block getRandomBlock(){
        int block = rws_select();
        Block newBlock;
        isPlusItem=false;
        //if (linesCleared_10==1) {
        if(linesCleared_10==1)
        {
            int itemNum= rws_selectItem();
            switch(itemNum){
                //올 클리어 아이템
                case 0:
                    linesCleared_10--;
                    return new ClearBlock();
                //기존 블럭을 수정하는 아이템 3개(줄삭제, 타이머, 라인채우기)
                case 1:
                    int block2 = rws_select();
                    newBlock = normalGetBlock(block2);
                    isPlusItem=true;
                    return newBlock;
                case 2:
                    return new WeightBlock();
                default:
                    return new ClearBlock();
            }

        } else {
            return normalGetBlock(block);
        }
    }
    private Block normalGetBlock(int block){
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
    @Override
    protected void placeBlock() {
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
                        switch (what_item){
                            case 0:
                                board_text[y+j][x+i]="L";
                                break;
                            case 1:
                                board_text[y+j][x+i]="F";
                                break;
                            case 2:
                                board_text[y+j][x+i]="S";
                                break;
                        }

                        board_color[offset + i]=Color.WHITE;
                    }
                }
            }
        }
    }

    @Override
    protected void lineClear() {
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
                if(linesCleared!=0 && linesCleared%10==0) {
                    //linesCleared 증가하는 것이 lineClear 내에서 증가는데
                    //이때 10단위로 증가할 때마다 linesCleared_10++
                    linesCleared_10++;
                    System.out.println("10개 라인 삭제"+linesCleared_10);
                }
                System.out.println("삭제된 라인 수"+linesCleared);
                row++;

            }
        }
    }
    private void lineFill() {
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
    protected void random_text(Block block){
        if(true){
            //if(linesCleared_10>0){
            Random random = new Random();

            linesCleared_10--;

            what_item=random.nextInt(how_many_items);
            //what_item=1;

            //블럭 shape 안에 1이 몇개 있는지 검사
            int block_count=0;
            for(int j=0; j< block.height(); j++)
                for(int i=0; i<block.width(); i++)
                    if(block.getShape(i,j)==1) block_count++;

            //블럭 shape 안에 1의 개수를 기반으로 랜덤한 숫자 배정
            int random_text_position=random.nextInt(block_count)+1;
            block_count=0;

            //블럭 shape 안에 random_text_position 번째의 1이 나오면
            //해당 블럭 shape 안의 1을 2로 설정하고 return
            for(int j=0; j< block.height(); j++)
                for(int i=0; i<block.width(); i++)
                {
                    if(block.getShape(i,j)==1) block_count++;
                    if(block_count==random_text_position){
                        block.setShape(i,j,2);
                        return;
                    }

                }
        }
    }

    @Override
    public void generateBlock() {
        if (y == 0) { // 블록이 맨 위에 도달했을 때
            notifyGameOver();
            System.out.println("게임오버");
            return;
        }
        // curr = SidePanel.getNextBlock();
        curr = nextBlock;
        nextBlock = getRandomBlock();
        if(isPlusItem==true){
            random_text(nextBlock);
        }
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
    public int rws_selectItem() { //확률에 따른 블럭 생성
        // 블럭들의 적합도(가중치)
        double A=10,B=30, C=10;


        // 블럭들의 적합도(가중치) 배열
        double[] fitness = {A,B,C};

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
}
