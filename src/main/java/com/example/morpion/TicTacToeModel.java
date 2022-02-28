package com.example.morpion;

import javafx.beans.binding.*;
import javafx.beans.property.*;

import java.util.Arrays;
import java.util.stream.Stream;

public class TicTacToeModel {
    /**
     * Taille du plateau de jeu (pour être extensible).
     */
    private final static int BOARD_WIDTH = 3;
    private final static int BOARD_HEIGHT = 3;

    /**
     * Nombre de pièces alignés pour gagner (idem).
     */
    //private final static int WINNING_COUNT = 3;

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


     private IntegerProperty freeCases = new SimpleIntegerProperty(9);

     private IntegerProperty firstPlayerCases = new SimpleIntegerProperty(0);

     private IntegerProperty secondPlayerCases = new SimpleIntegerProperty(0);

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
                 board[i][j].set(Owner.NONE);
                 winningBoard[i][j].set(false);
             }
         }
         //Tours.
         turn.setValue(Owner.FIRST);
         winner.set(Owner.NONE);
         freeCases.set(9);
         firstPlayerCases.set(0);
         secondPlayerCases.set(0);
     }

    public int getFreeCases() {
        return freeCases.get();
    }

    public IntegerProperty freeCasesProperty() {
        return freeCases;
    }

    public int getFirstPlayerCases() {
        return firstPlayerCases.get();
    }

    public IntegerProperty firstPlayerCasesProperty() {
        return firstPlayerCases;
    }

    public int getSecondPlayerCases() {
        return secondPlayerCases.get();
    }

    public IntegerProperty secondPlayerCasesProperty() {
        return secondPlayerCases;
    }

    public void setFreeCases(int freeCases) {
        this.freeCases.set(freeCases);
    }

    public void setFirstPlayerCases(int firstPlayerCases) {
        this.firstPlayerCases.set(firstPlayerCases);
    }

    public void setSecondPlayerCases(int secondPlayerCases) {
        this.secondPlayerCases.set(secondPlayerCases);
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
         //Si le jeu se termine et il n'y a pas de vainqueur.
         return Bindings.when(gameOver().and(winner.isEqualTo(Owner.NONE))).
                 then("Le Jeu est terminé, il n'y a pas de gagnant ! ")
                 .otherwise(
                         Bindings.when(winner.isEqualTo(Owner.FIRST))
                                 .then("Jeu terminé, Le gagnant est Premier joueur")
                                 .otherwise("Jeu terminé, Le gagnant est Deuxième joueur")
                 );
     }


     public void setWinner(Owner winner) {
         this.winner.set(winner);
     }


     public boolean validSquare(int row, int column) {
        return (row >= 0 && row < 3) && (column >= 0 && row < 3);
     }


     public void nextPlayer() {
         turn.set(turn.get().opposite());
     }


     /**
      * Jouer dans la case (row, column) quand c’est possible.
      */
     public void play(int row, int column) {
        //Si le carreau est vide.
        if(validSquare(row, column)) {
            getSquare(row, column).set(turn.get());
            detectWinningSquare(row, column);
            this.nextPlayer();
        }
     }


      /**
       * @return true s’il est possible de jouer dans la case
       * c’est-à-dire la case est libre et le jeu n’est pas terminé
       */
      public BooleanBinding legalMove(int row, int column) {
          ObjectProperty<Owner> square = getSquare(row, column);
          //Si le jeu carreau est libre ou le jeu non terminé.
          return Bindings.not(this.gameOver()).and(
              square.isEqualTo(Owner.NONE)
          );
      }


      public NumberExpression getScore(Owner owner) {
          return switch (owner) {
              case NONE -> freeCases;
              case FIRST -> firstPlayerCases;
              case SECOND -> secondPlayerCases;
          };
      }


      /**
       * @return true si le jeu est terminé
       * (soit un joueur a gagné, soit il n’y a plus de cases à jouer)
       */
      public BooleanBinding gameOver() {
          return winner.isNotEqualTo(Owner.NONE).or(freeCases.isEqualTo(0));
      }


      /**
       *  Fournir les coups gagnants pour chaque position.
       */
      private void detectWinningSquare(int row, int column) {
          //Si le carreau choisi est paire.
          if (getSquare(0, 0).get().equals(turn.get()) &&
                  getSquare(0, 1).get().equals(turn.get()) &&
                  getSquare(0, 2).get().equals(turn.get())) {
              getWinningSquare(0, 0).set(true);
              getWinningSquare(0, 1).set(true);
              getWinningSquare(0, 2).set(true);
              this.setWinner(turn.get());
          } else if (getSquare(1, 0).get().equals(turn.get()) &&
                  getSquare(1, 1).get().equals(turn.get()) &&
                  getSquare(1, 2).get().equals(turn.get())) {
              getWinningSquare(1, 0).set(true);
              getWinningSquare(1, 1).set(true);
              getWinningSquare(1, 2).set(true);
              this.setWinner(turn.get());
          } else if (getSquare(2, 0).get().equals(turn.get()) &&
                  getSquare(2, 1).get().equals(turn.get()) &&
                  getSquare(2, 2).get().equals(turn.get())) {
              getWinningSquare(2, 0).set(true);
              getWinningSquare(2, 1).set(true);
              getWinningSquare(2, 2).set(true);
              this.setWinner(turn.get());
          } else if (getSquare(0, 0).get().equals(turn.get()) &&
                  getSquare(1, 0).get().equals(turn.get()) &&
                  getSquare(2, 0).get().equals(turn.get())) {
              getWinningSquare(0, 0).set(true);
              getWinningSquare(1, 0).set(true);
              getWinningSquare(2, 0).set(true);
              this.setWinner(turn.get());
          } else if (getSquare(0, 1).get().equals(turn.get()) &&
                  getSquare(1, 1).get().equals(turn.get()) &&
                  getSquare(2, 1).get().equals(turn.get())) {
              getWinningSquare(0, 1).set(true);
              getWinningSquare(1, 1).set(true);
              getWinningSquare(2, 1).set(true);
              this.setWinner(turn.get());
          } else if (getSquare(0, 2).get().equals(turn.get()) &&
                  getSquare(1, 2).get().equals(turn.get()) &&
                  getSquare(2, 2).get().equals(turn.get())) {
              getWinningSquare(0, 2).set(true);
              getWinningSquare(1, 2).set(true);
              getWinningSquare(2, 2).set(true);
              this.setWinner(turn.get());
          } else if (getSquare(0, 0).get().equals(turn.get()) &&
                  getSquare(1, 1).get().equals(turn.get()) &&
                  getSquare(2, 2).get().equals(turn.get())) {
              getWinningSquare(0, 0).set(true);
              getWinningSquare(1, 1).set(true);
              getWinningSquare(2, 2).set(true);
              this.setWinner(turn.get());
          } else if (getSquare(0, 2).get().equals(turn.get()) &&
                  getSquare(1, 1).get().equals(turn.get()) &&
                  getSquare(2, 0).get().equals(turn.get())) {
              getWinningSquare(0, 2).set(true);
              getWinningSquare(1, 1).set(true);
              getWinningSquare(2, 0).set(true);
              this.setWinner(turn.get());
          }
      }
}
