package model.blocks.item;

import model.blocks.Block;

import java.awt.*;

public class ClearBlock extends Block {
    public ClearBlock() {
        shape = new int[][] {
                {1}
        };
        color = Color.WHITE;
        rotate_status=1;
        text = "C";
    }

    

}
