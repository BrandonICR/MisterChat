package mx.com.brandonicr.chat.control;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

import javafx.application.Platform;
import javafx.scene.web.WebEngine;
import mx.com.brandonicr.chat.common.dto.Archivo;

public class ChatFileDowloader extends Thread{
    
    private WebEngine we;
    private String userName;
    private Date dateUserName;
    private boolean isEnviar;
    private File f;
    private boolean isPrivado;
    
    public ChatFileDowloader( WebEngine we, String userName, Date dateUserName, boolean isEnviar, File archivo, boolean isPrivado){
        this.we = we;
        this.userName = userName;
        this.dateUserName = dateUserName;
        this.isEnviar = isEnviar;
        this.f = archivo;
        this.isPrivado = isPrivado;
    }

    public void recibirArchivo(){
        try {
            int posicion = 0;
            int total=0;
            
            ArrayList<Archivo> arch = new ArrayList<>();
            
            String pathIn = "R";
            
            try {
                MulticastSocket ms = new MulticastSocket(1025);
                ms.setReuseAddress(true);
                ms.setTimeToLive(255);
                InetAddress ia = InetAddress.getByName("230.0.0.1");
                ms.joinGroup(ia);            
                DatagramPacket dps = new DatagramPacket(new byte[1500],1500);
                ms.receive(dps);
                ByteArrayInputStream bais = new ByteArrayInputStream(dps.getData());
                ObjectInputStream ois = new ObjectInputStream(bais);
                Archivo acr = (Archivo)ois.readObject();
                posicion = acr.getOrden();
                arch.add(acr);

                //System.out.println("Se ha recibido "+0+"% del archivo "+(acr.getNombre())+", parte :"+(acr.getOrden()));

                ois.close();
                bais.close();
                ms.close();
                
                MulticastSocket ms2 = new MulticastSocket(1025);
                ms2.setReuseAddress(true);
                ms2.setTimeToLive(255);
                ms2.joinGroup(ia);      
                long tam = acr.getTamano();int enviados = 0, promedio = 0;
                boolean bandera=true;
                while(bandera){
                    DatagramPacket dps2 = new DatagramPacket(new byte[1500],1500);
                    ms2.receive(dps2);
                    ByteArrayInputStream bais2 = new ByteArrayInputStream(dps2.getData(),0,dps2.getLength());
                    ObjectInputStream ois2 = new ObjectInputStream(bais2);
                    Archivo a2 = (Archivo)ois2.readObject();
                    posicion = a2.getOrden();
                    boolean archExiste=false;
                    boolean esUltimo=false;

                    for (Archivo aux : arch){
                        if((aux.getOrden()==posicion)&&(!a2.getUltimo()))
                            archExiste=true;
                        else if((aux.getOrden()==posicion)&&(a2.getUltimo())){
                            archExiste=false; //Caso en que sea el ultimo y tambien que ya exista en la lista
                            esUltimo=true;
                        }
                    }

                    if(archExiste == false){
                        if(esUltimo==false){
                            arch.add(a2);
                            int n = a2.getEnviadoTam();
                            enviados = enviados + n;
                            promedio = (int)((enviados * 100)/tam);
                        }
                        if(a2.getUltimo()){
                            total = a2.getOrden()+1;
                        }
                    }

                    promedio = (arch.size()*100)/(arch.get(0).getNumFragmentos());

                    System.out.println("_______ Tenemos " + promedio + "% archivos.______"+ a2.getNumFragmentos()+"__ parte:"+a2.getOrden());

                    if(arch.size()>= (arch.get(0).getNumFragmentos())){
                        bandera=false;
                    }else{
                        boolean res = true;
                    }
                    ois2.close();
                    bais2.close();
                }
                ms2.close();
                
                File general = new File(pathIn + arch.get(0).getNombre());
                general.setWritable(true);
                general.setReadable(true);
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(general,true));
                for(int i=0;i<=arch.size();i++){
                    for(Archivo aux : arch){
                        //System.out.print("\n Comparamos:"+aux.getOrden()+"=="+i+" :");
                        if(aux.getOrden()==i){
                            //System.out.print("Si");
                            dos.write(aux.getArch(), 0, (int)(aux.getEnviadoTam()));
                            dos.flush();
                        }//else
                            //System.out.print("No");
                    }
                }
                
                System.out.println("Se ha recibido el archivo completo..."+general.getName());
                
                Platform.runLater(() -> we.executeScript("var salto = document.createElement(\"br\"); document.body.appendChild(salto);"));
                Platform.runLater(() -> we.executeScript("var salto = document.createElement(\"br\"); document.body.appendChild(salto);"));
                Platform.runLater(() -> we.executeScript("var para = document.createElement(\"p\"); para.style.display = \"inline\"; para.innerText = \""+((isPrivado == true) ? "Privado::" : "" )+(dateUserName)+"::"+userName+"::"+"\";document.body.appendChild(para);"));
                Platform.runLater(() -> we.executeScript("var salto = document.createElement(\"br\"); document.body.appendChild(salto);"));
                String sentencia = "var imag = document.createElement(\"img\"); imag.setAttribute(\"align\",\"middle\"); imag.setAttribute(\"src\",\"file:/E:/contenido/Practica3RC/"+general.getName()+"\"); imag.setAttribute(\"width\",\"250\"); imag.setAttribute(\"height\",\"250\"); document.body.appendChild(imag);";
                Platform.runLater(() -> we.executeScript(sentencia));
            
                System.out.println("Se ha mostrado la imagen en la pantalla..."+general.getName());
                dos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
    public void enviarArchivo(){
        try {
            f.setReadable(true);
            f.setWritable(true);
            MulticastSocket s  = new MulticastSocket(1025);
            s.setReuseAddress(true);
            s.setTimeToLive(255);
            InetAddress ia = InetAddress.getByName("230.0.0.1");
            s.joinGroup(ia);
            int n = 0;
            int kk = 0;
            while(kk < 200){
                DataInputStream disF = new DataInputStream(new FileInputStream(f.getAbsolutePath()));
                long tamano = f.length();
                String nombre = f.getName();
                int numPartes;
                boolean ultimo = false;
                if((tamano%1303)==0)
                    numPartes = (int)(tamano / 1303);
                else
                    numPartes = (int)((tamano / 1303) + 1);

                int partes = 0, leidos = 0, promedio = 0;
                while(partes < numPartes){
                    byte []b = new byte[1303];
                    n = disF.read(b);
                    leidos = leidos + n;
                    promedio = (int)((leidos*100)/tamano);

                    if(partes == numPartes)
                        ultimo = true;
                    else
                        ultimo = false;

                    Archivo archivo = new Archivo(b, nombre, partes, tamano, n, ultimo, numPartes);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(archivo);
                    oos.flush();

                    DatagramPacket dp = new DatagramPacket(baos.toByteArray(),0,baos.toByteArray().length,ia,1025);

                    s.send(dp);

                    //System.out.println("Se ha enviado el _"+promedio+"%_ del archivo _"+nombre+"_ en la parte _"+partes+"_ de tamano _"+baos.toByteArray().length);

                    oos.close();
                    baos.close();

                    partes++;
                }
                disF.close();
                System.out.println("Se ha enviado, vez"+kk);
                kk++;
            }
            s.close();
            System.out.println("Se ha enviado el archivo..."+f.getName());
        } catch (IOException ex) {
            Logger.getLogger(ChatFileDowloader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void run(){
        if(this.isEnviar)
            enviarArchivo();
        else
            recibirArchivo();
    }
}
