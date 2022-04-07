package local.dodotech.ehubank.vista;

import android.content.Intent;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import local.dodotech.ehubank.R;
import local.dodotech.ehubank.controlador.ControladorClientes;
import local.dodotech.ehubank.controlador.ControladorCuentaBancaria;
import local.dodotech.ehubank.controlador.ControladorCuentasUsuario;
import local.dodotech.ehubank.modelo.CuentaBancaria;
import local.dodotech.ehubank.modelo.Transaccion;

public class ActividadTransaccionInterna extends AppCompatActivity {
    private String cuentaOrigen;
    private String cuentaDestino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realizar_transaccion_interna);
        Spinner spCuentaOrigen = (Spinner)findViewById(R.id.realizar_transaccion_interna_spCuentaOrigen);
        Spinner spCuentaDestino = (Spinner) findViewById(R.id.realizar_transaccion_interna_spCuentaDestino);
        List<CuentaBancaria> lCuentas = ControladorCuentaBancaria.getControladorCuentaBancaria().getCuentasBancarias(ControladorCuentasUsuario.getControladorCuentasUsuario().getIdentificador());
        List<String> nombres = lCuentas.stream()
                .map(c->c.getNombre())
                .collect(Collectors.toList());
        SpinnerAdapter spa = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, nombres);
        spCuentaDestino.setAdapter(spa);
        spCuentaOrigen.setAdapter(spa);

        spCuentaDestino.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CuentaBancaria cuenta = lCuentas.get(i);
                Log.d("ATI", "Cuenta pulsada: "+cuenta.getCodigo());
                cuentaDestino=cuenta.getCodigo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
    public void evtRealizarTransaccionInterna(View v){
        EditText txtCantidad = (EditText) findViewById(R.id.realizar_transaccion_interna_txtCantidad);
        String cantidad = txtCantidad.getText().toString();
        boolean transferenciaCompleta = ControladorCuentaBancaria.getControladorCuentaBancaria().realizarTransferencia(cuentaOrigen, cuentaDestino, Float.parseFloat(cantidad), "Transferencia interna");//TODO Considerar ponerlo como recurso
        if(transferenciaCompleta){
            //TODO ir a actividad de inicio y mandar notificación de transferencia realizada
            Log.d("ActTransaccionInterna", "Transacción de "+cantidad+" euros desde "+cuentaOrigen+" hacia "+cuentaDestino+" realizada.");
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }

}
