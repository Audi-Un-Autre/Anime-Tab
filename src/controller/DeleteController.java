package controller;

import database.*;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class DeleteController {
    EntryInfo ei;
    ViewEntryController vec;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button deleteButton;

    @FXML
    private Button cancelDeleteButton;

    public void init(EntryInfo ei, ViewEntryController vec){
        this.vec = vec;
        this.ei = ei;
    }

    @FXML
    private void CancelDeleteButtonClicked(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        vec.CancelDelete();
        stage.close();
    }

    @FXML
    private void DeleteButtonClicked(ActionEvent event) throws Exception{
        DataEntry.Delete(ei);
        vec.EntryDeleted();
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
}
