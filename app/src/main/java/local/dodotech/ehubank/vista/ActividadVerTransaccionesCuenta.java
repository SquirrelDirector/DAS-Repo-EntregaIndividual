package local.dodotech.ehubank.vista;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import local.dodotech.ehubank.R;
import local.dodotech.ehubank.vista.fragmentsPrincipal.UltimosMovimientosAdapter;

/**
 * Created by Christian on 25/03/2022.
 */

public class ActividadVerTransaccionesCuenta extends AppCompatActivity{
    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {

        return super.onCreateView(name, context, attrs);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        String idCuentaBancaria=null;
        setContentView(R.layout.ver_transacciones_cuenta);
        if(extras!=null){
            Log.d("AVTC", "Cuenta a consultar: "+extras.getString("cuenta"));
            idCuentaBancaria=extras.getString("cuenta");
        }

        if(idCuentaBancaria!=null) {
            RecyclerView rv = (RecyclerView)findViewById(R.id.ver_transacciones_cuenta_rv_transacciones);
            LinearLayoutManager llm= new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
            rv.setLayoutManager(llm);
            rv.setAdapter(new MovimientosCuentaBancariaAdapter(idCuentaBancaria));
        }
    }


}
