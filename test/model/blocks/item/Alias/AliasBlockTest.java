package model.blocks.item.Alias;

import model.blocks.Block;
import model.blocks.item.ItemBlock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class AliasBlockTest {
    public AliasBlock block;

    @BeforeEach
    public void setUp() {
        block = new AliasBlock();
    }
    @Test
    void constructorTest() {
        assertTrue(block.subBlock instanceof Block);
        assertFalse(block.subBlock instanceof ItemBlock);
        assertFalse(block.subBlock instanceof AliasBlock);

    }

    @Test
    void randomItemPos() {
        block = new AliasBlock();
        int aliasCount = 0;
        for (int i = 0; i < block.getShapeArray().length; i++) {
            for (int j = 0; j < block.getShapeArray()[i].length; j++) {
                if (block.getShapeArray()[i][j] == 2) {
                    aliasCount++;
                }
            }
        }
        assertEquals(1, aliasCount);
    }

    @Test
    void rotate_x() {
        assertEquals(block.rotate_x(), block.subBlock.rotate_x() - 1);
    }

    @Test
    void rotate_y() {
        assertEquals(block.rotate_y(), block.subBlock.rotate_y());
    }


}
