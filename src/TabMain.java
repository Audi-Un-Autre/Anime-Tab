import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ui.WindowStyle;
import javafx.scene.paint.Color;

public class TabMain extends Application{
    
    

    public static void main(String[] args) throws Exception{
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ui/MainScene.fxml"));
        Parent root = loader.load();
        
        primaryStage.getIcons().add(new Image(TabMain.class.getResourceAsStream("ui/design/gon.png")));
        primaryStage.setTitle("Anime Tab!");
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        WindowStyle ws = new WindowStyle();
        ws.setStyle(root, primaryStage);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
