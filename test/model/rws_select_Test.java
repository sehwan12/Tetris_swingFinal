package model;

import junit.framework.TestCase;
import model.BoardModel;
import model.OutGameModel;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class rws_select_Test extends TestCase {

    public void rws_select_report(BoardModel boardModel, double[] fitnesses, String[] blocks, int simulationCount){
        // 전체 적합도 합 구하기
        double totalFitness = 0;
        for (double fitness : fitnesses) {
            totalFitness += fitness;
        }

        // 선택 결과를 저장할 배열
        int[] selections = new int[fitnesses.length];

        // 시뮬레이션을 수행하고 선택 결과를 저장
        for (int i = 0; i < simulationCount; i++) {
            int selectedIndex = boardModel.rws_select();
            selections[selectedIndex]++;
        }

        // 시뮬레이션 후 각 블럭별 선택 횟수와 비율 출력
        System.out.println(simulationCount+" 번 "+"시뮬레이션 후 각 블럭별 선택 횟수와 비율:");
        for (int i = 0; i < fitnesses.length; i++) {
            // 오차범위가 5% 이내인지 true false
            boolean is_error_under_5 = false;

            double selectionProbability = (fitnesses[i] / totalFitness);
            System.out.printf("블럭 %s: 설정된 선택 확률 %.6f | ", blocks[i], selectionProbability);

            double selectionRatio = (double) selections[i] / simulationCount;
            System.out.printf("%d번 선택, 선택 비율: %f, ", selections[i], selectionRatio);

            // 각 블럭별 선택 확률과의 오차를 백분율로 구하기
            double errorPercentage = Math.abs((selectionRatio - selectionProbability) / selectionProbability) * 100;
            System.out.printf("선택 확률과의 오차: %.2f%%\n", errorPercentage);

            // 오차범위가 5% 이내인지 각 블럭마다 테스트
            if( errorPercentage < 5.00 ) is_error_under_5 = true;
            assertTrue(is_error_under_5);
        }
        System.out.println();
    }

    @Test
    public void test_rws_select_Normal() {
        BoardModel boardModel = new BoardModel();

        // 블럭들의 적합도(가중치) Nomal에서는 I의 가중치가 10
        double I = 10, J = 10, L = 10, Z = 10, S = 10, T = 10, O = 10;
        // 블럭들의 적합도(가중치) 배열
        double[] fitnesses = {I, J, L, Z, S, T, O};
        // 출력 편의를 위한 블럭 배열
        String[] blocks = {"I", "J", "L", "Z", "S", "T", "O"};
        // 시뮬레이션 횟수
        int simulationCount = 100000;

        // 난이도 설정
        OutGameModel.setDifficulty(1);
        System.out.print("Normal ");

        rws_select_report(boardModel,fitnesses, blocks, simulationCount);
    }

    @Test
    public void test_rws_select_Hard() {
        BoardModel boardModel = new BoardModel();

        // 블럭들의 적합도(가중치) Hard에서는 I의 가중치가 8
        double I = 8, J = 10, L = 10, Z = 10, S = 10, T = 10, O = 10;
        // 블럭들의 적합도(가중치) 배열
        double[] fitnesses = {I, J, L, Z, S, T, O};
        // 출력 편의를 위한 블럭 배열
        String[] blocks = {"I", "J", "L", "Z", "S", "T", "O"};
        // 시뮬레이션 횟수
        int simulationCount =100000;

        // 난이도 설정
        OutGameModel.setDifficulty(2);
        System.out.print("Hard ");

        rws_select_report(boardModel,fitnesses, blocks, simulationCount);
    }

    @Test
    public void test_rws_select_Easy() {
        BoardModel boardModel = new BoardModel();

        // 블럭들의 적합도(가중치) Easy에서는 I의 가중치가 12
        double I = 12, J = 10, L = 10, Z = 10, S = 10, T = 10, O = 10;
        // 블럭들의 적합도(가중치) 배열
        double[] fitnesses = {I, J, L, Z, S, T, O};
        // 출력 편의를 위한 블럭 배열
        String[] blocks = {"I", "J", "L", "Z", "S", "T", "O"};
        // 시뮬레이션 횟수
        int simulationCount = 100000;

        // 난이도 설정
        OutGameModel.setDifficulty(0);
        System.out.print("Easy ");

        rws_select_report(boardModel,fitnesses, blocks, simulationCount);
    }


}