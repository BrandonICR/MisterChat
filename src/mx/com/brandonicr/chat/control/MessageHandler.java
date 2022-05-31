package mx.com.brandonicr.chat.control;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.text.html.HTML.Tag;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javafx.application.Platform;
import javafx.scene.web.WebEngine;
import mx.com.brandonicr.chat.common.constants.FilePaths;
import mx.com.brandonicr.chat.common.constants.SpecialCharacterConstants;
import mx.com.brandonicr.chat.common.dto.ConfigurationFileDowloader;
import mx.com.brandonicr.chat.common.dto.FileSessionInfo;
import mx.com.brandonicr.chat.common.dto.Message;
import mx.com.brandonicr.chat.common.dto.MessageInfo;
import mx.com.brandonicr.chat.common.dto.MessageInfoTypeEnum;
import mx.com.brandonicr.chat.common.dto.User;
import mx.com.brandonicr.chat.common.utils.ElementUtils;
import mx.com.brandonicr.chat.common.utils.ObjectValidator;
import mx.com.brandonicr.chat.common.utils.Utils;

public class MessageHandler {

    Logger log = Logger.getLogger(MessageHandler.class.getName());
    
    private WebEngine webEngine;
    private User user;
    private DirectoryHandler directoryHandler;

    private List<Message> allMessages;

    public MessageHandler(WebEngine webEngine, User user, DirectoryHandler directoryHandler){
        this.webEngine = webEngine;
        this.user = user;
        this.directoryHandler = directoryHandler;
        allMessages = new ArrayList<>();
    }
    
    public void showMessage(MessageInfo messageInfo){
        Platform.runLater(() -> {
            try{
                Document document = webEngine.getDocument(); 
                Node messageTextNode = ElementUtils.buildNodeMessage(document, messageInfo);
                Node body = document.getElementsByTagName(Tag.BODY.toString()).item(SpecialCharacterConstants.INT_ZERO);
                body.appendChild(messageTextNode);
            }catch(Exception e){
                log.log(Level.SEVERE, "Error: While trying to create the message", e);
            }
        });
    }

    public boolean isRepeated(Message messageReceived) {
        return allMessages.stream().anyMatch(message -> message.getSender().getUserName().compareTo(messageReceived.getSender().getUserName()) == SpecialCharacterConstants.INT_ZERO 
        && message.getBuildingDate().toString().compareTo(messageReceived.getBuildingDate().toString()) == SpecialCharacterConstants.INT_ZERO);
    }

    public void manage(Message messageReceived){
        if(isRepeated(messageReceived))
        {
            System.out.print(">>Mensaje repetido<<: ");
            System.out.println(messageReceived);
            return;
        }

        System.out.print(">>Mensaje recibido<<: ");
        System.out.println(messageReceived);
        allMessages.add(messageReceived);
        if(messageReceived.getConfig().isNewUser() && directoryHandler.existsContact(messageReceived.getSender())){
            System.out.println("El usuario ya está registrado");
        }else if(messageReceived.getConfig().isNewUser() && !messageReceived.getSender().getUserName().equals(user.getUserName())){
            directoryHandler.addContact(messageReceived.getSender(), messageReceived.getSender().getUserName());
            String message = "¡Se ha unido "+messageReceived.getSender().getUserName()+" al chat!";
            log.log(Level.INFO, message);
            showMessage(new MessageInfo("Chat", message, Utils.formatDate(messageReceived.getBuildingDate()), MessageInfoTypeEnum.INFORMATIVE));
            new Thread(new ChatUserPublisher(user)).start();
        }else if(messageReceived.getConfig().isNewUser() && messageReceived.getSender().getUserName().equals(user.getUserName())){
            directoryHandler.addContact(messageReceived.getSender(), "Yo");
            String message = String.format("¡Welcome %s!", user.getUserName());
            System.out.println(message);
            showMessage(new MessageInfo("Chat", message, Utils.formatDate(messageReceived.getBuildingDate()), MessageInfoTypeEnum.INFORMATIVE));
        }else if(messageReceived.getReceiver().getUserName().equals(user.getUserName()) && messageReceived.getConfig().isPrivate() && messageReceived.getConfig().hasImage()){
            System.out.println("Private image received");
            FileSessionInfo fileSessionInfo = new FileSessionInfo(new File(FilePaths.rootDowloadPath), messageReceived.getText(), messageReceived.getSender(), new ConfigurationFileDowloader(false, true), webEngine);
            new Thread(new ChatFileDowloader(fileSessionInfo)).start();
        }else if(!messageReceived.getSender().getUserName().equals(user.getUserName())  && !messageReceived.getConfig().isPrivate() && messageReceived.getConfig().hasImage()){
            System.out.println("Public image received");
            FileSessionInfo fileSessionInfo = new FileSessionInfo(new File(FilePaths.rootDowloadPath), messageReceived.getText(), messageReceived.getSender(), new ConfigurationFileDowloader(false, false), webEngine);
            new Thread(new ChatFileDowloader(fileSessionInfo)).start();
        }else if((messageReceived.getConfig().isPrivate() && messageReceived.getReceiver().getUserName().equals(user.getUserName())) || 
                (!messageReceived.getConfig().isPrivate() && !messageReceived.getSender().getUserName().equals(user.getUserName()))){
            String message = (String)ObjectValidator.ifTrueReturnOrElse(messageReceived.getConfig().isPrivate(), "PRIVATE: ", SpecialCharacterConstants.STR_EMPTY)+messageReceived.getText();
            System.out.println(message);
            showMessage(new MessageInfo(messageReceived.getSender().getUserName(), message, Utils.formatDate(messageReceived.getBuildingDate()), (MessageInfoTypeEnum)ObjectValidator.ifTrueReturnOrElse(messageReceived.getConfig().isPrivate(), MessageInfoTypeEnum.PRIVATE, MessageInfoTypeEnum.SENDER)));
        }
    }
    
}
