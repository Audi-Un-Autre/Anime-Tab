package controller;

import database.*;

import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Vector;
import java.util.Calendar;

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
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.collections.FXCollections;

public class ViewEntryController {

    private EntryInfo   ei;
    private Parent      root;
    private boolean     edited;
    private final static String imgLoc = System.getProperty("user.dir") + "/src/ui/design/related/covers/";

    @FXML
    private BorderPane rootPane;

    @FXML
    private GridPane editGrid, viewGrid;

    @FXML
    private AnchorPane viewPane;

    @FXML
    private Button editEntryButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button backButton;

    @FXML
    private ImageView imageView;

    @FXML
    private Label deletedLabel;

    @FXML
    private Label year;

    @FXML
    private Label format;

    @FXML
    private Label language;

    @FXML
    private Button browseButton;

    @FXML
    private Button finalizeEditButton;

    @FXML
    private TextField imageAddress;

    @FXML
    private Label title;

    @FXML
    private Label author;

    @FXML
    private ChoiceBox<String> editLanguage;

    @FXML
    private ChoiceBox<Integer> editYear;

    @FXML
    private ChoiceBox<String> editFormat;

    @FXML
    private Button cancelButton;

    @FXML
    private Label idView;

    @FXML
    private Label titleView;

    @FXML
    private TextField editAuthor;

    @FXML
    private TextField editTitle;

    public void initialize(){
        //populate choiceboxes
        Vector<Integer> year = new Vector<Integer>();
        Calendar c = Calendar.getInstance();
        for (int i = c.get(Calendar.YEAR); i >= 1900; i--){
            if (i == c.get(Calendar.YEAR)) year.add(null);
            year.add(i);
        }
        editYear.setItems(FXCollections.observableArrayList(year));

        String workTypes[] = {"", "Anime", "Manga", "Manhwa", "Comic", "Other"};
        editFormat.setItems(FXCollections.observableArrayList(workTypes));

        String language[] = {"", "Japanese", "English", "French", "Spanish", "German", "Other"};
        editLanguage.setItems(FXCollections.observableArrayList(language));
    }

    @FXML
    void BackButtonClicked(ActionEvent event) throws Exception{
        if (!edited){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../ui/SearchManageScene.fxml"));
            Parent root = loader.load();
            SearchManageController smController = loader.getController();
            smController.InitSearch();
            rootPane.getScene().setRoot(root);
        } else {
            root = null;
            root = FXMLLoader.load(getClass().getResource("../ui/SearchManageScene.fxml"));
            rootPane.getScene().setRoot(root);
        }
    }

    @FXML
    void BrowseButtonClicked(ActionEvent event) throws IOException{
        FileChooser f = new FileChooser();
        
        File file = f.showOpenDialog(browseButton.getScene().getWindow());
            if (file != null) imageAddress.setText(file.getAbsolutePath());
    }

    @FXML
    private void FinalizeEditButtonClicked() throws Exception{
        edited = true;

        // Entry update details
        {
            // check choiceboxes
            Integer yearCheck;
            String formatCheck;
            String languageCheck;
            if (editYear.getValue() == null) yearCheck = null;
            else yearCheck = editYear.getValue();

            if (editFormat.getValue() == null) formatCheck = "";
            else formatCheck = editFormat.getValue();

            if (editLanguage.getValue() == null) languageCheck = "";
            else languageCheck = editLanguage.getValue();

            ei.setTitle(editTitle.getText());
            ei.setAuthor(editAuthor.getText());
            ei.setYear(yearCheck);
            ei.setWorkType(formatCheck);
            ei.setLanguage(languageCheck);

            if (!imageAddress.getText().isBlank()){
                File f = new File(imageAddress.getText());
                String filename = f.getName();
            
                // need to make sure image filename doesn't already exist, if so, append (x)
            
                Files.copy(Paths.get(imageAddress.getText()), Paths.get(imgLoc + filename), StandardCopyOption.REPLACE_EXISTING);
                ei.setImage(filename);
            }
        }

        DataEntry.ModifyEntry(ei);

        // reset view to new details
        {
            idView.setText(String.valueOf(ei.getId()));
            titleView.setText(ei.getTitle());
            title.setText(ei.getTitle());
            author.setText(ei.getAuthor());
            year.setText(String.valueOf(ei.getYear()));
            format.setText(ei.getWorkType());
            language.setText(ei.getLanguage());
        }

        // display new edit
        {
            cancelButton.setVisible(false);
            finalizeEditButton.setVisible(false);
            viewGrid.setVisible(true);
            editGrid.setVisible(false);
        }
    }

    @FXML
    void CancelButtonClicked(ActionEvent event) {
        // set form for cancel of edit event
        {
            cancelButton.setVisible(false);
            finalizeEditButton.setVisible(false);
            viewGrid.setVisible(true);
            editGrid.setVisible(false);
        }
    }

    @FXML
    void DeleteClicked(ActionEvent event) throws Exception{
        edited = true;
        DataEntry.Delete(ei);

        // set form for delete consequence
        {
            viewPane.setVisible(false);
            deletedLabel.setVisible(true);
            deleteButton.setVisible(false);
            editEntryButton.setVisible(false);
        }
    }

    @FXML
    void EditClicked(ActionEvent event) {
        // set form for edit consequence
        {
            finalizeEditButton.setVisible(true);
            cancelButton.setVisible(true);
            editGrid.setVisible(true);
            viewGrid.setVisible(false);

            editTitle.setText(title.getText());
            editAuthor.setText(author.getText());
            editYear.setValue(Integer.parseInt(year.getText()));
            editFormat.setValue(format.getText());
            editLanguage.setValue(language.getText());
        }
    }

    public void CreateFormView(Parent root, EntryInfo ei) throws IOException{
        edited = false;
        this.root = root;
        this.ei = ei;
        
        // setup form with entry information
        {
            idView.setText(String.valueOf(ei.getId()));
            titleView.setText(ei.getTitle());
            title.setText(ei.getTitle());
            author.setText(ei.getAuthor());
            year.setText(String.valueOf(ei.getYear()));
            format.setText(ei.getWorkType());
            language.setText(ei.getLanguage());

            if (ei.getImage() != null){
                File f = new File(imgLoc + ei.getImage());

                Image image = new Image(f.toURI().toString());
                imageView.setImage(image);
            }
        }
    }
}
