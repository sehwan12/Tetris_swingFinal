package IO;


import model.OutGameModel;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ScoreIO {
    private final static String JSON_FILE = "userScore.json";

    public JSONArray jsonArr = null;

    public ScoreIO() {
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


    public static void writeScore(String name, int score, String difficulty) {
        if (isFileEmpty()) {
            writeFirstScore(name, score, difficulty);
        }
        else {
            writeAfterScore(name, score, difficulty);
        }
    }
    // 기록이 없는 경우
    public static void writeFirstScore(String name, int score, String difficulty) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("score", score);
        jsonObject.put("difficulty", difficulty);
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
    public static void writeAfterScore(String name, int score, String difficulty) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", name);
        jsonObject.put("score", score);
        jsonObject.put("difficulty", difficulty);
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
