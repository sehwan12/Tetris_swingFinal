package model.VersusMode;

import model.SingleMode.ModelStateChangeListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class VsTimeBoardModelTest {
    private VsTimeBoardModel vsTimeBoardModel;
    private static final int PLAYER_TYPE = 1;

    @BeforeEach
    public void setUp() {
        vsTimeBoardModel = new VsTimeBoardModel(PLAYER_TYPE);
    }

    @Test
    public void testInitialStartClock() {
        long initialTime = System.currentTimeMillis();
        long modelStartTime = vsTimeBoardModel.getStartClock();

        // Allow some leeway for the initial time difference
        assertTrue(Math.abs(initialTime - modelStartTime) < 100);
    }

    @Test
    public void testCheckTimeLimit_NotExceeded() {
        // Set the start time to the current time
        vsTimeBoardModel.setStartClock(System.currentTimeMillis());

        assertFalse(vsTimeBoardModel.checkTimeLimit());
    }

    @Test
    public void testCheckTimeLimit_Exceeded() throws InterruptedException {
        // Set the start time to 11 seconds ago
        vsTimeBoardModel.setStartClock(System.currentTimeMillis() - 11000);

        assertTrue(vsTimeBoardModel.checkTimeLimit());
    }

}