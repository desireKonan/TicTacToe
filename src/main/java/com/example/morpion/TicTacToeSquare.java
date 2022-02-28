package com.example.morpion;

import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;

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
        super.editableProperty().set(false);
        super.setMaxSize(400, 400);
        this.setAlignment(Pos.CENTER);
        this.ownerProperty().bind(model.getSquare(row, column));
        this.winnerProperty.bind(model.getWinningSquare(row, column));
        this.fontProperty().bind(
                Bindings.when(model.getWinningSquare(row, column))
                        .then(Bindings.createObjectBinding(() -> Font.font(50)))
                        .otherwise(Bindings.createObjectBinding(() -> Font.font(20)))
        );
        super.setOnMouseClicked((mouseEvent) -> {
            //Si le mouvement est possible et le jeu n'est pas terminé.
            if(model.legalMove(row, column).get() && !model.gameOver().get()) {
                this.textProperty().bind(
                        Bindings.when(ownerProperty().isEqualTo(Owner.NONE))
                                .then("")
                                .otherwise(
                                        Bindings.when(ownerProperty().isEqualTo(Owner.FIRST))
                                                .then("X")
                                                .otherwise("O"))
                );

                model.play(row, column);

                //On modifie le score ici.
                switch (ownerProperty().get()) {
                    case FIRST -> {
                        model.setFreeCases(model.getFreeCases() - 1);
                        model.setFirstPlayerCases(model.getFirstPlayerCases() + 1);
                        break;
                    }
                    case SECOND -> {
                        model.setFreeCases(model.getFreeCases() - 1);
                        model.setSecondPlayerCases(model.getSecondPlayerCases() + 1);
                        break;
                    }
                }
            }
        });


        super.setOnMouseEntered((mouseEvent) -> {
            if(model.legalMove(row, column).get()) {
                this.setStyle("-fx-background-color: #27ae60;");
            } else {
                this.setStyle("-fx-background-color: #e74c3c;");
            }
        });


        super.setOnMouseExited((mouseEvent) -> {
            this.setStyle("");
        });
    }
}
