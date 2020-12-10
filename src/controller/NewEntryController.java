package controller;

import database.*;
import ui.WindowStyle;

import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.io.File;
import java.util.Vector;
import java.util.Calendar;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.collections.FXCollections;
import javafx.css.PseudoClass;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class NewEntryController {
    private Image imageTemp;
    private final static String imgLoc = System.getProperty("user.dir") + "/src/ui/design/related/covers/";
    private final PseudoClass errorField = PseudoClass.getPseudoClass("error");

    @FXML
    private AnchorPane rootPane;

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
    private Button clearButton;

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
    private Label authorLabel;

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

        String language[] = {"", "Japanese", "English", "French", "Spanish", "German", "Other"};
        languages.setItems(FXCollections.observableArrayList(language));

        // textfield listeners
        title.textProperty().addListener((observable, oldValue, newValue)-> {
            titleLabel.setText(newValue);
        });

        author.textProperty().addListener((observable, oldValue, newValue)-> {
            authorLabel.setText(newValue);
        });
    }

    @FXML
    void AddButtonClicked(ActionEvent event) throws Exception{
        rootPane.getScene().getStylesheets().add(getClass().getResource("../ui/design/AddStyle.css").toExternalForm());

        // copy file local to app directory, store files absolute location
        String filename = "";
        if (!imageAddress.getText().isBlank()){
            File f = new File(imageAddress.getText());
            filename = f.getName();

            if (new File(imgLoc + filename).exists()){
                String fileTemp = filename;
                for (int i = 1; new File(imgLoc + fileTemp).exists(); i++){
                    fileTemp = FilenameUtils.getBaseName(filename) + "("+i+")." + FilenameUtils.getExtension(filename);
                }
                filename = fileTemp;
            }

            Files.copy(Paths.get(imageAddress.getText()), Paths.get(imgLoc + filename), StandardCopyOption.REPLACE_EXISTING);
        }

        // check choiceboxes
        Integer yearCheck;
        String formatCheck;
        String languageCheck;
        if (years.getValue() == null) yearCheck = null;
        else yearCheck = years.getValue();

        if (formats.getValue() == null) formatCheck = "";
        else formatCheck = formats.getValue();

        if (languages.getValue() == null) languageCheck = "";
        else languageCheck = languages.getValue();

        // titles, authors, and aliases cannot be null at the same time i.e. title and title alias cannot be null together, either or must have an entry
        if (title.getText().isBlank() && titleAlias.getText().isBlank()){
            title.pseudoClassStateChanged(errorField, true);
            titleAlias.pseudoClassStateChanged(errorField, true);
            return;
        }

        if (author.getText().isBlank() && authorAlias.getText().isBlank()){
            author.pseudoClassStateChanged(errorField, true);
            authorAlias.pseudoClassStateChanged(errorField, true);
            return;
        }
        
        // Add new entry into database
        EntryInfo ei = new EntryInfo(DataEntry.CreateID(), title.getText(), titleAlias.getText(), author.getText(), authorAlias.getText(), yearCheck, formatCheck, languageCheck, filename);
        DataEntry.Add(ei);
        System.out.println("Entry added.");
    }

    @FXML
    void BackButtonClicked(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../ui/MainScene.fxml"));
        Parent root = loader.load();
        
        WindowStyle ws = new WindowStyle();
        ws.setStyle(root, (Stage) rootPane.getScene().getWindow());

        rootPane.getScene().setRoot(root);
    }

    @FXML
    void BrowseButtonClicked(ActionEvent event) {
        FileChooser f = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.jpeg");
        f.getExtensionFilters().add(filter);

        // Browse and set image url
        File file = f.showOpenDialog(browseButton.getScene().getWindow());
            if (file != null){
                imageTemp = new Image(file.toURI().toString());
                imageAddress.setText(file.getAbsolutePath());
                imageView.setImage(imageTemp);
                clearButton.setVisible(true);
            }
    }

    @FXML
    void ClearButtonClicked(ActionEvent event){
        imageAddress.setText("");
        imageView.setImage(null);
        clearButton.setVisible(false);
    }
}
