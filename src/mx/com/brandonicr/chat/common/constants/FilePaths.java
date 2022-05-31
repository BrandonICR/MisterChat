package mx.com.brandonicr.chat.common.constants;

import mx.com.brandonicr.chat.common.utils.FilePathsSolver;

public class FilePaths {

    public static final String homePath = FilePathsSolver.solveSlash(System.getProperty("user.home"));
    public static final String rootDowloadPath = homePath.concat("/Downloads");
    public static final String localSource = "./src/";
    public static final String source = "/src";
    public static final String rootPath = "mx/com/brandonicr/chat";
    public static final String fileBasePath = "file:/";
    
    public static final String staticPath = rootPath.concat("/static");

    public static final String imagesPath = staticPath.concat("/images");
    public static final String iconPath = imagesPath.concat("/icons");
    public static final String stylesPath = staticPath.concat("/styles");
    public static final String templatesPath = localSource.concat(staticPath).concat("/templates");

    public static final String imageChatIconPath = "chatIcon.png";
    public static final String fileChatPath = "chat.html";
    public static final String fileChatStylePath = "chat.css";

    private FilePaths(){
        throw new IllegalStateException("This is a private class, you can't create an instance");
    }
    
}
