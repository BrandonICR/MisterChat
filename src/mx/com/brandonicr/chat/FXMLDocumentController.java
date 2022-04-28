package mx.com.brandonicr.chat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import mx.com.brandonicr.chat.common.constants.FilePaths;
import mx.com.brandonicr.chat.common.constants.SpecialCharacterConstants;
import mx.com.brandonicr.chat.common.dto.ConfigurationFileDowloader;
import mx.com.brandonicr.chat.common.dto.Directory;
import mx.com.brandonicr.chat.common.dto.FileSessionInfo;
import mx.com.brandonicr.chat.common.dto.MessageBuilder;
import mx.com.brandonicr.chat.common.dto.User;
import mx.com.brandonicr.chat.common.utils.ComponentBuilder;
import mx.com.brandonicr.chat.common.utils.ElementUtils;
import mx.com.brandonicr.chat.control.ChatFileDowloader;
import mx.com.brandonicr.chat.control.ChatListener;
import mx.com.brandonicr.chat.control.ChatUserPublisher;
import mx.com.brandonicr.chat.control.DirectoryHandler;
import mx.com.brandonicr.chat.control.EmojiGridPane;
import mx.com.brandonicr.chat.control.MessageHandler;
import mx.com.brandonicr.chat.control.MessageTransmitter;

import javax.swing.JFileChooser;
import javax.swing.text.html.HTML.Tag;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLImageElement;

/**
 * @author BrandonICR
 */
public class FXMLDocumentController implements Initializable {

    Logger log = Logger.getLogger(FXMLDocumentController.class.getName());
   
    @FXML
    private WebView webView;
    @FXML
    private AnchorPane anchorPaneInicio;
    @FXML
    private ListView<Button> listViewContactos;
    @FXML
    private Label labelMostrar;  
    @FXML
    private Button buttonIcons;
    @FXML
    private Button buttonAdjuntar;
    @FXML
    private Button buttonAgregar;
    @FXML
    private Button buttonActivo;
    @FXML
    private TextField textFieldEnviar;
    @FXML
    private TextField textFieldUsername;
    
    private ObservableList<Button> listObservable = FXCollections.observableArrayList();
    private WebEngine webEngine;
    private DirectoryHandler directoryHandler;
    private User user = new User();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        
        webView.setDisable(true);
        textFieldEnviar.setDisable(true);
        listViewContactos.setDisable(true);
        labelMostrar.setDisable(true);
        buttonIcons.setDisable(true);
        buttonAdjuntar.setDisable(true);
        buttonAgregar.setDisable(true);

        directoryHandler = new DirectoryHandler(webEngine, listViewContactos, listObservable, new Directory(), user);

        try(BufferedReader fileContentChat = new BufferedReader(new FileReader(new File(FilePaths.templatesPath.concat(SpecialCharacterConstants.STR_SLASH).concat(FilePaths.fileChatPath))));){
            String contentChat = fileContentChat.lines().reduce(SpecialCharacterConstants.STR_EMPTY, (line1, line2) -> line1.concat(SpecialCharacterConstants.STR_SPACE).concat(line2));
            webEngine.loadContent(contentChat);
        }catch(Exception e){
            log.log(Level.SEVERE, "Error: file static/templates/chat.html could not be loaded", e);
            Platform.exit();
        }
      
    }    
    
    @FXML
    private void agregarTexto(ActionEvent e){
        if(!textFieldEnviar.getText().trim().isEmpty()){
            String message = "Yo"+"::"+textFieldEnviar.getText().trim();
            
            Node messageTextNode = ElementUtils.buildNodeMessage(message, webEngine.getDocument());
            Document document = webEngine.getDocument(); 
            Node body = document.getElementsByTagName(Tag.BODY.toString()).item(SpecialCharacterConstants.INT_ZERO);
            body.appendChild(messageTextNode);

            Thread messageTransmitter = new Thread(new MessageTransmitter(new MessageBuilder().sender(user).text(textFieldEnviar.getText().trim()).controlInfo(false, false, false).build()));
            messageTransmitter.start();
            System.out.println("Se ha mandado un mensaje::"+textFieldEnviar.getText().trim());
        }
        textFieldEnviar.clear();
    }
    
    @FXML
    private void adjuntarArchivo(ActionEvent e){            
        JFileChooser fc = new JFileChooser();
        int status = fc.showOpenDialog(null);
        if(status == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try{
                Document document = webEngine.getDocument(); 
                Node body = document.getElementsByTagName(Tag.BODY.toString()).item(SpecialCharacterConstants.INT_ZERO);
                HTMLImageElement elementImage = (HTMLImageElement)ComponentBuilder.imageElement(document);
                System.out.println(FilePaths.fileBasePath.concat(file.getAbsolutePath()));
                elementImage.setSrc(FilePaths.fileBasePath.concat(file.getAbsolutePath()));
                body.appendChild(elementImage);
            }catch(Exception ex){
                log.log(Level.SEVERE, ex.getMessage(), ex);
            }
            try {
                Thread messageTransmitter = new Thread(new MessageTransmitter(new MessageBuilder().sender(user).text(file.getName()).controlInfo(true, false, false).build()));
                messageTransmitter.start();
                messageTransmitter.join();
                Thread.sleep(2*1000l);
            } catch (InterruptedException ex) {
                log.log(Level.INFO, "Message sended");
            }
            FileSessionInfo fileSessionInfo = new FileSessionInfo(file, file.getName(), user, new ConfigurationFileDowloader(true, false), webEngine);
            new Thread(new ChatFileDowloader(fileSessionInfo)).start();
            System.out.println("Se ha enviado el archivo: " + file.getName());
        }
    }
    
    @FXML
    private void activar(ActionEvent e) {
        String userName = textFieldUsername.getText().trim();
        if(!userName.isEmpty()) {
            user.setUserName(userName);
            user.setCreationDate(new Date());
            user.setIp("127.0.0.1");

            System.out.println("Me he unido a la sala <"+user.getUserName()+">");

            Thread listenerChat = new Thread(new ChatListener(new MessageHandler(webEngine, user, directoryHandler)));
            listenerChat.start();

            Thread publisherChat = new Thread(new ChatUserPublisher(user));
            publisherChat.start();

            webView.setDisable(false);
            textFieldEnviar.setDisable(false);
            listViewContactos.setDisable(false);
            labelMostrar.setDisable(false);
            buttonAdjuntar.setDisable(false);
            buttonIcons.setDisable(false);
            buttonAgregar.setDisable(false);

            buttonActivo.setStyle("fx-background-color: #ff0f00");
            buttonActivo.setVisible(false);
            textFieldUsername.setVisible(false);
            buttonActivo.setDisable(true);
            textFieldUsername.setDisable(true);

            labelMostrar.setFont(new Font(12));
            listViewContactos.setPrefHeight(535);
            return;
        }
        textFieldUsername.clear();
        textFieldUsername.setPromptText("Username not available...");
    }

    @FXML
    private void showEmojis(ActionEvent e) {
        EmojiGridPane emojiGridPane = new EmojiGridPane(webEngine, textFieldEnviar);
        emojiGridPane.show();
    }  
    
}