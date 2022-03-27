package local.dodotech.ehubank.modelo;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Christian on 18/03/2022.
 */

public class Cliente implements Serializable{
    private String nombre;
    private String apellidos;
    private Date fechaNacimiento;
    private String direccion;
    private String telefonoContacto;
    private String identificador;
    private String clave;


    /**
     * Constructor de modelo Cliente
     * @param nombre Nombre del cliente
     * @param apellidos Apellidos del cliente
     * @param fechaNacimiento Fecha de nacimiento
     * @param direccion Dirección de residencia
     * @param telefonoContacto Teléfono de contacto
     * @param identificador Identificador del cliente
     * @param clave Clave del cliente
     */
    public Cliente(String nombre, String apellidos, Date fechaNacimiento, String direccion, String telefonoContacto, String identificador, String clave) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.direccion = direccion;
        this.telefonoContacto = telefonoContacto;
        this.identificador = identificador;
        this.clave = clave;
    }

    /***
     * Obtiene el nombre de los cliente
     * @return Nombre del cliente
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene los apellidos del cliente
     * @return Apellidos
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Obtiene la fecha de nacimiento del cliente
     * @return Fecha de nacimiento
     */
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    /**
     * Obtiene la dirección de residencia del cliente
     * @return Dirección de residencia
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Obtiene el teléfono de contacto del cliente
     * @return Teléfono de contacto del cliente.
     */
    public String getTelefonoContacto() {
        return telefonoContacto;
    }

    /**
     * Obtiene el identificador del cliente
     * @return Identificador
     */
    public String getIdentificador() {
        return identificador;
    }

    /**
     * Obtiene la clave del cliente
     * @return Clave
     */
    public String getClave() {
        return clave;
    }

    /**
     * Asigna una dirección al cliente
     * @param direccion Dirección a asignar
     */
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    /**
     * Asigna un teléfono de contacto al cliente
     * @param telefonoContacto Teléfono de contacto
     */
    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    /**
     * Asigna una clave al cliente
     * @param clave Clave nueva
     */
    public void setClave(String clave) {
        this.clave = clave;
    }
}
