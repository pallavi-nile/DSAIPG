package com.phasmidsoftware.dsaipg.projects.mcts.connectfour;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit test for ConnectFourMain
 */
public class ConnectFourMainTest {

    @Before
    public void setup() {
        // No setup needed for this test since main launches the UI
    }

    @Test
    public void shouldLaunchGameWithoutErrors() {
        // Run main method to ensure no exceptions occur
        String[] args = new String[0];

        // Using invokeAndWait to ensure Swing runs safely in test
        try {
            javax.swing.SwingUtilities.invokeAndWait(() -> ConnectFourMain.main(args));
        } catch (Exception e) {
            assert false : "Game UI failed to launch: " + e.getMessage();
        }
    }
}
