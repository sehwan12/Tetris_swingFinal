package model;

import java.util.List;

// Written By GPT 4.0
public interface ModelStateChangeListener {
    void onModelStateChanged();
    void notifyUpdateBoard();

    void notifyGameOver();
}