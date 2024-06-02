package model.VersusMode;

import model.OutGame.OutGameModel;
import model.SingleMode.ModelStateChangeListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VsTimeBoardModel extends VsBoardModel {
    public long getStartClock() {
        return startClock;
    }

    public void setStartClock(long startClock) {
        this.startClock = startClock;
    }

    private long startClock;
    public static final int TIME_LIMIT = 60;
    public VsTimeBoardModel(int playerType) {
        super(playerType);
        startClock = System.currentTimeMillis();
    }

    public boolean checkTimeLimit() {
        return (System.currentTimeMillis() - startClock) / 1000 > TIME_LIMIT;
    }

    @Override
    protected void notifyStateChanged() {
        if (checkTimeLimit()) {
            for (ModelStateChangeListener listener : listeners) {
                listener.notifyGameOver(playerType);
            }
            return;
        }
        for (ModelStateChangeListener listener : listeners) {
            listener.onModelStateChanged(playerType);
        }
    }

}
