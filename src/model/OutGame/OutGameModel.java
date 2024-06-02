package model.OutGame;

import IO.ImportSettings;
import IO.ExportSettings;
import IO.CopySettings;
import IO.ScoreIO;

import java.util.HashMap;

public class OutGameModel {

    private static OutGameModel instance;

    private static HashMap<String, String> keyMap;

    private static boolean blindMode;
    private static int resX;
    private static int resY;

    private static int difficulty = 0; // 0 : easy, 1 : normal, 2 : Hard
    private int curGameMode = 0;

    private int curFocus = 0;

    private static final int buildType = 3;
    private static String[] buildString = { "Release", "Development", "Feature", "SE 7 team"};

    private static String[] menuString = { "Start Game", "Item Mode", "Versus Mode", "Settings", "ScoreBoard", "Quit" };

    private static String[] versusMenuString = { "Basic Mode", "Item Mode", "TimeLimit Mode" };

    private static String[] gameModeString = { "Single Play", "Duo Play" };

    private static String[] difficultyString = { "Easy", "Normal", "Hard" };

    // 초기화에 스코어보드 기록 초기화, 설정 되돌리기 포함시키기
    private static String[] optionString = { "해상도", "1p 키", "2p 키", "색맹모드", "초기화", "난이도"};

    private final static int[][] resolutionData = { {400, 600}, {500, 750}, {600, 900}};

    private static String[] recommendResolution = { "Small", "Medium", "Big"};

    private static String[] keyString = { "moveLeft", "moveRight", "moveDown", "drop", "rotate", "pause" };

    private static String[] blindString = { "색맹모드 켜기", "색맹모드 끄기"};

    private static String[] resetString = { "스코어보드 기록 초기화", "모두 기본 설정으로 초기화" };

    private static String[][] stringType = { recommendResolution, keyString, keyString, blindString, resetString, difficultyString };

    private int optionFocus = 0; // 0 : 해상도, 1 : 키 매핑, 2 : 색맹 모드, 3 : 초기화

    private int yCursor = 0; // 0 : section 이동, 1, 2, 3... --> 세부 옵션 이동


    private OutGameModel() {
        resX = Integer.parseInt(ImportSettings.getSetting("ResolutionSizeX"));
        resY = Integer.parseInt(ImportSettings.getSetting("ResolutionSizeY"));
        keyMap = new HashMap<>();
        for (int i = 0; i < keyString.length; i++) {
            keyMap.put(keyString[i] + "1P", ImportSettings.getSetting(keyString[i] + "1P"));
            keyMap.put(keyString[i] + "2P", ImportSettings.getSetting(keyString[i] + "2P"));
        }
        if (ImportSettings.getSetting("blindMode").equals("false")) {
            blindMode = false;
        }
        else {
            blindMode = true;
        }
    }

    public static synchronized OutGameModel getInstance() {
        if (instance == null) {
            instance = new OutGameModel();
        }
        return instance;
    }

    public static int getDifficultyInt() {
        return difficulty;
    }

    public String[] getBuildString() {
        return buildString;
    }


    public String[] getMenuString() {
        return menuString;
    }

    public static int getResX() {
        return resX;
    }

    public void setResX(int resX) {
        this.resX = resX;
    }

    public static int getResY() {
        return resY;
    }

    public void setResY(int resY) {
        this.resY = resY;
    }

    public int getCurFocus() {
        return curFocus;
    }

    public String[] getGameModeString() {
        return gameModeString;
    }

    public static String[] getVersusMenuString() {
        return versusMenuString;
    }

    public int getBuildType() {
        return buildType;
    }

    public static String[] getOptionString() {
        return optionString;
    }

    // 유닛 테스트를 위해 setOptionFocus 추가
    public void setOptionFocus(int optionFocus) {
        this.optionFocus = optionFocus;
    }

    public int getOptionFocus() {
        return optionFocus;
    }

    public void moveOptionFocus(int x) {
        if (optionFocus + x < 0) {
            optionFocus = optionString.length - 1;
        }
        else if (optionFocus + x >= optionString.length) {
            optionFocus = 0;
        }
        else {
            optionFocus += x;
        }
    }

