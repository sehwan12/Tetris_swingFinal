package controller;

import model.OutGameModel;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.SettingsView;

import javax.swing.*;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

class SettingsControllerTest2 {

    SettingsController controller;
    OutGameModel model;

    @BeforeEach
    void setUp() {
        controller = SettingsController.getInstance();
        model = OutGameModel.getInstance();
        controller.view = new SettingsView();
        controller.initFrame();
        controller.initFocus();
    }

    @Test
    public void testInitFocus() {
        // 테스트할 KeyStroke 배열 정의
        KeyStroke[] keyStrokes = {
                KeyStroke.getKeyStroke("UP"),
                KeyStroke.getKeyStroke("DOWN"),
                KeyStroke.getKeyStroke("OPEN_BRACKET"),
                KeyStroke.getKeyStroke("CLOSE_BRACKET"),
                KeyStroke.getKeyStroke("ENTER"),
                KeyStroke.getKeyStroke("ESCAPE")
        };

        // SettingsView 에서 InputMap 을 가져오고 테스트할 KeyStroke 을 포함하는지 확인
        InputMap inputMap = controller.view.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        for (KeyStroke keyStroke : keyStrokes) {
            assertNotNull(inputMap.get(keyStroke), "InputMap should contain " + keyStroke.toString());
        }

        // SettingsView 에서 ActionMap 을 가져오고 각 액션 이름을 포함하는지 확인
        ActionMap actionMap = controller.view.getRootPane().getActionMap();
        String[] actionNames = {"up", "down", "open_bracket", "close_bracket", "enter", "esc"};
        for (String actionName : actionNames) {
            assertNotNull(actionMap.get(actionName), "ActionMap should contain " + actionName);
        }
    }

    @Test
    public void testUpAction() {
        // 초기 버튼 포커스 설정
        model.setOptionFocus(0);
        model.setyCursor(1);

        // ActionMap 에서 "up" 액션을 가져와서 실행
        controller.view.getRootPane().getActionMap().get("up").actionPerformed(null);

        // 기대한 대로 버튼 포커스가 이동되었는지 확인
        assertEquals(0, model.getOptionFocus(), "Option focus should move up");
        assertEquals(0, model.getyCursor(), "yCursor should be reset to 0");
    }

    @Test
    public void testDownAction() {
        // 초기 버튼 포커스 설정
        model.setOptionFocus(0);
        model.setyCursor(0);

        // ActionMap 에서 "down" 액션을 가져와서 실행
        controller.view.getRootPane().getActionMap().get("down").actionPerformed(null);

        // 기대한 대로 버튼 포커스가 이동되었는지 확인
        assertEquals(0, model.getOptionFocus(), "Option focus should move down");
        assertEquals(1, model.getyCursor(), "yCursor should be incremented by 1");
    }

    @Test
    public void testOpenBracketAction() {
        // 초기 버튼 포커스 설정
        model.setOptionFocus(0);
        model.setyCursor(1);

        // ActionMap 에서 "open_bracket" 액션을 가져와서 실행
        controller.view.getRootPane().getActionMap().get("open_bracket").actionPerformed(null);

        // 기대한 대로 모델 상태가 변경되었는지 확인
        assertEquals(0, model.getyCursor(), "yCursor should be reset to 0");
        assertEquals(4, model.getOptionFocus(), "Option focus should move left by 1");
    }

    @Test
    public void testCloseBracketAction() {
        // 초기 버튼 포커스 설정
        model.setOptionFocus(0);
        model.setyCursor(1);

        // ActionMap 에서 "close_bracket" 액션을 가져와서 실행
        controller.view.getRootPane().getActionMap().get("close_bracket").actionPerformed(null);

        // 기대한 대로 모델 상태가 변경되었는지 확인
        assertEquals(0, model.getyCursor(), "yCursor should be reset to 0");
        assertEquals(1, model.getOptionFocus(), "Option focus should move right by 1");
    }

    @Test
    public void testEscAction() {
        // ActionMap 에서 "esc" 액션을 가져와서 실행
        controller.view.getRootPane().getActionMap().get("esc").actionPerformed(null);

        // 기대한 대로 모델 상태가 변경되었는지 확인
        assertEquals(0, model.getyCursor(), "yCursor should be reset to 0");
        assertNull(controller.view, "View should be disposed");
    }

