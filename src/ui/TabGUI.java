package ui;

import database.*;

import java.util.Vector;
import java.util.Calendar;
import java.util.ArrayList;
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
    final int           stageH = 600,
                        stageW = 1000;
    
    public static void main(String[] args) throws Exception{
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        window = primaryStage;
        window.getIcons().add(new Image(TabGUI.class.getResourceAsStream("gon.png")));
        window.setTitle("Anime Tab!");
        window.setResizable(false);

        window.setScene(HomeScene());

        window.show();
    }

    private VBox ReturnHome(){
        VBox aboveChoices = new VBox();
        aboveChoices.setStyle("-fx-background-color: yellow");

        Button back = new Button("<- Back");
        back.setOnAction(e -> window.setScene(HomeScene()));

        aboveChoices.setAlignment(Pos.CENTER);
        aboveChoices.prefWidthProperty().bind(window.widthProperty().multiply(.2));
        aboveChoices.getChildren().add(back);

        return aboveChoices;
    }

    private VBox MenuActions(){
        VBox choices = new VBox(10);

        Button add = new Button("Add new entry");
        add.setOnAction(e -> window.setScene(AddEntry()));

        Button search = new Button("Search and Manage");
        search.setOnAction(e -> window.setScene(Search()));

        choices.setAlignment(Pos.CENTER_LEFT);
        choices.prefWidthProperty().bind(window.widthProperty().multiply(.2));
        choices.getChildren().addAll(add, search);

        return choices;
    }

    private Scene HomeScene(){
        BorderPane main = new BorderPane();

        VBox settingsPane = new VBox(10);
        settingsPane.setAlignment(Pos.CENTER_RIGHT);

        Button settings = new Button("Settings");
        settings.setOnAction(e -> window.setScene(Settings()));
        settingsPane.getChildren().add(settings);

        main.setLeft(MenuActions());
        main.setBottom(settingsPane);

        Scene home = new Scene(main, stageW, stageH);
        home.getStylesheets().add(TabGUI.class.getResource("HomeStyle.css").toExternalForm());

        return home;
    }

    private Scene AddEntry(){
        // Center pane - Input
        GridPane inputFields = new GridPane();
        inputFields.prefWidthProperty().bind(window.widthProperty().multiply(.4));
        inputFields.setAlignment(Pos.CENTER_RIGHT);
        inputFields.setVgap(3);
        inputFields.setHgap(10);

        Label workTitle = new Label("Title");
        GridPane.setConstraints(workTitle, 0, 0);
        TextField titleInput = new TextField();
        GridPane.setConstraints(titleInput, 1, 0);

        Label workTitleAlias = new Label("Title alias");
        GridPane.setConstraints(workTitleAlias, 0, 1);
        TextField workTitleAliasInput = new TextField();
        GridPane.setConstraints(workTitleAliasInput, 1, 1);

        Label author = new Label("Author");
        GridPane.setConstraints(author, 0, 4);
        TextField authorInput = new TextField();
        GridPane.setConstraints(authorInput, 1, 4);

        Label authorAlias = new Label("Author alias");
        GridPane.setConstraints(authorAlias, 0, 5);
        TextField authorAliasInput = new TextField();
        GridPane.setConstraints(authorAliasInput, 1, 5);

        Label year = new Label("Year");
        GridPane.setConstraints(year, 0, 8);
        Vector<Integer> years = new Vector<Integer>();
        Calendar c = Calendar.getInstance();
        for (int i = c.get(Calendar.YEAR); i >= 1900; i--){
            years.add(i);
        }
        ComboBox<Integer> yearInput = new ComboBox<Integer>(FXCollections.observableArrayList(years));
        GridPane.setConstraints(yearInput, 1, 8);

        Label workType = new Label("Work Type");
        GridPane.setConstraints(workType, 0, 9);
        String workTypes[] = {"Anime", "Manga", "Manhwa", "Comic", "Other"};
        ComboBox<String> workTypeInput = new ComboBox<String>(FXCollections.observableArrayList(workTypes));
        GridPane.setConstraints(workTypeInput, 1, 9);

        Label language = new Label("Language");
        GridPane.setConstraints(language, 0, 10);
        String languages[] = {"Japanese", "English", "French", "Spanish", "German", "Other"};
        ComboBox<String> languageInput = new ComboBox<String>(FXCollections.observableArrayList(languages));
        GridPane.setConstraints(languageInput, 1, 10);

        Label image = new Label("Image");
        GridPane.setConstraints(image, 0, 11);
        TextField imageURL = new TextField();
        GridPane.setConstraints(imageURL, 1, 11);
        Button imageBrowse = new Button("Browse");
        GridPane.setConstraints(imageBrowse, 2, 11);

        FileChooser f = new FileChooser();
        ImageView img = new ImageView();
        imageBrowse.setOnAction((event) -> {
            File file = f.showOpenDialog(window);
            if (file != null){
                Image imageTemp = new Image(file.toURI().toString());
                img.setImage(imageTemp);
                img.setFitWidth(300);
                img.setPreserveRatio(true);
                imageURL.setText(file.toURI().toString());
                System.out.println(imageURL.getText());
            }
        });

        // Right pane - Preview output
        VBox preview = new VBox();
        TextArea previewArea = new TextArea();
        Label previewTitle = new Label("Preview");
        previewArea.setDisable(true);
        previewArea.setMaxHeight(150);
        previewArea.setMaxWidth(300);
        preview.setAlignment(Pos.CENTER);
        previewArea.setWrapText(true);
        preview.getChildren().addAll(previewTitle, previewArea);
        preview.prefWidthProperty().bind(window.widthProperty().multiply(.4));

        // Preview data
        Button previewButton = new Button("Preview");
        GridPane.setConstraints(previewButton, 1, 25);
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
        GridPane.setConstraints(submit, 1, 27);
        submit.setOnAction((event) -> {

            if (titleInput.getText().isBlank() && workTitleAlias.getText().isBlank()){
                return;
            }

            if (authorInput.getText().isBlank() && authorAliasInput.getText().isBlank()){
                return;
            }

            EntryInfo entry = new EntryInfo(titleInput.getText(), workTitleAliasInput.getText(), authorInput.getText(), authorAliasInput.getText(), yearInput.getSelectionModel().getSelectedItem(), workTypeInput.getSelectionModel().getSelectedItem(), languageInput.getSelectionModel().getSelectedItem(), img);
            DataEntry.Add(entry);
        });

        inputFields.getChildren().addAll(workTitle, titleInput, workTitleAlias, workTitleAliasInput, author, authorInput, authorAlias, authorAliasInput, year, yearInput, workType, workTypeInput, language, languageInput, image, imageURL, imageBrowse, previewButton, submit);

        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(ReturnHome());
        borderPane.setCenter(inputFields);
        borderPane.setRight(preview);

        Scene sceneAdd = new Scene(borderPane, stageW, stageH);
        sceneAdd.getStylesheets().add(TabGUI.class.getResource("AddStyle.css").toExternalForm());

        return sceneAdd;
    }

    private Scene Search(){
        GridPane searchPane = new GridPane();
        searchPane.setAlignment(Pos.CENTER);
        searchPane.prefWidthProperty().bind(window.widthProperty().multiply(.4));
    
        Label result = new Label();

        Label searchLabel = new Label("Search:");
        GridPane.setConstraints(searchLabel, 0, 0);

        TextField searchBar = new TextField();
        GridPane.setConstraints(searchBar, 0, 1);

        Button searchNow = new Button("Search!");
        GridPane.setConstraints(searchNow, 0, 2);
        searchPane.getChildren().addAll(searchLabel, searchBar, searchNow);

        VBox resultPane = new VBox(5);
        resultPane.setAlignment(Pos.CENTER);
        resultPane.prefWidthProperty().bind(window.widthProperty().multiply(.4));

        ToggleGroup parameters = new ToggleGroup();
        RadioButton author = new RadioButton("Author");
        RadioButton title = new RadioButton("Title");
        RadioButton year = new RadioButton("Year Published");
        RadioButton language = new RadioButton("Language");
        RadioButton workType = new RadioButton("Format");
        author.setToggleGroup(parameters);
        title.setToggleGroup(parameters);
        year.setToggleGroup(parameters);
        language.setToggleGroup(parameters);
        workType.setToggleGroup(parameters);

        resultPane.getChildren().addAll(result);
        
        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(ReturnHome());
        borderPane.setCenter(searchPane);
        borderPane.setRight(resultPane);

        Scene sceneSearch = new Scene(borderPane, stageW, stageH);

        // Query database for search parameters
        searchNow.setOnAction(e -> {
            String query = searchBar.getText();
            if (query.isBlank())
                return;
            else{
                resultPane.getChildren().clear();
                ArrayList<EntryInfo> queryResults = DataEntry.View(query);
                if (queryResults.isEmpty()){
                } else {
                    int resultCount = 0;
                    for (EntryInfo ei : queryResults){
                        resultCount++;
                        Hyperlink resultLink = new Hyperlink(ei.getTitle() + "\n");
                        resultLink.setOnAction(event -> {
                            window.setScene(Results(ei, sceneSearch));
                        });
                        result.setText("Result(s): " + resultCount);
                        resultPane.getChildren().add(resultLink);
                    }
                }
            }
        });

        return sceneSearch;
    }

    private Scene Settings(){
        BorderPane borderpane = new BorderPane();
        borderpane.setLeft(ReturnHome());
        Scene settings = new Scene(borderpane, stageW, stageH);
        return settings;
    }

    private Scene Results(EntryInfo ei, Scene scene){
        VBox back = new VBox(10);
        VBox Right = new VBox(10);
        Right.setAlignment(Pos.CENTER);
        Right.prefWidthProperty().bind(window.widthProperty().multiply(.4));
        VBox Center = new VBox(20);
        Center.setAlignment(Pos.CENTER);
        Center.prefWidthProperty().bind(window.widthProperty().multiply(.4));
        BorderPane borderpane = new BorderPane();
        back.setStyle("-fx-background-color: yellow");
        
        Label title = new Label(ei.getTitle());
        Right.getChildren().addAll(ei.getImage(), title);

        Label id = new Label(String.valueOf(ei.getId()));
        Label titleAlias = new Label(ei.getTitleAlias());
        Label author = new Label(ei.getAuthor());
        Label authorAlias = new Label(ei.getAuthorAlias());
        Label year = new Label(String.valueOf(ei.getYear()));
        Label workType = new Label(ei.getWorkType());
        Label language = new Label(ei.getLanguage());

        Center.getChildren().addAll(id, titleAlias, author, authorAlias, year, workType, language);

        Button backButton = new Button("<- Back");
        backButton.setOnAction(e -> window.setScene(scene));

        Button editEntry = new Button("Edit entry");
        editEntry.setOnAction(e -> window.setScene(Edit(editEntry.getScene())));

        Button deleteEntry = new Button("Delete entry");

        back.setAlignment(Pos.CENTER);
        back.prefWidthProperty().bind(window.widthProperty().multiply(.2));
        back.getChildren().addAll(backButton, editEntry, deleteEntry);

        borderpane.setLeft(back);
        borderpane.setRight(Right);
        borderpane.setCenter(Center);

        deleteEntry.setOnAction(e -> {
            borderpane.getChildren().removeAll(Center, back, Right);
            VBox successfulDelete = new VBox();
            successfulDelete.setAlignment(Pos.CENTER);
            Label label = new Label("Entry: " +ei.getTitle()+ " deleted.");
            successfulDelete.getChildren().add(label);
            borderpane.setLeft(backButton(Search()));
            borderpane.setCenter(successfulDelete);
            DataEntry.Delete(ei);
        });

        Scene results = new Scene(borderpane, stageW, stageH);
        return results;
    }

    private VBox backButton(Scene scene){
        VBox backPane = new VBox();
        backPane.prefWidthProperty().bind(window.widthProperty().multiply(.2));
        backPane.setAlignment(Pos.CENTER);
        backPane.setStyle("-fx-background-color: yellow");
        Button back = new Button("<- back");
        backPane.getChildren().add(back);
        back.setOnAction(e -> window.setScene(scene));
        return backPane;
    }

    private Scene Edit(Scene scene){
        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(backButton(scene));
        Scene edit = new Scene(borderPane, stageW, stageH);
        return edit;
    }

}
