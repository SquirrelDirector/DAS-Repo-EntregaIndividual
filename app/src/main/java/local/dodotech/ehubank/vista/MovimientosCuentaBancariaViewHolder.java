package local.dodotech.ehubank.vista;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import local.dodotech.ehubank.R;

/**
 * Created by Christian on 25/03/2022.
 */

public class MovimientosCuentaBancariaViewHolder extends RecyclerView.ViewHolder {
    public TextView fecha;
    public TextView concepto;
    public TextView cantidad;

    public MovimientosCuentaBancariaViewHolder(View itemView) {
        super(itemView);
        fecha=itemView.findViewById(R.id.item_transaccion_fecha);
        concepto=itemView.findViewById(R.id.item_transaccion_concepto);
        cantidad=itemView.findViewById(R.id.item_transaccion_cantidad);
    }
}
