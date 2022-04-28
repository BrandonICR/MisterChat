package mx.com.brandonicr.chat.common.dto;

public class ConfigurationFileDowloader {

    private boolean isSend;
    private boolean isPrivate;

    public ConfigurationFileDowloader() {
    }

    public ConfigurationFileDowloader(boolean isSend, boolean isPrivate) {
        this.isSend = isSend;
        this.isPrivate = isPrivate;
    }    

    public boolean isSend() {
        return this.isSend;
    }

    public void setIsSend(boolean isSend) {
        this.isSend = isSend;
    }

    public boolean isPrivate() {
        return this.isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
    
}
