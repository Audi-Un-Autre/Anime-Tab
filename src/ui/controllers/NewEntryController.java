package ui.controllers;

import database.*;

import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.io.File;
import java.util.Vector;
import java.util.Calendar;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;

public class NewEntryController {
    private Image imageTemp;
    private final static String imgLoc = System.getProperty("user.dir") + "/src/ui/design/related/covers/";

    @FXML
    private BorderPane rootPane;

    @FXML
    private AnchorPane metaAnchor;

    @FXML
    private Button addButton;

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
    private Button previewButton;

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
    void AddButtonClicked(ActionEvent event) throws Exception{
        // copy file local to app directory, store files absolute location
        File f = new File(imageAddress.getText());
        String filename = f.getName();
        Files.copy(Paths.get(imageAddress.getText()), Paths.get(imgLoc + filename), StandardCopyOption.REPLACE_EXISTING);
        
        // Add new entry into database
        EntryInfo ei = new EntryInfo(DataEntry.CreateID(), title.getText(), titleAlias.getText(), author.getText(), authorAlias.getText(), years.getValue(), formats.getValue(), languages.getValue(), filename);
        DataEntry.Add(ei);
        System.out.println("Entry added.");
    }

    @FXML
    void BackButtonClicked(ActionEvent event) throws IOException{
        ChangeUI("MainScene");
    }

    @FXML
    void BrowseButtonClicked(ActionEvent event) {
        FileChooser f = new FileChooser();

        // Browse and set image url
        File file = f.showOpenDialog(browseButton.getScene().getWindow());
            if (file != null){
                imageTemp = new Image(file.toURI().toString());
                imageAddress.setText(file.getAbsolutePath());
            }
    }

    @FXML
    void PreviewButtonClicked(ActionEvent event){
        metaAnchor.setVisible(true);

        // Set all meta into preview pane
        {
            titleLabel.setText(title.getText());
            titleAliasLabel.setText(titleAlias.getText());
            authorLabel.setText(author.getText());
            authorAliasLabel.setText(authorAlias.getText());
            yearLabel.setText(String.valueOf(years.getValue()));
            workTypeLabel.setText(formats.getValue());
            languageLabel.setText(languages.getValue());

            imageView.setImage(imageTemp);
            imageView.maxWidth(300);
        }
    }

    private void ChangeUI(String name) throws IOException{
        Parent root = null;
        root = FXMLLoader.load(getClass().getResource("../scenes/"+name+".fxml"));
        rootPane.getScene().setRoot(root);
    }

}
