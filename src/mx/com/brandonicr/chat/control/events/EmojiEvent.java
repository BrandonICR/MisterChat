package mx.com.brandonicr.chat.control.events;

import java.util.logging.Logger;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mx.com.brandonicr.chat.common.constants.Constants;
import mx.com.brandonicr.chat.common.constants.SpecialCharacterConstants;
import mx.com.brandonicr.chat.common.dto.EmojiInfo;

public class EmojiEvent implements EventHandler<ActionEvent> {

    Logger log = Logger.getLogger(EmojiEvent.class.getName());

    private EmojiInfo emojiInfo;

    public EmojiEvent(EmojiInfo emojiInfo){
        this.emojiInfo = emojiInfo;
    }

    @Override
    public void handle(ActionEvent event) {
        String emojiMessage = Constants.patternSubstitute(SpecialCharacterConstants.STR_ONE)
        .matcher(Constants.emojiForm).replaceAll(emojiInfo.getEmojiName());
        log.info(emojiMessage);
        Platform.runLater(()->{
            emojiInfo.getTextField().appendText(emojiMessage);
        });
    }
    
}
