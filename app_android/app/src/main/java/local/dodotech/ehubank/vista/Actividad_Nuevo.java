package local.dodotech.ehubank.vista;

import android.arch.lifecycle.Observer;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import local.dodotech.ehubank.R;
import local.dodotech.ehubank.controlador.ConectorInternet;
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
    public void evtBtnTransferInterna(View v){
        List<CuentaBancaria> cuentas = ControladorCuentaBancaria.getControladorCuentaBancaria().getCuentasBancarias(ControladorCuentasUsuario.getControladorCuentasUsuario().getIdentificador());
        if(cuentas.size()>=2){
            Intent tInterna = new Intent(this, ActividadTransaccionInterna.class);
            startActivityForResult(tInterna,2);
        }else{
            Toast toast = Toast.makeText(this, getString(R.string.actividad_nuevo_no_cuentas_bancarias_suficientes), Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    /**
     * Evento que se dispara cuando se pulsa el botón de "Envío a contacto"
     * Inicia la actividad de transferencia a contacto
     */
    public void evtBtnTransferContacto(View v){
        List<CuentaBancaria> cuentas = ControladorCuentaBancaria.getControladorCuentaBancaria().getCuentasBancarias(ControladorCuentasUsuario.getControladorCuentasUsuario().getIdentificador());
        if(cuentas.size()>=1){
            Intent tTransferContacto = new Intent(this, ActividadTransaccionContacto.class);
            startActivityForResult(tTransferContacto,2);
        }else{
            Toast toast = Toast.makeText(this, getString(R.string.actividad_nuevo_no_cuentas_bancarias_suficientes), Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    /**
     * Evento que se dispara cuando se pulsa el botón de "Transferencia bancaria externa"
     * Inicia la actividad de transferencia externa
     */
    public void evtTransferExterna(View v){
        List<CuentaBancaria> cuentas = ControladorCuentaBancaria.getControladorCuentaBancaria().getCuentasBancarias(ControladorCuentasUsuario.getControladorCuentasUsuario().getIdentificador());
        if(cuentas.size()>=1) {
            Intent tTransferExterna = new Intent(this, ActividadTransaccionExterna.class);
            startActivityForResult(tTransferExterna, 2);
        }else{
            Toast toast = Toast.makeText(this, getString(R.string.actividad_nuevo_no_cuentas_bancarias_suficientes), Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    /**
     * Evento que se dispara cuando se pulsa el botón de "Cuenta bancaria"
     * Inicia la actividad de creación de cuentas bancarias
     */
    public void evtNuevaCuentaBancaria(View v){
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
                crearCuenta(input.getText().toString());
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

    private void crearCuenta(String nomCuenta){
        /*Data datos = new Data.Builder()
                .put("accion", "nueva_cuenta_bancaria")
                .put("nombre_cuenta", nomCuenta)
                .build();
        OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(ConectorInternet.class)
                .setInputData(datos)
                .build();
        WorkManager.getInstance().getWorkInfoByIdLiveData(otwr.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if(workInfo != null && workInfo.getState().isFinished()){
                            System.out.println(workInfo.getState().name());
                            if(workInfo.getState().name().equals("SUCCEEDED")){
                                Log.d("Actividad_Nuevo", "Se han generado la cuenta");
                                String codigo = workInfo.getOutputData().getString("codigo");
                                Intent i = new Intent();
                                i.putExtra("cuenta", codigo);
                                setResult(RESULT_OK, i);
                                finish();

                            }
                        }
                    }
                });
        WorkManager.getInstance().enqueue(otwr);*/
        CuentaBancaria cuentaBancaria = ControladorCuentaBancaria.getControladorCuentaBancaria().generarCuentaBancaria(ControladorCuentasUsuario.getControladorCuentasUsuario().getIdentificador(), nomCuenta);
        if(cuentaBancaria!=null) {
            Log.d("Actividad_Nuevo", "Se han generado la cuenta");
            Intent i = new Intent();
            i.putExtra("cuenta", cuentaBancaria.getCodigo());
            setResult(RESULT_OK, i);
            finish();
        }

    }
}
