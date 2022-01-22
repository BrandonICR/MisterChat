package mx.com.brandonicr.chat;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import mx.com.brandonicr.chat.common.constants.FilePaths;
import mx.com.brandonicr.chat.common.constants.SpecialCharacterConstants;
import mx.com.brandonicr.chat.common.dto.Mensaje;
import mx.com.brandonicr.chat.control.ChatFileDowloader;
import mx.com.brandonicr.chat.control.ChatListener;

import javax.swing.JFileChooser;

/**
 * @author BrandonICR
 */
public class FXMLDocumentController implements Initializable {
   
    @FXML
    private WebView webView;
    @FXML
    private TextField textFieldEnviar;
    @FXML
    private ListView<Button> listViewContactos;
    @FXML
    private Label labelMostrar;
    @FXML
    private Button buttonEmoticon1;
    @FXML
    private Button buttonEmoticon2;
    @FXML
    private Button buttonEmoticon3;
    @FXML
    private Button buttonEmoticon4;    
    @FXML
    private Button buttonAdjuntar;
    @FXML
    private Button buttonAgregar;
    @FXML
    private AnchorPane anchorPaneInicio;
    @FXML
    public Button buttonActivo;
    @FXML
    private TextField textFieldUsername;
    
    private ObservableList<Button> list = FXCollections.observableArrayList();
    private WebEngine webEngine;
    private String userName;
    private Date dateUserName;
    private ArrayList<String> emojiNames;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        list.removeAll(list);
        
        emojiNames = new ArrayList<>();
        
        webEngine = webView.getEngine();
        webEngine.setJavaScriptEnabled(true);
        
        webView.setDisable(true);
        textFieldEnviar.setDisable(true);
        listViewContactos.setDisable(true);
        labelMostrar.setDisable(true);
        buttonEmoticon1.setDisable(true);
        buttonEmoticon2.setDisable(true);
        buttonEmoticon3.setDisable(true);
        buttonEmoticon4.setDisable(true);
        buttonAdjuntar.setDisable(true);
        buttonAgregar.setDisable(true);

