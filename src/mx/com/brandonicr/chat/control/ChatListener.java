package mx.com.brandonicr.chat.control;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.HTML.Attribute;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import mx.com.brandonicr.chat.FXMLDocumentController;
import mx.com.brandonicr.chat.common.constants.Constants;
import mx.com.brandonicr.chat.common.constants.FilePaths;
import mx.com.brandonicr.chat.common.constants.SpecialCharacterConstants;
import mx.com.brandonicr.chat.common.dto.Mensaje;
import mx.com.brandonicr.chat.common.dto.Usuario;
import mx.com.brandonicr.chat.common.utils.ElementUtils;
import mx.com.brandonicr.chat.common.utils.FilePathsSolver;
import mx.com.brandonicr.chat.common.utils.Utils;

public class ChatListener extends Thread{
    
    private WebEngine we;
    private ListView<Button> listViewContactos;
    private ObservableList<Button> list;
    private String userName;
    private Date dateUserName;
    
    public ChatListener(WebEngine we, ListView<Button> listViewContactos, ObservableList<Button> list, String userName, Date dateUserName){
        this.we = we;
        this.listViewContactos = listViewContactos;
        this.list = list;
        this.userName = userName;
        this.dateUserName = dateUserName;
    }
    
    public void agregarAlChat(String mensaje, Date fecha, String nombre){
        we.executeScript("var para = document.createElement(\"p\"); para.innerText = \""+(fecha.toString())+"::"+nombre+":"+mensaje+"\";document.body.appendChild(para);");
    }

    public void emojiNamesAlChat(String nombreEmoji, Date fecha, String nombre){
        
    }

    public void imageAlChat(String nombreImagen){
        
    }
    
