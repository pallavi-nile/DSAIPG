package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;

public class TicTacToeUI extends JFrame {
    private final JButton[][] buttons = new JButton[3][3];
    private final JLabel statusLabel = new JLabel("Your turn (X)", SwingConstants.CENTER);

    private TicTacToe game;
    private TicTacToe.TicTacToeState state;
    private final int humanPlayer = 1;
    private final int botPlayer = 0;

    public TicTacToeUI() {
        super("Tic Tac Toe - UI with MCTS Bot");
        setLayout(new BorderLayout());

        game = new TicTacToe();
        state = game.new TicTacToeState();

        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        Font font = new Font("Arial", Font.BOLD, 40);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int row = i;
                int col = j;

                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(font);
                buttons[i][j].addActionListener(e -> handleHumanMove(row, col));
                boardPanel.add(buttons[i][j]);
            }
        }

        statusLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        add(statusLabel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);

        setSize(400, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void handleHumanMove(int row, int col) {
        try {
            state = (TicTacToe.TicTacToeState) state.next(new TicTacToe.TicTacToeMove(humanPlayer, row, col));
            buttons[row][col].setText("X");
            buttons[row][col].setEnabled(false);

            if (state.isTerminal()) {
                endGame();
                return;
            }

            statusLabel.setText("Bot is thinking...");
            SwingUtilities.invokeLater(this::handleBotMove);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid move!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleBotMove() {
        Node<TicTacToe> root = new TicTacToeNode(state);
        MCTS mcts = new MCTS(root);
        Node<TicTacToe> best = mcts.run(500);
        state = (TicTacToe.TicTacToeState) best.state();

        updateBoardFromState();

        if (state.isTerminal()) {
            endGame();
        } else {
            statusLabel.setText("Your turn (X)");
        }
    }

    private void updateBoardFromState() {
        Position position = state.position();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int cell = position.projectRow(i)[j];
                if (cell == 0) {
                    buttons[i][j].setText("O");
                    buttons[i][j].setEnabled(false);
                }
            }
        }
    }

    private void endGame() {
        statusLabel.setText("Game Over");
        for (JButton[] row : buttons) {
            for (JButton button : row) {
                button.setEnabled(false);
            }
        }

        state.winner().ifPresentOrElse(
                w -> JOptionPane.showMessageDialog(this, (w == humanPlayer ? "You win!" : "Bot wins!")),
                () -> JOptionPane.showMessageDialog(this, "It’s a draw!")
        );
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToeUI::new);
    }
}
