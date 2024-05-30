package model.VersusMode;

import model.SingleMode.ModelStateChangeListener;

public interface VersusModelStateChangeListener extends ModelStateChangeListener {
    void notifyAttck(int playerType);
}
