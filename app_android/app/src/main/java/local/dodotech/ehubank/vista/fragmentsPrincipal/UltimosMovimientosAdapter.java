package local.dodotech.ehubank.vista.fragmentsPrincipal;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import local.dodotech.ehubank.R;
import local.dodotech.ehubank.controlador.ControladorClientes;
import local.dodotech.ehubank.controlador.ControladorCuentaBancaria;
import local.dodotech.ehubank.controlador.ControladorCuentasUsuario;
import local.dodotech.ehubank.modelo.Transaccion;

/**
 * Created by Christian on 25/03/2022.
 */

public class UltimosMovimientosAdapter extends RecyclerView.Adapter<UltimosMovimientosViewHolder>{
    private List<Transaccion> transacciones;

    public UltimosMovimientosAdapter(){
        transacciones= ControladorCuentaBancaria.getControladorCuentaBancaria().getUltimasTransacciones(ControladorCuentasUsuario.getControladorCuentasUsuario().getIdentificador());
    }

    @Override
    public UltimosMovimientosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaccion, parent, false);
        final UltimosMovimientosViewHolder umvh = new UltimosMovimientosViewHolder(itemLayout);
        /*itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = umvh.getAdapterPosition();
                Log.d("UltimosMovimientosAdapter", "Se ha pulsado en posición "+pos+". Transacción: "+transacciones.get(pos).getFecha());
            }
        });*/
        return umvh;
    }

    @Override
    public void onBindViewHolder(UltimosMovimientosViewHolder holder, int position) {
        holder.cantidad.setText(transacciones.get(position).getCantidad().toString());
        holder.concepto.setText(transacciones.get(position).getConcepto());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        holder.fecha.setText(sdf.format(transacciones.get(position).getFecha()));
    }

    @Override
    public int getItemCount() {
        return transacciones.size();
    }

    public void updateAdapter(){
            Log.d("UMAdp", "Cambiando datos");
            transacciones = ControladorCuentaBancaria.getControladorCuentaBancaria().getUltimasTransacciones(ControladorCuentasUsuario.getControladorCuentasUsuario().getIdentificador());
    }

}
