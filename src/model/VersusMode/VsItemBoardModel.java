package model.VersusMode;

import model.SingleMode.ItemBoardModel;
import model.SingleMode.ModelStateChangeListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VsItemBoardModel extends ItemBoardModel {
    protected List<VersusModelStateChangeListener> listeners = new ArrayList<>();
    private BlockChunk attackBlockChunk;

    private BlockChunk defenseBlockChunk;

    int playerType;
    public VsItemBoardModel(int playerType) {
        super();
        attackBlockChunk = new BlockChunk();
        defenseBlockChunk = new BlockChunk();
        this.playerType = playerType;
    }
    @Override
    public void addModelStateChangeListener(ModelStateChangeListener listener) {
        listeners.add((VersusModelStateChangeListener) listener);
    }

    @Override
    protected void notifyStateChanged() {
        for (VersusModelStateChangeListener listener : listeners) {
            listener.onModelStateChanged(playerType);
        }
    }
    @Override
    protected void notifyUpdateBoard() {
        for (VersusModelStateChangeListener listener : listeners) {
            listener.notifyUpdateBoard(playerType);
        }
    }
    @Override
    protected void notifyGameOver() {
        for (VersusModelStateChangeListener listener : listeners) {
            listener.notifyGameOver(playerType);
        }
    }

    public void notifyAttack(int playerType) {
        for (VersusModelStateChangeListener listener : listeners) {
            listener.notifyAttck(playerType);
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

        // Override : 공격 해야하는 경우
        if (linesToClear.size() >= 2) {
            saveAttackBlock();
            notifyAttack(playerType);
            attackBlockChunk = new BlockChunk();
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
    public void generateBlock() {
        if (defenseBlockChunk.grayLinesNum > 0) {
            addDefenseBlock();
            defenseBlockChunk = new BlockChunk();
        }
        super.generateBlock();
    }

    // 현재 블럭이 주어진 좌표에 존재하는지 확인하는 메서드
    public boolean checkCurrBlockArea(int p, int k) {
        for (int j = 0; j < curr.height(); j++) {
            for (int i = 0; i < curr.width(); i++) {
                if (curr.getShape(i, j) != 0) {
                    if ((getY() + j) == k && (getX() + i) == p) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // 가장 높은 줄의 인덱스를 반환하는 메서드
    public int checkBoardHeight() {
        for (int i = 0; i < HEIGHT; i++) { // 위에서부터 아래로 검사
            for (int j = 0; j < WIDTH; j++) {
                if (board[i][j] != 0) {
                    return i; // 첫 번째 비어있지 않은 행의 인덱스를 반환
                }
            }
        }
        return HEIGHT - 1; // 모든 행이 비어있으면 가장 아래 행의 인덱스 반환
    }

    // 상대방이 공격한 블럭을 저장하는 메서드
    public void saveDefenseBlock(BlockChunk blockChunk) {
        if (defenseBlockChunk.grayLinesNum == 10) {
            return;
        }
        else if (defenseBlockChunk.grayLinesNum > 0) { // defenseBlockChunk에 이미 줄이 존재하는 경우
            // 10줄을 넘지 않는 경우
            if ((defenseBlockChunk.grayLinesNum + blockChunk.grayLinesNum) <= 10) {
                for (int i = defenseBlockChunk.grayLinesNum - 1; i >=0 ; i--) {
                    for (int col = 0; col < WIDTH; col++) {
                        defenseBlockChunk.block[i + blockChunk.grayLinesNum] = defenseBlockChunk.block[i];
                    }
                }
            }
            else { // 10줄을 넘는 경우
                int overflow = (defenseBlockChunk.grayLinesNum + blockChunk.grayLinesNum) - 10;
                for (int i = defenseBlockChunk.grayLinesNum - overflow; i >= 0 ; i++) {
                    for (int col = 0; col < WIDTH; col++) {
                        defenseBlockChunk.block[i + blockChunk.grayLinesNum] = defenseBlockChunk.block[i];
                    }
                }
            }
            for (int i = 0; i < blockChunk.grayLinesNum; i++) {
                for (int col = 0; col < WIDTH; col++) {
                    defenseBlockChunk.block[i] = blockChunk.block[i];
                }
            }
            defenseBlockChunk.grayLinesNum += blockChunk.grayLinesNum;
        }
        else {
            defenseBlockChunk = blockChunk;
        }
    }

    public BlockChunk getDefenseBlock() {
        return defenseBlockChunk;
    }

    // generateBlock 호출 시 공격받은 블럭(DefenseBlock)을 추가하는 메서드
    public void addDefenseBlock() {
        if ((checkBoardHeight() - defenseBlockChunk.grayLinesNum) < 0) {
            notifyGameOver();
            return;
        }
        // blockChunk.grayLinesNum만큼 Board[][] 배열의 값을 y축으로 이동
        for (int j = checkBoardHeight(); j <= HEIGHT - 1; j++) {
            for (int i = 0; i < WIDTH; i++) {
                board[j - defenseBlockChunk.grayLinesNum][i] = board[j][i];
                board_color[(j - defenseBlockChunk.grayLinesNum + 1) * (WIDTH + 3) + i + 1] = board_color[(j + 1) * (WIDTH + 3) + i + 1];
                board_text[j - defenseBlockChunk.grayLinesNum][i] = board_text[j][i];
            }
        }
        // blockChunk.attackBlock을 Board[][] 배열에 추가
        for (int j = 0; j < defenseBlockChunk.grayLinesNum; j++) {
            for (int i = 0; i < WIDTH; i++) {
                board[HEIGHT - 1 - j][i] = defenseBlockChunk.block[j][i];
                board_color[(HEIGHT - j) * (WIDTH + 3) + i + 1] = Color.GRAY; // 회색으로 설정
                board_text[HEIGHT - 1 - j][i] = "O"; // 텍스트를 "O"로 설정
            }
        }
    }

    public void saveAttackBlock() {
        for (int i = 0; i < linesToClear.size(); i++) {
            for (int col = 0; col < WIDTH; col++) {
                // 삭제된 줄들이 가장 마지막에 들어온 블럭 부분을 - 그 줄을 삭제되게 만든 블럭 - 제외하고 attackBlock에 저장
                // 즉, 복사할 line에서 currBlock의 위치를 제외한 나머지 부분을 복사
                // 만약 board[linesToClear.get(i)][cols]에 currBlock에 존재하지 않는다면 attackBlock[i][col]에 board[linesToClear.get(i)][col]을 복사
                if (checkCurrBlockArea(col, linesToClear.get(i))) {
                    attackBlockChunk.block[i][col] = 0;
                }
                else {
                    attackBlockChunk.block[i][col] = board[linesToClear.get(i)][col];
                }
            }
        }

        // 추가된 줄의 수만큼 grayLinesNum을 증가시킴
        attackBlockChunk.grayLinesNum += linesToClear.size();
    }
    public BlockChunk getAttackBlock() {
        return attackBlockChunk;
    }
    public int getPlayerType() {
        return playerType;
    }
}
