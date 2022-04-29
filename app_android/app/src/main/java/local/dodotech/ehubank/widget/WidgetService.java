package local.dodotech.ehubank.widget;


import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViewsService;

/**
 * Clase de apoyo al widget para proporcionar los datos
 */
public class WidgetService extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.d("WidgetService", "Constructor");
        return (new WidgetRemoteViewsFactory(this.getApplicationContext(), intent));
    }


}
