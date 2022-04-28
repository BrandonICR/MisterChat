package mx.com.brandonicr.chat.common.constants;

import java.util.regex.Pattern;

public class Constants {

    public static final String emojiForm = "<<emoji-##1##->>";
    public static final Pattern patternEmoji = Pattern.compile("(<<emoji-\\w{1,50}\\.\\w{1,10}->>)");
    public static final String idMessage = "message-text";

    public static Pattern patternSubstitute(String numArg) {
        return Pattern.compile("##".concat(numArg).concat("##"));
    }

    private Constants() {
        throw new IllegalStateException("This is a private class, you can't create an instance");
    }

}
