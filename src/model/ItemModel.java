package model;

import model.blocks.*;

import java.util.Random;

public class ItemModel extends BoardModel {

    private int beforeLineCount;
    public ItemModel() {
        super();
        beforeLineCount = 0;
    }
    @Override
    protected Block getRandomBlock() {
        int maxThreshold = (beforeLineCount / 10) * 10;
        if ((linesCleared >= (maxThreshold + 10))) {
            beforeLineCount = linesCleared;
            return new WeightBlock();
        }
        else {
            return super.getRandomBlock();
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
        }
        else {
            while (!collisionCheck(0, 1)) { y++; }
            placeBlock();
            afterTime=System.currentTimeMillis();
            checkForScore();
            // LineClear 과정
            startLineClearAnimation();
        }

    }
}
