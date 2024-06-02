package IO;


import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ScoreIO {
    private static String JSON_FILE = System.getProperty("user.home") + "/Library/Application Support/Tetris/userScore.json";

    public JSONArray jsonArr = null;

    public ScoreIO() {
        String appDataPath = System.getProperty("user.home") + "/Library/Application Support/Tetris";
        File jsonFile = new File(appDataPath, "userScore.json");
        if (!jsonFile.exists()) {
            try {
                // 리소스 디렉토리에서 설정 파일을 복사
                Files.copy(Paths.get("/Applications/Tetris.app/Contents/app/resources/userScore.json"), jsonFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JSONParser parser = new JSONParser();
        if (!isFileEmpty()) {
            try {
                jsonArr = (JSONArray) parser.parse(new FileReader(JSON_FILE));
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public JSONArray getJsonArr() {
        return jsonArr;
    }


    public static void writeScore(String name, int score, String difficulty, String mode) {
        if (isFileEmpty()) {
            writeFirstScore(name, score, difficulty, mode);
        }
        else {
            writeAfterScore(name, score, difficulty, mode);
        }
    }
    // 기록이 없는 경우
    public static void writeFirstScore(String name, int score, String difficulty,String mode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("score", score);
        jsonObject.put("difficulty", difficulty);
        jsonObject.put("mode", mode);
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject);
        try (FileWriter file = new FileWriter(JSON_FILE)) {
            file.write(jsonArray.toJSONString());
            file.flush();
        } catch (IOException s) {
            s.printStackTrace();
        }
    }

    // 기록이 존재하는 경우
    public static void writeAfterScore(String name, int score, String difficulty, String mode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("score", score);
        jsonObject.put("difficulty", difficulty);
        jsonObject.put("mode", mode);
        try (FileReader reader = new FileReader(JSON_FILE)) {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(reader);
            jsonArray.add(jsonObject);

            try (FileWriter file = new FileWriter(JSON_FILE)) {
                file.write(jsonArray.toJSONString());
                file.flush();
            } catch (IOException s) {
                s.printStackTrace();
            }

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    //userScore.json 빈상태로 만들기
    public static void clearJsonFile() {
        try {
            // 기존 파일의 내용을 삭제하기 위해 파일을 읽고 내용을 비워서 다시 씁니다.
            Files.write(Paths.get(JSON_FILE), "".getBytes());
            System.out.println("JSON 파일 내용이 삭제되었습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //스코어보드가 비어있는지 아닌지 확인
    public static boolean isFileEmpty() {
        try {
            FileReader fileReader = new FileReader(JSON_FILE);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String firstLine = bufferedReader.readLine();
            bufferedReader.close();
            return firstLine == null || firstLine.isEmpty();
        } catch (IOException e) {
            e.printStackTrace();
            return true; // If an exception is thrown, we assume the file is empty
        }
    }
}
