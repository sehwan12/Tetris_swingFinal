package old_main;

import controller.BoardController;
import model.BoardModel;
import view.BoardView;
import view.SidePanelView;

public class Board {
    private BoardModel model;
    public BoardView view;
    private SidePanelView viewSide;
    private BoardController controller;

    public Board() {
        // 모델, 뷰, 컨트롤러 인스턴스 생성
        model = new BoardModel();
        view = new BoardView();
        viewSide = new SidePanelView();
        controller = new BoardController();

        // 뷰에 컨트롤러 설정
        view.setController(controller);
        // 게임 UI 표시
        view.setVisible(true);
    }
    public static void startGame() {
        // Tetris 게임 인스턴스 생성 및 게임 시작
        new Board();
    }

    public static void main(String[] args) {
        startGame();
    }
}
