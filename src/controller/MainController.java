package controller;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.stage.StageStyle;
import ui.WindowStyle;

public class MainController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Button settingsButton;

    @FXML
    private Button newEntryButton;

    @FXML
    private Button searchManageButton;

    @FXML
    private Button exitButton;

    @FXML
    private void NewEntryButtonClicked(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../ui/NewEntryScene.fxml"));
        Parent root = loader.load();
        
        WindowStyle ws = new WindowStyle();
        ws.setStyle(root, (Stage) rootPane.getScene().getWindow());

        rootPane.getScene().setRoot(root);
    }

    @FXML
    private void SearchManageButtonClicked(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../ui/SearchManageScene.fxml"));
        Parent root = loader.load();

        SearchManageController smController = loader.getController();
        smController.InitSearch();
        
        WindowStyle ws = new WindowStyle();
        ws.setStyle(root, (Stage) rootPane.getScene().getWindow());

        rootPane.getScene().setRoot(root);
    }

    @FXML
    private void SettingsButtonClicked(ActionEvent event) throws IOException{
        Parent root = null;
        root = FXMLLoader.load(getClass().getResource("../ui/SettingsScene.fxml"));
        Scene scene = new Scene(root);

        Stage stage = new Stage(StageStyle.UTILITY);
        stage.setTitle("Settings");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("../ui/design/gon.png")));
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    private void exitButtonClicked(ActionEvent event){
        Platform.exit();
    }
}
