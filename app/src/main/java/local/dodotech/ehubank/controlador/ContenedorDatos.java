package local.dodotech.ehubank.controlador;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import local.dodotech.ehubank.modelo.Cliente;
import local.dodotech.ehubank.modelo.CuentaBancaria;
import local.dodotech.ehubank.modelo.Transaccion;
import local.dodotech.ehubank.vista.MainActivity;

/**
 * Created by Christian on 17/03/2022.
 */

public class ContenedorDatos extends SQLiteOpenHelper{
    private static ContenedorDatos cd;
    private SQLiteDatabase db;

    /**
     * Contructor para patrón Singleton
     * @param context nulo
     * @param name Nombre de la base de datos
     * @param factory no procede
     * @param version Versión de la base de datos
     */
    private ContenedorDatos(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        db=getWritableDatabase();
    }

    /**
     * Patrón Singleton para tener una única instancia global de esta clase
     * @return Instancia Contenedor Datos
     */
    public static ContenedorDatos getContenedorDatos(){
        if(ContenedorDatos.cd==null){
            ContenedorDatos.cd = new ContenedorDatos(MainActivity.getContext(), "ehuBank", null,1);
        }
        return ContenedorDatos.cd;
    }

    /**
     * Sentencias que se ejecutan automáticamente si la base de datos no existía
     * @param sqLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //TODO Tareas de creación de tablas
        //ESQUEMA PROPUESTO:
        // USUARIO (DNI (FK CLIENTE), CLAVE)
        // CLIENTE (DNI, NOMBRE, APELLIDOS, DIRECCION, FECHA_NACIMIENTO, TELEFONO)
        // CUENTABANCARIA (ID_CUENTA, DNI (FK CLIENTE), DESCRIPCION)
        // TRANSACCION (ID_CUENTA_ORIGEN, @NULLABLE ID_CUENTA_DESTINO, CANTIDAD, FECHA, CONCEPTO)
        sqLiteDatabase.execSQL("CREATE TABLE USUARIO ('DNI' VARCHAR(255) PRIMARY KEY, 'CLAVE' VARCHAR(255))");
        sqLiteDatabase.execSQL("CREATE TABLE CLIENTE ('DNI' VARCHAR(255) PRIMARY KEY, 'NOMBRE' VARCHAR(255), 'APELLIDOS' VARCHAR(155), 'DIRECCION' VARCHAR(255), 'FECHA_NACIMIENTO' VARCHAR(255), 'TELEFONO' VARCHAR(50), 'CLAVE' VARCHAR(50))");
        sqLiteDatabase.execSQL("CREATE TABLE CUENTABANCARIA ('DNI' VARCHAR(255), 'ID_CUENTA' VARCHAR(255), 'DESCRIPCION' VARCHAR(255))");
        sqLiteDatabase.execSQL("CREATE TABLE TRANSACCION ('ID_CUENTA_ORIGEN' VARCHAR(255), 'ID_CUENTA_DESTINO' VARCHAR(255), 'CANTIDAD' VARCHAR(255), 'FECHA' VARCHAR(255), 'CONCEPTO' VARCHAR(255))");

    }

    /**
     * Sentencias que se ejecutan automáticamente si es necesario realizar una actualización en la base de datos
     * @param sqLiteDatabase
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    /**
     * Función que inserta datos de registro del usuario en la base de datos.
     * Conviene tener lo relacionado con los datos en esta clase, para evitar errores si hay cambios en las tablas.
     * @param id Identificador (DNI) del cliente
     * @param nom Nombre del cliente
     * @param ape Apellidos del cliente
     * @param clave Contraseña del cliente
     * @param direccion Dirección del cliente
     * @param fechaNac Fecha de nacimiento del cliente
     * @param telContacto Teléfono de contacto del clietne
     * @return True si los datos han sido insertados correctamente, False en caso contrario.
     */
    public boolean registrarUsuario(String id, String nom, String ape, String clave, String direccion, String fechaNac, String telContacto) {
        Log.d("ContenedorDatos","Registrando usuario. Identificación: "+id+". Nombre: "+nom+". Apellidos: "+ape+". Contraseña: "+clave+". Dirección: "+direccion
                                            +". Fecha de nacimiento: "+fechaNac+". Teléfono de contacto: "+telContacto);
        ContentValues valoresTblCuentasUsuarios= new ContentValues();
        valoresTblCuentasUsuarios.put("DNI", id);
        valoresTblCuentasUsuarios.put("CLAVE", clave);
        ContentValues valoresTblClientes = new ContentValues();
        valoresTblClientes.put("DNI", id);
        valoresTblClientes.put("NOMBRE", nom);
        valoresTblClientes.put("APELLIDOS", ape);
        valoresTblClientes.put("DIRECCION", direccion);
        valoresTblClientes.put("FECHA_NACIMIENTO", fechaNac);
        valoresTblClientes.put("TELEFONO", telContacto);
        valoresTblClientes.put("CLAVE", clave);

        long insercionUsuario = db.insert("USUARIO", null, valoresTblCuentasUsuarios);
        long insercionCliente = db.insert("CLIENTE", null, valoresTblClientes);
        Log.d("ContenedorDatos", "¿Inserción correcta? Del usuario: "+(insercionUsuario!=-1)+". Del cliente: "+(insercionCliente!=-1));
        return insercionUsuario!=-1 && insercionCliente!=-1; //Si ambos dan un valor distinto a -1, la inserción es correcta
    }

