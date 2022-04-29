package local.dodotech.ehubank;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import local.dodotech.ehubank.controlador.ContenedorDatos;
import local.dodotech.ehubank.controlador.ControladorCuentaBancaria;
import local.dodotech.ehubank.controlador.ControladorCuentasUsuario;
import local.dodotech.ehubank.widget.WidgetRemoteViewsFactory;
import local.dodotech.ehubank.widget.WidgetService;

/**
 * BroadcastReceiver del widget
 */

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("AlarmManagerBroadcastReceiver", "onReceive");
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        ComponentName tipowidget = new ComponentName(context, Widget.class);
        //manager.updateAppWidget(tipowidget, );
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        //views.setTextViewText(R.id.appwidget_text, widgetText);
        ContenedorDatos.getContenedorDatos(context);//Fallback para evitar NullPointerException

        //Aplicamos https://stackoverflow.com/a/13017431 dado a que el sistema guarda cosas en caché
        manager.notifyAppWidgetViewDataChanged(/*intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID)*/manager.getAppWidgetIds(tipowidget), R.layout.widget);



        if(ControladorCuentasUsuario.getControladorCuentasUsuario().inicioSesionExistente()){
            if(ControladorCuentaBancaria.getControladorCuentaBancaria().getUltimasTransacciones(ControladorCuentasUsuario.getControladorCuentasUsuario().getIdentificador()).size()>0){
                Log.d("AlarmManagerBroadcastReceiver", "Set Remote Adapter");
                Intent service = new Intent(context, WidgetService.class);
                service.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, manager.getAppWidgetIds(tipowidget)); //https://stackoverflow.com/a/20705254
                views.setRemoteAdapter(R.id.widget_lista, new Intent(context, WidgetService.class));
                views.setViewVisibility(R.id.widget_lista, View.VISIBLE);
                views.setViewVisibility(R.id.widget_no_inicio_sesion, View.GONE);
                views.setViewVisibility(R.id.widget_no_transacciones, View.GONE);
            }else{
                views.setViewVisibility(R.id.widget_lista, View.GONE);
                views.setViewVisibility(R.id.widget_no_inicio_sesion, View.GONE);
                views.setViewVisibility(R.id.widget_no_transacciones, View.VISIBLE);
            }
        }else{
            //Si ningún usuario ha iniciado sesión, se le hace indicar en el widget
            views.setViewVisibility(R.id.widget_lista, View.GONE);
            views.setViewVisibility(R.id.widget_no_inicio_sesion, View.VISIBLE);
            views.setViewVisibility(R.id.widget_no_transacciones, View.GONE);
        }
        manager.updateAppWidget(tipowidget, null);
        manager.updateAppWidget(tipowidget, views);
        Log.d("AlarmManagerBroadcastReceiver", "Notificando cambios en los datos");

    }
}
