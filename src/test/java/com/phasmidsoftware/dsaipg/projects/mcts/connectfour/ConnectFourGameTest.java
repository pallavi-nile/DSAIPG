package com.phasmidsoftware.dsaipg.projects.mcts.connectfour;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;

import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

/**
 * JUnit tests for ConnectFourGame
 */
public class ConnectFourGameTest {

    private ConnectFourGame connectFourGame;

    @Before
    public void setup() {
        this.connectFourGame = new ConnectFourGame();
    }

    @Test
    public void shouldStart() {
        State<ConnectFourGame> state = connectFourGame.start();
        assertNotNull("Start state should not be null", state);
        assertEquals("Starting player should be PLAYER_1", ConnectFourGame.PLAYER_1, state.player());
    }

    @Test
    public void shouldOpener() {
        int actualValue = connectFourGame.opener();
        assertEquals("Opener should be PLAYER_1", ConnectFourGame.PLAYER_1, actualValue);
    }

    @Test
    public void shouldOpponent_Player0() {
        int opponent = ConnectFourGame.opponent(0);
        assertEquals("Opponent of 0 should be 1", 1, opponent);
    }

    @Test
    public void shouldOpponent_Player1() {
        int opponent = ConnectFourGame.opponent(1);
        assertEquals("Opponent of 1 should be 0", 0, opponent);
    }

    @Test
    public void shouldToString() {
        String actualValue = connectFourGame.toString();
        assertEquals("ConnectFourGame", actualValue);
    }
}
