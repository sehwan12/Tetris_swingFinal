package model.blocks.item.Alias;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class TimerBlockTest {
    public TimerBlock block;

    @BeforeEach
    public void setUp() {
        block = new TimerBlock();
    }

    @Test
    void constructorTest() {
        assertEquals("S", block.getItemText());
    }
}
