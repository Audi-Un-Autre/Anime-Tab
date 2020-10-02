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
    void SearchButtonClicked(ActionEvent event) throws Exception{
        String query = search.getText();
        if (query.isBlank())
            return;
        else{
            resultList.getItems().clear();
            ArrayList<EntryInfo> queryResults = DataEntry.View(query);
            if (queryResults.isEmpty()){
            } else {

                // create list of entry hyperlinks and display
                for (EntryInfo ei : queryResults){
                    Hyperlink resultLink = new Hyperlink(ei.getTitle() + "\n");
                    resultLink.setOnAction(e -> {

                        // onclick
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

                    // onhover
                    resultLink.setOnMouseEntered(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent me){
                            infoGrid.setVisible(true);
                            id.setText(String.valueOf(ei.getId()));
                            title.setText(ei.getTitle());
                            titleAlias.setText(ei.getTitleAlias());
                            author.setText(ei.getAuthor());
                            authorAlias.setText(ei.getAuthorAlias());
                            year.setText(String.valueOf(ei.getYear()));
                            workType.setText(ei.getWorkType());
                            language.setText(ei.getLanguage());

                            File f = new File(System.getProperty("user.dir") + "/src/ui/design/related/covers/" + ei.getImage());

                            Image image = new Image(f.toURI().toString());
                            imageView.setImage(image);
                        }
                    });
                    resultList.getItems().add(resultLink);
                }
            }
        }
    }

    private void ChangeUI(String name) throws IOException{
        Parent root = null;
        root = FXMLLoader.load(getClass().getResource("../scenes/"+name+".fxml"));
        rootPane.getScene().setRoot(root);
    }

}
