package mx.com.brandonicr.chat.common.utils;

import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;

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
        imageElement.setAttribute(Attribute.CLASS.toString(), "message-icon");
        return imageElement;
    }
    

    public static Element paragraphElement(Document document, String text) throws DOMException{
        Element paragraphElement = document.createElement(Tag.P.toString());
        paragraphElement.setAttribute(Attribute.CLASS.toString(), "message-paragraph");
        paragraphElement.setTextContent(text);
        return paragraphElement;
    }
    
    public static Element messageElement(Document document, String selectorPrefix) throws DOMException{
        Element divMessage = document.createElement(Tag.DIV.toString());
        Element divMessageContent = document.createElement(Tag.DIV.toString());
        Element divMessageText = document.createElement(Tag.DIV.toString());
        Element divMessageName = document.createElement(Tag.DIV.toString());
        Element divMessageValue = document.createElement(Tag.DIV.toString());
        Element divMessageHour = document.createElement(Tag.DIV.toString());
        Element br1 = document.createElement(Tag.BR.toString());
        Element br2 = document.createElement(Tag.BR.toString());
        divMessage.setAttribute(Attribute.CLASS.toString(), "message");
        divMessageContent.setAttribute(Attribute.CLASS.toString(), selectorPrefix.concat("message-content"));
        divMessageText.setAttribute(Attribute.ID.toString(), "message-text");
        divMessageText.setAttribute(Attribute.CLASS.toString(), selectorPrefix.concat("message-text"));
        divMessageName.setAttribute(Attribute.CLASS.toString(), selectorPrefix.concat("message-name"));
        divMessageName.setAttribute(Attribute.ID.toString(), "message-name");
        divMessageValue.setAttribute(Attribute.CLASS.toString(), selectorPrefix.concat("message-value"));
        divMessageValue.setAttribute(Attribute.ID.toString(), "message-value");
        divMessageHour.setAttribute(Attribute.CLASS.toString(), selectorPrefix.concat("message-hour"));
        divMessageHour.setAttribute(Attribute.ID.toString(), "message-hour");
        br1.setAttribute(Attribute.CLASS.toString(), selectorPrefix.concat("message-br1"));
        br2.setAttribute(Attribute.CLASS.toString(), selectorPrefix.concat("message-br2"));
        divMessageText.appendChild(divMessageName);
        divMessageText.appendChild(br1);
        divMessageText.appendChild(divMessageValue);
        divMessageText.appendChild(br2);
        divMessageText.appendChild(divMessageHour);
        divMessageContent.appendChild(divMessageText);
        divMessage.appendChild(divMessageContent);
        return divMessage;
    }

    public static Element imageElement(Document document) throws DOMException{
        Element imageElement = document.createElement(Tag.IMG.toString());
        imageElement.setAttribute(Attribute.ALIGN.toString(), "middle");
        imageElement.setAttribute(Attribute.WIDTH.toString(), "250");
        imageElement.setAttribute(Attribute.HEIGHT.toString(), "250");
        return imageElement;
    }
    
}
