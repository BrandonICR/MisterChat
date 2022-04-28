package mx.com.brandonicr.chat.common.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

import mx.com.brandonicr.chat.common.constants.SpecialCharacterConstants;

public class Utils {

    public static String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static String formatDateFile(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("_HH_mm_ss");
        return simpleDateFormat.format(date);
    }

    public static String solveIp(){
        String ip = SpecialCharacterConstants.STR_EMPTY;
        try{
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while(networkInterfaces.hasMoreElements()){
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if(networkInterface.supportsMulticast()){
                    Enumeration<InetAddress> enumInetAddress = networkInterface.getInetAddresses();
                    while(enumInetAddress.hasMoreElements()){
                        InetAddress inetAddress = enumInetAddress.nextElement();
                        if(inetAddress.isAnyLocalAddress())
                            ip = inetAddress.getHostAddress();
                    }
                }
            }
        }catch(SocketException s){
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, "Error trying to get the ip address");
        }
        return ip;
    }

    private Utils(){
        throw new IllegalStateException("This is a private class, you can't create an instance");
    }
    
}
