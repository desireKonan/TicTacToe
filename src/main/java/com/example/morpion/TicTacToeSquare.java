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
        this.setFont(Font.font(20));
        this.ownerProperty().bind(model.getSquare(row, column));
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
            }
        });


        super.setOnMouseEntered((mouseEvent) -> {
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
