package ui.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.layout.BorderPane;

public class ViewEntryController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private Button editEntryButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button backButton;

    @FXML
    private ImageView imageView;

    @FXML
    private Label titleLabel;

    @FXML
    private Label authorLabel;

    @FXML
    private Label yearLabel;

    @FXML
    private Label formatLabel;

    @FXML
    private Label languageLabel;

    @FXML
    private Label imageLabel;

    @FXML
    private Button browseButton;

    @FXML
    private TextField imageAddress;

    @FXML
    private TextField editTitleLabel;

    @FXML
    private TextField editAuthorLabel;

    @FXML
    private Label title;

    @FXML
    private Label author;

    @FXML
    private ChoiceBox<?> editLanguage;

    @FXML
    private ChoiceBox<?> editYear;

    @FXML
    private ChoiceBox<?> editFormat;

    @FXML
    private Button cancelButton;

    @FXML
    private Label idView;

    @FXML
    private Label titleView;

    @FXML
    void BackButtonClicked(ActionEvent event) throws IOException{
        SettingsUI("SearchManageScene");
    }

    @FXML
    void BrowseButtonClicked(ActionEvent event) throws IOException{

    }

    @FXML
    void CancelButtonClicked(ActionEvent event) {

    }

    @FXML
    void DeleteClicked(ActionEvent event) {

    }

    @FXML
    void EditClicked(ActionEvent event) {

    }

    private void SettingsUI(String name) throws IOException{
        Parent root = null;
        root = FXMLLoader.load(getClass().getResource("../scenes/"+name+".fxml"));
        rootPane.getScene().setRoot(root);
    }

}
