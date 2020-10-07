package ui.controllers;

import database.*;

import java.io.IOException;
import java.util.ArrayList;
import java.io.File;

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

public class SearchManageController {
    private boolean init = true;

    private final static String imgLoc = System.getProperty("user.dir") + "/src/ui/design/related/covers/";

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
        root = FXMLLoader.load(getClass().getResource("../scenes/MainScene.fxml"));
        rootPane.getScene().setRoot(root);
    }

    @FXML
    void SearchButtonClicked(ActionEvent event) throws Exception{
        String query = search.getText();
        if (query.isBlank())
            DisplayLinks(DataEntry.ViewInit());
        else{
            ArrayList<EntryInfo> queryResults = DataEntry.View(query);
            if (!queryResults.isEmpty()) DisplayLinks(queryResults);
        }
    }

    private void DisplayLinks(ArrayList<EntryInfo> queryResults){
        resultList.getItems().clear();

        // create list of entry hyperlinks and display
        for (EntryInfo ei : queryResults){
            Hyperlink resultLink = new Hyperlink(ei.getTitle() + "\n");
            resultList.getItems().add(resultLink);
            resultCount.setText(String.valueOf(queryResults.size()));

            resultLink.setOnAction(e -> {
                // onclick view entry
                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../scenes/ViewEntryScene.fxml"));
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

    public void InitSearch() throws Exception{
        // on scene load, add all existing entries in database to the list
        DisplayLinks(DataEntry.ViewInit());
        init = false;
    }

}
