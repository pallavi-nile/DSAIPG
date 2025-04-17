
 package com.phasmidsoftware.dsaipg.projects.mcts.connectfour;

//  import com.phasmidsoftware.dsaipg.projects.mcts.connectfour;
 
 import javax.swing.SwingUtilities;

// import com.phasmidsoftware.dsaipg.projects.mcts.connectfour;
 
 /**
  * Main class to start the Connect Four game.
  */
 public class ConnectFourMain {
     /**
      * Main method to start the application.
      * 
      * @param args command line arguments.
      */
     public static void main(String[] args) {
         SwingUtilities.invokeLater(ConnectFourUI::new);
     }
 }