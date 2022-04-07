package local.dodotech.ehubank.vista;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import local.dodotech.ehubank.R;
import local.dodotech.ehubank.controlador.ControladorCuentasUsuario;
import local.dodotech.ehubank.modelo.Cliente;

public class MainActivity extends AppCompatActivity {
    private static MainActivity instance;
    private final Calendar myCalendar= Calendar.getInstance();
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences pm = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Log.d("MAIN", "Idioma: "+pm.getString("idioma_pref", "es"));
        String idioma = pm.getString("idioma_pref", null);
        if(idioma==null){
            SharedPreferences.Editor editor = pm.edit();
            editor.putString("idioma_pref", "es").apply();
            editor.commit();

            Locale myLocale = new Locale("es");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        }else{
            Locale myLocale = new Locale(idioma);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;
            res.updateConfiguration(conf, dm);
        }

        instance = this;
        if(existeBD()){
            setContentView(R.layout.iniciar_sesion);
        }else{
            setContentView(R.layout.registrarse);
            //https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
            editText=(EditText) findViewById(R.id.registrarse_txtFechaNacimiento);
            final DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int day) {
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH,month);
                    myCalendar.set(Calendar.DAY_OF_MONTH,day);
                    updateLabel();
                }

                private void updateLabel() {
                    String myFormat="dd/MM/yy";
                    SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.FRANCE);
                    editText.setText(dateFormat.format(myCalendar.getTime()));
                }
            };
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DatePickerDialog(MainActivity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
        }
    }

    /**
     * Este método detecta si la base de datos existe o no.
     * (https://stackoverflow.com/questions/3386667/query-if-android-database-exists)
     * @return true si la base de datos existe. false en caso contrario.
     */
    private boolean existeBD() {
        File dbFile = getApplicationContext().getDatabasePath("ehuBank"); //TODO probar con this en lugar de getApplicationContext()
        return dbFile.exists();
    }

    /**
     * Evento que se dispara cuando se pulsa el botón de registrarse
     */
    public void evtRegistrarse(View v){
        Log.d("MAIN", "Evento de registro activado");
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

    /**
     * Evento que se dispara cuando se pulsa el botón de Iniciar Sesión
     */
    public void evtIniciarSesion(View v){
        Log.d("MAIN", "Evento de inicio de sesión activado");
        EditText ident = (EditText)findViewById(R.id.iniciar_sesion_identificador);
        EditText clave = (EditText) findViewById(R.id.iniciar_sesion_clave);
        if(ident.getText().toString().trim().isEmpty() || clave.getText().toString().trim().isEmpty()){
            Toast toast = Toast.makeText(this, "Rellene todos los datos", Toast.LENGTH_SHORT); //TODO Localizar como recurso
            toast.show();
        }else{
            ControladorCuentasUsuario cd = ControladorCuentasUsuario.getControladorCuentasUsuario();
            Cliente c= cd.validarInicioSesion(ident.getText().toString(), clave.getText().toString());
            if(c!=null){
                Intent i = new Intent(this, ActividadPantallaPrincipal.class);
                i.putExtra("cliente", c);
                startActivity(i);
                finish();
            }else{
                Toast toast = Toast.makeText(this, "Inicio de sesión incorrecto. Revise los datos e inténtelo de nuevo.", Toast.LENGTH_SHORT); //TODO Localizar como recurso
                toast.show();
            }
        }
    }

    public void evtAbrirIURegistro(View v){
        Intent i = new Intent(getContext(), ActividadRegistro.class);
        startActivity(i);
        finish();
    }

    //https://stackoverflow.com/questions/12849930/passing-context-to-sqliteopenhelper
    public static Context getContext()
    {
        return instance;
    }
}