    /**
     * Valida el inicio de sesión de un usuario.
     * @param identificacion El identificador de cliente.
     * @param clave La clave del cliente.
     * @return True si en la base de datos existe un identificador y una clave con los datos especificados por el usuario. False en caso contrario.
     */
    public boolean validarInicioSesion(String identificacion, String clave) {
        Log.d("ContenedorDatos", "Iniciando sesión. Identificador: "+identificacion+". Clave: "+clave);
        String[] columnas = {"DNI"};
        String[] datosWhere={identificacion, clave};
        Cursor c = db.query("USUARIO", columnas, "DNI=? AND CLAVE=?", datosWhere, null, null, null);
        int numDatos = c.getCount();
        Log.d("ContenedorDatos", "Número de cuentas con esos datos: "+numDatos);
        return numDatos>0;
    }

    /**
     * Obtiene los datos del cliente
     * @param identificacion El identificador del cliente.
     * @return El cliente con todos los datos
     */
    public Cliente obtenerDatosCliente(String identificacion) {
        Log.d("ContenedorDatos", "Obteniendo datos del cliente: "+identificacion);
        String [] columnas = {"DNI", "NOMBRE", "APELLIDOS", "DIRECCION", "FECHA_NACIMIENTO", "TELEFONO", "CLAVE"};
        String [] datosWhere={identificacion};
        Cursor c = db.query("CLIENTE", columnas, "DNI=?", datosWhere, null, null, null);
        Cliente cli=null;
        if(c.getCount()>0){
            Log.d("ContenedorDatos", "Hay datos");
            c.moveToNext();
            cli = new Cliente(c.getString(1), c.getString(2), new Date(c.getString(4)), c.getString(3), c.getString(5), c.getString(0), c.getString(6));
        }
        return cli;
    }

    /**
     * Registra una cuenta bancaria en la base de datos.
     * @param identificacion DNI del cliente.
     * @param idCuenta Identificador de la cuenta bancaria. //TODO Valorar generar IBAN
     * @param descCuenta Descripcuón dada por el usuario.
     * @return true si la inserción en la base de datos ha sido correcta. False en caso contrario.
     */
    public boolean registrarCuentaBancaria(String identificacion, String idCuenta, String descCuenta){
        Log.d("ContenedorDatos", "Registrando cuenta bancaria. Identificación del cliente: "+identificacion+". Identificación de la cuenta: "+idCuenta
                                            +". Descripción de la cuenta: "+descCuenta);
        ContentValues valoresTblCuentaBancaria=new ContentValues();
        valoresTblCuentaBancaria.put("DNI", identificacion);
        valoresTblCuentaBancaria.put("ID_CUENTA", idCuenta);
        valoresTblCuentaBancaria.put("DESCRIPCION", descCuenta);
        long insercionCuentaBancaria = db.insert("CUENTABANCARIA", null, valoresTblCuentaBancaria);
        Log.d("ContenedorDatos", "¿Inserción correcta? "+(insercionCuentaBancaria!=-1));
        return insercionCuentaBancaria!=-1;
    }

    /**
     * Devuelve una lista de cuentas bancarias dado un identificador de cliente
     * @param identificadorCliente DNI del cliente
     * @return Listado con los datos de las cuentas bancarias asociadas
     */
    public List<CuentaBancaria> obtenerDatosCuentasBancarias(String identificadorCliente){
        Log.d("ContenedorDatos", "Obteniendo datos de cuentas bancarias del cliente "+identificadorCliente);
        // CUENTABANCARIA (ID_CUENTA, DNI (FK CLIENTE), DESCRIPCION)
        ArrayList<CuentaBancaria> listadoCuentas = new ArrayList<>();
        String [] columnas = {"ID_CUENTA", "DESCRIPCION"};
        String [] datosWhere={identificadorCliente};
        Cursor c = db.query("CUENTABANCARIA", columnas, "DNI=?", datosWhere, null, null, null);
        //c.moveToFirst();
        //listadoCuentas.add(new CuentaBancaria(c.getString(1), c.getString(0), obtenerDatosCliente(identificadorCliente)));
        while(c.moveToNext()){
            listadoCuentas.add(new CuentaBancaria(c.getString(1), c.getString(0), obtenerDatosCliente(identificadorCliente)));
        }
        Log.d("ContenedorDatos", "Cantidad de datos obtenidos: "+listadoCuentas.size());
        return listadoCuentas;
    }

