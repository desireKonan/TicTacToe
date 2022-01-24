package com.example.morpion;

import javafx.beans.binding.*;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.HashMap;

public class TicTacToeModel {
    /**
     * Taille du plateau de jeu (pour être extensible).
     */
    private final static int BOARD_WIDTH = 3;
    private final static int BOARD_HEIGHT = 3;

    /**
     * Nombre de pièces alignés pour gagner (idem).
     */
    private final static int WINNING_COUNT = 3;

    /**
     * Joueur courant.
     */
    private final ObjectProperty<Owner> turn = new SimpleObjectProperty<>(Owner.FIRST);


    /**
     * Vainqueur du jeu, NONE si pas de vainqueur.
     */
     private final ObjectProperty<Owner> winner = new SimpleObjectProperty<>(Owner.NONE);

     /**
      * Plateau de jeu.
      */
     private final ObjectProperty<Owner>[][] board = new SimpleObjectProperty[3][3];

     /**
      * Positions gagnantes.
      */
     private final BooleanProperty[][] winningBoard = new BooleanProperty[3][3];

     //Comment faire une liste de coups gagnants pour chaque position.
     private final HashMap<Integer, Integer[][]> winnningSquareList = new HashMap();

    /**
     * Constructeur privé.
     */
     private TicTacToeModel() {
        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                board[i][j] = new SimpleObjectProperty<>(Owner.NONE);
                winningBoard[i][j] = new SimpleBooleanProperty(false);
            }
        }
     }

    /**
     * @return la seule instance possible du jeu.
     */
     public static TicTacToeModel getInstance() {
         return TicTacToeModelHolder.INSTANCE;
     }


    /**
     * Classe interne selon le pattern singleton.
     */
     private static class TicTacToeModelHolder {
         private static final TicTacToeModel INSTANCE = new TicTacToeModel();
     }


     public void restart() {
         for (int i = 0; i < BOARD_WIDTH; i++) {
             for (int j = 0; j < BOARD_HEIGHT; j++) {
                 board[i][j] = new SimpleObjectProperty<>(Owner.NONE);
                 winningBoard[i][j] = new SimpleBooleanProperty(false);
             }
         }
         //Tours.
         turn.setValue(Owner.FIRST);
         winner.set(Owner.NONE);
     }

     public final ObjectProperty<Owner> turnProperty() {
        return turn;
     }


    public final ObjectProperty<Owner> getSquare(int row, int column) {
        return board[row][column];
    }

    public final BooleanProperty getWinningSquare(int row, int column) {
        return winningBoard[row][column];
    }

    /**
     * Cette fonction ne doit donner le bon résultat que si le jeu
     * est terminé. L’affichage peut être caché avant la fin du jeu.
     *
     * @return résultat du jeu sous forme de texte
     */
     public final StringExpression getEndOfGameMessage() {
         return null;
     }

     public void setWinner(Owner winner) {
         System.out.println("Gagnant : " + winner);
         this.winner.set(winner);
     }

     public boolean validSquare(int row, int column) {
        return winningBoard[row][column].getValue();
     }

     public void nextPlayer() {
         turn.set(turn.get().opposite());
     }

     /**
      * Jouer dans la case (row, column) quand c’est possible.
      */
     public void play(int row, int column) {
        //Si le carreau est vide.
        getSquare(row, column).set(turn.get());
        this.nextPlayer();
     }

      /**
       * @return true s’il est possible de jouer dans la case
       * c’est-à-dire la case est libre et le jeu n’est pas terminé
       */
      public BooleanBinding legalMove(int row, int column) {
          ObjectProperty<Owner> square = getSquare(row, column);
          //Si le jeu carreau est libre ou le jeu non terminé.
          return square.isEqualTo(new SimpleObjectProperty<>(Owner.NONE)).and(turn.isNotEqualTo(Owner.NONE));
      }

      public NumberExpression getScore(Owner owner) {
          return null;
      }

      /**
       * @return true si le jeu est terminé
       * (soit un joueur a gagné, soit il n’y a plus de cases à jouer)
       */
      public BooleanBinding gameOver() {
          return winner.isNotEqualTo(Owner.NONE).or(turn.isEqualTo(Owner.NONE));
      }

    /**
     * Fournir les coups gagnants pour chaque position.
     */
    public void detectWinningSquare(int row, int column) {
        //Si le carreau choisi est paire.
        if((row + column) % 2 == 0) {
            //Il a entre 3 et 4 coups gagnants.

            //Position à 3 coups.
            if(row == 0 && column == 0) {
                //On prend l'intersection de la ligne colonne, diagonal de nos élements.
                //Sens Horizontale.
                for(int i = 0; i < BOARD_WIDTH; i++) {
                    winnningSquareList.put(0, new Integer[row][i]);
                }

                //Sens Vertical.
                for(int i = 0; i < BOARD_HEIGHT; i++) {
                    winnningSquareList.put(1, new Integer[i][column]);
                }

                //Sens diagonal.
                for(int i = 0; i < BOARD_HEIGHT; i++) {
                    winnningSquareList.put(2, new Integer[i][i]);
                }

                for(int j = 0; j < winnningSquareList.size(); j++) {
                    for (int k = 0; k < winnningSquareList.get((j)).length; k++) {
                        System.out.println("Liste des coups gagnants : " + (j + ", " + k));
                    }
                }
            }

            //Position à 4 coups.

        } else {
            //Sinon il en a que 2.
        }
    }
}
