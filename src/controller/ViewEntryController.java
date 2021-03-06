package controller;

import database.*;
import ui.WindowStyle;

import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Vector;
import java.util.Calendar;

import org.apache.commons.io.FilenameUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.collections.FXCollections;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Scene;
import javafx.stage.Modality;

public class ViewEntryController {

    private EntryInfo   ei;
    private Parent      root;
    private boolean     edited;
    private final static String imgLoc = System.getProperty("user.dir") + "/src/ui/design/related/covers/";

    @FXML
    private AnchorPane rootPane;

    @FXML
    private GridPane editGrid, viewGrid;

    @FXML
    private Button editEntryButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button backButton;

    @FXML
    private ImageView imageView;

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
    private Label deleteLabel;

    public void initialize(){
        //populate choiceboxes
        Vector<Integer> year = new Vector<Integer>();
        Calendar c = Calendar.getInstance();
        for (int i = c.get(Calendar.YEAR); i >= 1900; i--){
            if (i == c.get(Calendar.YEAR)) year.add(null);
            year.add(i);
        }
        editYear.setItems(FXCollections.observableArrayList(year));

        String workTypes[] = {"Anime", "Manga", "Manhwa", "Comic", "Other"};
        editFormat.setItems(FXCollections.observableArrayList(workTypes));

        String language[] = {"", "Japanese", "English", "French", "Spanish", "German", "Other"};
        editLanguage.setItems(FXCollections.observableArrayList(language));
    }

    @FXML
    void BackButtonClicked(ActionEvent event) throws Exception{
        if (!edited){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../ui/SearchManageScene.fxml"));
            root = loader.load();
            SearchManageController smController = loader.getController();
            smController.InitSearch();
        } else {
            root = null;
            root = FXMLLoader.load(getClass().getResource("../ui/SearchManageScene.fxml"));
        }

        WindowStyle ws = new WindowStyle();
        ws.setStyle(root, (Stage) rootPane.getScene().getWindow());
        rootPane.getScene().setRoot(root);
    }

    @FXML
    void BrowseButtonClicked(ActionEvent event) throws IOException{
        FileChooser f = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.jpeg");
        f.getExtensionFilters().add(filter);
        
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

                if (new File(imgLoc + filename).exists()){
                    String fileTemp = filename;
                    for (int i = 1; new File(imgLoc + fileTemp).exists(); i++){
                        fileTemp = FilenameUtils.getBaseName(filename) + "("+i+")." + FilenameUtils.getExtension(filename);
                    }
                    filename = fileTemp;
                }
                        
                Files.copy(Paths.get(imageAddress.getText()), Paths.get(imgLoc + filename), StandardCopyOption.REPLACE_EXISTING);
                ei.setImage(filename);
            }
        }

        DataEntry.ModifyEntry(ei);

        // reset view to new details
        {
            idView.setText(String.valueOf(ei.getId()));
            titleView.setText(ei.getTitle());
            author.setText(ei.getAuthor());
            year.setText(String.valueOf(ei.getYear()));
            format.setText(ei.getWorkType());
            language.setText(ei.getLanguage());
        }

        // display new edit
        {
            cancelButton.setVisible(false);
            finalizeEditButton.setVisible(false);
            editEntryButton.setVisible(true);
            deleteButton.setVisible(true);
            viewGrid.setVisible(true);
            editGrid.setVisible(false);
        }
    }

    @FXML
    void CancelButtonClicked(ActionEvent event) {
        // set form for cancel of edit event
        {
            cancelButton.setVisible(false);
            editEntryButton.setVisible(true);
            deleteButton.setVisible(true);
            finalizeEditButton.setVisible(false);
            viewGrid.setVisible(true);
            editGrid.setVisible(false);
        }
    }

    @FXML
    void DeleteClicked(ActionEvent event) throws Exception{

        //ready form for delete entry
        {
            editEntryButton.setVisible(false);
            deleteButton.setVisible(false);
        }

        // Delete Confirm window
        Parent root = null;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../ui/DeleteScene.fxml"));
        root = loader.load();
        Scene scene = new Scene(root);

        DeleteController dc = loader.getController();
        dc.init(ei, this);

        Stage stage = new Stage(StageStyle.UTILITY);
        stage.setTitle("Confirm Delete");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("../ui/design/gon.png")));
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    void EditClicked(ActionEvent event) {
        // set form for edit consequence
        {
            finalizeEditButton.setVisible(true);
            cancelButton.setVisible(true);
            editEntryButton.setVisible(false);
            deleteButton.setVisible(false);
            editGrid.setVisible(true);
            viewGrid.setVisible(false);

            editTitle.setText(titleView.getText());
            editAuthor.setText(author.getText());
            editYear.setValue(Integer.parseInt(year.getText()));
            editFormat.setValue(format.getText());
            editLanguage.setValue(language.getText());
        }
    }

    public void CancelDelete(){
        deleteButton.setVisible(true);
        editEntryButton.setVisible(true);
    }

    public void EntryDeleted(){
        edited = true;
        idView.setText("Deleted.");
        deleteLabel.setVisible(true);
        rootPane.getChildren().removeAll(viewGrid, editGrid, deleteButton, editEntryButton);
    }

    public void CreateFormView(Parent root, EntryInfo ei) throws IOException{
        edited = false;
        this.root = root;
        this.ei = ei;
        
        // setup form with entry information
        {
            idView.setText(String.valueOf(ei.getId()));
            titleView.setText(ei.getTitle());
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
