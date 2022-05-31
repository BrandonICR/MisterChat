package mx.com.brandonicr.chat.control;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.text.html.HTML.Tag;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.html.HTMLImageElement;

import javafx.application.Platform;
import mx.com.brandonicr.chat.common.constants.FilePaths;
import mx.com.brandonicr.chat.common.constants.ServicesConstants;
import mx.com.brandonicr.chat.common.constants.SpecialCharacterConstants;
import mx.com.brandonicr.chat.common.dto.FileChat;
import mx.com.brandonicr.chat.common.dto.FileChatBuilder;
import mx.com.brandonicr.chat.common.dto.FileSessionInfo;
import mx.com.brandonicr.chat.common.dto.MessageInfo;
import mx.com.brandonicr.chat.common.dto.MessageInfoTypeEnum;
import mx.com.brandonicr.chat.common.utils.ComponentBuilder;
import mx.com.brandonicr.chat.common.utils.ElementUtils;
import mx.com.brandonicr.chat.common.utils.Utils;

public class ChatFileDowloader implements Runnable {

    Logger log = Logger.getLogger(ChatFileDowloader.class.getName());
    
    private FileSessionInfo fileSessionInfo;
    
    public ChatFileDowloader(FileSessionInfo fileSessionInfo){
        this.fileSessionInfo = fileSessionInfo;
    }

    public void sleepThread(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            log.log(Level.INFO, "File Pat Sended");
        }
    }

    public void receiveFile(){
        FileChatBuilder fileChatBuilder = new FileChatBuilder(FilePaths.rootDowloadPath, fileSessionInfo.getFileName());
        try {
            MulticastSocket ms = new MulticastSocket(ServicesConstants.FILE_GROUP_PORT);
            ms.setReuseAddress(true);
            ms.setTimeToLive(ServicesConstants.FILE_GROUP_TIME_TO_LIVE);
            InetAddress ia = InetAddress.getByName(ServicesConstants.FILE_GROUP_ADDRESS);
            ms.joinGroup(ia);
            while(!fileChatBuilder.isCompleted()){
                DatagramPacket dps = new DatagramPacket(new byte[ServicesConstants.GENERAL_SIZE_PACKET], ServicesConstants.GENERAL_SIZE_PACKET);
                ms.receive(dps);
                ByteArrayInputStream bais = new ByteArrayInputStream(dps.getData());
                ObjectInputStream ois = new ObjectInputStream(bais);
                FileChat fileChat = (FileChat)ois.readObject();
                log.log(Level.INFO, "FilePart recibido: {0}", fileChat);
                if(fileChatBuilder.processFileFragment(fileChat))
                    log.log(Level.INFO, "Progress: {0} FileName: {1} CurrentPart: {2}", new Object[]{fileChatBuilder.getProgress(), fileChat.getFileName(), fileChat.getFragmentPosition()});
                else
                    log.log(Level.INFO, "Fragmento no valido obtenido");
                ois.close();
                bais.close();
            }
            ms.close();
        } catch (ClassNotFoundException|IOException ex) {
            ex.printStackTrace();
        }
        if(fileChatBuilder.getIsCompleted()){
            log.log(Level.INFO, "The file was received successfully: {}", fileChatBuilder.getFileName());
            if(!fileChatBuilder.build())
                return;
            Platform.runLater(() -> {
                try{
                    Document document = fileSessionInfo.getWebEngine().getDocument(); 
                    Node body = document.getElementsByTagName(Tag.BODY.toString()).item(SpecialCharacterConstants.INT_ZERO);
                    HTMLImageElement elementImage = (HTMLImageElement)ComponentBuilder.imageElement(document);
                    elementImage.setSrc(FilePaths.fileBasePath.concat(fileChatBuilder.getAbsolutePathFile()));
                    MessageInfo messageInfo = new MessageInfo(fileSessionInfo.getSender().getUserName(), SpecialCharacterConstants.STR_EMPTY, Utils.formatDate(new Date()), MessageInfoTypeEnum.SENDER);
                    Node messageTextNode = ElementUtils.buildNodeMessage(document, messageInfo);
                    body.appendChild(messageTextNode);
                    body.appendChild(elementImage);
                }catch(Exception ex){
                    log.log(Level.SEVERE, ex.getMessage(), ex);
                }
            });
            log.log(Level.INFO, "The image was showed: {0}", fileChatBuilder.getFileName());
        }else
            log.log(Level.SEVERE, "The file was not received successfully: {0}", fileChatBuilder.getFileName());
    }
    
    public void sendFile(){
        try {
            File file = fileSessionInfo.getFile();
            String filename = file.getName();
            byte []fileBase64 = Base64.getEncoder().encode(Files.readAllBytes(file.toPath()));
            int fragmentBlock = (int)Math.ceil(((double)fileBase64.length) / ((double)ServicesConstants.FILE_GROUP_NUMBER_PARTS));
            MulticastSocket ms  = new MulticastSocket(ServicesConstants.FILE_GROUP_PORT);
            ms.setReuseAddress(true);
            ms.setTimeToLive(ServicesConstants.FILE_GROUP_TIME_TO_LIVE);
            InetAddress ia = InetAddress.getByName(ServicesConstants.FILE_GROUP_ADDRESS);
            ms.joinGroup(ia);
            int timesSended = 1;
            while(timesSended < ServicesConstants.FILE_GROUP_TIMES_TO_SEND){
                int readed = 0;
                int averageReaded = 0;
                for(int fragmentPosition = 0; fragmentPosition < ServicesConstants.FILE_GROUP_NUMBER_PARTS; fragmentPosition++){
                    int fragmentSize = ((fragmentPosition + 1) * fragmentBlock < fileBase64.length) ? fragmentBlock : (fileBase64.length - (fragmentPosition * fragmentBlock));
                    byte []bytesReaded = new byte[fragmentSize];
                    System.arraycopy(fileBase64, fragmentPosition * fragmentBlock, bytesReaded, SpecialCharacterConstants.INT_ZERO, fragmentSize);
                    readed += fragmentSize;
                    averageReaded = (readed * SpecialCharacterConstants.INT_ONE_HUNDRED)/fileBase64.length;
                    
                    FileChat objFilePart = new FileChat(bytesReaded, filename, fileBase64.length, fragmentSize, fragmentPosition, ServicesConstants.FILE_GROUP_NUMBER_PARTS, (fragmentPosition + 1) == ServicesConstants.FILE_GROUP_NUMBER_PARTS);
                    
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(objFilePart);
                    oos.flush();

                    DatagramPacket dp = new DatagramPacket(baos.toByteArray(), 0, baos.toByteArray().length, ia, ServicesConstants.FILE_GROUP_PORT);

                    ms.send(dp);
                    
                    String progressMessage = String.format("It was sending _%d%%_ of file _%s_ part _%d_  with size _%d_", averageReaded, filename, fragmentPosition, fragmentSize);
                    log.info(progressMessage);

                    sleepThread(500l);

                    oos.close();
                    baos.close();
                }
                log.log(Level.INFO, "The file has send, times {0}", timesSended);
                timesSended++;
            }
            ms.close();
            log.info("It has already send the file..." + file.getName());
        } catch (IOException ex) {
            Logger.getLogger(ChatFileDowloader.class.getName()).log(Level.SEVERE, "Error while trying to send the file", ex);
        }
    }
    
    @Override
    public void run(){
        if(fileSessionInfo.getConfig().isSend()){
            sendFile();
            return;
        }
        receiveFile();
    }
}
