package model.SingleMode;

import java.util.List;

// Written By GPT 4.0
public interface ModelStateChangeListener {
    void onModelStateChanged(int playerType);
    void notifyUpdateBoard(int playerType);

    void notifyGameOver(int playerType);

    void onVersusUpdateBoard(int playerType);
}