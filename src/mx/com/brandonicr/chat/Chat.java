package mx.com.brandonicr.chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * @author BrandonICR
 */
public class Chat extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        Image icono = new Image("mx/com/brandonicr/chat/static/images/chatIcon.png");
        stage.getIcons().add(icono);
        stage.setResizable(false);
        stage.setTitle("Chat");
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
