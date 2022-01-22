package mx.com.brandonicr.chat.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("");
        String strDateWithFormat = simpleDateFormat.format(date);
        return strDateWithFormat;
    }

    private Utils(){
        throw new IllegalStateException("This is a private class, you can't create an instance");
    }
    
}
