package model.VersusMode;

import model.SingleMode.BoardModel;
import model.SingleMode.ModelStateChangeListener;

public class VsBoardModel extends BoardModel {

    public int playerType;
    public VsBoardModel(int playerType) {
        super();
        this.playerType = playerType;
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
        }
    }
}
