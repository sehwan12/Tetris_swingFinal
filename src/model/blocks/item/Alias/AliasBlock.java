package model.blocks.item.Alias;

import model.blocks.Block;
import model.blocks.*;
import model.blocks.item.ItemBlock;

import java.awt.*;
import java.util.Random;

public class AliasBlock extends ItemBlock {
    public Block subBlock;
    String itemText;
    public AliasBlock() {
        Random rnd = new Random(System.currentTimeMillis());
        int r = rnd.nextInt(7);
        switch (r) {
            case 0:
                subBlock = new IBlock();
                break;
            case 1:
                subBlock = new JBlock();
                break;
            case 2:
                subBlock = new LBlock();
                break;
            case 3:
                subBlock = new OBlock();
                break;
            case 4:
                subBlock = new SBlock();
                break;
            case 5:
                subBlock = new TBlock();
                break;
            case 6:
                subBlock = new IBlock();

//                subBlock = new ZBlock();
                break;
            default:
                subBlock = new IBlock();
                break;
        }

        shape = subBlock.getShapeArray();
        color = subBlock.getColor();
        text = subBlock.getText();
        rotate_status=1;
        randomItemPos();
    }

    public void randomItemPos() {
        int blockCount = 0;
        Random random = new Random();
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] >= 1) {
                    blockCount++;
                }
            }
        }
        int random_text_position = random.nextInt(blockCount) + 1;
        blockCount = 0;
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] >= 1) {
                    blockCount++;
                    if (blockCount == random_text_position) {
                        shape[i][j] = 2;
                    }
                }
            }
        }
    }


    //블럭 회전할 때 임의의 회전 축을 중심으로 회전하도록 하기 위한
    //x, y위치 변경 시 필요한 rotate_x, rotate_y
    @Override
    public int rotate_x(){
        System.out.println("subBlock rotate");
        if (subBlock instanceof OBlock) {
            return subBlock.rotate_x();
        }
        else if (subBlock instanceof IBlock) {
            return subBlock.rotate_x() - 2;
        }
        return subBlock.rotate_x() - 1;
    }
    @Override
    public int rotate_y(){
        if (subBlock instanceof IBlock) {
            return subBlock.rotate_y() + 1;
        }

        return subBlock.rotate_y();
    }

    public String getItemText() {
        return itemText;
    }
}