    public void moveButtonFocus(int y) {
        int limit = stringType[optionFocus].length;
        if (yCursor + y < 0) {
            yCursor = limit - 1;
        }
        else if (yCursor + y >= limit) {
            yCursor = 0;
        }
        else {
            yCursor += y;
        }
    }

    public static String[] getRecommendResolution() {
        return recommendResolution;
    }

    public static void setRecommendResolution(String[] recommendResolution) {
        OutGameModel.recommendResolution = recommendResolution;
    }

    public static String[] getKeyString() {
        return keyString;
    }

    public static String[] getBlindString() {
        return blindString;
    }

    public static String[] getResetString() {
        return resetString;
    }

    public static String[] getDifficultyString() {
        return difficultyString;
    }

    public int getyCursor() {
        return yCursor;
    }

    public void setyCursor(int yCursor) {
        this.yCursor = yCursor;
    }

    public static HashMap<String, String> getKeyMap() {
        return keyMap;
    }

    public static boolean isBlindMode() {
        return blindMode;
    }

    public static void setBlindMode() {
        if (instance.yCursor == 0) {
            blindMode = true;
            ExportSettings.saveSettings("blindMode", "true");
        }
        else {
            blindMode = false;
            ExportSettings.saveSettings("blindMode", "false");
        }
    }

    public static void setDifficulty() {
        difficulty = instance.yCursor;
        ExportSettings.saveSettings("difficulty", String.valueOf(difficulty));
        // 추후 세팅 Export 추가해야함!
    }

    public static void setDifficulty(int difficulty) {
        OutGameModel.difficulty = difficulty;
        ExportSettings.saveSettings("difficulty", String.valueOf(difficulty));
        // 추후 세팅 Export 추가해야함!
    }

    public static String getDifficulty(){
        return difficultyString[difficulty];
    }


    public static void setKeyMap(String KeyText) {
        keyMap.put(keyString[instance.yCursor], KeyText);
        ExportSettings.saveSettings(keyString[instance.yCursor], KeyText);
    }

    public static void setP1KeyMap(String KeyText) {
        keyMap.put(keyString[instance.yCursor] + "1P", KeyText);
        ExportSettings.saveSettings(keyString[instance.yCursor] + "1P", KeyText);
    }

    public static void setP2KeyMap(String KeyText) {
        keyMap.put(keyString[instance.yCursor] + "2P", KeyText);
        ExportSettings.saveSettings(keyString[instance.yCursor] + "2P", KeyText);
    }

    public void setResolution() {
        resX = resolutionData[yCursor][0];
        resY = resolutionData[yCursor][1];
        // 아래에 export Setting 코드 넣기

        ExportSettings.saveSettings("ResolutionSizeX", Integer.toString(resX));
        ExportSettings.saveSettings("ResolutionSizeY", Integer.toString(resY));
    }

    public void resetScoreBoard() {
        ScoreIO.clearJsonFile();
    }

    public void resetDefault() {
        ImportSettings.importDefaultKeyMap();
        resX = Integer.parseInt(ImportSettings.getSetting("ResolutionSizeX"));
        resY = Integer.parseInt(ImportSettings.getSetting("ResolutionSizeY"));
        keyMap = new HashMap<>();
        for (int i = 0; i < keyString.length; i++) {
            keyMap.put(keyString[i] + "1P", ImportSettings.getSetting(keyString[i] + "1P"));
            keyMap.put(keyString[i] + "2P", ImportSettings.getSetting(keyString[i] + "2P"));

        }
        if (ImportSettings.getSetting("blindMode").equals("false")) {
            blindMode = false;
        }
        else {
            blindMode = true;
        }
        difficulty=Integer.parseInt(ImportSettings.getSetting("difficulty"));
        CopySettings.restore();
    }

    public void moveFocus(int x) {
        if ((x > 0 && (curFocus + x) < menuString.length) || (x < 0 && curFocus + x >= 0))
            curFocus += x;
    }

    public void moveVersusFocus(int x) {
        if ((x > 0 && (curFocus + x) < versusMenuString.length) || (x < 0 && curFocus + x >= 0))
            curFocus += x;
    }


    public String[][] getStringType() {
        return stringType;
    }
}
