package com.example.morpion;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;
import javafx.scene.control.TextField;

public class TicTacToeSquare extends TextField {

    private static TicTacToeModel model = TicTacToeModel.getInstance();
    private ObjectProperty<Owner> ownerProperty = new SimpleObjectProperty<>(Owner.NONE);
    private BooleanProperty winnerProperty = new SimpleBooleanProperty(false);

    public ObjectProperty<Owner> ownerProperty() {
        return this.ownerProperty;
    }

    public BooleanProperty colorProperty() {
        return this.colorProperty();
    }

    public TicTacToeSquare(final int row, final int column) {
        System.out.println("Joueur actuel : " + model.turnProperty().get().toString());
        super.editableProperty().set(false);
        super.setMaxSize(400, 400);
        Bindings.bindBidirectional(this.ownerProperty(), model.getSquare(row, column));

        super.setOnMouseClicked((mouseEvent) -> {
            System.out.println("Carreau [" + row + "," + column + "] cliqué");
            //Si le mouvement est possible et le jeu n'est pas terminé.
            if(model.legalMove(row, column).get() && !model.gameOver().get()) {

                if(model.turnProperty().isEqualTo(Owner.FIRST).getValue()) {
                    this.textProperty().set("X");
                    System.out.println("X");
                } else {
                    this.textProperty().set("O");
                    System.out.println("O");
                }

                model.play(row, column);

                model.detectWinningSquare(row, column);

                System.out.println(model.getSquare(row, column).getValue());
            }

        });


        super.setOnMouseEntered((mouseEvent) -> {
            System.out.println("Carreau [" + row + "," + column + "]");
            if(model.legalMove(row, column).get()) {
                this.setStyle("-fx-background-color: green;");
            } else {
                this.setStyle("-fx-background-color: red;");
            }
        });


        super.setOnMouseExited((mouseEvent) -> {
            this.setStyle("");
        });
    }
}
