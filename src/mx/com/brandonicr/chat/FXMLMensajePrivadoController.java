package mx.com.brandonicr.chat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import mx.com.brandonicr.chat.common.dto.Mensaje;
import mx.com.brandonicr.chat.common.dto.Usuario;
import mx.com.brandonicr.chat.control.ChatFileDowloader;

import javax.swing.JFileChooser;

/**
 * @author BrandonICR
 */
public class FXMLMensajePrivadoController implements Initializable {
    @FXML
    private Button buttonOgroPrivado;
    @FXML
    private Button buttonFantasmaPrivado;
    @FXML
    private Button buttonDemonioPrivado;
    @FXML
    private Button buttonAlienPrivado;
    @FXML
    private Button buttonEnviarPrivado;
    @FXML
    private Button buttonAdjuntarPrivado;
    @FXML
    private TextField textFieldTextoPrivado;
    @FXML
    private Label labelIpUsuario;
    @FXML
    private AnchorPane anchorPanePrivado;
    
    
    private String nombreUsuario;
    private Date fechaNombreUsuario;
    private String dirIP;
    private String nombreUsuarioDestino;
    private Date fechaNombreUsuarioDestino;
    private String dirIPDestino;
    private ArrayList<String> emoji;
    
    private Stage stagePrivado;
    private WebEngine we;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        emoji = new ArrayList<>();
    }
    
    @FXML
    public void enviarPrivado(ActionEvent e){
        stagePrivado = ((Stage)anchorPanePrivado.getScene().getWindow());
        nombreUsuario = ((Usuario)stagePrivado.getUserData()).getMiNombreUsuario();
        fechaNombreUsuario = ((Usuario)stagePrivado.getUserData()).getMiFecha();
        dirIP = ((Usuario)stagePrivado.getUserData()).getMiDirIP();
        nombreUsuarioDestino = ((Usuario)stagePrivado.getUserData()).getNombreUsuario();
        fechaNombreUsuarioDestino = ((Usuario)stagePrivado.getUserData()).getFecha();
        dirIPDestino = ((Usuario)stagePrivado.getUserData()).getDirIP();
        we = ((Usuario)stagePrivado.getUserData()).getWebView();
        
        System.out.println(nombreUsuarioDestino);
        System.out.println(fechaNombreUsuario);
        System.out.println(dirIP);
        System.out.println(nombreUsuarioDestino);
        System.out.println(fechaNombreUsuarioDestino);
        System.out.println(dirIPDestino);
        
        if(!textFieldTextoPrivado.getText().trim().isEmpty()){
            we.executeScript("var para = document.createElement(\"p\"); para.innerText = \""+"Yo"+"::"+textFieldTextoPrivado.getText().trim()+"\";document.body.appendChild(para);");
            Mensaje archivoU = new Mensaje(nombreUsuario, new Date(), emoji, true, false, textFieldTextoPrivado.getText().trim(),false,nombreUsuarioDestino,fechaNombreUsuarioDestino);
            int i = 0;
            while((i++)<20){
            try {
                MulticastSocket msa = new MulticastSocket();
                msa.setReuseAddress(true);
                msa.setTimeToLive(255);
                InetAddress ia = InetAddress.getByName("230.0.0.1");
                msa.joinGroup(ia);
                InetAddress iaD = InetAddress.getByName(dirIPDestino);
                msa.connect(iaD, 1024);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(archivoU);
                DatagramPacket dp = new DatagramPacket(baos.toByteArray(),baos.toByteArray().length,ia,1024);
                msa.disconnect();
                msa.send(dp);
                msa.leaveGroup(ia);
                msa.close();
            } catch (IOException ex) {
                //Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
            textFieldTextoPrivado.clear();
            System.out.println("Se ha mandado un mensaje::"+textFieldTextoPrivado.getText().trim());
        }
        textFieldTextoPrivado.clear();
        System.out.println("Se han eliminado los emojis...");
        if(emoji.size()>0){
            emoji.removeAll(emoji);
        }
        
        stagePrivado.close();
    }
    
    @FXML
    public void adjuntarPrivado(ActionEvent e){            
        JFileChooser fc = new JFileChooser();
        int val = fc.showOpenDialog(null);
        if(val == JFileChooser.APPROVE_OPTION){
            File f = fc.getSelectedFile();
            /*we.executeScript("var salto = document.createElement(\"br\"); document.body.appendChild(salto);");
            String sentencia = "var imag = document.createElement(\"img\"); imag.setAttribute(\"align\",\"middle\"); imag.setAttribute(\"src\",\"file:/G:/contenido/Practica3RC/"+f.getName()+"\"); imag.setAttribute(\"width\",\"250\"); imag.setAttribute(\"height\",\"250\"); document.body.appendChild(imag);";
            we.executeScript(sentencia);
            */
            ArrayList<String> vacio = new ArrayList<>();
            Mensaje archivoU = new Mensaje(nombreUsuario, new Date(), vacio, true, true, "nada",false, nombreUsuarioDestino, new Date());
            int i = 0;
            while((i++)<20){
            try {
                MulticastSocket msa = new MulticastSocket();
                msa.setReuseAddress(true);
                msa.setTimeToLive(255);
                InetAddress ia = InetAddress.getByName("230.0.0.1");
                msa.joinGroup(ia);
                InetAddress iaD = InetAddress.getByName(dirIPDestino);
                msa.connect(iaD, 1024);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(archivoU);
                DatagramPacket dp = new DatagramPacket(baos.toByteArray(),baos.toByteArray().length,ia,1024);
                msa.close();
                msa.send(dp);
                msa.leaveGroup(ia);
                msa.close();
            } catch (IOException ex) {
                //Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
            }
            try {
                Thread.sleep(2*1000);
            } catch (InterruptedException ex) {
            }
            (new ChatFileDowloader( we, nombreUsuario, new Date(), true, f, false)).start();
            System.out.println("Se ha enviado el archivo:"+f.getName());
        }
    }
    
    @FXML
    public void ogroPrivado(ActionEvent e){
        System.out.println("Se ha enviado el emoticon <<OgroRojo>>");
        textFieldTextoPrivado.appendText("<<em1>>");
        emoji.add("<<em1>>");
    }    
    
    @FXML
    public void fantasmaPrivado(ActionEvent e){
        System.out.println("Se ha enviado el emoticon <<Fantasma>>");
        textFieldTextoPrivado.appendText(" <<em2>> ");
        emoji.add("<<em2>>");
    }
    
    @FXML
    public void demonioPrivado(ActionEvent e){
        System.out.println("Se ha enviado el emoticon <<Demonio>>");
        textFieldTextoPrivado.appendText(" <<em3>> ");
        emoji.add("<<em3>>");
    }
    
    @FXML
    public void alienPrivado(ActionEvent e){
        System.out.println("Se ha enviado el emoticon <<Alien>>");
        textFieldTextoPrivado.appendText(" <<em4>> ");
        emoji.add("<<em4>>");
    }
}
