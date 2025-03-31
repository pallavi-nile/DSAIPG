package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.Instant;

public class TicTacToeUI extends JFrame {
    private final JButton[][] buttons = new JButton[3][3];
    private final JLabel statusLabel = new JLabel("Your turn", SwingConstants.CENTER);
    private final JLabel scoreLabel = new JLabel("Score - You: 0 | Opponent: 0", SwingConstants.CENTER);
    private final JButton newGameButton = new JButton("New Game");
    private final JButton resetButton = new JButton("Reset Score");
    private final JButton exitButton = new JButton("Exit");

    private TicTacToe game;
    private TicTacToe.TicTacToeState state;

    private final int humanPlayer = 1;
    private final int botPlayer = 0;

    private int humanScore = 0;
    private int botScore = 0;

    private Instant sessionStart;

    public TicTacToeUI() {
        super("Tic Tac Toe");

        // 🖤 Black & White Theme
        UIManager.put("Panel.background", Color.BLACK);
        UIManager.put("OptionPane.background", Color.BLACK);
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("Label.foreground", Color.WHITE);
        UIManager.put("Button.background", Color.DARK_GRAY);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("TextField.background", Color.BLACK);
        UIManager.put("TextField.foreground", Color.WHITE);
        UIManager.put("TextField.caretForeground", Color.WHITE);
        UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.BOLD, 14));

        setLayout(new BorderLayout());
        sessionStart = Instant.now();

        initGame();

        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        boardPanel.setBackground(Color.BLACK);

        Font font = new Font("Arial", Font.BOLD, 40);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int row = i;
                int col = j;

                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(font);
                buttons[i][j].setBackground(Color.WHITE);
                buttons[i][j].setForeground(Color.BLACK);
                buttons[i][j].addActionListener(e -> handleHumanMove(row, col));
                boardPanel.add(buttons[i][j]);
            }
        }

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setBackground(Color.BLACK);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        statusLabel.setForeground(Color.WHITE);
        scoreLabel.setForeground(Color.WHITE);
        topPanel.add(statusLabel);
        topPanel.add(scoreLabel);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setBackground(Color.BLACK);

        Font buttonFont = new Font("Arial", Font.PLAIN, 14);
        JButton[] allButtons = { newGameButton, resetButton, exitButton };
        for (JButton b : allButtons) {
            b.setFont(buttonFont);
            b.setBackground(Color.DARK_GRAY);
            b.setForeground(Color.WHITE);
        }

        newGameButton.addActionListener(e -> resetBoardOnly());
        resetButton.addActionListener(e -> resetGame());
        exitButton.addActionListener(e -> {
            long seconds = Duration.between(sessionStart, Instant.now()).getSeconds();
            System.out.println("Game Over");
            System.out.println("Total session time: " + seconds + " seconds");
            System.exit(0);
        });

        bottomPanel.add(newGameButton);
        bottomPanel.add(resetButton);
        bottomPanel.add(exitButton);

        add(topPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setSize(600, 700);
        setResizable(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initGame() {
        game = new TicTacToe();
        state = game.new TicTacToeState();
        statusLabel.setText("Your turn");
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

            statusLabel.setText("Opponent is thinking...");
            SwingUtilities.invokeLater(this::handleBotMove);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid move!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleBotMove() {
        Node<TicTacToe> root = new TicTacToeNode(state);
        MCTS mcts = new MCTS(root);

        long start = System.currentTimeMillis();
        Node<TicTacToe> best = mcts.run(500);
        long end = System.currentTimeMillis();
        System.out.println("MCTS opponent move took: " + (end - start) + " ms");

        state = (TicTacToe.TicTacToeState) best.state();
        updateBoardFromState();

        if (state.isTerminal()) {
            endGame();
        } else {
            statusLabel.setText("Your turn");
        }
    }

    private void updateBoardFromState() {
        Position position = state.position();
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                int value = position.projectRow(i)[j];
                if (value == 0) {
                    buttons[i][j].setText("O");
                    buttons[i][j].setEnabled(false);
                }
            }
    }

    private void endGame() {
        statusLabel.setText("Game Over");
        for (JButton[] row : buttons)
            for (JButton button : row)
                button.setEnabled(false);

        state.winner().ifPresentOrElse(
                w -> {
                    if (w == humanPlayer) {
                        humanScore++;
                        JOptionPane.showMessageDialog(this, "You win!");
                    } else {
                        botScore++;
                        JOptionPane.showMessageDialog(this, "Opponent wins!");
                    }
                },
                () -> JOptionPane.showMessageDialog(this, "It's a draw!")
        );

        updateScoreLabel();
    }

    private void updateScoreLabel() {
        scoreLabel.setText("Score - You: " + humanScore + " | Opponent: " + botScore);
    }

    private void resetBoardOnly() {
        initGame();
        for (JButton[] row : buttons)
            for (JButton button : row) {
                button.setText("");
                button.setEnabled(true);
            }
    }

    private void resetGame() {
        humanScore = 0;
        botScore = 0;
        updateScoreLabel();
        resetBoardOnly();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToeUI::new);
    }
}
