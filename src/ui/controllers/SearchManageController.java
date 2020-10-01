package ui.controllers;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;

public class SearchManageController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private Button backButton;

    @FXML
    private TextField search;

    @FXML
    private ListView<Hyperlink> resultList;

    @FXML
    private Button searchButton;

    @FXML
    private ChoiceBox<Integer> years;

    @FXML
    private ChoiceBox<String> workTypes;

    @FXML
    private ChoiceBox<String> languages;

    @FXML
    private ImageView imageView;

    @FXML
    private Label idLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Label titleAliasLabel;

    @FXML
    private Label authorLabel;

    @FXML
    private Label authorAliasLabel;

    @FXML
    private Label yearLabel;

    @FXML
    private Label formatLabel;

    @FXML
    private Label languageLabel;

    @FXML
    private Label id;

    @FXML
    private Label title;

    @FXML
    private Label titleAlias;

    @FXML
    private Label author;

    @FXML
    private Label authorAlias;

    @FXML
    private Label year;

    @FXML
    private Label workType;

    @FXML
    private Label language;

    @FXML
    void BackButtonClicked(ActionEvent event) throws IOException{
        ChangeUI("MainScene");
    }

    @FXML
    void SearchButtonClicked(ActionEvent event) throws IOException{

    }

    private void ChangeUI(String name) throws IOException{
        Parent root = null;
        root = FXMLLoader.load(getClass().getResource("../scenes/"+name+".fxml"));
        rootPane.getScene().setRoot(root);
    }

}
