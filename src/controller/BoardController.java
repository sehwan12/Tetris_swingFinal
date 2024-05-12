package controller;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import javax.swing.*;

import IO.ScoreIO;
import controller.OutGame.MenuController;
import controller.OutGame.ScoreBoardController;
import model.SingleMode.BoardModel;
import model.OutGame.OutGameModel;
import view.BoardView;
import model.SingleMode.ModelStateChangeListener;
import view.SidePanelView;

public class BoardController implements ModelStateChangeListener {

    protected BoardModel model;
    protected BoardView view;
    protected SidePanelView sidePanelView;
    protected KeyListener playerKeyListener;


    protected int selectedOption = 1;

    protected String currentKey;

    public BoardController() {
        model = new BoardModel();
        initView();
        this.model.addModelStateChangeListener(this);
    }
    public BoardController(BoardModel model) {
        this.model = model;
        initView();
        this.model.addModelStateChangeListener(this);
    }
    public BoardController(BoardModel model, BoardView view, SidePanelView viewSidePanel) {
        this.model = model;
        this.view = view;
        this.sidePanelView = viewSidePanel;
        view.setController(this);
        view.getContentPane().add(viewSidePanel, BorderLayout.EAST);
        view.setVisible(true);
        playerKeyListener = new PlayerKeyListener();
        // addKeyListener, setFocusable, requestFocus를 BoardView의 메서드로 대체
        view.addKeyListenerToFrame(playerKeyListener);
        this.model.addModelStateChangeListener(this);
    }



    public void initView() {
        view = new BoardView();
        sidePanelView = new SidePanelView();
        view.setController(this);
        view.getContentPane().add(sidePanelView, BorderLayout.EAST);
        view.setVisible(true);
        playerKeyListener = new PlayerKeyListener();
        // addKeyListener, setFocusable, requestFocus를 BoardView의 메서드로 대체
        view.addKeyListenerToFrame(playerKeyListener);
    }


    public int getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(int selectedOption) {
        this.selectedOption = selectedOption;
    }

    public void onModelStateChanged(int playerType) {
        // Model에서 상태 변경 시 호출될 메서드
        // 여기서 View의 갱신 로직 호출
        model.moveDown();
        updateBoard();
    }
    public void notifyUpdateBoard(int playerType) {
        updateBoard();
    }

    public void notifyGameOver(int playerType) {
        gameOver();
    }

    @Override
    public void onVersusUpdateBoard(int playerType) {

    }

    public void pauseGame() {
        if (!model.isPaused()) {
            model.setPaused(true);
            view.showPauseScreen(true);
        }
    }

    public void resumeGame() {
        if (model.isPaused()) {
            model.setPaused(false);
            view.showPauseScreen(false);
        }
    }

    public void gameOver() {
        model.setPaused(true);
        view.setVisible(false);
        ScoreBoardController temp = ScoreBoardController.getInstance();
        temp.initFrame();

        int checkUpdatebool = view.showConfirmDialog(
                "점수를 저장하시겠습니까?\n (No : 게임 종료)", "Ask for updating score"
        );

        if (checkUpdatebool == JOptionPane.YES_OPTION) {
            String name = JOptionPane.showInputDialog("이름을 입력하세요");
            ScoreIO.writeScore(name, model.getTotalscore(),OutGameModel.getDifficulty(), model.getGamemode());
            temp.destroyView();
            temp.initFrame(name, model.getTotalscore());
        }

        int response = view.showConfirmDialog(
                "Game Over. 시작 메뉴로 돌아가시겠습니까?\n (No : 게임 종료)",
                "Game Over"
        );

        if (response == JOptionPane.YES_OPTION) {
            temp.destroyView();
            MenuController.getInstance().initFrame();
        } else {
            // 게임 종료
            System.exit(0);
        }
    }

    public void gameExit() {
        //Yes No버튼 선택할 때 마우스입력만 되는건가?
        //만약 마우스입력만 된다면 이후에 키보드로도 선택할 수 있도록 수정
        model.setPaused(true);
        int response = view.showConfirmDialog(
                "게임을 종료하시겠습니까?", "Game Exit");
        if (response == JOptionPane.YES_OPTION) {
            System.exit(0);

        } else { }
    }

