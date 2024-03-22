package InProgress;
import java.io.*;
import java.util.*;

public class ImportSettings {
    private Map<String, String> settings = new HashMap<>();

    public ImportSettings() {
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

    public String getSetting(String key) {
        return settings.getOrDefault(key, "Unknown");
    }

}
