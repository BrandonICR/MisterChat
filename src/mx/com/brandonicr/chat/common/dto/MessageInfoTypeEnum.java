package mx.com.brandonicr.chat.common.dto;

public enum MessageInfoTypeEnum {
    INFORMATIVE("INFORMATIVE","info-"),
    OWNER("OWNER","owner-"),
    PRIVATE("PRIVATE","private-"),
    SENDER("SENDER", "sender-")
    ;

    private String type;
    private String selectorPrefix;

    private MessageInfoTypeEnum(String type, String selectorPrefix) {
        this.type = type;
        this.selectorPrefix = selectorPrefix;
    }

    public String getType(){
        return type;
    }

    public String getSelectorPrefix(){
        return selectorPrefix;
    }
    
}
