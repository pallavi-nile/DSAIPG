package com.phasmidsoftware.dsaipg.projects.mcts.connectfour;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

public class MCTSConnectFour {

    private static final int ITERATIONS = 300;

    public static ConnectFourMove findBestMove(State<ConnectFourGame> state) {
        Node<ConnectFourGame> root = new ConnectFourNode(state);

        for (int i = 0; i < ITERATIONS; i++) {
            mcts(root);
        }

        return getBestMoveFromChildren(root);
    }

    private static void mcts(Node<ConnectFourGame> node) {
        if (node.isLeaf()) return;

        if (node.children().isEmpty()) {
            node.explore();
            return;
        }

        Node<ConnectFourGame> bestChild = selectBestChild(node);
        mcts(bestChild);
        node.backPropagate();
    }

    private static Node<ConnectFourGame> selectBestChild(Node<ConnectFourGame> node) {
        Node<ConnectFourGame> best = null;
        double bestScore = -1;

        for (Node<ConnectFourGame> child : node.children()) {
            if (child.playouts() == 0) return child;

            double winRate = (double) child.wins() / child.playouts();
            double exploration = Math.sqrt(Math.log(node.playouts() + 1) / child.playouts());
            double score = winRate + 1.41 * exploration;

            if (score > bestScore) {
                bestScore = score;
                best = child;
            }
        }
        return best;
    }

    private static ConnectFourMove getBestMoveFromChildren(Node<ConnectFourGame> root) {
        ConnectFourState rootState = (ConnectFourState) root.state();
        int[][] base = rootState.getBoard();

        for (Node<ConnectFourGame> child : root.children()) {
            ConnectFourState nextState = (ConnectFourState) child.state();
            int[][] after = nextState.getBoard();

            for (int col = 0; col < ConnectFourGame.COLS; col++) {
                for (int row = 0; row < ConnectFourGame.ROWS; row++) {
                    if (base[row][col] != after[row][col]) {
                        return new ConnectFourMove(rootState.player(), col);
                    }
                }
            }
        }

        return null;
    }
}
