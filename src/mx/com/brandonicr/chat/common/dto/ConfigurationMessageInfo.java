package mx.com.brandonicr.chat.common.dto;

import java.io.Serializable;

public class ConfigurationMessageInfo implements Serializable {

    private boolean isPrivate;
    private boolean isNewUser;
    private boolean hasImage;

    public ConfigurationMessageInfo(boolean isPrivate, boolean isNewUser, boolean hasImage) {
        this.isPrivate = isPrivate;
        this.isNewUser = isNewUser;
        this.hasImage = hasImage;
    }

    public boolean isPrivate() {
        return this.isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public boolean isNewUser() {
        return this.isNewUser;
    }

    public void setIsNewUser(boolean isNewUser) {
        this.isNewUser = isNewUser;
    }

    public boolean hasImage() {
        return this.hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    @Override
    public String toString() {
        return "{" +
            " isPrivate='" + isPrivate() + "'" +
            ", isNewUser='" + isNewUser() + "'" +
            ", hasImage='" + hasImage() + "'" +
            "}";
    }
    
}
