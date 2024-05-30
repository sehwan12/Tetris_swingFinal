package IO;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ImportSettings {
    private static Map<String, String> settings = new HashMap<>();

    // 싱글톤
    private static ImportSettings Singleton = new ImportSettings();

    private ImportSettings() {
        try {
            String appDataPath = System.getProperty("user.home") + "/Library/Application Support/Tetris";
            File appDataDir = new File(appDataPath);
            if (!appDataDir.exists()) {
                appDataDir.mkdirs();  // 디렉토리가 없다면 생성
            }
            File settingsFile = new File(appDataPath, "settings.ini");
            if (!settingsFile.exists()) {
                try {
                    // 리소스 디렉토리에서 설정 파일을 복사
                    Files.copy(Paths.get("/Applications/Tetris.app/Contents/app/resources/settings.ini"), settingsFile.toPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            File file = new File(appDataPath, "settings.ini");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null) {
                if (st.contains("=")) {
                    String[] parts = st.split("=");
                    settings.put(parts[0].trim(), parts[1].trim());
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ImportSettings getInstance() {
        return Singleton;
    }

    public static String getSetting(String key) {
        return settings.getOrDefault(key, "Unknown");
    }

    public static void importDefaultKeyMap() {
        String os = System.getProperty("os.name").toLowerCase();
        File file;
        try {
            if (os.contains("win")) {
                file = new File("/Applications/Tetris.app/Contents/app/defaultWindowsSettings.ini");
            }
            else {
                String appDataPath = System.getProperty("user.home") + "/Library/Application Support/Tetris";
                file = new File(appDataPath, "defaultSettings.ini");
                if (!file.exists()) {
                    try {
                        // 리소스 디렉토리에서 설정 파일을 복사
                        Files.copy(Paths.get("/Applications/Tetris.app/Contents/app/resources/defaultSettings.ini"), file.toPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            String st;
            while ((st = br.readLine()) != null) {
                if (st.contains("=")) {
                    String[] parts = st.split("=");
                    settings.put(parts[0].trim(), parts[1].trim());
                }
            }

            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
