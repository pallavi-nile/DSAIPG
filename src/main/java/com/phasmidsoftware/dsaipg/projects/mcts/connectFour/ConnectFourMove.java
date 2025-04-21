package com.phasmidsoftware.dsaipg.projects.mcts.connectfour;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;

/**
 * A move in Connect Four: dropping a piece in a specific column.
 */
public class ConnectFourMove implements Move<ConnectFourGame> {
    private final int player;
    private final int column;

    public ConnectFourMove(int player, int column) {
        this.player = player;
        this.column = column;
    }

    @Override
    public int player() {
        return player;
    }

    public int column() {
        return column;
    }

    @Override
    public String toString() {
        return "Player " + (player + 1) + " drops in column " + column;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConnectFourMove)) return false;
        ConnectFourMove move = (ConnectFourMove) o;
        return player == move.player && column == move.column;
    }

    @Override
    public int hashCode() {
        return 31 * player + column;
    }
}
