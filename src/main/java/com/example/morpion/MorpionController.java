package com.example.morpion;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(url.toString());
        for (int i = 0; i < gridPane.getColumnCount(); i++) {
            for (int j = 0; j < gridPane.getRowCount(); j++) {
                System.out.println(gridPane.getChildren().toString());
                gridPane.add(new TicTacToeSquare(i, j), i, j);
            }
        }
    }


    public void restart(ActionEvent actionEvent) {
        TicTacToeModel tictactoe = TicTacToeModel.getInstance();
        tictactoe.restart();
    }
}