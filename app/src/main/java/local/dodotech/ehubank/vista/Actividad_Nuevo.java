package local.dodotech.ehubank.vista;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import local.dodotech.ehubank.R;
import local.dodotech.ehubank.controlador.ControladorCuentaBancaria;
import local.dodotech.ehubank.controlador.ControladorCuentasUsuario;
import local.dodotech.ehubank.modelo.CuentaBancaria;

public class Actividad_Nuevo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantalla_nuevo);
    }

    /**
     * Evento que se dispara cuando se pulsa el botón de "Transferencia bancaria interna"
     * Inicia la actividad de transferencia interna
     */
    protected void evtBtnTransferInterna(View v){
        List<CuentaBancaria> cuentas = ControladorCuentaBancaria.getControladorCuentaBancaria().getCuentasBancarias(ControladorCuentasUsuario.getControladorCuentasUsuario().getIdentificador());
        if(cuentas.size()>=2){
            Intent tInterna = new Intent(this, ActividadTransaccionInterna.class);
            startActivityForResult(tInterna,2);
        }else{
            Toast toast = Toast.makeText(this, "No tienes suficientes cuentas bancarias para realizar esta acción.", Toast.LENGTH_SHORT); //TODO Localizar como recurso
            toast.show();
        }

    }

    /**
     * Evento que se dispara cuando se pulsa el botón de "Envío a contacto"
     * Inicia la actividad de transferencia a contacto
     */
    protected void evtBtnTransferContacto(View v){
        List<CuentaBancaria> cuentas = ControladorCuentaBancaria.getControladorCuentaBancaria().getCuentasBancarias(ControladorCuentasUsuario.getControladorCuentasUsuario().getIdentificador());
        if(cuentas.size()>=1){
            Intent tTransferContacto = new Intent(this, ActividadTransaccionContacto.class);
            startActivityForResult(tTransferContacto,2);
        }else{
            Toast toast = Toast.makeText(this, "No tienes suficientes cuentas bancarias para realizar esta acción.", Toast.LENGTH_SHORT); //TODO Localizar como recurso
            toast.show();
        }

    }

    /**
     * Evento que se dispara cuando se pulsa el botón de "Transferencia bancaria externa"
     * Inicia la actividad de transferencia externa
     */
    protected void evtTransferExterna(View v){
        List<CuentaBancaria> cuentas = ControladorCuentaBancaria.getControladorCuentaBancaria().getCuentasBancarias(ControladorCuentasUsuario.getControladorCuentasUsuario().getIdentificador());
        if(cuentas.size()>=1) {
            Intent tTransferExterna = new Intent(this, ActividadTransaccionExterna.class);
            startActivityForResult(tTransferExterna, 2);
        }else{
            Toast toast = Toast.makeText(this, "No tienes suficientes cuentas bancarias para realizar esta acción.", Toast.LENGTH_SHORT); //TODO Localizar como recurso
            toast.show();
        }

    }

    /**
     * Evento que se dispara cuando se pulsa el botón de "Cuenta bancaria"
     * Inicia la actividad de creación de cuentas bancarias
     */
    protected void evtNuevaCuentaBancaria(View v){
        //Código tomado de https://stackoverflow.com/questions/10903754/input-text-dialog-android
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Crear nueva cuenta bancaria");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Crear cuenta", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CuentaBancaria cuentaBancaria = ControladorCuentaBancaria.getControladorCuentaBancaria().generarCuentaBancaria(ControladorCuentasUsuario.getControladorCuentasUsuario().getIdentificador(), input.getText().toString());
                if(cuentaBancaria!=null){
                    Intent i = new Intent();
                    i.putExtra("cuenta", cuentaBancaria.getCodigo());
                    setResult(RESULT_OK, i);
                    finish();
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Intent i = new Intent();
            setResult(RESULT_OK, i);
            finish();
        }
    }
}
