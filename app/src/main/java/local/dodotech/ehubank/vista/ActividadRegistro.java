package local.dodotech.ehubank.vista;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import local.dodotech.ehubank.R;
import local.dodotech.ehubank.controlador.ControladorCuentasUsuario;
import local.dodotech.ehubank.modelo.Cliente;

/**
 * Created by Christian on 26/03/2022.
 */

public class ActividadRegistro extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrarse);
    }

    public void evtRegistrarse(View v){
        Log.d("ActividadRegistro", "Evento de registro activado");
        EditText ident = (EditText)findViewById(R.id.registrarse_txtIdentificador);
        EditText clave = (EditText)findViewById(R.id.registrarse_txtClave);
        EditText nombre = (EditText) findViewById(R.id.registrarse_txtNombre);
        EditText apellidos = (EditText) findViewById(R.id.registrarse_txtApellidos);
        EditText direccion = (EditText) findViewById(R.id.registrarse_direccion);
        EditText fechaNac = (EditText) findViewById(R.id.registrarse_txtFechaNacimiento);
        EditText telContacto = (EditText) findViewById(R.id.registrarse_telefono);
        CheckBox condicionesAceptadas = (CheckBox) findViewById(R.id.registrarse_chkTerminosYCondiciones);

        if(!condicionesAceptadas.isChecked()){
            //Animación del checkbox condicionesAceptadas.setAnimation(AnimationUtils.loadInterpolator(this, android.R.anim.bounce_interpolator));
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(this, "Debes aceptar los términos y condiciones para poder registrarte", duration); //TODO Localizar como recurso
            toast.show();
        }else{
            if(ident.getText().toString().trim().isEmpty() || clave.getText().toString().trim().isEmpty() ||
                    nombre.getText().toString().trim().isEmpty() || apellidos.getText().toString().trim().isEmpty() ||
                    direccion.getText().toString().trim().isEmpty() || fechaNac.getText().toString().trim().isEmpty() ||
                    telContacto.getText().toString().trim().isEmpty()){
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(this, "Rellena todos los datos para poder registrarte", duration); //TODO Localizar como recurso
                toast.show();
            }else{
                ControladorCuentasUsuario cr = ControladorCuentasUsuario.getControladorCuentasUsuario();
                boolean registroCorrecto = cr.registrarse(ident.getText().toString(), nombre.getText().toString(), apellidos.getText().toString(), clave.getText().toString(),
                        direccion.getText().toString(), fechaNac.getText().toString(), telContacto.getText().toString());
                if(registroCorrecto){
                    Cliente c = new Cliente(nombre.getText().toString(), apellidos.getText().toString(), new Date(fechaNac.getText().toString()),
                            direccion.getText().toString(), telContacto.getText().toString(), ident.getText().toString(), clave.getText().toString());
                    Intent i = new Intent(this, ActividadPantallaPrincipal.class);
                    i.putExtra("cliente", c);
                    startActivity(i);
                    finish();
                }else{
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(this, "Se ha producido un fallo. Revise los datos e inténtelo de nuevo", duration); //TODO Localizar como recurso
                    toast.show();
                }
            }
        }
    }
}
