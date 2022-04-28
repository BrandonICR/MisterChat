package mx.com.brandonicr.chat.common.constants;

public class ServicesConstants {

    public static final String LISTENER_ADDRESS = "230.0.0.1";
    public static final Integer LISTENER_PORT = 1024;
    public static final Integer LISTENER_TIME_TO_LIVE = 255;
    public static final Long PUBLISHER_DELAY = 1000l;
    public static final Integer PUBLISHER_NUMBER_MESSAGES = 20;

    public static final String SYSTEM_ADDRESS = "127.0.0.1";

    public static final String FILE_GROUP_ADDRESS = "230.0.0.1";
    public static final Integer FILE_GROUP_PORT = 1025;
    public static final Integer FILE_GROUP_TIME_TO_LIVE = 255;
    public static final Integer FILE_GROUP_TIMES_TO_SEND = 5;
    public static final Integer FILE_GROUP_NUMBER_PARTS = 10;

    public static final Integer GENERAL_SIZE_PACKET = 8*10000000;
    
    private ServicesConstants(){
        throw new IllegalStateException("This is a private class, you can't create an instance");
    }
    
}
