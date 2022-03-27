package local.dodotech.ehubank.vista.fragmentsPrincipal;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import local.dodotech.ehubank.R;

/**
 * Created by Christian on 25/03/2022.
 */

public class CuentasBancariasViewHolder extends RecyclerView.ViewHolder {
    public TextView identificadorCuenta;
    public TextView descripcionCuenta;

    public CuentasBancariasViewHolder(View itemView) {
        super(itemView);
        identificadorCuenta = (TextView)itemView.findViewById(R.id.item_cuenta_bancaria_identificador);
        descripcionCuenta = (TextView)itemView.findViewById(R.id.item_cuenta_bancaria_descripción);
    }
}
