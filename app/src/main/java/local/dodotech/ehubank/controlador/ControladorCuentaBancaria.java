package local.dodotech.ehubank.controlador;

import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import local.dodotech.ehubank.modelo.Cliente;
import local.dodotech.ehubank.modelo.CuentaBancaria;
import local.dodotech.ehubank.modelo.Transaccion;

/**
 * Created by Christian on 21/03/2022.
 */

public class ControladorCuentaBancaria {
    private static ControladorCuentaBancaria ccb;
    private ControladorCuentaBancaria(){

    }

    public static ControladorCuentaBancaria getControladorCuentaBancaria(){
        if(ControladorCuentaBancaria.ccb==null){
            ccb = new ControladorCuentaBancaria();
        }
        return ControladorCuentaBancaria.ccb;
    }

    public CuentaBancaria generarCuentaBancaria(String identificadorCliente, String descripcion){
        Log.d("Control-CuentaBancaria", "Generando una nueva cuenta. ID del cliente: "+identificadorCliente+". Descripción de cuenta: "+descripcion);
        String idAzar="";
        //https://stackoverflow.com/questions/50457159/generate-bank-account-number-with-random
        String card = "ES";
        Random rand = new Random();
        for (int i = 0; i < 14; i++)
        {
            int n = rand.nextInt(10) + 0;
            card += Integer.toString(n);
        }
        for (int i = 0; i < 16; i++)
        {
            if(i % 4 == 0) {
                idAzar+=" ";
            }
            idAzar+=card.charAt(i);
        }
        boolean insert=ContenedorDatos.getContenedorDatos().registrarCuentaBancaria(identificadorCliente, idAzar, descripcion);
        if(insert){
            return ContenedorDatos.getContenedorDatos().obtenerDatosCuentaBancaria(idAzar);
        }
        return null;
    }

    public CuentaBancaria getCuentaBancaria(String idCuentaBancaria){
        return ContenedorDatos.getContenedorDatos().obtenerDatosCuentaBancaria(idCuentaBancaria);
    }

    public boolean realizarTransferencia(String cuentaOrigen, String cuentaDestino, float cantidad, String concepto){
        Log.d("Control-CuentaBancaria", "Realizando una transferencia");
        Date fechaActual = new Date();
        return ContenedorDatos.getContenedorDatos().registrarTransaccion(cuentaOrigen, cuentaDestino, cantidad, fechaActual, concepto);
    }

    public List<Transaccion> getUltimasTransacciones(String idUsuario){
        List<CuentaBancaria> cuentasUsuario = ContenedorDatos.getContenedorDatos().obtenerDatosCuentasBancarias(idUsuario);
        Iterator<CuentaBancaria> itr = cuentasUsuario.iterator();
        ArrayList<Transaccion> lTransacciones= new ArrayList<>();
        while(itr.hasNext()){
            CuentaBancaria cb = itr.next();
            String idCuenta = cb.getCodigo();
            List<Transaccion> transac = ContenedorDatos.getContenedorDatos().obtenerDatosTransacciones(idCuenta);
            lTransacciones.addAll(transac);
        }
        Comparator<Transaccion> comparator = new Comparator<Transaccion>() {
            @Override
            public int compare(Transaccion t1, Transaccion t2) {
                return t1.getFecha().compareTo(t2.getFecha());
            }
        };


        return lTransacciones
                .stream()
                .sorted(comparator)
                .collect(Collectors.<Transaccion>toList());
    }

    public List<CuentaBancaria> getCuentasBancarias(String idUsuario){
        return ContenedorDatos.getContenedorDatos().obtenerDatosCuentasBancarias(idUsuario);
    }

    public List<Transaccion> getTransaccionesCuentaBancaria(String idCuenta){
        return ContenedorDatos.getContenedorDatos().obtenerDatosTransacciones(idCuenta);
    }

}
