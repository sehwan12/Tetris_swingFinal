package model.blocks;
import model.blocks.Block;

import java.awt.Color;

public class WeightBlock extends Block {
    public WeightBlock() {
        shape = new int[][] {
                {0, 1, 1, 0},
                {1, 1, 1, 1},
        };
        color = Color.WHITE;
        text = "O";
        rotate_status=1;
    }

    public WeightBlock(boolean color_blind, int pattern) {
        shape = new int[][] {
                {0, 1, 1, 0},
                {1, 1, 1, 1},
        };
        color = Color.WHITE;
        if(pattern==1) text="O";
        else text="O";
        rotate_status=1;
    }

    //ZBlock 회전 시 변경되어야 할 x, y 위치
    @Override
    public int rotate_x(){
        int rotate_x;
        switch (rotate_status){
            case 1:
                rotate_x=1;
                break;
            case 2:
                rotate_x=-1;
                break;
            case 3:
                rotate_x=0;
                break;
            case 4:
                rotate_x=0;
                break;
            default:
                rotate_x=0;
                break;
        }
        return rotate_x;
    }

    @Override
    public int rotate_y(){
        int rotate_y;
        switch (rotate_status){
            case 1:
                rotate_y=0;
                break;
            case 2:
                rotate_y=1;
                break;
            case 3:
                rotate_y=-1;
                break;
            case 4:
                rotate_y=0;
                break;
            default:
                rotate_y=0;
                break;
        }
        return rotate_y;
    }
}
