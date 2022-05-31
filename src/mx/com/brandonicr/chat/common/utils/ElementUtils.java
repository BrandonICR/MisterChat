package mx.com.brandonicr.chat.common.utils;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.HTML.Attribute;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javafx.application.Platform;
import mx.com.brandonicr.chat.common.constants.Constants;
import mx.com.brandonicr.chat.common.dto.MessageInfo;

public class ElementUtils {

    public static Node buildNodeMessage(Document document, MessageInfo messageInfo){
        Pattern patternEmoji = Constants.patternEmoji;
        Node messageNode = ComponentBuilder.messageElement(document, messageInfo.getType().getSelectorPrefix());
        Node messageTextNode = ElementUtils.findNodeById(messageNode, Constants.idMessage);
        if(Objects.isNull(messageTextNode)){
            Platform.exit();
            return null;
        }
        Node messageNameNode = ElementUtils.findNodeById(messageTextNode, Constants.idMessageName);
        Node messageValueNode = ElementUtils.findNodeById(messageTextNode, Constants.idMessageValue);
        Node messageHourNode = ElementUtils.findNodeById(messageTextNode, Constants.idMessageHour);
        if(Objects.isNull(messageNameNode) || Objects.isNull(messageValueNode) || Objects.isNull(messageHourNode)){
            Platform.exit();
            return null;
        }
        messageNameNode.setTextContent(messageInfo.getName());
        messageHourNode.setTextContent(messageInfo.getHour());
        Matcher matcher = patternEmoji.matcher(messageInfo.getValue());
        int indexEndEmoji = 0;
        int indexStartEmoji = 0;
        while(matcher.find()){
            indexStartEmoji = matcher.start();
            ((Element)messageValueNode).appendChild(ComponentBuilder.paragraphElement(document, messageInfo.getValue().substring(indexEndEmoji, indexStartEmoji)));
            indexEndEmoji = matcher.end();
            String emojiName = messageInfo.getValue().substring(indexStartEmoji, indexEndEmoji);
            ((Element)messageValueNode).appendChild(ComponentBuilder.iconElement(document, FilePathsSolver.iconSolverPath(emojiName)));
        }
        ((Element)messageValueNode).appendChild(ComponentBuilder.paragraphElement(document, messageInfo.getValue().substring(indexEndEmoji, messageInfo.getValue().length())));
        return messageNode;
    }

    public static Node findNodeById(Node node, String id){
        NodeList nodeList = node.getChildNodes();
        for(int i = 0; i < nodeList.getLength(); i++){
            Node nodeItem = nodeList.item(i);
            String valueAttribute = ((Element)nodeItem).getAttribute(Attribute.ID.toString());
            if(!Objects.isNull(valueAttribute) && valueAttribute.equals(id))
                return nodeItem;
            Node childItem = findNodeById(nodeItem, id);
            if(Objects.isNull(childItem))
                continue;
            return childItem;
        }
        return null;
    }

    private ElementUtils(){
        throw new IllegalStateException("This is a private class, you can't create an instance");
    }
    
}
