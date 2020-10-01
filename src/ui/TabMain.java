package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class TabMain extends Application{
    
    public static void main(String[] args) throws Exception{
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("scenes/MainScene.fxml"));
        primaryStage.getIcons().add(new Image(TabMain.class.getResourceAsStream("design/gon.png")));
        primaryStage.setTitle("Anime Tab!");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.show();
    }

/*
    private Scene AddEntry(){
    
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
    }

    private Scene Search(){
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

    private Scene Results(EntryInfo ei, Scene scene){
        Label id = new Label(String.valueOf(ei.getId()));
        Label titleAlias = new Label(ei.getTitleAlias());
        Label author = new Label(ei.getAuthor());
        Label authorAlias = new Label(ei.getAuthorAlias());
        Label year = new Label(String.valueOf(ei.getYear()));
        Label workType = new Label(ei.getWorkType());
        Label language = new Label(ei.getLanguage());

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
*/
}
