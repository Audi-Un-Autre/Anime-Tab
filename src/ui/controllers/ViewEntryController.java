package ui.controllers;

import database.*;

import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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

public class ViewEntryController {

    private EntryInfo   ei;
    private Parent      root;
    private boolean     edited;
    private Image       imageTemp;
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

    @FXML
    void BackButtonClicked(ActionEvent event) throws Exception{
        if (!edited){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/SearchManageScene.fxml"));
            Parent root = loader.load();
            SearchManageController smController = loader.getController();
            smController.InitSearch();
            rootPane.getScene().setRoot(root);
        } else {
            root = null;
            root = FXMLLoader.load(getClass().getResource("../scenes/SearchManageScene.fxml"));
            rootPane.getScene().setRoot(root);
        }
    }

    @FXML
    void BrowseButtonClicked(ActionEvent event) throws IOException{
        FileChooser f = new FileChooser();
        
        File file = f.showOpenDialog(browseButton.getScene().getWindow());
            if (file != null){
                imageTemp = new Image(file.toURI().toString());
                imageAddress.setText(file.getAbsolutePath());
            }
    }

    @FXML
    private void FinalizeEditButtonClicked() throws Exception{
        edited = true;

        // Entry update details
        {
            ei.setTitle(editTitle.getText());
            ei.setAuthor(editAuthor.getText());
            ei.setYear(editYear.getValue());
            ei.setWorkType(editFormat.getValue());
            ei.setLanguage(editLanguage.getValue());

            File f = new File(imageAddress.getText());
            String filename = f.getName();
        
            // need to make sure image filename doesn't already exist, if so, append (x)
        
            Files.copy(Paths.get(imageAddress.getText()), Paths.get(imgLoc + filename), StandardCopyOption.REPLACE_EXISTING);
            ei.setImage(filename);
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

            File f = new File(imgLoc + ei.getImage());

            Image image = new Image(f.toURI().toString());
            imageView.setImage(image);
            System.out.println(f.toURI().toString());
        }
    }
}