    /**
     * Obtiene datos de una cuenta bancario dado su identificador
     * @param idCuentaBancaria Identificador cde cuenta bancaria
     * @return La cuenta bancaria con todos sus datos
     */
    public CuentaBancaria obtenerDatosCuentaBancaria(String idCuentaBancaria){
        Log.d("ContenedorDatos", "Obteniendo datos de la cuenta bancaria "+idCuentaBancaria);
        String [] columnas = {"ID_CUENTA", "DESCRIPCION", "DNI"};
        String [] datosWhere={idCuentaBancaria};
        Cursor c = db.query("CUENTABANCARIA", columnas, "ID_CUENTA=?", datosWhere, null, null, null);
        CuentaBancaria cuentaBancaria=null;
        if(c.getCount()>0){
            Log.d("ContenedorDatos", "Hay datos");
            c.moveToNext();
            cuentaBancaria= new CuentaBancaria(c.getString(1), c.getString(0), obtenerDatosCliente(c.getString(2)));
        }
        return cuentaBancaria;
    }

    /**
     * Se emite una orden de movimiento de dinero de una cuenta a otra
     * @param cuentaOrigen Identificador de cuenta de origen
     * @param cuentaDestino Identificador de cuenta de destino
     * @param cantidad Cantidad
     * @param fecha fecha
     * @param concepto Motivo de la operación
     * @return booleano que indica si la transaccion es correcta o no.
     */
    public boolean registrarTransaccion(String cuentaOrigen, String cuentaDestino, float cantidad, Date fecha, String concepto) {
        Log.d("ContenedorDatos", "Registrando transacción en base de datos. ID_CUENTA_ORIGEN: "+cuentaOrigen+". ID_CUENTA_DESTINO: "+cuentaDestino+
                    ". CANTIDAD: "+cantidad+". FECHA: "+fecha+". CONCEPTO: "+concepto);
        ContentValues valoresTblTransaccion=new ContentValues();
        valoresTblTransaccion.put("ID_CUENTA_ORIGEN", cuentaOrigen);
        valoresTblTransaccion.put("ID_CUENTA_DESTINO", cuentaDestino);
        valoresTblTransaccion.put("CANTIDAD", cantidad);
        valoresTblTransaccion.put("FECHA", fecha.toString());
        valoresTblTransaccion.put("CONCEPTO", concepto);
        long insercionTransaccion = db.insert("TRANSACCION", null, valoresTblTransaccion);
        Log.d("ContenedorDatos", "¿Inserción correcta? "+(insercionTransaccion!=-1));
        return insercionTransaccion!=-1;
    }


    /**
     * Dado un identificador de cuenta se muestran transacciones hacia o desde ésta
     * @param identificadorCuenta Identificador de la cuenta en la que analizar las transacciones
     * @return Listado de transacciones
     */
    public List<Transaccion> obtenerDatosTransacciones(String identificadorCuenta){
        Log.d("ContenedorDatos", "Obteniendo datos de la cuenta "+identificadorCuenta);
        ArrayList<Transaccion> listadoTransacciones = new ArrayList<>();
        String [] columnas = {"ID_CUENTA_ORIGEN", "ID_CUENTA_DESTINO", "CANTIDAD", "FECHA", "CONCEPTO"};
        String [] datosWhere={identificadorCuenta, identificadorCuenta};
        Cursor c = db.query("TRANSACCION", columnas, "ID_CUENTA_ORIGEN=? OR ID_CUENTA_DESTINO=?", datosWhere, null, null, null);
        //c.moveToFirst();
        //listadoTransacciones.add(new Transaccion(obtenerDatosCuentaBancaria(c.getString(0)), obtenerDatosCuentaBancaria(c.getString(1)), new BigDecimal(c.getString(2)), new Date(c.getString(3)), c.getString(4)));
        while(c.moveToNext()){
            listadoTransacciones.add(new Transaccion(obtenerDatosCuentaBancaria(c.getString(0)), obtenerDatosCuentaBancaria(c.getString(1)), new BigDecimal(c.getString(2)), new Date(c.getString(3)), c.getString(4)));
        }
        Log.d("ContenedorDatos", "Cantidad de datos obtenidos: "+listadoTransacciones.size());
        return listadoTransacciones;
    }

    public boolean modificarClaveUsuario(String identificacionUsuario, String claveNueva) {
        Log.d("ContenedorDatos", "Cambiando clave de la cuenta: "+identificacionUsuario+". Clave a emplear: "+claveNueva);
        ContentValues modificacion = new ContentValues();
        modificacion.put("CLAVE",claveNueva);
        String [] datosWhere={identificacionUsuario};
        int resultado = db.update("CLIENTE", modificacion, "DNI=?", datosWhere);
        int resultado2 = db.update("USUARIO", modificacion, "DNI=?", datosWhere);
        return resultado!=-1 && resultado2!=-1;
    }
}
