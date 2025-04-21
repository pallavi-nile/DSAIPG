package com.phasmidsoftware.dsaipg.projects.mcts.connectfour;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class ConnectFourUITest {

    private ConnectFourUI ui;

    @Before
    public void setUp() {
        SwingUtilities.invokeLater(() -> {
            ui = new ConnectFourUI();
        });

        // Give Swing time to build UI
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInitialTurnDisplayed() {
        JLabel label = (JLabel) TestUtils.getComponentOfType(ui, JLabel.class);
        assertNotNull("Status label should exist", label);
        assertTrue("Initial text should mention Player 1", label.getText().contains("Player 1"));
    }

    @Test
    public void testPlayWithFriendTogglesAI() {
        JButton friendButton = TestUtils.getButtonByText(ui, "Play with Friend");
        assertNotNull(friendButton);
        friendButton.doClick();
        assertTrue(friendButton.getText().contains("Play with AI") || friendButton.getText().contains("Friend"));
    }

    @Test
    public void testPlayerVsAIButtonSetsAIMode() {
        JButton aiButton = TestUtils.getButtonByText(ui, "Player vs AI");
        assertNotNull(aiButton);
        aiButton.doClick();

        JLabel label = (JLabel) TestUtils.getComponentOfType(ui, JLabel.class);
        assertTrue(label.getText().contains("Player vs AI Mode"));
    }

    @Test
    public void testNewGameButtonWorks() {
        JButton newGame = TestUtils.getButtonByText(ui, "New Game");
        assertNotNull(newGame);
        newGame.doClick();

        JLabel label = (JLabel) TestUtils.getComponentOfType(ui, JLabel.class);
        assertTrue(label.getText().contains("Player 1's Turn"));
    }

    @Test
    public void testResetButtonWorks() {
        JButton reset = TestUtils.getButtonByText(ui, "Reset");
        assertNotNull(reset);
        reset.doClick();

        JLabel label = (JLabel) TestUtils.getComponentOfType(ui, JLabel.class);
        assertTrue(label.getText().contains("Player 1's Turn"));
    }

    @Test
    public void testExitButtonExists() {
        JButton exit = TestUtils.getButtonByText(ui, "Exit");
        assertNotNull(exit);
    }
}
