package model.blocks.item.Alias;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class LineClearBlockTest {
    public LineClearBlock block;

    @BeforeEach
    public void setUp() {
        block = new LineClearBlock();
    }

    @Test
    void constructorTest() {
        assertEquals("L", block.getItemText());
    }
}
