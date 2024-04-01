package IO;
import java.io.*;

// Written By GPT 4.0
public class CopySettings {
    public static void restore() {
        // 파일 경로 지정
        String sourceFilePath = "defaultSettings.ini";
        String destinationFilePath = "settings.ini";

        // 파일 복사를 위한 스트림 선언
        try (BufferedReader reader = new BufferedReader(new FileReader(sourceFilePath));
             BufferedWriter writer = new BufferedWriter(new FileWriter(destinationFilePath))) {

            String currentLine;

            // 파일 내용을 한 줄씩 읽으면서 대상 파일에 쓴다.
            while ((currentLine = reader.readLine()) != null) {
                writer.write(currentLine);
                writer.newLine(); // 줄바꿈 추가
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
