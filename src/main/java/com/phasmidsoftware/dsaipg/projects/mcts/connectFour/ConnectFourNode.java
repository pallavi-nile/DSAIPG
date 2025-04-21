/*
 * Copyright (c) 2024. Robin Hillyard
 */

 package com.phasmidsoftware.dsaipg.projects.mcts.connectfour;

 import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
 import com.phasmidsoftware.dsaipg.projects.mcts.core.State;
 
 import java.util.ArrayList;
 import java.util.Collection;
 import java.util.Objects;
 
 /**
  * Implementation of the Node interface for Connect Four using MCTS.
  */
 public class ConnectFourNode implements Node<ConnectFourGame> {
     private final State<ConnectFourGame> state;
     private final Collection<Node<ConnectFourGame>> children;
     private int wins;
     private int playouts;
     
     /**
      * Constructor for a Connect Four node.
      * 
      * @param state the state represented by this node.
      */
     public ConnectFourNode(State<ConnectFourGame> state) {
         this.state = state;
         this.children = new ArrayList<>();
         
         // If this is a terminal state, set the playouts and wins accordingly
         if (state.isTerminal()) {
             this.playouts = 1;
             this.wins = calculateWins();
         } else {
             this.playouts = 0;
             this.wins = 0;
         }
     }
     
     /**
      * Calculate the number of wins for this terminal node.
      * 
      * @return 2 for a win, 1 for a draw, 0 for a loss.
      */
     private int calculateWins() {
         // Get the winner if there is one
         return state.winner().map(winner -> {
             // If the player who played to this state (opponent of current state's player) won
             // then this node is a win for that player
             int previous = ConnectFourGame.opponent(state.player());
             if (winner == previous) {
                 return 2; // Win
             } else {
                 return 0; // Loss
             }
         }).orElse(1); // Draw
     }
     
     @Override
     public boolean isLeaf() {
         return state.isTerminal();
     }
     
     @Override
     public State<ConnectFourGame> state() {
         return state;
     }
     
     @Override
     public boolean white() {
         // Check if the player who played to this state is the opening player
         ConnectFourGame game = state.game();
         int opponentPlayer = ConnectFourGame.opponent(state.player());
         return opponentPlayer == game.opener();
     }
     
     @Override
     public Collection<Node<ConnectFourGame>> children() {
         return children;
     }
     
     @Override
     public void backPropagate() {
         // Reset counters
         this.wins = 0;
         this.playouts = 0;
         
         // If this is a leaf node
         if (isLeaf()) {
             this.playouts = 1;
             this.wins = calculateWins();
         } 
         // If it has children
         else if (!children.isEmpty()) {
             for (Node<ConnectFourGame> child : children) {
                 this.playouts += child.playouts();
                 // If this node is a "white" node, then we care about black's wins and vice versa
                 if (white()) {
                     // For white node, count black's wins
                     this.wins += (2 * child.playouts() - child.wins());
                 } else {
                     // For black node, count white's wins
                     this.wins += child.wins();
                 }
             }
         }
     }
     
     @Override
     public void addChild(State<ConnectFourGame> state) {
         children.add(new ConnectFourNode(state));
     }
     
     @Override
     public int wins() {
         return wins;
     }
     
     @Override
     public int playouts() {
         return playouts;
     }
     
     @Override
     public String toString() {
         return "ConnectFourNode{" +
                 "state=" + state +
                 ", children=" + children.size() +
                 ", wins=" + wins +
                 ", playouts=" + playouts +
                 '}';
     }
     
     @Override
     public boolean equals(Object o) {
         if (this == o) return true;
         if (!(o instanceof ConnectFourNode)) return false;
         ConnectFourNode that = (ConnectFourNode) o;
         return Objects.equals(state, that.state);
     }
     
     @Override
     public int hashCode() {
         return Objects.hash(state);
     }
 }
