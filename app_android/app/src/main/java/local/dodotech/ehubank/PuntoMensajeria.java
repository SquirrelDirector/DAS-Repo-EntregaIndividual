package local.dodotech.ehubank;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import local.dodotech.ehubank.controlador.ContenedorDatos;
import local.dodotech.ehubank.controlador.ControladorCuentaBancaria;

/**
 * Punto de recepción de mensajes de Firebase Cloud Messaging
 */

public class PuntoMensajeria extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Context c = getBaseContext();
        ContenedorDatos.getContenedorDatos(c);//Fallback
        //super.onMessageReceived(remoteMessage);
        Log.d("PuntoMensajeria", "Mensaje recibido");
        String cuentaOrigen = remoteMessage.getData().get("cuenta_origen");
        String cuentaDestino = remoteMessage.getData().get("cuenta_destino");
        String concepto = remoteMessage.getData().get("concepto");
        float cantidad = Float.parseFloat(remoteMessage.getData().get("cantidad"));
        //Si la transacción es correcta, se procede a notificar al usuario
        if(ControladorCuentaBancaria.getControladorCuentaBancaria().realizarTransferencia(cuentaOrigen, cuentaDestino, cantidad, concepto)){
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
        }
    }
}
