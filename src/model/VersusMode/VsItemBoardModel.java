package model.VersusMode;

import model.SingleMode.ItemBoardModel;
import model.SingleMode.ModelStateChangeListener;

public class VsItemBoardModel extends ItemBoardModel {
    int playerType;
    public VsItemBoardModel(int playerType) {
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
            System.out.println(playerType + "번 플레이어 패배");
        }
    }
}