        try(BufferedReader fileContentChat = new BufferedReader(new FileReader(new File(FilePaths.rootPath.concat(FilePaths.fileChatPath))));){
            String contentChat = fileContentChat.lines().reduce(SpecialCharacterConstants.STR_EMPTY, (line1, line2) -> line1.concat(SpecialCharacterConstants.STR_SPACE).concat(line2));
            webEngine.loadContent(contentChat);
        }catch(Exception e){
            System.out.println("Error: file 'static/templates/chat.html' couldn't be loaded");
            Platform.exit();
        }
      
    }    
    
    @FXML
    private void agregarTexto(ActionEvent e){
        if(!textFieldEnviar.getText().trim().isEmpty()){
            webEngine.executeScript("var para = document.createElement(\"p\"); para.innerText = \""+"Yo"+"::"+textFieldEnviar.getText().trim()+"\";document.body.appendChild(para);");
            Mensaje archivoU = new Mensaje(userName, new Date(), emojiNames, false, false, textFieldEnviar.getText().trim(),false,"null",new Date());
            int i = 0;
            while((i++)<20){
                try {
                    MulticastSocket msa = new MulticastSocket();
                    msa.setReuseAddress(true);
                    msa.setTimeToLive(255);
                    InetAddress ia = InetAddress.getByName("230.0.0.1");
                    msa.joinGroup(ia);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(archivoU);
                    DatagramPacket dp = new DatagramPacket(baos.toByteArray(),baos.toByteArray().length,ia,1024);
                    msa.send(dp);
                    msa.leaveGroup(ia);
                    msa.close();
                } catch (IOException ex) {
                    //Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            textFieldEnviar.clear();
            System.out.println("Se ha mandado un mensaje::"+textFieldEnviar.getText().trim());
        }
        textFieldEnviar.clear();
        System.out.println("Se han eliminado los emojiNamess...");
        if(emojiNames.size()>0){
            emojiNames.removeAll(emojiNames);
        }
    }
    
    @FXML
    private void presionarIcono1(ActionEvent e){
        System.out.println("Se ha enviado el emoticon <<OgroRojo>>");
        textFieldEnviar.appendText("<<em1>>");
        //webEngine.executeScript("var imag = document.createElement(\"img\"); imag.setAttribute(\"src\",\"file:/D:/BRAND/Documentos/ESCOM/5toSemestre/AplicacionesParaComunicacionesDeRed/3CM7BrandonFernando/2doCorte/Practicas/Chat/src/practica3rc/emoticon1.jpg\"); imag.setAttribute(\"width\",\"79\"); imag.setAttribute(\"height\",\"79\"); document.body.appendChild(imag);");     
        emojiNames.add("<<em1>>");
    }
    
        @FXML
    private void presionarIcono2(ActionEvent e){
        System.out.println("Se ha enviado el emoticon <<Fantasma>>");
        textFieldEnviar.appendText(" <<em2>> ");
        //webEngine.executeScript("var imag = document.createElement(\"img\"); imag.setAttribute(\"src\",\"file:/D:/BRAND/Documentos/ESCOM/5toSemestre/AplicacionesParaComunicacionesDeRed/3CM7BrandonFernando/2doCorte/Practicas/Chat/src/practica3rc/emoticon2.jpg\"); imag.setAttribute(\"width\",\"79\"); imag.setAttribute(\"height\",\"79\"); document.body.appendChild(imag);");     
        emojiNames.add("<<em2>>");
    }
    
        @FXML
    private void presionarIcono3(ActionEvent e){
        System.out.println("Se ha enviado el emoticon <<Demonio>>");
        textFieldEnviar.appendText(" <<em3>> ");
        //webEngine.executeScript("var imag = document.createElement(\"img\"); imag.setAttribute(\"src\",\"file:/D:/BRAND/Documentos/ESCOM/5toSemestre/AplicacionesParaComunicacionesDeRed/3CM7BrandonFernando/2doCorte/Practicas/Chat/src/practica3rc/emoticon3.jpg\"); imag.setAttribute(\"width\",\"79\"); imag.setAttribute(\"height\",\"79\"); document.body.appendChild(imag);");     
        emojiNames.add("<<em3>>");
    }
    
        @FXML
    private void presionarIcono4(ActionEvent e){
        System.out.println("Se ha enviado el emoticon <<Alien>>");
        textFieldEnviar.appendText(" <<em4>> ");
        //E.executeScript("var imag = document.createElement(\"img\"); imag.setAttribute(\"src\",\"file:/D:/BRAND/Documentos/ESCOM/5toSemestre/AplicacionesParaComunicacionesDeRed/3CM7BrandonFernando/2doCorte/Practicas/Chat/src/practica3rc/emoticon4.jpg\"); imag.setAttribute(\"width\",\"79\"); imag.setAttribute(\"height\",\"79\"); document.body.appendChild(imag);");     
        emojiNames.add("<<em4>>");
    }
    
    @FXML
    private void adjuntarArchivo(ActionEvent e){            
        JFileChooser fc = new JFileChooser();
        int val = fc.showOpenDialog(null);
        if(val == JFileChooser.APPROVE_OPTION){
            File f = fc.getSelectedFile();
            webEngine.executeScript("var salto = document.createElement(\"br\"); document.body.appendChild(salto);");
            String sentencia = "var imag = document.createElement(\"img\"); imag.setAttribute(\"align\",\"middle\"); imag.setAttribute(\"src\",\"file:/E:/contenido/Practica3RC/"+f.getName()+"\"); imag.setAttribute(\"width\",\"250\"); imag.setAttribute(\"height\",\"250\"); document.body.appendChild(imag);";
            webEngine.executeScript(sentencia);
            
            ArrayList<String> vacio = new ArrayList<>();
            Mensaje archivoU = new Mensaje(userName, new Date(), vacio, false, true, "nada",false,"null",new Date());
            int i = 0;
            while((i++)<20){
                try {
                    MulticastSocket msa = new MulticastSocket();
                    msa.setReuseAddress(true);
                    msa.setTimeToLive(255);
                    InetAddress ia = InetAddress.getByName("230.0.0.1");
                    msa.joinGroup(ia);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(archivoU);
                    DatagramPacket dp = new DatagramPacket(baos.toByteArray(),baos.toByteArray().length,ia,1024);
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
            (new ChatFileDowloader( webEngine, userName, new Date(), true, f, false)).start();
            System.out.println("Se ha enviado el archivo:"+f.getName());
        }
    }
    
    @FXML
    private void activar(ActionEvent e) throws InterruptedException{
        if(!textFieldUsername.getText().trim().isEmpty()){

            String username = textFieldUsername.getText();
            userName = username;
            dateUserName = new Date();

            System.out.println("Soy <"+userName+">");

            Mensaje newUser = new Mensaje(userName, dateUserName, emojiNames, false, false, "nada", true, "null", new Date());
            int i = 0;
            while((i++)<20){
               try {
                   MulticastSocket msa = new MulticastSocket();
                   msa.setReuseAddress(true);
                   msa.setTimeToLive(255);
                   InetAddress ia = InetAddress.getByName("230.0.0.1");
                   msa.joinGroup(ia);
                   ByteArrayOutputStream baos = new ByteArrayOutputStream();
                   ObjectOutputStream oos = new ObjectOutputStream(baos);
                   oos.writeObject(newUser);
                   DatagramPacket dp = new DatagramPacket(baos.toByteArray(),baos.toByteArray().length,ia,1024);
                   msa.send(dp);
                   msa.leaveGroup(ia);
                   msa.close();
               } catch (IOException ex) {
                   //Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
               }
            }

            ChatListener lector1 = new ChatListener(webEngine,listViewContactos,list,userName,dateUserName);
            lector1.start();


            webView.setDisable(false);
            textFieldEnviar.setDisable(false);
            listViewContactos.setDisable(false);
            labelMostrar.setDisable(false);
            buttonEmoticon1.setDisable(false);
            buttonEmoticon2.setDisable(false);
            buttonEmoticon3.setDisable(false);
            buttonEmoticon4.setDisable(false);
            buttonAdjuntar.setDisable(false);
            buttonAgregar.setDisable(false);

            buttonActivo.setStyle("fx-background-color: #ff0f00");
            buttonActivo.setVisible(false);
            textFieldUsername.setVisible(false);
            buttonActivo.setDisable(true);
            textFieldUsername.setDisable(true);

            labelMostrar.setText("En la sala "+username+"\nIP:"+"17.0.0.1");
            labelMostrar.setFont(new Font(12));
            listViewContactos.setPrefHeight(535);
        }else{
            textFieldUsername.clear();
            textFieldUsername.setPromptText("Nombre de usuario no valido...");
        }
    }
}