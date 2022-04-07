package local.dodotech.ehubank.vista;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.stream.Collectors;

import local.dodotech.ehubank.R;
import local.dodotech.ehubank.controlador.ControladorCuentaBancaria;
import local.dodotech.ehubank.controlador.ControladorCuentasUsuario;
import local.dodotech.ehubank.modelo.CuentaBancaria;

public class ActividadTransaccionContacto extends AppCompatActivity {
    private String cuentaOrigen;
    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.realizar_transaccion_contacto);
        Button btnSeleccionContacto = (Button)findViewById(R.id.realizar_transaccion_contacto_seleccionar_contacto);


        Spinner spCuentaOrigen = (Spinner)findViewById(R.id.realizar_transaccion_contacto_spCuentaOrigen);
        List<CuentaBancaria> lCuentas = ControladorCuentaBancaria.getControladorCuentaBancaria().getCuentasBancarias(ControladorCuentasUsuario.getControladorCuentasUsuario().getIdentificador());
        List<String> nombres = lCuentas.stream()
                .map(c->c.getNombre())
                .collect(Collectors.toList());
        SpinnerAdapter spa = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, nombres);
        spCuentaOrigen.setAdapter(spa);
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

        btnSeleccionContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //https://stackoverflow.com/questions/12123302/android-showing-phonebook-contacts-and-selecting-one

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, 1);
            }
        });
    }

    /**
     * Evento que se dispara cuando se pulsa el botón de "Transferencia bancaria interna"
     * Inicia la actividad de transferencia interna
     */
    public void evtRealizarTransaccionContacto(View v){
        EditText txtCantidad = findViewById(R.id.realizar_transaccion_contacto_txtCantidad);
        EditText txtConcepto = findViewById(R.id.realizar_transaccion_contacto_txtConcepto);

        String cuentaDestino="Contacto";
        String cantidad = txtCantidad.getText().toString();
        String concepto = txtConcepto.getText().toString();


        boolean transferenciaCompleta = ControladorCuentaBancaria.getControladorCuentaBancaria().realizarTransferencia(cuentaOrigen, cuentaDestino, Float.parseFloat(cantidad), concepto);
        if(transferenciaCompleta){
            //Intent envioDatos = new Intent();
            //envioDatos.setType("text/plain");

            Intent envioDatos = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("sms", number, null));
            envioDatos.putExtra("sms_body", "Hola, te acabo en enviar "+cantidad+" euros a tu cuenta. Concepto: "+concepto); //https://stackoverflow.com/questions/9798657/send-a-sms-via-intent
            //startActivity(Intent.createChooser(envioDatos, "Envía la información de transferencia a alguien"));

            NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder ncb = new NotificationCompat.Builder(this, "Notificaciones");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel nc = new NotificationChannel("Notificaciones", "Notificaciones generales",
                        NotificationManager.IMPORTANCE_DEFAULT);
                nc.setDescription("Canal de notificaciones de transacciones");
                nm.createNotificationChannel(nc);
            }
            ncb.setSmallIcon(android.R.drawable.stat_sys_upload_done)
                    .setContentTitle("Transferencia realizada")
                    .setContentText("Cuenta de origen: "+cuentaOrigen+"\nCuenta de destino: "+cuentaDestino)
                    .setVibrate(new long[]{0, 1000, 500, 1000})
                    .setAutoCancel(true)
                    .addAction(R.drawable.ic_menu_send, "Compartir datos", PendingIntent.getActivity(this, 0, envioDatos,0));
            nm.notify(1, ncb.build());


            Log.d("ActTransaccionContacto", "Transacción a contacto de "+cantidad+" euros desde "+cuentaOrigen+" realizada.");
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if(requestCode == RQS_PICK_CONTACT){
            if(resultCode == RESULT_OK){
                Uri contactData = data.getData();
                Cursor cursor =  managedQuery(contactData, null, null, null, null);
                cursor.moveToFirst();
                number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER));
                TextView txt = findViewById(R.id.realizar_transaccion_contacto_lblTelefono);
                txt.setVisibility(View.VISIBLE);
                txt.setText("Contacto: "+number);
            }
        //}
    }
}
