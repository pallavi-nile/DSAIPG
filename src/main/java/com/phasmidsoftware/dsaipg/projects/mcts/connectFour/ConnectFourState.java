package com.phasmidsoftware.dsaipg.projects.mcts.connectfour;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;
import com.phasmidsoftware.dsaipg.projects.mcts.core.RandomState;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

/**
 * State for Connect Four game.
 */
public class ConnectFourState implements State<ConnectFourGame> {
    private final ConnectFourGame game;
    private final int[][] board;
    private final int currentPlayer;
    private final RandomState randomState;

    public static final int EMPTY = -1;

    public ConnectFourState(ConnectFourGame game, int[][] board, int currentPlayer, RandomState randomState) {
        this.game = game;
        this.board = deepCopy(board);
        this.currentPlayer = currentPlayer;
        this.randomState = randomState;
    }

    private int[][] deepCopy(int[][] original) {
        int[][] copy = new int[ConnectFourGame.ROWS][ConnectFourGame.COLS];
        for (int i = 0; i < ConnectFourGame.ROWS; i++) {
            if (original[i] == null) Arrays.fill(copy[i], EMPTY);
            else System.arraycopy(original[i], 0, copy[i], 0, ConnectFourGame.COLS);
        }
        return copy;
    }

    @Override
    public ConnectFourGame game() {
        return game;
    }

    @Override
    public boolean isTerminal() {
        return winner().isPresent() || isBoardFull();
    }

    private boolean isBoardFull() {
        for (int col = 0; col < ConnectFourGame.COLS; col++) {
            if (board[0][col] == EMPTY) return false;
        }
        return true;
    }

    @Override
    public int player() {
        return currentPlayer;
    }

    @Override
    public Optional<Integer> winner() {
        for (int p : new int[]{ConnectFourGame.PLAYER_1, ConnectFourGame.PLAYER_2}) {
            if (checkWin(p)) return Optional.of(p);
        }
        return Optional.empty();
    }

    private boolean checkWin(int p) {
        // Horizontal
        for (int row = 0; row < ConnectFourGame.ROWS; row++) {
            for (int col = 0; col <= ConnectFourGame.COLS - ConnectFourGame.CONNECT_N; col++) {
                boolean win = true;
                for (int k = 0; k < ConnectFourGame.CONNECT_N; k++) {
                    if (board[row][col + k] != p) win = false;
                }
                if (win) return true;
            }
        }

        // Vertical
        for (int col = 0; col < ConnectFourGame.COLS; col++) {
            for (int row = 0; row <= ConnectFourGame.ROWS - ConnectFourGame.CONNECT_N; row++) {
                boolean win = true;
                for (int k = 0; k < ConnectFourGame.CONNECT_N; k++) {
                    if (board[row + k][col] != p) win = false;
                }
                if (win) return true;
            }
        }

        // Diagonal right
        for (int row = 0; row <= ConnectFourGame.ROWS - ConnectFourGame.CONNECT_N; row++) {
            for (int col = 0; col <= ConnectFourGame.COLS - ConnectFourGame.CONNECT_N; col++) {
                boolean win = true;
                for (int k = 0; k < ConnectFourGame.CONNECT_N; k++) {
                    if (board[row + k][col + k] != p) win = false;
                }
                if (win) return true;
            }
        }

        // Diagonal left
        for (int row = ConnectFourGame.CONNECT_N - 1; row < ConnectFourGame.ROWS; row++) {
            for (int col = 0; col <= ConnectFourGame.COLS - ConnectFourGame.CONNECT_N; col++) {
                boolean win = true;
                for (int k = 0; k < ConnectFourGame.CONNECT_N; k++) {
                    if (board[row - k][col + k] != p) win = false;
                }
                if (win) return true;
            }
        }

        return false;
    }

    @Override
    public Random random() {
        return new Random(randomState.longValue());
    }

    @Override
    public Collection<Move<ConnectFourGame>> moves(int player) {
        List<Move<ConnectFourGame>> moves = new ArrayList<>();
        for (int col = 0; col < ConnectFourGame.COLS; col++) {
            if (isValidMove(col)) moves.add(new ConnectFourMove(player, col));
        }
        return moves;
    }

    public boolean isValidMove(int col) {
        return col >= 0 && col < ConnectFourGame.COLS && board[0][col] == EMPTY;
    }

    private int getLowestEmptyRow(int col) {
        for (int row = ConnectFourGame.ROWS - 1; row >= 0; row--) {
            if (board[row][col] == EMPTY) return row;
        }
        return -1;
    }

    @Override
    public State<ConnectFourGame> next(Move<ConnectFourGame> move) {
        ConnectFourMove m = (ConnectFourMove) move;
        int col = m.column();
        int row = getLowestEmptyRow(col);
        if (row == -1) throw new IllegalStateException("Column full: " + col);

        int[][] newBoard = deepCopy(board);
        newBoard[row][col] = m.player();
        return new ConnectFourState(game, newBoard, ConnectFourGame.opponent(m.player()), randomState.next());
    }

    public int[][] getBoard() {
        return deepCopy(board);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : board) {
            for (int cell : row) {
                sb.append(switch (cell) {
                    case -1 -> ". ";
                    case 0 -> "X ";
                    case 1 -> "O ";
                    default -> "? ";
                });
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
