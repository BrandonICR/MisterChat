package mx.com.brandonicr.chat.common.dto;

import java.io.Serializable;

/**
 *
 * @author BrandonICR
 */
public class Archivo implements Serializable{
    
    private byte []arch;
    private String nombre;
    private int orden;
    private long tamano;
    private int enviadoTam;
    private boolean ultimo;
    private int fragmentos;

    public Archivo(byte[] arch, String nombre, int orden, long tamano, int enviadoTam, boolean ultimo, int fragmentos) {
        this.arch = arch;
        this.nombre = nombre;
        this.orden = orden;
        this.tamano = tamano;
        this.enviadoTam = enviadoTam;
        this.ultimo = ultimo;
        this.fragmentos = fragmentos;
    }

    public byte[] getArch() {
        return arch;
    }

    public void setArch(byte[] arch) {
        this.arch = arch;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getOrden() {
        return orden;
    }

    public void setOrden(int orden) {
        this.orden = orden;
    }

    public long getTamano() {
        return tamano;
    }

    public void setTamano(long tamano) {
        this.tamano = tamano;
    }

    public int getEnviadoTam() {
        return enviadoTam;
    }

    public void setEnviadoTam(int enviadoTam) {
        this.enviadoTam = enviadoTam;
    }
    
    public boolean getUltimo() {
        return ultimo;
    }

    public void setUltimo(boolean ultimo) {
        this.ultimo = ultimo;
    }
    
    public void setNumFragmentos(int fragmentos) {
        this.fragmentos = fragmentos;
    }

    public int getNumFragmentos() {
        return fragmentos;
    }
    
}
