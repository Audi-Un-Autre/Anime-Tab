package ui.controllers;

import java.io.IOException;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.stage.StageStyle;

public class MainController {
    
    @FXML
    private BorderPane rootPane;

    @FXML
    private Button settingsButton;

    @FXML
    private Button newEntryButton;

    @FXML
    private Button searchManageButton;

    @FXML
    private void NewEntryButtonClicked(ActionEvent event) throws IOException{
        ChangeUI("NewEntryScene");
    }

    @FXML
    private void SearchManageButtonClicked(ActionEvent event) throws IOException{
        ChangeUI("SearchManageScene");
    }

    @FXML
    private void SettingsButtonClicked(ActionEvent event) throws IOException{
        SettingsUI("SettingsScene");
    }

    private void ChangeUI(String name) throws IOException{
        Parent root = null;
        root = FXMLLoader.load(getClass().getResource("../scenes/"+name+".fxml"));
        rootPane.getScene().setRoot(root);
    }

    private void SettingsUI(String name) throws IOException{
        Parent root = null;
        root = FXMLLoader.load(getClass().getResource("../scenes/"+name+".fxml"));
        Scene scene = new Scene(root);
        Stage stage = new Stage(StageStyle.UTILITY);
        stage.setTitle("Settings");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("../design/gon.png")));
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }
}
