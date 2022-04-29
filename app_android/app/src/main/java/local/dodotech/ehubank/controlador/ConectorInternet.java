package local.dodotech.ehubank.controlador;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import local.dodotech.ehubank.modelo.Cliente;
import local.dodotech.ehubank.modelo.CuentaBancaria;

/**
 * Clase que actúa como thread para conectar a Internet.
 */

public class ConectorInternet extends Worker{
    private DataOutputStream request;


    public ConectorInternet(Context c, WorkerParameters w){
        super(c, w);

    }
    @NonNull
    @Override
    public Result doWork() {
        String valor= getInputData().getString("accion");
        if(valor.equals("registrar")){  //Acción para registrar un usuario
            Log.d("ConectorInternet", "Registrando usuario: "+getInputData().getString("nombre"));
            Map<String, Object> mapa = getInputData().getKeyValueMap();
            ControladorCuentasUsuario cr = ControladorCuentasUsuario.getControladorCuentasUsuario();
            File f = null;
            if(mapa.get("imagen")!=null){
                f=new File(mapa.get("imagen").toString());
            }
            boolean registroCorrecto = cr.registrarse(mapa.get("identificacion").toString(),
                    mapa.get("nombre").toString(), mapa.get("apellidos").toString(),
                    mapa.get("clave").toString(), mapa.get("direccion").toString(),
                    mapa.get("fecha_nacimiento").toString(), mapa.get("telefono").toString(),
                    f);
            if(registroCorrecto){
                return Result.success();
            }else{
                return Result.failure();
            }
        }else if (valor.equals("iniciar_sesion")){ //Acción para iniciar sesión
            Log.d("ConectorInternet", "Iniciando sesión con usuario: "+getInputData().getString("identificacion"));
            Cliente c= ControladorCuentasUsuario.getControladorCuentasUsuario().validarInicioSesion(getInputData().getString("identificacion"), getInputData().getString("clave"));
            if(c!=null){
                return Result.success();
            }else{
                return Result.failure();
            }
        }else if(valor.equals("modificar_clave")){ //Acción para realizar cambio de claves en un usuario específico
            Log.d("ConectorInternet", "Intentando modificar clave");
            String b =ControladorCuentasUsuario.getControladorCuentasUsuario().modificarClaveUsuario(getInputData().getString("identificacion"), getInputData().getString("clave_antigua"), getInputData().getString("clave"));
            Log.d("ConectorInternet", "Resultado: "+b);
            if(b.equals("OK")){
                return Result.success();
            }else{
                Log.d("ConectorInternet","Devolviendo resultado de error: "+b);
                Data resultadoError = new Data.Builder()
                        .putString("error", b)
                        .build();
                return Result.failure(resultadoError);
            }
        }else if (valor.equals("seleccionar_usuario")){ //Acción para ver en el apartado de perfil los datos del usuario
            Cliente c = ControladorClientes.getControladorClientes().getCliente(getInputData().getString("identificacion"));
            if(c!=null){
                Data resultados = new Data.Builder()
                        .putString("nombre",c.getNombre())
                        .putString("apellidos",c.getApellidos())
                        .putString("fecha_nacimiento", c.getFechaNacimiento().toString())
                        .putString("domicilio", c.getDireccion())
                        .putString("path_imagen",c.getPathImagen())
                        .build();
                return Result.success(resultados);
            }else{
                return Result.failure();
            }

        }else if (valor.equals("nueva_cuenta_bancaria")){ //Acción para listar cuentas bancarias (se requieren datos del cliente propietario, y estos datos se encuentran en remoto)
            CuentaBancaria cuentaBancaria = ControladorCuentaBancaria.getControladorCuentaBancaria().generarCuentaBancaria(ControladorCuentasUsuario.getControladorCuentasUsuario().getIdentificador(), getInputData().getString("nombre_cuenta"));
            if(cuentaBancaria!=null){
                Data resultado = new Data.Builder()
                        .putString("codigo", cuentaBancaria.getCodigo())
                        .build();
                return Result.success(resultado);
            }else{
                return Result.failure();
            }
        }
        return Result.failure();
    }
}
