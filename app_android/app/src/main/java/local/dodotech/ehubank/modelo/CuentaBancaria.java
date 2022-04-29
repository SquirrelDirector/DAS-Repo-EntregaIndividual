package local.dodotech.ehubank.modelo;

/**
 * Created by Christian on 18/03/2022.
 */

public class CuentaBancaria {
    private String nombre;
    private String codigo;
    private String propietario;

    /**
     * Constructor de cuenta bancaria
     * @param nombre Nombre de la cuenta
     * @param codigo Código de la cuenta
     * @param propietario Propietario
     */
    public CuentaBancaria(String nombre, String codigo, String propietario) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.propietario = propietario;
    }

    /**
     * Obtiene el nombre de la cuenta
     * @return Nombre de la cuenta
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el código de cuenta
     * @return Código
     */
    public String getCodigo() {
        return codigo;
    }

    /**
     * Obtiene el propietario de la cuenta
     * @return Propietario
     */
    public String getPropietario() {
        return propietario;
    }

    /**
     * Asigna un nuevo nombre a la cuenta
     * @param nombre Nombre a asignar
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
