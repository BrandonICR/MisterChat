package mx.com.brandonicr.chat.common.dto;

import java.io.Serializable;
import java.util.Date;

import mx.com.brandonicr.chat.common.constants.SpecialCharacterConstants;

/**
 *
 * @author BrandonICR
 */
public class User implements Serializable {

    private String userName;
    private Date creationDate;
    private String ip;

    public User() {
        this.userName = SpecialCharacterConstants.STR_EMPTY;
        this.ip = SpecialCharacterConstants.STR_EMPTY;
    }

    public User(String userName, Date creationDate, String ip) {
        this.userName = userName;
        this.creationDate = creationDate;
        this.ip = ip;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "{" +
            " userName='" + getUserName() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", ip='" + getIp() + "'" +
            "}";
    }

}
