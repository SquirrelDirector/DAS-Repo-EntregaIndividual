package local.dodotech.ehubank;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RemoteViews;

import local.dodotech.ehubank.controlador.ContenedorDatos;
import local.dodotech.ehubank.controlador.ControladorCuentaBancaria;
import local.dodotech.ehubank.controlador.ControladorCuentasUsuario;
import local.dodotech.ehubank.vista.fragmentsPrincipal.UltimosMovimientosAdapter;
import local.dodotech.ehubank.widget.WidgetRemoteViewsFactory;
import local.dodotech.ehubank.widget.WidgetService;

/**
 * Implementation of App Widget functionality.
 */
public class Widget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        //views.setTextViewText(R.id.appwidget_text, widgetText);
        ContenedorDatos.getContenedorDatos(context);//Fallback para evitar NullPointerException
        if(ControladorCuentasUsuario.getControladorCuentasUsuario().inicioSesionExistente()){
            if(ControladorCuentaBancaria.getControladorCuentaBancaria().getUltimasTransacciones(ControladorCuentasUsuario.getControladorCuentasUsuario().getIdentificador()).size()>0){
                Log.d("Widget", "Set Remote Adapter");
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


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
        //appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,views.getLayoutId());
    }

    /**
     * Método que gestiona las actualizaciones del widget
     * @param context
     * @param appWidgetManager
     * @param appWidgetIds
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("Widget", "onUpdate");
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    /**
     * Función que salta una vez se inserta el widget
     * @param context
     */
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Log.d("Widget", "onEnabled");
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.RTC, System.currentTimeMillis()+ 1000 * 3, /*60000*/10000 , pi);

    }

    /**
     * Método que controla qué ocurre cuando se deshabilita el widget
     * @param context
     */
    @Override
    public void onDisabled(Context context) {
        Log.d("Widget", "onDisabled");
        // Enter relevant functionality for when the last widget is disabled
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(PendingIntent.getBroadcast(context, 100, new Intent(context, AlarmManagerBroadcastReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT));
    }

    /**
     * Gestión de recepción de petición de actualización
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Widget", "onReceive");
        super.onReceive(context, intent);
        if (intent.getAction().equals("local.dodotech.ehubank.ACTUALIZAR_WIDGET")) {
            int widgetId = intent.getIntExtra( AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
            if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                updateAppWidget(context, widgetManager, widgetId);
            }
        }
    }
}

