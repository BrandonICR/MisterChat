package mx.com.brandonicr.chat.common.constants;

public class FilePaths {

    public static final String rootPath = "./src/mx/com/brandonicr/chat/";
    public static final String staticPath = rootPath.concat("static/");
    public static final String imagesPath = staticPath.concat("images/");
    public static final String iconPath = imagesPath.concat("icons/");
    public static final String templatesPath = staticPath.concat("templates/");
    public static final String fileChatPath = "chat.html";
    public static final String messageComponentPath = "components/message.html";
    public static final String iconComponentPath = "components/icon.html";

    private FilePaths(){
        throw new IllegalStateException("This is a private class, you can't create an instance");
    }
    
}
