package com.phasmidsoftware.dsaipg.projects.mcts.connectfour;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class ConnectFourMoveTest {

    private ConnectFourMove connectFourMove;

    @Before
    public void setup() {
        this.connectFourMove = new ConnectFourMove(1, 3); // player 1 drops in column 3
    }

    @Test
    public void shouldReturnPlayer() {
        int actualPlayer = connectFourMove.player();
        assertEquals(1, actualPlayer);
    }

    @Test
    public void shouldReturnColumn() {
        int actualColumn = connectFourMove.column();
        assertEquals(3, actualColumn);
    }

    @Test
    public void shouldReturnCorrectToString() {
        String result = connectFourMove.toString();
        assertEquals("Player 2 drops in column 3", result); // player index 1 => Player 2
    }

    @Test
    public void shouldBeEqual() {
        ConnectFourMove sameMove = new ConnectFourMove(1, 3);
        assertTrue(connectFourMove.equals(sameMove));
    }

    @Test
    public void shouldNotBeEqual_DifferentPlayer() {
        ConnectFourMove differentPlayer = new ConnectFourMove(0, 3);
        assertFalse(connectFourMove.equals(differentPlayer));
    }

    @Test
    public void shouldNotBeEqual_DifferentColumn() {
        ConnectFourMove differentColumn = new ConnectFourMove(1, 5);
        assertFalse(connectFourMove.equals(differentColumn));
    }

    @Test
    public void shouldNotBeEqual_NullOrDifferentClass() {
        assertFalse(connectFourMove.equals(null));
        assertFalse(connectFourMove.equals("Not a move"));
    }

    @Test
    public void shouldReturnConsistentHashCode() {
        ConnectFourMove sameMove = new ConnectFourMove(1, 3);
        assertEquals(connectFourMove.hashCode(), sameMove.hashCode());
    }
}
