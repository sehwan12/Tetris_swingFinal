package IO;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ImportDefaultSettingsTest {
    Map<String, String> settings;

    @Before
    public void setUp() {
        settings = new HashMap<>();
    }
    @Test
    public void testImportDefaultKeyMap(){
        read_File_settings("defaultSettings.ini");

        ImportSettings.importDefaultKeyMap();
        for (Map.Entry<String, String> entry : settings.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            // 각 키-값 쌍에 대한 작업 수행, 테스트
            assertEquals(value, ImportSettings.getSetting(key));
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
}
