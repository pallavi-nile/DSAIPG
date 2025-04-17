package com.phasmidsoftware.dsaipg.projects.mcts.connectfour;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Game;
import com.phasmidsoftware.dsaipg.projects.mcts.core.RandomState;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

public class ConnectFourGame implements Game<ConnectFourGame> {
    public static final int ROWS = 6;
    public static final int COLS = 7;
    public static final int PLAYER_1 = 0;
    public static final int PLAYER_2 = 1;
    public static final int CONNECT_N = 4;

    @Override
    public State<ConnectFourGame> start() {
        return new ConnectFourState(this, new int[ROWS][COLS], PLAYER_1, new RandomState(COLS));
    }

    @Override
    public int opener() {
        return PLAYER_1;
    }

    public static int opponent(int player) {
        return player == PLAYER_1 ? PLAYER_2 : PLAYER_1;
    }

    @Override
    public String toString() {
        return "ConnectFourGame";
    }
}
