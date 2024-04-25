package controller;

import org.junit.Before;
import org.junit.Test;
import view.SettingsView;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

//by chatGPT3.5
public class SettingsControllerTest {
    private SettingsController controller;

    @Before
    public void setUp() {
        // 테스트 전에 실행되는 초기화 메서드
        controller = SettingsController.getInstance();
    }

    @Test
    public void testGetInstance() {
        // given
        SettingsController instance1 = SettingsController.getInstance();

        assertNotNull(instance1);
    }

    @Test
    public void testInitFocus() {
        // Mock 생성
        ActionMap mockActionMap = mock(ActionMap.class);
        InputMap mockInputMap = mock(InputMap.class);
        JRootPane mockRootPane = mock(JRootPane.class);

        // SettingsView에 대한 Mock 설정
        SettingsView mockView = mock(SettingsView.class);
        when(mockView.getRootPane()).thenReturn(mockRootPane);
        when(mockRootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)).thenReturn(mockInputMap);
        when(mockRootPane.getActionMap()).thenReturn(mockActionMap);

        // SettingsController에 Mock 설정 적용
        controller.view = mockView;

        // 테스트할 액션들
        Action upAction = mock(AbstractAction.class);
        Action downAction = mock(AbstractAction.class);
        Action openBracketAction = mock(AbstractAction.class);
        Action closeBracketAction = mock(AbstractAction.class);
        Action enterAction = mock(AbstractAction.class);
        Action escAction = mock(AbstractAction.class);

        // Mock ActionMap에 각 액션 매핑
        when(mockActionMap.get("up")).thenReturn(upAction);
        when(mockActionMap.get("down")).thenReturn(downAction);
        when(mockActionMap.get("open_bracket")).thenReturn(openBracketAction);
        when(mockActionMap.get("close_bracket")).thenReturn(closeBracketAction);
        when(mockActionMap.get("enter")).thenReturn(enterAction);
        when(mockActionMap.get("esc")).thenReturn(escAction);

        // Mock InputMap에 각 키 매핑
        when(mockInputMap.get(KeyStroke.getKeyStroke("UP"))).thenReturn("up");
        when(mockInputMap.get(KeyStroke.getKeyStroke("DOWN"))).thenReturn("down");
        when(mockInputMap.get(KeyStroke.getKeyStroke("OPEN_BRACKET"))).thenReturn("open_bracket");
        when(mockInputMap.get(KeyStroke.getKeyStroke("CLOSE_BRACKET"))).thenReturn("close_bracket");
        when(mockInputMap.get(KeyStroke.getKeyStroke("ENTER"))).thenReturn("enter");
        when(mockInputMap.get(KeyStroke.getKeyStroke("ESCAPE"))).thenReturn("esc");

        // 키 입력 시뮬레이션 및 테스트
        controller.initFocus();

        // up 키 입력
        mockRootPane.getActionMap().get("up").actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "up"));
        verify(upAction, times(1)).actionPerformed(any(ActionEvent.class));

        // down 키 입력
        mockRootPane.getActionMap().get("down").actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "down"));
        verify(downAction, times(1)).actionPerformed(any(ActionEvent.class));

        // open_bracket 키 입력
        mockRootPane.getActionMap().get("open_bracket").actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "open_bracket"));
        verify(openBracketAction, times(1)).actionPerformed(any(ActionEvent.class));

        // close_bracket 키 입력
        mockRootPane.getActionMap().get("close_bracket").actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "close_bracket"));
        verify(closeBracketAction, times(1)).actionPerformed(any(ActionEvent.class));

        // enter 키 입력
        mockRootPane.getActionMap().get("enter").actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "enter"));
        verify(enterAction, times(1)).actionPerformed(any(ActionEvent.class));

        // esc 키 입력
        mockRootPane.getActionMap().get("esc").actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "esc"));
        verify(escAction, times(1)).actionPerformed(any(ActionEvent.class));
    }

    @Test
    public void testInitFrame() {
        // given
        //controller = SettingsController.getInstance();

        SettingsController controller = SettingsController.getInstance();

        // when
        controller.initFrame();

        // then (테스트는 에러가 발생하지 않고 정상적으로 실행되는지 확인)
        assertNotNull(controller.view);  // view가 null이 아니어야 함
    }

}