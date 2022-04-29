package local.dodotech.ehubank.widget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import local.dodotech.ehubank.R;
import local.dodotech.ehubank.Widget;
import local.dodotech.ehubank.controlador.ContenedorDatos;
import local.dodotech.ehubank.controlador.ControladorClientes;
import local.dodotech.ehubank.controlador.ControladorCuentaBancaria;
import local.dodotech.ehubank.controlador.ControladorCuentasUsuario;
import local.dodotech.ehubank.modelo.Transaccion;


/**
 * Clase que gestiona los datos que debe leer el widget
 */
public class WidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context c;
    private int appWidgetId;
    private List<Transaccion> transacciones;

    public WidgetRemoteViewsFactory(Context c, Intent i){
        Log.d("WidgetRemoteViewsFactory", "Constructor");
        this.c=c;
        this.appWidgetId=i.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    public void actualizarWidget(){

        Log.d("WidgetRemoteViewsFactory", "Contexto: "+c);
        ContenedorDatos.getContenedorDatos(c);
        if(ControladorCuentasUsuario.getControladorCuentasUsuario().inicioSesionExistente()){
            transacciones= ControladorCuentaBancaria.getControladorCuentaBancaria().getUltimasTransacciones(ControladorCuentasUsuario.getControladorCuentasUsuario().getIdentificador());
        }

    }

    @Override
    public void onCreate() {
        actualizarWidget();
    }

    @Override
    public void onDataSetChanged() {
        Log.d("WidgetRemoteViewsFactory", "onDataSetChanged");
        actualizarWidget();
    }

    @Override
    public void onDestroy() {

    }

    /**
     * Se indica el número de elementos que ha de tener la lista
     * @return
     */
    @Override
    public int getCount() {
        if(transacciones!=null){
            return transacciones.size();
        }else{
            return 0;
        }

    }

    /**
     * De manera similar al ViewHolder y RecyclerView, se indica en la posición i qué debe tener la vista
     * @param i
     * @return
     */
    @Override
    public RemoteViews getViewAt(int i) {

        RemoteViews remoteView = new RemoteViews(
                c.getPackageName(), R.layout.item_transaccion_widget);

        if(transacciones!=null){//Si el arraylist es nulo, no hay transacciones
            remoteView.setTextViewText(R.id.item_transaccion_cantidad_widget, transacciones.get(i).getCantidad().toString());
            remoteView.setTextViewText(R.id.item_transaccion_concepto_widget, transacciones.get(i).getConcepto());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            remoteView.setTextViewText(R.id.item_transaccion_fecha_widget, sdf.format(transacciones.get(i).getFecha()));
        }

        //remoteView.setTextViewText(R.id.widget_test, "pos: "+i);
        Log.d("WidgetRemoteViewsFactory", "Rellenando lista, con posición "+i+".");
        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}
