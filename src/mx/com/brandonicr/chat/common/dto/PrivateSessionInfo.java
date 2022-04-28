package mx.com.brandonicr.chat.common.dto;

import javafx.scene.web.WebEngine;

public class PrivateSessionInfo {

    private User sender;
    private User receiver;
    private WebEngine webEngine;

    public PrivateSessionInfo() {
    }

    public PrivateSessionInfo(User sender, User receiver, WebEngine webEngine) {
        this.sender = sender;
        this.receiver = receiver;
        this.webEngine = webEngine;
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

    public WebEngine getWebEngine() {
        return this.webEngine;
    }

    public void setWebEngine(WebEngine webEngine) {
        this.webEngine = webEngine;
    }
    
}
