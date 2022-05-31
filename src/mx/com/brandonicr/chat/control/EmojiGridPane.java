package mx.com.brandonicr.chat.control;

import java.io.File;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.GridPane;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import mx.com.brandonicr.chat.common.constants.FilePaths;
import mx.com.brandonicr.chat.common.constants.SpecialCharacterConstants;
import mx.com.brandonicr.chat.common.dto.EmojiInfo;
import mx.com.brandonicr.chat.control.events.EmojiEvent;

public class EmojiGridPane {

    Logger log = Logger.getLogger(EmojiGridPane.class.getName());

    private WebEngine webEngine;
    private TextField textField;
    private GridPane gridPane;

    public EmojiGridPane(WebEngine webEngine, TextField textField){
        this.webEngine = webEngine;
        this.textField = textField;
        gridPane = new GridPane();
    }

    public void show(){
        loadEmojis();
        Scene scene = new Scene(gridPane);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    public void loadEmojis(){
        File iconDir = new File("src/mx/com/brandonicr/chat/static/images/icons");
        if(!iconDir.isDirectory()){
            log.info("el directorio icons no fue encontrado");
            Platform.exit();
        }
        File []icons = iconDir.listFiles();
        for(int i = 0; i < icons.length; i++){
            Button button = new Button();
            log.info(icons[i].getName());
            button.setBackground(new Background(new BackgroundImage(
                new Image(FilePaths.iconPath+SpecialCharacterConstants.STR_SLASH+icons[i].getName()), 
                BackgroundRepeat.NO_REPEAT, 
                BackgroundRepeat.NO_REPEAT, 
                BackgroundPosition.CENTER, 
                BackgroundSize.DEFAULT)));
            button.setOnAction(new EmojiEvent(new EmojiInfo(webEngine, textField, icons[i].getName(), FilePaths.iconPath)));
            button.setPrefSize(64, 64);
            gridPane.add(button, i%9, i/9);
        }
    }
    
}
