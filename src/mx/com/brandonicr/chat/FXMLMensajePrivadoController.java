package mx.com.brandonicr.chat;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import mx.com.brandonicr.chat.common.constants.FilePaths;
import mx.com.brandonicr.chat.common.constants.SpecialCharacterConstants;
import mx.com.brandonicr.chat.common.dto.ConfigurationFileDowloader;
import mx.com.brandonicr.chat.common.dto.FileSessionInfo;
import mx.com.brandonicr.chat.common.dto.MessageBuilder;
import mx.com.brandonicr.chat.common.dto.PrivateSessionInfo;
import mx.com.brandonicr.chat.common.utils.ComponentBuilder;
import mx.com.brandonicr.chat.common.utils.ElementUtils;
import mx.com.brandonicr.chat.control.ChatFileDowloader;
import mx.com.brandonicr.chat.control.EmojiGridPane;
import mx.com.brandonicr.chat.control.MessageTransmitter;

import javax.swing.JFileChooser;
import javax.swing.text.html.HTML.Tag;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLImageElement;

/**
 * @author BrandonICR
 */
public class FXMLMensajePrivadoController implements Initializable {

    Logger log = Logger.getLogger(FXMLMensajePrivadoController.class.getName());

    @FXML
    private Button buttonEnviarPrivado;
    @FXML
    private Button buttonAdjuntarPrivado;
    @FXML
    private TextField textFieldTextoPrivado;
    @FXML
    private Label labelIpUsuario;
    @FXML
    private AnchorPane anchorPanePrivado;

    PrivateSessionInfo pSessionInfo;

    private WebEngine webEngine;
    private Stage stagePrivado;
    
    public void init() {
        if(webEngine != null)
            return;
        log.info("Initializing FXML component for private message");
        stagePrivado = ((Stage)anchorPanePrivado.getScene().getWindow());
        pSessionInfo = ((PrivateSessionInfo)stagePrivado.getUserData());
        webEngine = pSessionInfo.getWebEngine();
    }
    
    @FXML
    public void enviarPrivado(ActionEvent e){
        init();
        
        System.out.println(String.format("Enviando mensaje privado a: %s", pSessionInfo.getReceiver().getUserName()));

        if(!textFieldTextoPrivado.getText().trim().isEmpty()){
            String message = "Yo"+"::"+textFieldTextoPrivado.getText().trim();

            Node messageTextNode = ElementUtils.buildNodeMessage(message, webEngine.getDocument());
            Document document = webEngine.getDocument(); 
            Node body = document.getElementsByTagName(Tag.BODY.toString()).item(SpecialCharacterConstants.INT_ZERO);
            body.appendChild(messageTextNode);

            Thread messageTransmitter = new Thread(new MessageTransmitter(new MessageBuilder().sender(pSessionInfo.getSender()).receiver(pSessionInfo.getReceiver()).text(textFieldTextoPrivado.getText().trim()).controlInfo(false, true, false).build()));
            messageTransmitter.start();
            System.out.println("Se ha mandado un mensaje privado::"+textFieldTextoPrivado.getText().trim());
        }
        textFieldTextoPrivado.clear();
        stagePrivado.close();
    }
    
    @FXML
    public void adjuntarPrivado(ActionEvent e){  
        init();

        JFileChooser fc = new JFileChooser();
        int status = fc.showOpenDialog(null);
        if(status == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try{
                Document document = webEngine.getDocument(); 
                Node body = document.getElementsByTagName(Tag.BODY.toString()).item(SpecialCharacterConstants.INT_ZERO);
                HTMLImageElement elementImage = (HTMLImageElement)ComponentBuilder.imageElement(document);
                elementImage.setSrc(FilePaths.fileBasePath.concat(file.getAbsolutePath()));
                body.appendChild(elementImage);
            }catch(Exception ex){
                log.log(Level.SEVERE, ex.getMessage(), ex);
            }
            try {
                Thread messageTransmitter = new Thread(new MessageTransmitter(new MessageBuilder().sender(pSessionInfo.getSender()).receiver(pSessionInfo.getReceiver()).text(file.getName()).controlInfo(true, true, false).build()));
                messageTransmitter.start();
                messageTransmitter.join();
                Thread.sleep(2*1000l);
            } catch (InterruptedException ex) {
                log.log(Level.INFO, "Mensaje de aviso de archivo privado enviado");
            }
            FileSessionInfo fileSessionInfo = new FileSessionInfo(file, file.getName(), pSessionInfo.getSender(), pSessionInfo.getReceiver(), new ConfigurationFileDowloader(true, true), webEngine);
            new Thread(new ChatFileDowloader(fileSessionInfo)).start();
            System.out.println("The private file was sended: " + file.getName());
        }   
    }

    @FXML
    private void showEmojis(ActionEvent e) {
        init();
        EmojiGridPane emojiGridPane = new EmojiGridPane(webEngine, textFieldTextoPrivado);
        emojiGridPane.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Not implemented
    }  

}
