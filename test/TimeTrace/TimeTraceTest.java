package TimeTrace;

import controller.BoardController;
import model.SingleMode.BoardModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import view.BoardView;
import view.SidePanelView;

import java.awt.event.KeyEvent;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TimeTraceTest {

    private BoardModel model;
    private BoardView view;
    private SidePanelView sidePanelView;
    private BoardController controller;
    private BoardController.PlayerKeyListener keyListener;

    private static final int keyResponseTimeLimit = 50;

    @BeforeEach
    public void setUp() {
        model = mock(BoardModel.class);
        view = mock(BoardView.class);
        sidePanelView = mock(SidePanelView.class);
        controller = new BoardController(model, view, sidePanelView);
        keyListener = controller.new PlayerKeyListener();
    }
    // stateChange 소요 시간 측정
    @Test
    public void stateChangeTest() {
        BoardController controller = new BoardController();
        long start = System.currentTimeMillis();
        controller.onModelStateChanged(0);
        long end = System.currentTimeMillis();
        assertTrue(end - start < 100);
    }

    // keyPress에 따른 응답 속도 측정
    @Test
    public void keyResponseTest() {
        BoardController controller = new BoardController();
        controller.onModelStateChanged(0);
        BoardController.PlayerKeyListener listener = controller.new PlayerKeyListener();
        KeyEvent mockKeyEvent = mock(KeyEvent.class);
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_DOWN);
        long start = System.currentTimeMillis();
        listener.keyPressed(mockKeyEvent);
        // 키보드 입력에 따른 반응
        long end = System.currentTimeMillis();
        assertTrue(end - start < keyResponseTimeLimit);

        start = System.currentTimeMillis();
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_RIGHT);
        listener.keyPressed(mockKeyEvent);
        // 키보드 입력에 따른 반응
        end = System.currentTimeMillis();
        assertTrue(end - start < keyResponseTimeLimit);

        start = System.currentTimeMillis();
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_LEFT);
        listener.keyPressed(mockKeyEvent);
        // 키보드 입력에 따른 반응
        end = System.currentTimeMillis();
        assertTrue(end - start < keyResponseTimeLimit);

        start = System.currentTimeMillis();
        when(mockKeyEvent.getKeyCode()).thenReturn(KeyEvent.VK_UP);
        listener.keyPressed(mockKeyEvent);
        // 키보드 입력에 따른 반응
        end = System.currentTimeMillis();
        assertTrue(end - start < keyResponseTimeLimit);
    }
}
