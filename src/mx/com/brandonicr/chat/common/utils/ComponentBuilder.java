package mx.com.brandonicr.chat.common.utils;

import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLImageElement;

public class ComponentBuilder {

    private ComponentBuilder() {
    }

    public static Element iconElement(Document document, String src) throws DOMException{
        HTMLImageElement imageElement = (HTMLImageElement)document.createElement(Tag.IMG.toString());
        imageElement.setAttribute(Attribute.SRC.toString(), src);
        imageElement.setAttribute(Attribute.ALT.toString(), "x");
        imageElement.setAttribute(Attribute.WIDTH.toString(), "30");
        imageElement.setAttribute(Attribute.HEIGHT.toString(), "30");
        imageElement.setAttribute(Attribute.STYLE.toString(), "display: inline;");
        return imageElement;
    }
    

    public static Element paragraphElement(Document document, String text) throws DOMException{
        Element imageElement = document.createElement(Tag.P.toString());
        imageElement.setAttribute(Attribute.STYLE.toString(), "display: inline;");
        imageElement.setTextContent(text);
        return imageElement;
    }
    
    public static Element messageElement(Document document) throws DOMException{
        Element divMessage = document.createElement(Tag.DIV.toString());
        Element divMessageContent = document.createElement(Tag.DIV.toString());
        Element divMessageText = document.createElement(Tag.DIV.toString());
        divMessage.setAttribute(Attribute.CLASS.toString(), "message");
        divMessageContent.setAttribute(Attribute.CLASS.toString(), "message-content");
        divMessageText.setAttribute(Attribute.ID.toString(), "message-text");
        divMessageText.setAttribute(Attribute.CLASS.toString(), "message-text");
        divMessageContent.appendChild(divMessageText);
        divMessage.appendChild(divMessageContent);
        return divMessage;
    }

    public static Element imageElement(Document document) throws DOMException, ParserConfigurationException{
        Element imageElement = document.createElement(Tag.IMG.toString());
        imageElement.setAttribute(Attribute.ALIGN.toString(), "middle");
        imageElement.setAttribute(Attribute.WIDTH.toString(), "250");
        imageElement.setAttribute(Attribute.HEIGHT.toString(), "250");
        return imageElement;
    }
    
}
