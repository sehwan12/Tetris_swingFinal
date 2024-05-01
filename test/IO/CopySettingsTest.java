package IO;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import IO.CopySettings;
import static org.junit.jupiter.api.Assertions.*;

public class CopySettingsTest {
    @Test
    public void testRestore() {
        // Arrange: defaultSettings.ini 파일의 경로 지정
        String defaultSettingsFilePath = "defaultSettings.ini";
        // defaultSettings.ini 파일의 내용 저장
        String defaultSettingsContent = readFileContents(defaultSettingsFilePath);

        // Act: 복구 메서드 호출
        CopySettings.restore();

        // Assert: settings.ini 파일의 내용이 defaultSettings.ini 파일의 내용과 일치하는지 확인
        String settingsFilePath = "settings.ini";
        String settingsFileContent = readFileContents(settingsFilePath);
        System.out.println(settingsFileContent);
        assertEquals(defaultSettingsContent, settingsFileContent);
    }

    // 파일의 내용을 읽어오는 유틸리티 메서드
    private String readFileContents(String filePath) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            fail("Failed to read file: " + filePath);
        }
        return content.toString();
    }

}