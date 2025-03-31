# Tic Tac Toe – Monte Carlo Tree Search (MCTS)

This is a Java Swing-based Tic Tac Toe game that uses **Monte Carlo Tree Search (MCTS)** for the AI opponent. The application is equipped with a clean **black and white user interface**, real-time move evaluation, and a scoring system.

---

##  Features

-  Two-player mode: You vs Opponent (Bot powered by MCTS)
-  MCTS-based move selection with playout simulation
-  Clean dark-themed UI with:
    - New Game button
    - Reset Score button
    - Exit button (prints playtime to console)
- Real-time status display ("Your turn" / "Opponent is thinking...")
-  Console logs MCTS move timing and total session time on exit
-  Responsive and resizable Swing GUI

---

##  Algorithm: Monte Carlo Tree Search (MCTS)

This implementation uses MCTS with:
- **Selection**: UCB1 formula
- **Expansion**: By simulating possible states
- **Simulation**: Random playouts
- **Backpropagation**: Win/draw updates from leaf to root

---

##  Technologies Used

- Java 17+
- Swing GUI
- Maven project structure
- JUnit 5 (for tests)

---

##  How to Run

1. Clone the repo or download the source code.

2. Open in IntelliJ or any Java IDE.

3. Ensure the following directory structure:
4. src/ main/ java/ com/phasmidsoftware/dsaipg/projects/mcts/tictactoe/ TicTacToeUI.java and Run this file .
