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

public class ElementUtils {

    public static Node buildNodeMessage(String message, Document document){
        Pattern patternEmoji = Constants.patternEmoji;
        Node messageNode = ComponentBuilder.messageElement(document);
        Node messageTextNode = ElementUtils.findNodeById(messageNode, Constants.idMessage);
        if(Objects.isNull(messageTextNode)){
            Platform.exit();
            return null;
        }
        Matcher matcher = patternEmoji.matcher(message);
        int indexEndEmoji = 0;
        int indexStartEmoji = 0;
        while(matcher.find()){
            indexStartEmoji = matcher.start();
            ((Element)messageTextNode).appendChild(ComponentBuilder.paragraphElement(document, message.substring(indexEndEmoji, indexStartEmoji)));
            indexEndEmoji = matcher.end();
            String emojiName = message.substring(indexStartEmoji, indexEndEmoji);
            ((Element)messageTextNode).appendChild(ComponentBuilder.iconElement(document, FilePathsSolver.iconSolverPath(emojiName)));
        }
        ((Element)messageTextNode).appendChild(ComponentBuilder.paragraphElement(document, message.substring(indexEndEmoji, message.length())));
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
