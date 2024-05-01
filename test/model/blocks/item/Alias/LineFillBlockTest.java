package model.blocks.item.Alias;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class LineFillBlockTest {
    public LineFillBlock block;

    @BeforeEach
    public void setUp() {
        block = new LineFillBlock();
    }

    @Test
    void constructorTest() {
        assertEquals("F", block.getItemText());
    }
}
