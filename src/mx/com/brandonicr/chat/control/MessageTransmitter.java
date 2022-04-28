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

public class MessageTransmitter implements Runnable {

    Logger log = Logger.getLogger(MessageTransmitter.class.getName());

    private Message message;

    public MessageTransmitter(Message message) {
        this.message = message;
    }

    @Override
    public void run() {
        try {
            MulticastSocket msa = new MulticastSocket();
            msa.setReuseAddress(Boolean.TRUE);
            msa.setTimeToLive(ServicesConstants.LISTENER_TIME_TO_LIVE);
            InetAddress ia = InetAddress.getByName(ServicesConstants.LISTENER_ADDRESS);
            msa.joinGroup(ia);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(message);
            for (int i = SpecialCharacterConstants.INT_ZERO; i < ServicesConstants.PUBLISHER_NUMBER_MESSAGES; i++) {
                DatagramPacket dp = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length, ia, ServicesConstants.LISTENER_PORT);
                msa.send(dp);
            }
            msa.leaveGroup(ia);
            msa.close();
        } catch (IOException ex) {
            log.log(Level.SEVERE, "Error trying to send the message", ex);
        }
    }

}
