package mx.com.brandonicr.chat.control.events;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mx.com.brandonicr.chat.common.dto.PrivateSessionInfo;

public class PrivateMessageEvent implements EventHandler<ActionEvent>{

    Logger log = Logger.getLogger(PrivateMessageEvent.class.getName());

    PrivateSessionInfo pSessionInfo;

    public PrivateMessageEvent(PrivateSessionInfo pSessionInfo){
        this.pSessionInfo = pSessionInfo;
    }

    @Override
    public void handle(ActionEvent event) {
        try {
            System.out.println("~~Creando ventana de mensaje privado");
            AnchorPane root = FXMLLoader.load(getClass().getResource("../../FXMLMensajePrivado.fxml"));
            Scene privateMessageScene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(privateMessageScene);
            stage.setTitle("Private message to: "+pSessionInfo.getReceiver().getUserName());
            stage.setUserData(pSessionInfo);
            Image icono = new Image("mx/com/brandonicr/chat/static/images/chatIcon.png");
            stage.getIcons().add(icono);
            stage.show();
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Error while executing the button\n", ex);
        }   
    }
    
}
