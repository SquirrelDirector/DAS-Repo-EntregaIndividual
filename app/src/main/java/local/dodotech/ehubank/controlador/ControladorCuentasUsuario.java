package local.dodotech.ehubank.controlador;

/**
 * Created by Christian on 19/03/2022.
 */

import local.dodotech.ehubank.modelo.Cliente;

/**
 * Esta clase se encarga de manejar el registro e inicio de sesión de los usuarios, empleando el patrón MVC
 */
public class ControladorCuentasUsuario {
    private static ControladorCuentasUsuario ccu;
    private String identificador;

    /**
     * Constructor
     */
    private ControladorCuentasUsuario(){

    }

    /**
     * Patrón Singleton
     * @return Única instancia de ControladorCuentasUsuario
     */
    public static ControladorCuentasUsuario getControladorCuentasUsuario(){
        if(ControladorCuentasUsuario.ccu==null){
            ControladorCuentasUsuario.ccu = new ControladorCuentasUsuario();
        }
        return ControladorCuentasUsuario.ccu;
    }

    /**
     * Clase que controla el registro de datos de cliente.
     * @param id Identificador del usuario.
     * @param nom Nombre del cliente.
     * @param ape Apellidos del cliente.
     * @param clave Contraseña del cliente.
     * @param direccion Dirección de residencia del cliente.
     * @param fechaNac Fecha de nacimiento del cliente.
     * @param telContacto Teléfono de contacto del cliente.
     * @return True si el registro ha sido correcto. False en caso contrario.
     */
    public boolean registrarse(String id, String nom, String ape, String clave, String direccion, String fechaNac, String telContacto){
        ContenedorDatos cd = ContenedorDatos.getContenedorDatos();
        boolean b=cd.registrarUsuario(id, nom, ape, clave, direccion, fechaNac, telContacto);
        if(b){
            this.identificador=id;
        }
        return b;
    }

    /**
     * Clase que se encarga de validar el inicio de sesión
     * @param id Identificador del usuario.
     * @param clave Clave del usuario.
     * @return El objeto con los datos del cliente si el inicio de sesión es correcto. Null en caso contrario.
     */
    public Cliente validarInicioSesion(String id, String clave){
        Cliente c = null;
        boolean inicioSesion =ContenedorDatos.getContenedorDatos().validarInicioSesion(id, clave);
        if(inicioSesion){
            c = ControladorClientes.getControladorClientes().getCliente(id);
            this.identificador=id;
        }
        return c;
    }

    /**
     * Devuelve el identificador del usuario, si éste ha iniciado sesión.
     * @return Identificador del usuario
     */
    public String getIdentificador(){
        return this.identificador;
    }

    /**
     * Modifica las claves del usuario
     * @param identificacionUsuario Identificador del usuario
     * @param claveNueva Clave a modificar
     * @return True si la modificación ha sido correcta. False en caso contrario
     */
    public boolean modificarClaveUsuario(String identificacionUsuario, String claveNueva) {
        return ContenedorDatos.getContenedorDatos().modificarClaveUsuario(identificacionUsuario,claveNueva);
    }
}
