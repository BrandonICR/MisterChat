package mx.com.brandonicr.chat.common.utils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import mx.com.brandonicr.chat.common.constants.Constants;
import mx.com.brandonicr.chat.common.constants.FilePaths;
import mx.com.brandonicr.chat.common.constants.SpecialCharacterConstants;

public class ElementUtils {

    public static final String imageTagString = "<img src=\"##1##\" alt=\"x\" width=\"30\" height=\"30\" style=\"display: inline;\">";

    public static Node buildNodeFromDocumentByUri(String uri, String id) throws ParserConfigurationException, SAXException, IOException{
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document messageDocument = documentBuilder.parse(new File(uri));
        return messageDocument.getElementById(id);
    }

    public static Node buildNodeFromDocumentByUri(String uri) throws ParserConfigurationException, SAXException, IOException{
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document messageDocument = documentBuilder.parse(new File(uri));
        Node body = messageDocument.getElementsByTagName(Tag.BODY.toString()).item(SpecialCharacterConstants.INT_ZERO);
        return body.getFirstChild();
    }

    public static Node buildNodeMessage(String message) throws ParserConfigurationException, SAXException, IOException{
        Pattern patternEmoji = Constants.patternEmoji;
        Matcher matcher = patternEmoji.matcher(message);
        Node messageNode = buildNodeFromDocumentByUri(FilePaths.templatesPath.concat(FilePaths.messageComponentPath));
        Node messageTextNode = ElementUtils.findNodeById(messageNode, Constants.idMessage);
        if(messageTextNode == null)
            throw new RuntimeException();
        StringBuilder messageText = new StringBuilder();
        int indexEndEmoji = 0;
        int indexStartEmoji = 0;
        while(matcher.find()){
            indexStartEmoji = matcher.regionStart();
            messageText.append(message.substring(indexEndEmoji, indexStartEmoji+1));
            indexEndEmoji = matcher.regionEnd()-1;
            String emojiName = message.substring(indexStartEmoji, indexEndEmoji);
            messageText.append(Constants.patternSubstitute(SpecialCharacterConstants.STR_ONE).matcher(ElementUtils.imageTagString).replaceAll(FilePathsSolver.iconSolverPath(emojiName)));
        }
        messageText.append(message.substring(indexEndEmoji, message.length()));
        ((Element)messageTextNode).setTextContent(messageText.toString());
        return messageNode;
    }

    public static Node findNodeById(Node node, String id){
        NodeList nodeList = node.getChildNodes();
        for(int i = 0; i < nodeList.getLength(); i++){
            Node nodeItem = nodeList.item(i);
            if(((Element)nodeItem).getAttribute(Attribute.ID.toString()).equals(id))
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
