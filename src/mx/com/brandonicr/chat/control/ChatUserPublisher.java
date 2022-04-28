package mx.com.brandonicr.chat.control;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.com.brandonicr.chat.common.constants.ServicesConstants;
import mx.com.brandonicr.chat.common.constants.SpecialCharacterConstants;
import mx.com.brandonicr.chat.common.dto.Message;
import mx.com.brandonicr.chat.common.dto.MessageBuilder;
import mx.com.brandonicr.chat.common.dto.User;

public class ChatUserPublisher implements Runnable {

    Logger log = Logger.getLogger(ChatUserPublisher.class.getName());

    private User currentUser;

    public ChatUserPublisher(User currentUser){
        this.currentUser = currentUser;
    }

    @Override
    public void run() {
        try {
            Message newUser = new MessageBuilder().sender(currentUser).text(SpecialCharacterConstants.STR_EMPTY).controlInfo(false, false, true).build();
            MulticastSocket msa = new MulticastSocket();
            msa.setReuseAddress(true);
            msa.setTimeToLive(ServicesConstants.LISTENER_TIME_TO_LIVE);
            InetAddress ia = InetAddress.getByName(ServicesConstants.LISTENER_ADDRESS);
            msa.joinGroup(ia);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            for(int i = SpecialCharacterConstants.INT_ZERO; i < ServicesConstants.PUBLISHER_NUMBER_MESSAGES; i++) {
                oos.writeObject(newUser);
                DatagramPacket dp = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length, ia, ServicesConstants.LISTENER_PORT);
                msa.send(dp);
            }
            msa.leaveGroup(ia);
            msa.close();
            Thread.sleep(ServicesConstants.PUBLISHER_DELAY);
        } catch (IOException|InterruptedException ex) {
            log.log(Level.SEVERE, null, ex);
        }
    }

}