    // 게임 상태 업데이트 후 View update를 위한 메서드
    public void updateBoard() {
        // 게임 로직 처리...
        // 예를 들어, 게임 보드 상태를 업데이트하는 로직 수행

        // View에 게임 보드 그리기 요청
        view.drawBoard(model.getBoard(), model.getBoard_color(), model.getBoard_text());
        // SidePanel에 다음 블럭 넘기기
        sidePanelView.drawBoard(model.getNextBlock());
        // 사이드 패널의 점수를 view에 넘겨야 한다
        sidePanelView.setScoreText(model.getTotalscore());
    }

    public BoardView getView() {
        return view;
    }


    public class PlayerKeyListener implements KeyListener {
        private HashMap<String, String> keyMap;

        private String moveLeft;
        private String moveRight;
        private String moveDown;
        private String drop;
        private String rotate;
        private String pause;

        public PlayerKeyListener() {
          keyMap = OutGameModel.getInstance().getKeyMap();
            // 아래는 getKeyText를 통해 저장된 값임
          moveLeft = keyMap.get("moveLeft1P");
          moveRight = keyMap.get("moveRight1P");
          drop = keyMap.get("drop1P");
          moveDown = keyMap.get("moveDown1P");
          rotate = keyMap.get("rotate1P");
          pause = keyMap.get("pause1P");
        }


        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            currentKey = KeyEvent.getKeyText(e.getKeyCode());
            if (!model.isPaused()) {
                if (currentKey.equals(moveDown)) {
                    if(!model.isDowned()){
                        model.setDowned(true);
                    }
                    model.moveDown();
                    updateBoard();
                }
                else if (currentKey.equals(moveRight)) {
                    model.moveRight();
                    updateBoard();
                }
                else if (currentKey.equals(moveLeft)) {
                    model.moveLeft();
                    updateBoard();
                }
                else if (currentKey.equals(rotate)) {
                    model.moveRotate();
                    updateBoard();
                }
                else if (currentKey.equals(drop)) {
                    if(!model.isDowned()){
                        model.setDowned(true);
                    }
                    // 위치 이동 메서드
                    model.moveBottom();
                    updateBoard();
                }
                else if (currentKey.equals(pause)) {
                    pauseGame();
                }
                /*
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_DOWN:
                        if(!model.isDowned()){
                           model.setDowned(true);
                        }
                        moveDownControl();
                        updateBoard();
                        break;
                    case KeyEvent.VK_RIGHT:
                        model.moveRight();
                        updateBoard();
                        break;
                    case KeyEvent.VK_LEFT:
                        model.moveLeft();
                        updateBoard();
                        break;
                    case KeyEvent.VK_UP:
                        model.moveRotate();
                        updateBoard();
                        break;
                    case KeyEvent.VK_ENTER:
                        if(!model.isDowned()){
                            model.setDowned(true);
                        }
                        // 위치 이동 메서드
                        moveBottomControl();
                        updateBoard();
                        break;
                        // 두벌식에서 P를 누르는 경우 인식X --> ESC 키로 변경
                    case KeyEvent.VK_ESCAPE:
                        pauseGame();
                        break;
                }
                */
            }
            else {
                // 일시 정지 상태인 경우, 스위치 문을 사용하여 추가 키를 처리합니다.
                // KeyEvent.VK_P까지 by chatGPT3.5 이후는 추가 작성
                switch (e.getKeyCode()) {
                    // 두벌식에서 P를 누르는 경우 인식X --> ESC 키로 변경
                    case KeyEvent.VK_ESCAPE:
                        resumeGame();
                        break;
                    case KeyEvent.VK_DOWN:
                        selectedOption++;
                        if(selectedOption>3) selectedOption=1;
                        view.glassRepaint();
                        break;
                    case KeyEvent.VK_UP:
                        selectedOption--;
                        if(selectedOption<1) selectedOption=3;
                        view.glassRepaint();
                        break;
                    case KeyEvent.VK_ENTER:
                        switch(selectedOption){
                            case 1: //메인메뉴
                                view.setVisible(false);
                                MenuController.getInstance().initFrame();
                                break;
                            case 2: //재시작
                                resumeGame();
                                break;
                            case 3: //게임종료
                                gameExit();
                                break;
                        }
                }
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    }

}
