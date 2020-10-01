package ui.controllers;

import java.io.IOException;
import java.util.Vector;
import java.util.Calendar;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;

public class NewEntryController {

    @FXML
    private BorderPane rootPane;

    @FXML
    private Button backButton;

    @FXML
    private TextField title;

    @FXML
    private TextField titleAlias;

    @FXML
    private TextField author;

    @FXML
    private Button browseButton;

    @FXML
    private TextField authorAlias;

    @FXML
    private ChoiceBox<Integer> years;

    @FXML
    private ChoiceBox<String> formats;

    @FXML
    private ChoiceBox<String> languages;

    @FXML
    private TextField imageAddress;

    @FXML
    private ImageView imageView;

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
    private Label workTypeLabel;

    @FXML
    private Label languageLabel;

    public void initialize(){
        //populate choiceboxes
        Vector<Integer> year = new Vector<Integer>();
        Calendar c = Calendar.getInstance();
        for (int i = c.get(Calendar.YEAR); i >= 1900; i--){
            year.add(i);
        }
        years.setItems(FXCollections.observableArrayList(year));

        String workTypes[] = {"Anime", "Manga", "Manhwa", "Comic", "Other"};
        formats.setItems(FXCollections.observableArrayList(workTypes));

        String language[] = {"Japanese", "English", "French", "Spanish", "German", "Other"};
        languages.setItems(FXCollections.observableArrayList(language));
    }

    @FXML
    void BackButtonClicked(ActionEvent event) throws IOException{
        ChangeUI("MainScene");
    }

    @FXML
    void BrowseButtonClicked(ActionEvent event) {
    }

    private void ChangeUI(String name) throws IOException{
        Parent root = null;
        root = FXMLLoader.load(getClass().getResource("../scenes/"+name+".fxml"));
        rootPane.getScene().setRoot(root);
    }

}
