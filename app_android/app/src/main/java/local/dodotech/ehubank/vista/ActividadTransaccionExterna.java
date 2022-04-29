package local.dodotech.ehubank.vista;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import local.dodotech.ehubank.R;
import local.dodotech.ehubank.controlador.ControladorCuentaBancaria;
import local.dodotech.ehubank.controlador.ControladorCuentasUsuario;
import local.dodotech.ehubank.modelo.CuentaBancaria;

public class ActividadTransaccionExterna extends AppCompatActivity {
    private String cuentaOrigen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realizar_transaccion_externa);
        Spinner spCuentaOrigen = (Spinner)findViewById(R.id.realizar_transaccion_externa_spCuentaOrigen);
        List<CuentaBancaria> lCuentas = ControladorCuentaBancaria.getControladorCuentaBancaria().getCuentasBancarias(ControladorCuentasUsuario.getControladorCuentasUsuario().getIdentificador());
        List<String> nombres = lCuentas.stream()
                .map(c->c.getNombre())
                .collect(Collectors.toList());
        SpinnerAdapter spa = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, nombres);
        spCuentaOrigen.setAdapter(spa);

        spCuentaOrigen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CuentaBancaria cuenta = lCuentas.get(i);
                Log.d("ATI", "Cuenta pulsada: "+cuenta.getCodigo());
                cuentaOrigen=cuenta.getCodigo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * Evento que se dispara cuando se pulsa el botón de "Transferencia bancaria interna"
     * Inicia la actividad de transferencia interna
     */
    public void evtRealizarTransaccionExterna(View v){
        EditText txtCantidad = (EditText) findViewById(R.id.realizar_transaccion_externa_txtCantidad);
        EditText txtCuentaDestino = (EditText) findViewById(R.id.realizar_transaccion_externa_txtCuentaDestino);
        EditText txtConcepto = (EditText) findViewById(R.id.realizar_transaccion_externa_txtConcepto);

        String cuentaDestino=txtCuentaDestino.getText().toString();
        String cantidad = txtCantidad.getText().toString();
        String concepto = txtConcepto.getText().toString();

        boolean transferenciaCompleta = ControladorCuentaBancaria.getControladorCuentaBancaria().realizarTransferencia(cuentaOrigen, cuentaDestino, Float.parseFloat(cantidad), concepto);
        if(transferenciaCompleta){
            NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder ncb = new NotificationCompat.Builder(this, "Notificaciones");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel nc = new NotificationChannel("Notificaciones", "Notificaciones generales",
                        NotificationManager.IMPORTANCE_DEFAULT);
                nc.setDescription("Canal de notificaciones de transacciones");
                nm.createNotificationChannel(nc);
            }
            ncb.setSmallIcon(android.R.drawable.stat_sys_upload_done)
                    .setContentTitle(getString(R.string.punto_mensajeria_transferencia_realizada))
                    .setContentText(getString(R.string.punto_mensajeria_cuenta_origen)+cuentaOrigen+"\n"+getString(R.string.punto_mensajeria_cuenta_destino)+cuentaDestino)
                    .setVibrate(new long[]{0, 1000, 500, 1000})
                    .setAutoCancel(true);
            nm.notify(1, ncb.build());

            Log.d("ActTransaccionExterna", "Transacción externa de "+cantidad+" euros desde "+cuentaOrigen+" hacia "+cuentaDestino+" realizada.");
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }

}
