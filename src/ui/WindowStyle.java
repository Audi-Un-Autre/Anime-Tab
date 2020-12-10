package ui;

import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.shape.Rectangle;

public class WindowStyle {
    private double offsetX = 0;
    private double offsetY = 0;

    public void setStyle(Parent root, Stage stage){
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
                stage.setX(event.getScreenX() - offsetX);
                stage.setY(event.getScreenY() - offsetY);
            }
        });

        // round corners of the stage
        Rectangle rect = new Rectangle(450, 750);
        rect.setArcHeight(20.0);
        rect.setArcWidth(20.0);
        root.setClip(rect);
    }
}
