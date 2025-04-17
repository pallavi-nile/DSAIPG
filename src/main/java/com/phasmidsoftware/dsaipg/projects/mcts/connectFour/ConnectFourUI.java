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

   