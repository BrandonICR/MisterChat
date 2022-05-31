package mx.com.brandonicr.chat.common.dto;

public class MessageInfo {

    private String name;
    private String value;
    private String hour;
    private MessageInfoTypeEnum type;

    public MessageInfo(String name, String value, String hour, MessageInfoTypeEnum type) {
        this.name = name;
        this.value = value;
        this.hour = hour;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getHour() {
        return this.hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public MessageInfoTypeEnum getType() {
        return this.type;
    }

    public void setType(MessageInfoTypeEnum type) {
        this.type = type;
    }
    
}
