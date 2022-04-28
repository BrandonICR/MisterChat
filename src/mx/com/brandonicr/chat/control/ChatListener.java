package mx.com.brandonicr.chat.control;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.com.brandonicr.chat.common.constants.ServicesConstants;
import mx.com.brandonicr.chat.common.constants.SpecialCharacterConstants;
import mx.com.brandonicr.chat.common.dto.Message;

public class ChatListener implements Runnable{

    Logger log = Logger.getLogger(ChatListener.class.getName());
    
    MessageHandler messageHandler;

    public ChatListener(MessageHandler messageHandler){
        this.messageHandler = messageHandler;
    }

    @Override
    public void run(){
        while(true){
            System.out.println("Waiting for messages...");
            try(MulticastSocket ms = new MulticastSocket(ServicesConstants.LISTENER_PORT);){
                ms.setReuseAddress(Boolean.TRUE.booleanValue());
                ms.setTimeToLive(ServicesConstants.LISTENER_TIME_TO_LIVE);
                InetAddress inetAddress = InetAddress.getByName(ServicesConstants.LISTENER_ADDRESS);
                ms.joinGroup(inetAddress);
                while(true){
                    DatagramPacket dp = new DatagramPacket(new byte[1500],1500);
                    ms.receive(dp);
                    ByteArrayInputStream baos = new ByteArrayInputStream(dp.getData(),SpecialCharacterConstants.INT_ZERO,dp.getData().length);
                    ObjectInputStream oos = new ObjectInputStream(baos);
                    Message messageReceived = (Message)oos.readObject();
                    messageReceived.getSender().setIp(dp.getAddress().getHostAddress());
                    messageHandler.manage(messageReceived);
                }
            } catch (Exception e) {
                log.log(Level.SEVERE, "Error: Has happened an error while processing the sockets in the group", e);
            }
        }
    }
}
