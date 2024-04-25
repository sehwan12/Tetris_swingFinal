package controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import view.ScoreBoardView;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

//by chatGPT3.5
public class ScoreBoardControllerTest {
    private ScoreBoardController controller;

    @Before
    public void setUp() {
        controller = ScoreBoardController.getInstance();
    }

    @After
    public void tearDown() {
        controller.destroyView();
    }

    @Test
    public void testGetInstance() {
        // initFrame 호출
        controller.initFrame();

        ScoreBoardController instance1 = ScoreBoardController.getInstance();
        ScoreBoardController instance2 = ScoreBoardController.getInstance();

        // 두 인스턴스는 동일해야 함
        assertSame(instance1, instance2);
    }

    @Test
    public void testInitFrameWithoutParams() {
        if(controller.getView()!=null) controller.destroyView();

        // initFrame 호출 전에 view는 null이어야 함
        assertNull(controller.getView());

        // initFrame 호출
        controller.initFrame();

        // initFrame 호출 후에 view는 null이 아니어야 함
        assertNotNull(controller.getView());
    }

    @Test
    public void testInitFrameWithParams() {
        if(controller.getView()!=null) controller.destroyView();

        // initFrame 호출 전에 view는 null이어야 함
        assertNull(controller.getView());

        // initFrame 호출
        controller.initFrame("Player1", 100);

        // initFrame 호출 후에 view는 null이 아니어야 함
        assertNotNull(controller.getView());
    }

    @Test
    public void testInitFocusLogic() {
        // Arrange
        ScoreBoardView fakeView = new ScoreBoardView(); // 가짜 ScoreBoardView 생성
        controller.setView(fakeView); // controller에 가짜 ScoreBoardView 설정

        // Act
        controller.initFocus(); // initFocus() 실행

        // Assert
        InputMap im = fakeView.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        assertNotNull(im.get(KeyStroke.getKeyStroke("ESCAPE"))); // ESCAPE 키에 매핑된 KeyStroke 확인

        ActionMap am = fakeView.getRootPane().getActionMap();
        assertNotNull(am.get("esc")); // "esc"에 매핑된 AbstractAction 확인
    }

}