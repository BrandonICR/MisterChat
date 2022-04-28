package mx.com.brandonicr.chat.common.dto;

import java.util.Date;

public class MessageBuilder {

    Message message;

    public MessageBuilder(){
        message = new Message();
    }

    public MessageBuilder reset(){
        message = new Message();
        return this;
    }

    public MessageBuilder sender(String nombre, Date fecha, String ip){
        message.setSender(new User(nombre, fecha, ip));
        return this;
    }

    public MessageBuilder sender(User user){
        message.setSender(user);
        return this;
    }

    public MessageBuilder receiver(String nombre, Date fecha, String ip){
        message.setReceiver(new User(nombre, fecha, ip));
        return this;
    }

    public MessageBuilder receiver(User user){
        message.setReceiver(user);
        return this;
    }

    public MessageBuilder text(String text){
        message.setText(text);
        return this;
    }

    public MessageBuilder controlInfo(boolean hasImage, boolean isPrivate, boolean isNewUser){
        message.setConfig(new ConfigurationMessageInfo(isPrivate, isNewUser, hasImage));
        return this;
    }

    public MessageBuilder controlInfo(ConfigurationMessageInfo config){
        message.setConfig(config);
        return this;
    }

    public Message build(){
        message.setBuildingDate(new Date());
        return message;
    }
    
}
