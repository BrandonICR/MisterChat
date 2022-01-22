package mx.com.brandonicr.chat.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author BrandonICR
 */
public class Mensaje implements Serializable {
    private String nombre;
    private Date fecha;
    private ArrayList<String> emoji;
    private boolean isPrivate;
    private boolean imagen;
    private String texto;
    private boolean isNew;
    private String nombreDestino;
    private Date fechaDestino;

    public Mensaje(String nombre, Date fecha, ArrayList<String> emoji, boolean isPrivate, boolean imagen, String texto, boolean isNew, String nombreDestino, Date fechaDestino) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.emoji = emoji;
        this.isPrivate = isPrivate;
        this.imagen = imagen;
        this.texto = texto;
        this.isNew = isNew;
        this.nombreDestino = nombreDestino;
        this.fechaDestino = fechaDestino;
    }
    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getEmoji(int i) {
        return emoji.get(i);
    }

    public void setEmoji(String emoji) {
        this.emoji.add(emoji);
    }
    
    public int getLongEmoji(){
        return this.emoji.size();
    }

    public boolean isIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public boolean isImagen() {
        return imagen;
    }

    public void setImagen(boolean imagen) {
        this.imagen = imagen;
    }

    public String getArray() {
        return texto;
    }

    public void setArray(String array) {
        this.texto = array;
    }
    
    
    public boolean isIsNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public String getNombreDestino() {
        return nombreDestino;
    }

    public void setNombreDestino(String nombreDestino) {
        this.nombreDestino = nombreDestino;
    }

    public Date getFechaDestino() {
        return fechaDestino;
    }

    public void setFechaDestino(Date fechaDestino) {
        this.fechaDestino = fechaDestino;
    }
    
    
}
