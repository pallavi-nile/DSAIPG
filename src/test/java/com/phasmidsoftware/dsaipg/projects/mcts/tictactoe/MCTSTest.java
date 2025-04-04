package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MCTSTest {

    private MCTS mcts;
    private TicTacToe.TicTacToeState initialState;

    @BeforeEach
    void setUp() {
        TicTacToe game = new TicTacToe();
        initialState = game.new TicTacToeState();
        Node<TicTacToe> root = new TicTacToeNode(initialState);
        mcts = new MCTS(root);
    }

    @Test
    void testRunMCTS() {
        Node<TicTacToe> bestMove = mcts.run(1000);
        assertNotNull(bestMove, "Best move should not be null");
        assertNotEquals(initialState, bestMove.state(), "State should change after running MCTS");
    }

    @Test
    void testSelection() {
        Node<TicTacToe> selected = mcts.run(10);
        assertNotNull(selected, "Selection should return a valid node");
    }

    @Test
    void testBestChild() {
        Node<TicTacToe> root = new TicTacToeNode(initialState);
        MCTS mctsTest = new MCTS(root);
        mctsTest.run(100);
        Node<TicTacToe> bestChild = mctsTest.run(10);
        assertNotNull(bestChild, "Best child should be selected");
    }

    @Test
    void testUCB1Selection() {
        Node<TicTacToe> root = new TicTacToeNode(initialState);
        MCTS mctsTest = new MCTS(root);
        Node<TicTacToe> bestChild = mctsTest.run(50);
        assertNotNull(bestChild, "Best UCB1 child should be found");
    }
}