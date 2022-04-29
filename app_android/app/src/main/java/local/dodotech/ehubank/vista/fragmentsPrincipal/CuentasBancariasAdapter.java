package local.dodotech.ehubank.vista.fragmentsPrincipal;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import local.dodotech.ehubank.R;
import local.dodotech.ehubank.controlador.ControladorCuentaBancaria;
import local.dodotech.ehubank.controlador.ControladorCuentasUsuario;
import local.dodotech.ehubank.modelo.CuentaBancaria;
import local.dodotech.ehubank.modelo.Transaccion;
import local.dodotech.ehubank.vista.ActividadPantallaPrincipal;
import local.dodotech.ehubank.vista.ActividadVerTransaccionesCuenta;

/**
 * Created by Christian on 25/03/2022.
 */

public class CuentasBancariasAdapter extends RecyclerView.Adapter<CuentasBancariasViewHolder>{
    private List<CuentaBancaria> cuentas;

    public CuentasBancariasAdapter(){
        cuentas = ControladorCuentaBancaria.getControladorCuentaBancaria().getCuentasBancarias(ControladorCuentasUsuario.getControladorCuentasUsuario().getIdentificador());
    }

    @Override
    public CuentasBancariasViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cuenta_bancaria, parent, false);
        final CuentasBancariasViewHolder cbvh = new CuentasBancariasViewHolder(itemLayout);
        itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = cbvh.getAdapterPosition();
                Log.d("MovimientosCuentaBancariaAdapter", "Se ha pulsado en posición "+pos+". Nombre de cuenta: "+cuentas.get(pos).getNombre());
                Intent i = new Intent(parent.getContext(), ActividadVerTransaccionesCuenta.class);
                i.putExtra("cuenta",cuentas.get(pos).getCodigo());
                parent.getContext().startActivity(i);
            }
        });
        return cbvh;
    }

    @Override
    public void onBindViewHolder(CuentasBancariasViewHolder holder, int position) {
        holder.identificadorCuenta.setText(cuentas.get(position).getCodigo());
        holder.descripcionCuenta.setText(cuentas.get(position).getNombre());
    }

    @Override
    public int getItemCount() {
        return cuentas.size();
    }

    public void agregarDato(CuentaBancaria cb) {
        cuentas.add(cb);
    }
}