    public void run(){
        ArrayList<Mensaje> allMensajes = new ArrayList<>();
        System.out.println("Esperando mensaje...");
        allMensajes.removeAll(allMensajes);
        boolean repetido = false;
        boolean infinito = true;
        while(infinito){
            try {
                MulticastSocket ms = new MulticastSocket(1024);
                ms.setReuseAddress(true);
                ms.setTimeToLive(255);
                InetAddress ia = InetAddress.getByName("230.0.0.1");
                ms.joinGroup(ia);
                DatagramPacket dp = new DatagramPacket(new byte[1500],1500);
                ms.receive(dp);
                ByteArrayInputStream baos = new ByteArrayInputStream(dp.getData(),0,dp.getData().length);
                ObjectInputStream oos = new ObjectInputStream(baos);
                Mensaje recibido = (Mensaje)oos.readObject();
                for(Mensaje mens : allMensajes){
                    if(mens.getFecha().toString().equals(recibido.getFecha().toString())){
                        repetido = true;
                        break;
                    }
                }
                System.out.println("He recibido un mensaje de " + recibido.getNombre() + "asunto, " + ((recibido.isImagen()==true) ? "enviarme imagen.":((recibido.isIsNew() == true) ? "se quiere conectar." : ((recibido.getLongEmoji() > 0) ? "texto con emojiNamess." : ((recibido.isIsPrivate()==true) ? "es privado." : "sin condicion.")))));
                if(repetido == false){
                   
                    allMensajes.add(recibido);
                    if((recibido.isIsNew()==true)&&(!recibido.getNombre().equals(this.userName))){
                        list.removeAll(list);
                        Button usuario = new Button(recibido.getNombre());
                        
                        System.out.println("DESTINO.."+recibido.getNombre()+"..Destino.."+dp.getAddress().getHostAddress()+"..Destino.."+recibido.getFecha()+"--YO--"+userName+"--YO--"+"127.0.0.1"+"--YO--"+dateUserName);
                        
                        usuario.setOnAction(new EventHandler<ActionEvent>() {
                            @Override 
                            public void handle(ActionEvent e) {
                                try {
                                    Parent root = FXMLLoader.load(getClass().getResource("FXMLMensajePrivado.fxml"));
                                    Scene mensajePrivado = new Scene(root);
                                    Stage escenario = new Stage();
                                    escenario.setScene(mensajePrivado);
                                    escenario.setTitle("Mensaje privado a: "+recibido.getNombre());
                                    Usuario usuario = new Usuario(recibido.getNombre(),dp.getAddress().getHostAddress(), recibido.getFecha(), userName, "127.0.0.1", dateUserName, we);
                                    escenario.setUserData(usuario);
                                    escenario.show();
                                } catch (IOException ex) {
                                    System.err.println("ERROR en el button \n");
                                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        });
                        Platform.runLater(() -> list.addAll(usuario));
                        Platform.runLater(() -> listViewContactos.getItems().addAll(list));
                        Platform.runLater(() -> we.executeScript("var para = document.createElement(\"p\"); para.innerText = \""+Utils.formatDate(recibido.getFecha())+":: ¡Se ha unido "+recibido.getNombre()+" al chat!\";document.body.appendChild(para);"));  
                        ArrayList<String> sinEmoji = new ArrayList<String>();
                        Mensaje newUser = new Mensaje(userName, dateUserName, sinEmoji, false, false, "false",true, "null", new Date());
                        int i = 0;
                        while((i++)<250){
                           try {
                               MulticastSocket msa = new MulticastSocket();
                               msa.setReuseAddress(true);
                               msa.setTimeToLive(255);
                               InetAddress ia2 = InetAddress.getByName("230.0.0.1");
                               msa.joinGroup(ia2);
                               ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                               ObjectOutputStream oos2 = new ObjectOutputStream(baos2);
                               oos2.writeObject(newUser);
                               DatagramPacket dp2 = new DatagramPacket(baos2.toByteArray(),baos2.toByteArray().length,ia2,1024);
                               msa.send(dp2);
                               msa.leaveGroup(ia2);
                               msa.close();
                           } catch (IOException ex) {
                             
                           }
                            System.out.println("Reconocimiento "+i+".");
                        }
                        System.out.println("Se ha unido "+recibido.getNombre()+" a la sala...");
                    }else if(recibido.getNombreDestino().equals(this.userName) && recibido.isIsPrivate() && recibido.isImagen()){
                        System.out.println("Se esta recibiendo una imagen privada de "+recibido.getNombre());
                        (new ChatFileDowloader( we, recibido.getNombre(), recibido.getFecha(), false, new File("E:\\"), true)).start();
                    }else if(!recibido.getNombre().equals(this.userName) && recibido.isImagen() && !recibido.isIsPrivate()){
                        System.out.println("Se esta recibiendo una imagen de "+recibido.getNombre());
                        (new ChatFileDowloader( we, recibido.getNombre(), recibido.getFecha(), false, new File("E:\\"), false)).start();
                    }else if(recibido.getNombreDestino().equals(this.userName) && recibido.isIsPrivate() && recibido.getLongEmoji()!=0){
                        System.out.println("Se ha recibido un mensaje de "+recibido.getNombre()+"::"+recibido.getArray());
                        Platform.runLater(() -> we.executeScript("var salto = document.createElement(\"br\"); document.body.appendChild(salto);"));
                        Platform.runLater(() -> we.executeScript("var salto = document.createElement(\"br\"); document.body.appendChild(salto);"));
                        Platform.runLater(() -> we.executeScript("var para = document.createElement(\"p\"); para.style.display= \"inline\"; para.innerText = \"Privado::"+Utils.formatDate(recibido.getFecha())+"::"+recibido.getNombre()+"::"+"\";document.body.appendChild(para);"));
                        
                        boolean isEmoji = false;
                        String sentenciaAnidada = "";
                        int contadorCadena = 0;
                        String mensaje = recibido.getArray();
                         for (int i = 0; i < recibido.getLongEmoji(); i++) {
                            for(;contadorCadena<recibido.getArray().length();){
                                if(recibido.getArray().charAt(contadorCadena)==("<").charAt(0)){
                                    sentenciaAnidada = sentenciaAnidada + recibido.getArray().charAt(contadorCadena);
                                    contadorCadena++;
                                    if(recibido.getArray().charAt(contadorCadena)==("<").charAt(0)){
                                        sentenciaAnidada = sentenciaAnidada + recibido.getArray().charAt(contadorCadena);
                                        contadorCadena++;
                                        if(recibido.getArray().charAt(contadorCadena)==("e").charAt(0)){
                                        sentenciaAnidada = sentenciaAnidada + recibido.getArray().charAt(contadorCadena);
                                        contadorCadena++;
                                            if(recibido.getArray().charAt(contadorCadena)==("m").charAt(0)){
                                                sentenciaAnidada = sentenciaAnidada + recibido.getArray().charAt(contadorCadena);
                                                contadorCadena++;
                                                if((recibido.getArray().charAt(contadorCadena)==("1").charAt(0))||(recibido.getArray().charAt(contadorCadena)==("2").charAt(0))||(recibido.getArray().charAt(contadorCadena)==("3").charAt(0))||(recibido.getArray().charAt(contadorCadena)==("4").charAt(0))){
                                                    sentenciaAnidada = sentenciaAnidada + recibido.getArray().charAt(contadorCadena);
                                                    contadorCadena++;
                                                    if(recibido.getArray().charAt(contadorCadena)==(">").charAt(0)){
                                                        sentenciaAnidada = sentenciaAnidada + recibido.getArray().charAt(contadorCadena);
                                                        contadorCadena++;
                                                        if(recibido.getArray().charAt(contadorCadena)==(">").charAt(0)){
                                                            sentenciaAnidada = "";
                                                            final String a = mensaje;
                                                            mensaje = "";
                                                            Platform.runLater(() -> we.executeScript("var para = document.createElement(\"p\"); para.style.display = \"inline\"; para.innerText = \""+a+"\";document.body.appendChild(para);"));  
                                                            contadorCadena++;
                                                            if(recibido.getArray().charAt(contadorCadena-3)==("1").charAt(0)){
                                                                String sentencia = "var imag = document.createElement(\"img\"); imag.style.display = \"inline\"; imag.setAttribute(\"src\",\"file:/E:/contenido/Practica3RC/src/practica3rc/emoticon"+"1"+".jpg\"); imag.setAttribute(\"width\",\"30\"); imag.setAttribute(\"height\",\"30\"); document.body.appendChild(imag);";
                                                                Platform.runLater(() -> we.executeScript(sentencia));
                                                            }else if(recibido.getArray().charAt(contadorCadena-3)==("2").charAt(0)){
                                                                String sentencia = "var imag = document.createElement(\"img\"); imag.style.display = \"inline\"; imag.setAttribute(\"src\",\"file:/E:/contenido/Practica3RC/src/practica3rc/emoticon"+"2"+".jpg\"); imag.setAttribute(\"width\",\"30\"); imag.setAttribute(\"height\",\"30\"); document.body.appendChild(imag);";
                                                                Platform.runLater(() -> we.executeScript(sentencia));
                                                            }else if(recibido.getArray().charAt(contadorCadena-3)==("3").charAt(0)){
                                                                String sentencia = "var imag = document.createElement(\"img\"); imag.style.display = \"inline\"; imag.setAttribute(\"src\",\"file:/E:/contenido/Practica3RC/src/practica3rc/emoticon"+"3"+".jpg\"); imag.setAttribute(\"width\",\"30\"); imag.setAttribute(\"height\",\"30\"); document.body.appendChild(imag);";
                                                                Platform.runLater(() -> we.executeScript(sentencia));
                                                            }else if(recibido.getArray().charAt(contadorCadena-3)==("4").charAt(0)){
                                                                String sentencia = "var imag = document.createElement(\"img\"); imag.style.display = \"inline\"; imag.setAttribute(\"src\",\"file:/E:/contenido/Practica3RC/src/practica3rc/emoticon"+"4"+".jpg\"); imag.setAttribute(\"width\",\"30\"); imag.setAttribute(\"height\",\"30\"); document.body.appendChild(imag);";
                                                                Platform.runLater(() -> we.executeScript(sentencia));
                                                            }
                                                           }else{
                                                            sentenciaAnidada = sentenciaAnidada + recibido.getArray().charAt(contadorCadena);
                                                            contadorCadena++;
                                                        }
                                                    }else{
                                                        sentenciaAnidada = sentenciaAnidada + recibido.getArray().charAt(contadorCadena);
                                                        contadorCadena++;
                                                    }
                                                }else{
                                                    sentenciaAnidada = sentenciaAnidada + recibido.getArray().charAt(contadorCadena);
                                                    contadorCadena++;
                                                }
                                            }else{
                                                sentenciaAnidada = sentenciaAnidada + recibido.getArray().charAt(contadorCadena);
                                                contadorCadena++;
                                            }
                                        }else{
                                            sentenciaAnidada = sentenciaAnidada + recibido.getArray().charAt(contadorCadena);
                                            contadorCadena++;
                                        }
                                    }else{
                                        sentenciaAnidada = sentenciaAnidada + recibido.getArray().charAt(contadorCadena);
                                        contadorCadena++;
                                    }
                                    mensaje = mensaje + sentenciaAnidada;
                                }else{
                                    mensaje = mensaje + recibido.getArray().charAt(contadorCadena);
                                    contadorCadena++;
                                }
                            }
                            final String a = mensaje;
                            Platform.runLater(() -> we.executeScript("var para = document.createElement(\"p\"); para.style.display = \"inline\"; para.innerText = \""+a+"\";document.body.appendChild(para);"));  
                            if(contadorCadena>=recibido.getArray().length()){
                                i = recibido.getLongEmoji();
                            }
                        }
                    }else if((!recibido.getNombre().equals(this.userName))&&(recibido.getLongEmoji()!=0)&&(!recibido.isIsPrivate()==true)){
                        System.out.println("Se ha recibido un mensaje de "+recibido.getNombre()+"::"+recibido.getArray());
                        Platform.runLater(() -> we.executeScript("var salto = document.createElement(\"br\"); document.body.appendChild(salto);"));
                        Platform.runLater(() -> we.executeScript("var salto = document.createElement(\"br\"); document.body.appendChild(salto);"));
                        Platform.runLater(() -> we.executeScript("var para = document.createElement(\"p\"); para.style.display= \"inline\"; para.innerText = \""+(recibido.getFecha().toString())+"::"+recibido.getNombre()+"::"+"\";document.body.appendChild(para);"));
                        String mensaje = "";
                        boolean isEmoji = false;
                        String sentenciaAnidada = "";
                        int contadorCadena = 0;
                         for (int i = 0; i < recibido.getLongEmoji(); i++) {
                            for(;contadorCadena<recibido.getArray().length();){
                                if(recibido.getArray().charAt(contadorCadena)==("<").charAt(0)){
                                    sentenciaAnidada = sentenciaAnidada + recibido.getArray().charAt(contadorCadena);
                                    contadorCadena++;
                                    if(recibido.getArray().charAt(contadorCadena)==("<").charAt(0)){
                                        sentenciaAnidada = sentenciaAnidada + recibido.getArray().charAt(contadorCadena);
                                        contadorCadena++;
                                        if(recibido.getArray().charAt(contadorCadena)==("e").charAt(0)){
                                        sentenciaAnidada = sentenciaAnidada + recibido.getArray().charAt(contadorCadena);
                                        contadorCadena++;
                                            if(recibido.getArray().charAt(contadorCadena)==("m").charAt(0)){
                                                sentenciaAnidada = sentenciaAnidada + recibido.getArray().charAt(contadorCadena);
                                                contadorCadena++;
                                                if((recibido.getArray().charAt(contadorCadena)==("1").charAt(0))||(recibido.getArray().charAt(contadorCadena)==("2").charAt(0))||(recibido.getArray().charAt(contadorCadena)==("3").charAt(0))||(recibido.getArray().charAt(contadorCadena)==("4").charAt(0))){
                                                    sentenciaAnidada = sentenciaAnidada + recibido.getArray().charAt(contadorCadena);
                                                    contadorCadena++;
                                                    if(recibido.getArray().charAt(contadorCadena)==(">").charAt(0)){
                                                        sentenciaAnidada = sentenciaAnidada + recibido.getArray().charAt(contadorCadena);
                                                        contadorCadena++;
                                                        if(recibido.getArray().charAt(contadorCadena)==(">").charAt(0)){
                                                            sentenciaAnidada = "";
                                                            final String a = mensaje;
                                                            mensaje = "";
                                                            Platform.runLater(() -> we.executeScript("var para = document.createElement(\"p\"); para.style.display = \"inline\"; para.innerText = \""+a+"\";document.body.appendChild(para);"));  
                                                            contadorCadena++;
                                                            if(recibido.getArray().charAt(contadorCadena-3)==("1").charAt(0)){
                                                                String sentencia = "var imag = document.createElement(\"img\"); imag.style.display = \"inline\"; imag.setAttribute(\"src\",\"file:/E:/contenido/Practica3RC/src/practica3rc/emoticon"+"1"+".jpg\"); imag.setAttribute(\"width\",\"30\"); imag.setAttribute(\"height\",\"30\"); document.body.appendChild(imag);";
                                                                Platform.runLater(() -> we.executeScript(sentencia));
                                                            }else if(recibido.getArray().charAt(contadorCadena-3)==("2").charAt(0)){
                                                                String sentencia = "var imag = document.createElement(\"img\"); imag.style.display = \"inline\"; imag.setAttribute(\"src\",\"file:/E:/contenido/Practica3RC/src/practica3rc/emoticon"+"2"+".jpg\"); imag.setAttribute(\"width\",\"30\"); imag.setAttribute(\"height\",\"30\"); document.body.appendChild(imag);";
                                                                Platform.runLater(() -> we.executeScript(sentencia));
                                                            }else if(recibido.getArray().charAt(contadorCadena-3)==("3").charAt(0)){
                                                                String sentencia = "var imag = document.createElement(\"img\"); imag.style.display = \"inline\"; imag.setAttribute(\"src\",\"file:/E:/contenido/Practica3RC/src/practica3rc/emoticon"+"3"+".jpg\"); imag.setAttribute(\"width\",\"30\"); imag.setAttribute(\"height\",\"30\"); document.body.appendChild(imag);";
                                                                Platform.runLater(() -> we.executeScript(sentencia));
                                                            }else if(recibido.getArray().charAt(contadorCadena-3)==("4").charAt(0)){
                                                                String sentencia = "var imag = document.createElement(\"img\"); imag.style.display = \"inline\"; imag.setAttribute(\"src\",\"file:/E:/contenido/Practica3RC/src/practica3rc/emoticon"+"4"+".jpg\"); imag.setAttribute(\"width\",\"30\"); imag.setAttribute(\"height\",\"30\"); document.body.appendChild(imag);";
                                                                Platform.runLater(() -> we.executeScript(sentencia));
                                                            }
                                                           }else{
                                                            sentenciaAnidada = sentenciaAnidada + recibido.getArray().charAt(contadorCadena);
                                                            contadorCadena++;
                                                        }
                                                    }else{
                                                        sentenciaAnidada = sentenciaAnidada + recibido.getArray().charAt(contadorCadena);
                                                        contadorCadena++;
                                                    }
                                                }else{
                                                    sentenciaAnidada = sentenciaAnidada + recibido.getArray().charAt(contadorCadena);
                                                    contadorCadena++;
                                                }
                                            }else{
                                                sentenciaAnidada = sentenciaAnidada + recibido.getArray().charAt(contadorCadena);
                                                contadorCadena++;
                                            }
                                        }else{
                                            sentenciaAnidada = sentenciaAnidada + recibido.getArray().charAt(contadorCadena);
                                            contadorCadena++;
                                        }
                                    }else{
                                        sentenciaAnidada = sentenciaAnidada + recibido.getArray().charAt(contadorCadena);
                                        contadorCadena++;
                                    }
                                    mensaje = mensaje + sentenciaAnidada;
                                }else{
                                    mensaje = mensaje + recibido.getArray().charAt(contadorCadena);
                                    contadorCadena++;
                                }
                            }
                            final String a = mensaje;
                            Platform.runLater(() -> we.executeScript("var para = document.createElement(\"p\"); para.style.display = \"inline\"; para.innerText = \""+a+"\";document.body.appendChild(para);"));  
                            if(contadorCadena>=recibido.getArray().length()){
                                i = recibido.getLongEmoji();
                            }
                        }
                    }else if((recibido.getNombreDestino().equals(this.userName))&&(recibido.isIsPrivate()==true)){
                        Platform.runLater(() -> we.executeScript("var salto = document.createElement(\"br\"); document.body.appendChild(salto);"));
                        Platform.runLater(() -> we.executeScript("var salto = document.createElement(\"br\"); document.body.appendChild(salto);"));
                        Platform.runLater(() -> we.executeScript("var para = document.createElement(\"p\"); para.style.display = \"inline\"; para.innerText = \"Privado::"+(recibido.getFecha().toString())+"::"+recibido.getNombre()+"::"+recibido.getArray()+"\";document.body.appendChild(para);"));  
                    }else if((!recibido.getNombre().equals(this.userName))&&(!recibido.isIsPrivate()==true)){
                        Platform.runLater(() -> we.executeScript("var salto = document.createElement(\"br\"); document.body.appendChild(salto);"));
                        Platform.runLater(() -> we.executeScript("var salto = document.createElement(\"br\"); document.body.appendChild(salto);"));
                        Platform.runLater(() -> we.executeScript("var para = document.createElement(\"p\"); para.style.display = \"inline\"; para.innerText = \""+(recibido.getFecha().toString())+"::"+recibido.getNombre()+"::"+recibido.getArray()+"\";document.body.appendChild(para);"));  
                    }
                }
                repetido = false;
                ms.leaveGroup(ia);
                ms.close();
            } catch (Exception ex) {
            }
        }
    }
}
