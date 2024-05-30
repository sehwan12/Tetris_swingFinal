package IO;
import java.io.*;
import java.util.*;

public class ImportSettings {
    private static Map<String, String> settings = new HashMap<>();

    // 싱글톤
    private static ImportSettings Singleton = new ImportSettings();

    private ImportSettings() {
        try {
            File file = new File("settings.ini");
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
                file = new File("defaultWindowsSettings.ini");
            }
            else {
                file = new File("defaultSettings.ini");
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
