package IO;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.*;

public class ExportSettingsTest {
    Map<String, String> settings;
    Map<String, String> settings2;

    @Test
    public void testsaveSettings() {
        settings = new HashMap<>();
        settings2 = new HashMap<>();

        read_File_settings("settings.ini");
        for (Map.Entry<String, String> entry : settings.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            // 각 키-값 쌍에 대한 작업 수행, 테스트
            ExportSettings.saveSettings(key,value+"asdf");
            read_modified_File_settings("settings.ini");
            assertEquals(value+"asdf",settings2.get(key));
        }

        for (Map.Entry<String, String> entry : settings.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            // 각 키-값 쌍에 대해 원래 값으로 되돌리기
            ExportSettings.saveSettings(key,value);
        }

    }

    private void read_File_settings(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("=")) {
                    String[] parts = line.split("=");
                    settings.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            fail("Failed to read file: " + filePath);
        }
    }

    private void read_modified_File_settings(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("=")) {
                    String[] parts = line.split("=");
                    settings2.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            fail("Failed to read file: " + filePath);
        }
    }
}