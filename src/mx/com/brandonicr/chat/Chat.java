package mx.com.brandonicr.chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import mx.com.brandonicr.chat.common.constants.FilePaths;
import mx.com.brandonicr.chat.common.constants.SpecialCharacterConstants;

/**
 * @author BrandonICR
 */
public class Chat extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        Image icono = new Image(FilePaths.imagesPath.concat(SpecialCharacterConstants.STR_SLASH).concat(FilePaths.imageChatIconPath));
        stage.getIcons().add(icono);
        stage.setResizable(false);
        stage.setTitle("Chat");
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
