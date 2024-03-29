package controller;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;

import javax.swing.*;

import model.BoardModel;
import view.BoardView;
import model.ModelStateChangeListener;
import view.SidePanelView;

public class BoardController implements ModelStateChangeListener {

    private BoardModel model;
    private BoardView view;
    private SidePanelView viewSidePanel;
    private KeyListener playerKeyListener;

    private int selectedOption = 1;
    public BoardController(BoardModel model, BoardView view, SidePanelView viewSide) {
        this.model = model;
        this.view = view;
        this.viewSidePanel = viewSide;
        // view와 controller의 상호작용
        this.view.setController(this);
        // view에 sidepanel 추가
        view.getContentPane().add(viewSidePanel, BorderLayout.EAST);

        playerKeyListener = new PlayerKeyListener();
        // addKeyListener, setFocusable, requestFocus를 BoardView의 메서드로 대체
        view.addKeyListenerToFrame(playerKeyListener);
        this.model.addModelStateChangeListener(this);
    }


    public int getSelectedOption() {
        return selectedOption;
    }

    public void setSelectedOption(int selectedOption) {
        this.selectedOption = selectedOption;
    }

    public void onModelStateChanged() {
        // Model에서 상태 변경 시 호출될 메서드
        // 여기서 View의 갱신 로직 호출
        moveDownControl();
        updateBoard();
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
        int response = view.showGameOverDialog();

        if (response == JOptionPane.YES_OPTION) {
            view.setVisible(false);
        } else {
            // 게임 종료
            System.exit(0);
        }
    }

    public void gameExit() {
        //Yes No버튼 선택할 때 마우스입력만 되는건가?
        //만약 마우스입력만 된다면 이후에 키보드로도 선택할 수 있도록 수정
        model.setPaused(true);
        int response = view.showGameExitDialog();
        if (response == JOptionPane.YES_OPTION) {
            System.exit(0);

        } else { }
    }

    public void moveDownControl() {
        if (!model.moveDown()) {
            gameOver();
        }
    }

    public void moveBottomControl() {
        if (!model.moveBottom()) {
            gameOver();
        }
    }
    // 게임 상태 업데이트 후 View update를 위한 메서드
    public void updateBoard() {
        // 게임 로직 처리...
        // 예를 들어, 게임 보드 상태를 업데이트하는 로직 수행

        // View에 게임 보드 그리기 요청
        view.drawBoard(model.getBoard(), model.getBoard_color(), model.getBoard_text());
        // SidePanel에 다음 블럭 넘기기
        viewSidePanel.drawBoard(model.getNextBlock());
        // 사이드 패널의 점수를 view에 넘겨야 한다
        viewSidePanel.setScoreText(model.getTotalscore());
    }


    public class PlayerKeyListener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (!model.isPaused()) {
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
                                resumeGame(); //이후에 메인메뉴 추가
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
    public static void main(String[] args) {
        BoardModel model = new BoardModel(); // 모델 생성
        BoardView view = new BoardView(); // 뷰 생성
        SidePanelView viewSide = new SidePanelView();
        BoardController controller = new BoardController(model, view, viewSide); // 컨트롤러 생성 및 모델과 뷰 연결

        view.setController(controller); // 뷰에 컨트롤러 설정
        view.setVisible(true); // 뷰를 보이게 설정
    }

}
