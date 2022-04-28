package mx.com.brandonicr.chat.common.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mx.com.brandonicr.chat.common.constants.SpecialCharacterConstants;

public class Directory {

    private List<User> contacts = new ArrayList<>();

    public void addContact(User user) {
        contacts.add(user);
    }

    public User getContact(String name, Date creationDate) {
        for(User user: contacts) {
            if(user.getUserName().equals(name)&&user.getCreationDate().compareTo(creationDate)==SpecialCharacterConstants.INT_ZERO) {
                return user;
            }
        }
        return null;
    }
}
