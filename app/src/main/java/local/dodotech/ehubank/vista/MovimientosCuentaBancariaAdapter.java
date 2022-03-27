package local.dodotech.ehubank.vista;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.List;

import local.dodotech.ehubank.R;
import local.dodotech.ehubank.controlador.ControladorCuentaBancaria;
import local.dodotech.ehubank.controlador.ControladorCuentasUsuario;
import local.dodotech.ehubank.modelo.Transaccion;

/**
 * Created by Christian on 25/03/2022.
 */

public class MovimientosCuentaBancariaAdapter extends RecyclerView.Adapter<MovimientosCuentaBancariaViewHolder>{
    private List<Transaccion> transacciones;

    public MovimientosCuentaBancariaAdapter(String codCuentaBancaria){
        transacciones= ControladorCuentaBancaria.getControladorCuentaBancaria().getTransaccionesCuentaBancaria(codCuentaBancaria);
        Log.d("MCBA", "Cantidad de transacciones: "+transacciones.size());
    }

    @Override
    public MovimientosCuentaBancariaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaccion, parent, false);
        final MovimientosCuentaBancariaViewHolder mcbvh = new MovimientosCuentaBancariaViewHolder(itemLayout);
        return mcbvh;
    }

    @Override
    public void onBindViewHolder(MovimientosCuentaBancariaViewHolder holder, int position) {
        holder.cantidad.setText(transacciones.get(position).getCantidad().toString());
        holder.concepto.setText(transacciones.get(position).getConcepto());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        holder.fecha.setText(sdf.format(transacciones.get(position).getFecha()));
    }

    @Override
    public int getItemCount() {
        return transacciones.size();
    }


}
