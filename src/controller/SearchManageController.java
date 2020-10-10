package controller;

import database.*;

import java.io.IOException;
import java.util.ArrayList;
import java.io.File;
import java.util.Calendar;
import java.util.Vector;

import javafx.scene.image.Image;
import javafx.event.EventHandler;
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
import javafx.scene.layout.GridPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.RadioButton;
import javafx.collections.FXCollections;

public class SearchManageController {
    private boolean init = true, radioSelected = false;

    private final static String imgLoc = System.getProperty("user.dir") + "/src/ui/design/related/covers/";

    private String column;

    private Object query;

    @FXML
    private BorderPane rootPane;

    @FXML
    private GridPane infoGrid;

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
    private ToggleGroup filterGroup;

    @FXML
    private RadioButton titleRadio;

    @FXML
    private RadioButton authorRadio;

    @FXML
    private RadioButton yearRadio;

    @FXML
    private RadioButton formatRadio;

    @FXML
    private RadioButton languageRadio;

    @FXML
    private ImageView imageView;

    @FXML
    private Label id;

    @FXML
    private Label title;

    @FXML
    private Label resultCount;

    @FXML
    void BackButtonClicked(ActionEvent event) throws IOException{
        Parent root = null;
        root = FXMLLoader.load(getClass().getResource("../ui/MainScene.fxml"));
        rootPane.getScene().setRoot(root);
    }

    @FXML
    void SearchButtonClicked(ActionEvent event) throws Exception{
        if (search.getText().isBlank() && !radioSelected)
            DisplayLinks(DataEntry.ViewInit());
        else{
            ArrayList<EntryInfo> queryResults;

            if (radioSelected && (column == "year" || column == "work_type" || column == "language" || column == "title" || column == "author")) {
                switch (column){
                    case "year": query = years.getValue();
                        break;
                    case "work_type": query = workTypes.getValue();
                        break;
                    case "language": query = languages.getValue();
                        break;
                    default: query = search.getText();
                }
                queryResults = DataEntry.View(query, column);
            }
            else queryResults = DataEntry.View(search.getText());

            if (!queryResults.isEmpty()) DisplayLinks(queryResults);
            else {
                resultList.getItems().clear();
                resultList.getItems().add(new Hyperlink("No Results."));
            }
        }
    }

    private void DisplayLinks(ArrayList<EntryInfo> queryResults){
        resultList.getItems().clear();

        // create list of entry hyperlinks and display
        for (EntryInfo ei : queryResults){
            String nameDisplay;

            if (ei.getTitle() == null) nameDisplay = ei.getTitleAlias();
            else nameDisplay = ei.getTitle();

            Hyperlink resultLink = new Hyperlink(nameDisplay + "\n");
            resultList.getItems().add(resultLink);
            resultCount.setText(String.valueOf(queryResults.size()));

            resultLink.setOnAction(e -> {
                // onclick view entry
                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../ui/ViewEntryScene.fxml"));
                    Parent root = loader.load();
                    ViewEntryController veController = loader.getController();
                    veController.CreateFormView(rootPane.getScene().getRoot(), ei);
                    rootPane.getScene().setRoot(root);
                } catch (Exception ex){
                    System.out.println(ex);
                }
            });

            // onhover show a preview of the entry
            resultLink.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent me){
                    infoGrid.setVisible(true);
                    id.setText(String.valueOf(ei.getId()));
                    title.setText(ei.getTitle());

                    File f = new File(imgLoc + ei.getImage());
                    Image image = new Image(f.toURI().toString());
                    imageView.setImage(image);
                }
            });
        }
    }

    private void ToggleControl(String selected){
        radioSelected = true;
        switch(selected){
            case "year":
                years.setVisible(true);
                workTypes.setVisible(false);
                languages.setVisible(false);
                search.setDisable(true);
                column = selected;
                break;

            case "language":
                languages.setVisible(true);
                workTypes.setVisible(false);
                years.setVisible(false);
                search.setDisable(true);
                column = selected;
                break;

            case "work_type":
                workTypes.setVisible(true);
                languages.setVisible(false);
                years.setVisible(false);
                search.setDisable(true);
                column = selected;
                break;

            case "author":
                years.setVisible(false);
                languages.setVisible(false);
                workTypes.setVisible(false);
                column = selected;
                break;
            
            case "title":
                years.setVisible(false);
                languages.setVisible(false);
                workTypes.setVisible(false);
                column = selected;
                break;
        }
    }

    @FXML
    void titleRadioSelected(ActionEvent event) {
        ToggleControl("title");
    }

    @FXML
    void authorRadioSelected(ActionEvent event) {
        ToggleControl("author");
    }

    @FXML
    void formatRadioSelected(ActionEvent event) {
        ToggleControl("work_type");
    }

    @FXML
    void languageRadioSelected(ActionEvent event) {
        ToggleControl("language");
    }

    @FXML
    void yearRadioSelected(ActionEvent event) {
        ToggleControl("year");
    }

    public void InitSearch() throws Exception{
        // on scene load, add all existing entries in database to the list
        DisplayLinks(DataEntry.ViewInit());
        init = false;

        //populate choiceboxes
        Vector<Integer> year = new Vector<Integer>();
        Calendar c = Calendar.getInstance();
        for (int i = c.get(Calendar.YEAR); i >= 1900; i--){
            year.add(i);
        }
        years.setItems(FXCollections.observableArrayList(year));

        String formats[] = {"", "Anime", "Manga", "Manhwa", "Comic", "Other"};
        workTypes.setItems(FXCollections.observableArrayList(formats));

        String language[] = {"", "Japanese", "English", "French", "Spanish", "German", "Other"};
        languages.setItems(FXCollections.observableArrayList(language));
    }

}
