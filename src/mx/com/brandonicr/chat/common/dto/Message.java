package mx.com.brandonicr.chat.common.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * @author BrandonICR
 */
public class Message implements Serializable {
    
    private User sender;
    private User receiver;
    private String text;
    private Date buildingDate;
    private ConfigurationMessageInfo config;

    public User getSender() {
        return this.sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        if(Objects.isNull(receiver))
            return new User();
        return this.receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getBuildingDate() {
        return this.buildingDate;
    }

    public void setBuildingDate(Date buildingDate) {
        this.buildingDate = buildingDate;
    }

    public ConfigurationMessageInfo getConfig() {
        return this.config;
    }

    public void setConfig(ConfigurationMessageInfo config) {
        this.config = config;
    }

    @Override
    public String toString() {
        return "{" +
            " sender='" + getSender() + "'" +
            ", receiver='" + getReceiver() + "'" +
            ", text='" + getText() + "'" +
            ", buildingDate='" + getBuildingDate() + "'" +
            ", config='" + getConfig() + "'" +
            "}";
    }

}
