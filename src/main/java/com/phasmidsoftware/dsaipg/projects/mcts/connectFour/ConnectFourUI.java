package com.phasmidsoftware.dsaipg.projects.mcts.connectfour;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ConnectFourUI extends JFrame {
    private static final int ROWS = 6;
    private static final int COLS = 7;

    private final JButton[] columnButtons = new JButton[COLS];
    private final CellPanel[][] cells = new CellPanel[ROWS][COLS];
    private final JLabel statusLabel = new JLabel("Player 1's Turn");
    private final JButton resetButton = new JButton("Reset");
    private final JButton newGameButton = new JButton("New Game");
    private final JButton toggleModeButton = new JButton("Play with Friend");
    private final JButton playerVsAIButton = new JButton("Player vs AI");
    private final JButton exitButton = new JButton("Exit");

    private int[][] board = new int[ROWS][COLS]; // 0 = empty, 1 = player1, 2 = player2
    private boolean playWithAI = false;
    private int currentPlayer = 1;
    private int[] score = new int[3]; // index 1: player1, index 2: player2
    private final List<Point> winningDiscs = new ArrayList<>();

    public ConnectFourUI() {
        setTitle("Connect Four - MCTS AI");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // Top button panel
        JPanel buttonPanel = new JPanel(new GridLayout(1, COLS));
        buttonPanel.setBackground(Color.BLACK);
        for (int col = 0; col < COLS; col++) {
            JButton btn = new JButton("↓");
            btn.setFont(new Font("SansSerif", Font.BOLD, 18));
            btn.setBackground(Color.white);
            btn.setForeground(Color.BLACK);
            btn.setFocusPainted(false);
            int finalCol = col;
            btn.addActionListener(e -> dropDisc(finalCol));
            columnButtons[col] = btn;
            buttonPanel.add(btn);
        }

        // Board Panel
        JPanel boardPanel = new JPanel(new GridLayout(ROWS, COLS));
        boardPanel.setBackground(Color.BLACK);
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                cells[row][col] = new CellPanel(row, col);
                boardPanel.add(cells[row][col]);
            }
        }

        // Bottom control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        controlPanel.setBackground(Color.BLACK);
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

        controlPanel.add(statusLabel);
        controlPanel.add(toggleModeButton);
        controlPanel.add(playerVsAIButton);
        controlPanel.add(resetButton);
        controlPanel.add(newGameButton);
        controlPanel.add(exitButton);

        // Button actions
        toggleModeButton.addActionListener(e -> toggleGameMode());
        resetButton.addActionListener(e -> resetBoard());
        newGameButton.addActionListener(e -> newGame());
        exitButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Game Over! Final Score\nPlayer 1: " + score[1] + " | Player 2: " + score[2]);
            System.exit(0);
        });

        playerVsAIButton.addActionListener(e -> {
            newGame();
            playWithAI = true;
            toggleModeButton.setEnabled(false);
            statusLabel.setText("Player vs AI Mode: You are Player 1");
        });

        add(buttonPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
        setSize(700, 650);
        setLocationRelativeTo(null);
        setVisible(true);

        resetBoard();
    }

    private void dropDisc(int col) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][col] == 0) {
                board[row][col] = currentPlayer;
                cells[row][col].setPlayer(currentPlayer);
                if (checkWin(row, col)) {
                    score[currentPlayer]++;
                    statusLabel.setText("Player " + currentPlayer + " wins!");
                    showWinPopup(currentPlayer);
                    disableBoard();
                    repaint();
                } else if (isBoardFull()) {
                    statusLabel.setText("Draw Game!");
                } else {
                    currentPlayer = 3 - currentPlayer;
                    statusLabel.setText("Player " + currentPlayer + "'s Turn");

                    if (playWithAI && currentPlayer == 2) {
                        SwingUtilities.invokeLater(this::aiMove);
                    }
                }
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Column is full!");
    }

    private void aiMove() {
        try {
            System.out.println("AI is calculating using MCTS...");
    
            long startTime = System.currentTimeMillis();
    
            // Create state for MCTS
            ConnectFourGame game = new ConnectFourGame();
            ConnectFourState state = new ConnectFourState(game, board, 1, null);
    
            // Get best move from MCTS
            ConnectFourMove bestMove = MCTSConnectFour.findBestMove(state);
    
            long endTime = System.currentTimeMillis();
            System.out.println("MCTS AI move took: " + (endTime - startTime) + " ms");
    
            if (bestMove != null) {
                Thread.sleep(1000); // delay AI response for 1 second
                dropDisc(bestMove.column());
                return;
            }
    
        } catch (Exception e) {
            System.err.println("MCTS failed: " + e.getMessage());
        }
    
        // Fallback AI: try to win
        for (int col = 0; col < COLS; col++) {
            if (canDrop(col)) {
                int row = getAvailableRow(col);
                board[row][col] = 2;
                if (checkWin(row, col)) {
                    board[row][col] = 0;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {}
                    dropDisc(col);
                    return;
                }
                board[row][col] = 0;
            }
        }
    
        // Fallback AI: block opponent
        for (int col = 0; col < COLS; col++) {
            if (canDrop(col)) {
                int row = getAvailableRow(col);
                board[row][col] = 1;
                if (checkWin(row, col)) {
                    board[row][col] = 0;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ignored) {}
                    dropDisc(col);
                    return;
                }
                board[row][col] = 0;
            }
        }
    
        // Fallback AI: preferred center strategy
        int[] priority = {3, 2, 4, 1, 5, 0, 6};
        for (int col : priority) {
            if (canDrop(col)) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
                dropDisc(col);
                return;
            }
        }
    }
    
    

    private boolean checkWin(int row, int col) {
        winningDiscs.clear();
        int player = board[row][col];
        return checkDirection(row, col, 1, 0, player) ||
               checkDirection(row, col, 0, 1, player) ||
               checkDirection(row, col, 1, 1, player) ||
               checkDirection(row, col, 1, -1, player);
    }

    private boolean checkDirection(int row, int col, int dr, int dc, int player) {
        List<Point> temp = new ArrayList<>();
        temp.add(new Point(row, col));

        int r = row + dr, c = col + dc;
        while (r >= 0 && r < ROWS && c >= 0 && c < COLS && board[r][c] == player) {
            temp.add(new Point(r, c));
            r += dr; c += dc;
        }

        r = row - dr; c = col - dc;
        while (r >= 0 && r < ROWS && c >= 0 && c < COLS && board[r][c] == player) {
            temp.add(0, new Point(r, c));
            r -= dr; c -= dc;
        }

        if (temp.size() >= 4) {
            winningDiscs.addAll(temp.subList(0, 4));
            return true;
        }
        return false;
    }

    private boolean canDrop(int col) {
        return board[0][col] == 0;
    }

    private int getAvailableRow(int col) {
        for (int row = ROWS - 1; row >= 0; row--) {
            if (board[row][col] == 0) return row;
        }
        return -1;
    }

    private boolean isBoardFull() {
        for (int col = 0; col < COLS; col++) {
            if (board[0][col] == 0) return false;
        }
        return true;
    }

    private void toggleGameMode() {
        playWithAI = !playWithAI;
        toggleModeButton.setText(playWithAI ? "Play with AI" : "Play with Friend");
    }

    private void disableBoard() {
        for (JButton btn : columnButtons) btn.setEnabled(false);
    }

    private void enableBoard() {
        for (JButton btn : columnButtons) btn.setEnabled(true);
    }

    private void resetBoard() {
        board = new int[ROWS][COLS];
        winningDiscs.clear();
        for (int row = 0; row < ROWS; row++)
            for (int col = 0; col < COLS; col++)
                cells[row][col].setPlayer(0);
        currentPlayer = 1;
        enableBoard();
        statusLabel.setText("Player 1's Turn");
        repaint();
    }

    private void newGame() {
        resetBoard();
        score[1] = 0;
        score[2] = 0;
        toggleModeButton.setEnabled(true);
        statusLabel.setText("Player 1's Turn");
    }

    private void showWinPopup(int winningPlayer) {
        JOptionPane.showMessageDialog(this,
            " Player " + winningPlayer + " wins!\n\nFinal Score\nPlayer 1: " + score[1] + " | Player 2: " + score[2],
            "Game Over", JOptionPane.INFORMATION_MESSAGE);
    }

    private class CellPanel extends JPanel {
        private final int row, col;
        private int player = 0;

        public CellPanel(int row, int col) {
            this.row = row;
            this.col = col;
        }

        public void setPlayer(int player) {
            this.player = player;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.lightGray);
            Graphics2D g2 = (Graphics2D) g;

            boolean isWinner = winningDiscs.contains(new Point(row, col));
            g2.setColor(Color.black);
            g2.drawRect(0, 0, getWidth(), getHeight());

            if (player == 1) g2.setColor(isWinner ? Color.BLACK : Color.BLUE);
            else if (player == 2) g2.setColor(isWinner ? Color.BLACK : Color.YELLOW);
            else g2.setColor(Color.WHITE);

            g2.fillOval(5, 5, getWidth() - 10, getHeight() - 10);

            if (isWinner) {
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(3));
                g2.drawOval(5, 5, getWidth() - 10, getHeight() - 10);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ConnectFourUI::new);
    }
}
