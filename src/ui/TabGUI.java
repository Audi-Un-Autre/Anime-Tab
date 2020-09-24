package ui;

import database.*;

import java.util.Vector;
import java.util.Calendar;
import java.io.*;

import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.stage.Stage;
import javafx.stage.FileChooser;

import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.collections.*;

public class TabGUI extends Application{
    Stage               window;
    TextArea            previewArea;
    VBox                preview;

    TextField           titleInput;
    TextField           workTitleAliasInput;
    TextField           authorInput;
    TextField           authorAliasInput;
    ComboBox<Integer>   yearInput;
    ComboBox<String>    workTypeInput;
    ComboBox<String>    languageInput;

    ImageView           img;    
    
    public static void main(String[] args) throws Exception{
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        window.getIcons().add(new Image(TabGUI.class.getResourceAsStream("gon.png")));
        window.setTitle("Anime Tab!");

        // Left pane - Actions
        VBox choices = new VBox(20);
        Button add = new Button("Add New Entry");
        Button edit = new Button("Edit Entry");
        Button search = new Button("Search");
        choices.setAlignment(Pos.CENTER);
        choices.setPadding(new Insets(0, 0, 0, 50));
        choices.getChildren().addAll(add, edit, search);

    // Center pane - Input
        GridPane inputFields = new GridPane();
        inputFields.setPadding(new Insets(0, 0, 0, 150));
        inputFields.setAlignment(Pos.CENTER);
        inputFields.setVgap(3);
        inputFields.setHgap(10);

        Label workTitle = new Label("Title");
        GridPane.setConstraints(workTitle, 0, 0);
        titleInput = new TextField();
        GridPane.setConstraints(titleInput, 1, 0);

        Label workTitleAlias = new Label("Title alias");
        GridPane.setConstraints(workTitleAlias, 0, 1);
        workTitleAliasInput = new TextField();
        GridPane.setConstraints(workTitleAliasInput, 1, 1);

        Label author = new Label("Author");
        GridPane.setConstraints(author, 0, 4);
        authorInput = new TextField();
        GridPane.setConstraints(authorInput, 1, 4);

        Label authorAlias = new Label("Author alias");
        GridPane.setConstraints(authorAlias, 0, 5);
        authorAliasInput = new TextField();
        GridPane.setConstraints(authorAliasInput, 1, 5);

        Label year = new Label("Year");
        GridPane.setConstraints(year, 0, 8);
        Vector<Integer> years = new Vector<Integer>();
        Calendar c = Calendar.getInstance();
        for (int i = c.get(Calendar.YEAR); i >= 1900; i--){
            years.add(i);
        }
        yearInput = new ComboBox<Integer>(FXCollections.observableArrayList(years));
        GridPane.setConstraints(yearInput, 1, 8);

        Label workType = new Label("Work Type");
        GridPane.setConstraints(workType, 0, 9);
        String workTypes[] = {"Anime", "Manga", "Manhwa", "Comic", "Other"};
        workTypeInput = new ComboBox<String>(FXCollections.observableArrayList(workTypes));
        GridPane.setConstraints(workTypeInput, 1, 9);

        Label language = new Label("Language");
        GridPane.setConstraints(language, 0, 10);
        String languages[] = {"Japanese", "English", "French", "Spanish", "German", "Other"};
        languageInput = new ComboBox<String>(FXCollections.observableArrayList(languages));
        GridPane.setConstraints(languageInput, 1, 10);

        Label image = new Label("Image");
        GridPane.setConstraints(image, 0, 11);
        TextField imageURL = new TextField();
        GridPane.setConstraints(imageURL, 1, 11);
        imageURL.setDisable(true);
        Button imageBrowse = new Button("Browse");
        GridPane.setConstraints(imageBrowse, 2, 11);

        FileChooser f = new FileChooser();
        img = new ImageView();
        imageBrowse.setOnAction((event) -> {
            File file = f.showOpenDialog(window);
            if (file != null){
                Image imageTemp = new Image(file.toURI().toString());
                img.setImage(imageTemp);
                img.setFitWidth(300);
                img.setPreserveRatio(true);
                imageURL.setText(file.toURI().toString());
            }
        });

        // Preview data
        Button previewButton = new Button("Preview");
        GridPane.setRowIndex(previewButton, 25);
        previewButton.setOnAction((event) -> {
            previewArea.setText("");
            if (preview.getChildren().contains(img)){
                preview.getChildren().remove(img);
            }
            previewArea.appendText("Title: " + titleInput.getText());
            previewArea.appendText("\nTitle alias: " + workTitleAliasInput.getText());
            previewArea.appendText("\nAuthor: " + authorInput.getText());
            previewArea.appendText("\nAuthor alias: " + authorAliasInput.getText());
            previewArea.appendText("\nYear: " + yearInput.getSelectionModel().getSelectedItem());
            previewArea.appendText("\nWork Type: " + workTypeInput.getSelectionModel().getSelectedItem());
            previewArea.appendText("\nLangauge: " + languageInput.getSelectionModel().getSelectedItem());
            if (!imageURL.getText().isEmpty()){
                preview.getChildren().add(img);
            }
        });

        // Submit to database
        Button submit = new Button("Add Entry!");
        GridPane.setRowIndex(submit, 27);
        submit.setOnAction((event) -> {
            NewEntry entry = new NewEntry(titleInput.getText(), workTitleAliasInput.getText(), authorInput.getText(), authorAliasInput.getText(), yearInput.getSelectionModel().getSelectedItem(), workTypeInput.getSelectionModel().getSelectedItem(), languageInput.getSelectionModel().getSelectedItem(), img);
            DataEntry enterData = new DataEntry(entry, DataEntry.Commands.ADD);
        });

        inputFields.getChildren().addAll(workTitle, titleInput, workTitleAlias, workTitleAliasInput, author, authorInput, authorAlias, authorAliasInput, year, yearInput, workType, workTypeInput, language, languageInput, image, imageURL, imageBrowse, previewButton, submit);

        // Right pane - Preview output
        preview = new VBox();
        previewArea = new TextArea();
        Label previewTitle = new Label("Preview");
        previewArea.setPrefHeight(200);
        previewArea.setPrefWidth(300);
        previewArea.setDisable(true);
        preview.setPadding(new Insets(0, 50, 0, 0));
        preview.setAlignment(Pos.CENTER);
        previewArea.setWrapText(true);
        preview.getChildren().addAll(previewTitle, previewArea);

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(choices);
        borderPane.setCenter(inputFields);
        borderPane.setRight(preview);

        Scene scene = new Scene(borderPane, 1000, 600);
        //scene.getStylesheets().add(TabGUI.class.getResource("TabStyle.css").toExternalForm());
        window.setScene(scene);
        window.show();
    }
}