    @Test
    public void testEnterAction_0() {
        // 초기 버튼 포커스 설정
        model.setOptionFocus(0);

        // 해상도
        int[][] resolutionData = { {400, 600}, {500, 750}, {600, 900}};
        String[] recommendResolution = { "Small", "Medium", "Big"};

        for(int yCursor =0; yCursor<3; yCursor++) {
            model.setyCursor(yCursor);

            // ActionMap 에서 "enter" 액션을 가져와서 실행
            controller.view.getRootPane().getActionMap().get("enter").actionPerformed(null);

            // 기대한 대로 모델 상태가 변경되었는지 확인
            assertEquals(yCursor, model.getyCursor(), "yCursor should be reset to 0");

            System.out.println(recommendResolution[yCursor]);

            assertEquals(resolutionData[yCursor][0], model.getResX(), "Resolution X should be updated");
            assertEquals(resolutionData[yCursor][1], model.getResY(), "Resolution Y should be updated");

        }

        // 해상도 small 로 되돌리기
        model.setyCursor(0);
        controller.view.getRootPane().getActionMap().get("enter").actionPerformed(null);
    }

    @Test
    public void testEnterAction_1() {
        // 초기 버튼 포커스 설정
        model.setOptionFocus(1);
        model.setyCursor(0);

        // ActionMap 에서 "enter" 액션을 가져와서 실행
        controller.view.getRootPane().getActionMap().get("enter").actionPerformed(null);

        // 키 매핑 모드가 올바르게 설정되었는지 확인
        assertTrue(controller.isKeyMappingMode, "Key mapping mode should be set to true");
    }

    @Test
    public void testEnterAction_2() {
        // 초기 버튼 포커스 설정
        model.setOptionFocus(2);
        model.setyCursor(0);

        // ActionMap 에서 "enter" 액션을 가져와서 실행
        controller.view.getRootPane().getActionMap().get("enter").actionPerformed(null);

        // 색맹모드가 켜졌는지 확인
        assertTrue(model.isBlindMode(), "Blind mode should be updated");

        // 초기 버튼 포커스 설정
        model.setOptionFocus(2);
        model.setyCursor(1);

        // ActionMap 에서 "enter" 액션을 가져와서 실행
        controller.view.getRootPane().getActionMap().get("enter").actionPerformed(null);

        // 색맹모드가 꺼졌는지 확인
        assertFalse(model.isBlindMode(), "Blind mode should be updated");

    }

    ////////////////////////////////////////////////////////////
    //
    // testEnterAction_3 즉 case 3은 스코어보드, 옵션 초기화인데
    // 이로인한 오류 발생 가능성이 있어서 게임이 거의 다 완성되었을 때 완성
    //
    ////////////////////////////////////////////////////////////

    @Test
    public void testEnterAction_3() {
        // 초기 버튼 포커스 설정
        model.setOptionFocus(3);
        model.setyCursor(0);
    }

    @Test
    public void testEnterAction_4() {
        // 초기 버튼 포커스 설정
        model.setOptionFocus(4);
        String[] difficultyString = {"Easy", "Normal", "Hard"};

        for (int yCursor = 0; yCursor < 3; yCursor++) {
            model.setyCursor(yCursor);

            // ActionMap 에서 "enter" 액션을 가져와서 실행
            controller.view.getRootPane().getActionMap().get("enter").actionPerformed(null);

            // 난이도 설정이 올바르게 수행되었는지 확인
            assertEquals(difficultyString[yCursor], model.getDifficulty(), "Difficulty should be updated");
        }
    }

    @Test
    void testKeyMappingMode() {
        SettingsController settingsController = SettingsController.getInstance();

        // 가상의 KeyEvent 객체 생성 (예: Enter 키 누름)
        KeyEvent enterKeyEvent = new KeyEvent(new JPanel(), KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_ENTER, KeyEvent.CHAR_UNDEFINED);

        // 키 매핑 모드를 활성화 시킴
        settingsController.isKeyMappingMode = true;

        // 키 입력 처리
        settingsController.view.getKeyListeners()[0].keyPressed(enterKeyEvent);

        // 키 매핑 모드가 비활성화되었는지 확인
        assertFalse(settingsController.isKeyMappingMode);
    }
}
