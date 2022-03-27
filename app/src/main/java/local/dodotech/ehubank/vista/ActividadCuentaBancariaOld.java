package local.dodotech.ehubank.vista;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import local.dodotech.ehubank.R;

/**
 * Created by Christian on 27/03/2022.
 */

@Deprecated
public class ActividadCuentaBancariaOld extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.ver_transacciones_cuenta);
        RecyclerView rv = findViewById(R.id.ver_transacciones_cuenta_rv_transacciones);
        rv.setAdapter(new MovimientosCuentaBancariaAdapter(savedInstanceState.getString("cuenta")));
    }
}
