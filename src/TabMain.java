import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class TabMain extends Application{
    
    private double offsetX = 0;
    private double offsetY = 0;

    public static void main(String[] args) throws Exception{
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("ui/MainScene.fxml"));        
        primaryStage.getIcons().add(new Image(TabMain.class.getResourceAsStream("ui/design/gon.png")));
        primaryStage.setTitle("Anime Tab!");
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.TRANSPARENT);

        root.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                offsetX = event.getSceneX();
                offsetY = event.getSceneY();
            }
        });
        root.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() - offsetX);
                primaryStage.setY(event.getScreenY() - offsetY);
            }
        });

        // round corners of the stage
        Rectangle rect = new Rectangle(450, 750);
        rect.setArcHeight(20.0);
        rect.setArcWidth(20.0);
        root.setClip(rect);

        Scene scene = new Scene(root, 450, 750);
        scene.setFill(Color.TRANSPARENT);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
