package mx.com.brandonicr.chat.common.dto;

import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;

public class EmojiInfo {

    private WebEngine webEngine;
    private TextField textField;
    private String emojiName;
    private String basePath;

    public EmojiInfo(WebEngine webEngine, TextField textField, String emojiName, String basePath) {
        this.webEngine = webEngine;
        this.textField = textField;
        this.emojiName = emojiName;
        this.basePath = basePath;
    }

    public WebEngine getWebEngine() {
        return this.webEngine;
    }

    public void setWebEngine(WebEngine webEngine) {
        this.webEngine = webEngine;
    }

    public TextField getTextField() {
        return this.textField;
    }

    public void setTextField(TextField textField) {
        this.textField = textField;
    }

    public String getEmojiName() {
        return this.emojiName;
    }

    public void setEmojiName(String emojiName) {
        this.emojiName = emojiName;
    }

    public String getBasePath() {
        return this.basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

}
