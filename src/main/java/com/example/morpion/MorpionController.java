package com.example.morpion;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;


public class MorpionController implements Initializable {

    @FXML
    private Label message;

    @FXML
    private Label xNumLabel;

    @FXML
    private Label oNumLabel;

    @FXML
    private Label freeCaseLabel;

    @FXML
    private Button restart;

    @FXML
    private GridPane gridPane;

    private TicTacToeModel tictactoe = TicTacToeModel.getInstance();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (int i = 0; i < gridPane.getColumnCount(); i++) {
            for (int j = 0; j < gridPane.getRowCount(); j++) {
                System.out.println(gridPane.getChildren().toString());
                gridPane.add(new TicTacToeSquare(i, j), i, j);
            }
        }
        //Message de fin du jeu.
        message.textProperty().bind(
                Bindings.when(tictactoe.gameOver())
                        .then(tictactoe.getEndOfGameMessage())
                        .otherwise("")
        );


        xNumLabel.textProperty().bind(
                StringBinding
                        .stringExpression(tictactoe.getScore(Owner.FIRST))
                        .concat(" case pour X")
        );

        oNumLabel.textProperty().bind(
                Bindings.createStringBinding(() -> tictactoe.getScore(Owner.SECOND).getValue().toString() + " cases pour O")
        );

        freeCaseLabel.textProperty().bind(
                Bindings.createStringBinding(() -> tictactoe.getScore(Owner.NONE).getValue().toString() + " cases libres")
        );

        System.out.println((int) tictactoe.getScore(Owner.FIRST).getValue());
    }


    public void restart(ActionEvent actionEvent) {
        tictactoe.restart();
    }
}