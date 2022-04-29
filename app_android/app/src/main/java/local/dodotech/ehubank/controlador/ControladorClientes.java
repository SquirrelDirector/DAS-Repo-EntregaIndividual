package local.dodotech.ehubank.controlador;

import local.dodotech.ehubank.modelo.Cliente;

/**
 * Created by Christian on 21/03/2022.
 */

public class ControladorClientes {
    private static ControladorClientes cc;

    /**
     * Constructor
     */
    private ControladorClientes(){

    }

    /**
     * Patrón Singleton de controlador de clientes
     * @return El controlador único
     */
    public static ControladorClientes getControladorClientes(){
        if(ControladorClientes.cc==null){
            ControladorClientes.cc = new ControladorClientes();
        }
        return ControladorClientes.cc;
    }

    /**
     * Obtiene un cliente del controlador de datos dado un identificador
     * @param identificador Identificador del cliente
     * @return El Cliente, con todos los datos
     */
    public Cliente getCliente(String identificador){
        return ContenedorDatos.getContenedorDatos().obtenerDatosCliente(identificador);
    }
}
