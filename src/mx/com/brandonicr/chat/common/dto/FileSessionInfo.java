package mx.com.brandonicr.chat.common.dto;

import java.io.File;

import javafx.scene.web.WebEngine;

public class FileSessionInfo {

    private File file;
    private String fileName;
    private User sender;
    private User receiver;
    private ConfigurationFileDowloader config;
    private WebEngine webEngine;

    public FileSessionInfo() {
    }

    public FileSessionInfo(File file, String fileName, User sender, ConfigurationFileDowloader config, WebEngine webEngine) {
        this.file = file;
        this.fileName = fileName;
        this.sender = sender;
        this.config = config;
        this.webEngine = webEngine;
    }

    public FileSessionInfo(File file, String fileName, User sender, User receiver, ConfigurationFileDowloader config, WebEngine webEngine) {
        this.file = file;
        this.fileName = fileName;
        this.sender = sender;
        this.receiver = receiver;
        this.config = config;
        this.webEngine = webEngine;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public User getSender() {
        return this.sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return this.receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public ConfigurationFileDowloader getConfig() {
        return this.config;
    }

    public void setConfig(ConfigurationFileDowloader config) {
        this.config = config;
    }

    public WebEngine getWebEngine() {
        return this.webEngine;
    }

    public void setWebEngine(WebEngine webEngine) {
        this.webEngine = webEngine;
    }
    
}
