package mx.com.brandonicr.chat.common.dto;

import java.util.Date;
import javafx.scene.web.WebEngine;

/**
 *
 * @author BrandonICR
 */
public class Usuario {
    
    private String nombreUsuario;
    private String dirIP;
    private Date fecha;
    private String miNombreUsuario;
    private String miDirIP;
    private Date miFecha;
    private WebEngine webView;

    public Usuario(String nombreUsuario, String dirIP, Date fecha, String miNombreUsuario, String miDirIP, Date miFecha, WebEngine webView) {
        this.nombreUsuario = nombreUsuario;
        this.dirIP = dirIP;
        this.fecha = fecha;
        this.miNombreUsuario = miNombreUsuario;
        this.miDirIP = miDirIP;
        this.miFecha = miFecha;
        this.webView = webView;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getDirIP() {
        return dirIP;
    }

    public void setDirIP(String dirIP) {
        this.dirIP = dirIP;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getMiNombreUsuario() {
        return miNombreUsuario;
    }

    public void setMiNombreUsuario(String miNombreUsuario) {
        this.miNombreUsuario = miNombreUsuario;
    }

    public String getMiDirIP() {
        return miDirIP;
    }

    public void setMiDirIP(String miDirIP) {
        this.miDirIP = miDirIP;
    }

    public Date getMiFecha() {
        return miFecha;
    }

    public void setMiFecha(Date miFecha) {
        this.miFecha = miFecha;
    }

    public WebEngine getWebView() {
        return webView;
    }

    public void setWebView(WebEngine webView) {
        this.webView = webView;
    }
    
}
