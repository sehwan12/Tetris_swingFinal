package model;

import org.json.simple.*;
import IO.ScoreIO;

public class ScoreBoardModel {
    private static String[] columnString = { "순위","난이도","모드","이름", "점수" };


    private static JSONArray jsonArr;

    // 생성자 영역
    public ScoreBoardModel() {
        initData();
    }

    // Getter, Setter 영역

    public void initData() {
        jsonArr = new ScoreIO().getJsonArr();
    }

    public static String[] getColumnString() {
        return columnString;
    }

    public static void setColumnString(String[] columnString) {
        ScoreBoardModel.columnString = columnString;
    }

    public static JSONArray getJsonArr() {
        return jsonArr;
    }

    public void setJsonArr(JSONArray jsonArr) {
        this.jsonArr = jsonArr;
    }

    public static void sortScore() {
        // jsonArr를 점수 기준으로 내림차순 정렬(초기상태)
        jsonArr.sort((o1, o2) -> {
            long score1 = (Long) ((JSONObject) o1).get("score");
            long score2 = (Long) ((JSONObject) o2).get("score");
            return Long.compare(score2, score1);
        });
    }


}
