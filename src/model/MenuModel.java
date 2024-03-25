package model;

import javax.swing.*;
import java.awt.*;
import InProgress.ImportSettings;

public class MenuModel {
    private ImportSettings set;
    private int resX;
    private int resY;
    private int curGameMode = 0;

    private int curFocus = 0;

    private static final int buildType = 2;
    private static String[] buildString = { "Release", "Development", "Feature", "InProgress"};

    private static String[] menuString = { "Start Game", "Settings", "ScoreBoard", "Quit" };

    private static String[] gameModeString = { "Single Play", "Duo Play"};


    public MenuModel() {
        set = ImportSettings.getInstance();
        resX = Integer.parseInt(set.getSetting("ResolutionSizeX"));
        resY = Integer.parseInt(set.getSetting("ResolutionSizeY"));
    }

    public MenuModel(int resX, int resY) {
        this.resX  = resX;
        this.resY = resY;
    }

    public String[] getBuildString() {
        return buildString;
    }


    public String[] getMenuString() {
        return menuString;
    }

    public int getResX() {
        return resX;
    }

    public void setResX(int resX) {
        this.resX = resX;
    }

    public int getResY() {
        return resY;
    }

    public void setResY(int resY) {
        this.resY = resY;
    }

    public int getCurGameMode() {
        return curGameMode;
    }

    public void setCurGameMode(int curGameMode) {
        this.curGameMode = curGameMode;
    }

    public int getCurFocus() {
        return curFocus;
    }

    public void setCurFocus(int curFocus) {
        this.curFocus = curFocus;
    }

    public String[] getGameModeString() {
        return gameModeString;
    }

    public void setGameModeString(String[] gameModeString) {
        MenuModel.gameModeString = gameModeString;
    }

    public int getBuildType() {
        return buildType;
    }

    public void moveFocus(int x) {
        if ((x > 0 && (curFocus + x) < menuString.length) || (x < 0 && curFocus + x >= 0))
            curFocus += x;
    }

}
