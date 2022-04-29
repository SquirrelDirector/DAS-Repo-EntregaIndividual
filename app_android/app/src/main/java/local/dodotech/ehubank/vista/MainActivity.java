package local.dodotech.ehubank.vista;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import local.dodotech.ehubank.R;
import local.dodotech.ehubank.controlador.ConectorInternet;
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
        if(ControladorCuentasUsuario.getControladorCuentasUsuario().inicioSesionExistente()){
            Intent i = new Intent(this, ActividadPantallaPrincipal.class);
            startActivity(i);
            finish();
        }
        /*if(existeBD()){
            setContentView(R.layout.iniciar_sesion);
        }else{*/
        setContentView(R.layout.iniciar_sesion);
        //https://stackoverflow.com/questions/14933330/datepicker-how-to-popup-datepicker-when-click-on-edittext
        /*editText=(EditText) findViewById(R.id.registrarse_txtFechaNacimiento);
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
        });*/
    }
    //}

    /**
     * Este método detecta si la base de datos existe o no.
     * (https://stackoverflow.com/questions/3386667/query-if-android-database-exists)
     * @return true si la base de datos existe. false en caso contrario.

    private boolean existeBD() {
        File dbFile = getApplicationContext().getDatabasePath("ehuBank");
        return dbFile.exists();
    }*/

    /**
     * Evento que se dispara cuando se pulsa el botón de Iniciar Sesión
     */
    public void evtIniciarSesion(View v){
        Log.d("MAIN", "Evento de inicio de sesión activado");
        EditText ident = (EditText)findViewById(R.id.iniciar_sesion_identificador);
        EditText clave = (EditText) findViewById(R.id.iniciar_sesion_clave);
        if(ident.getText().toString().trim().isEmpty() || clave.getText().toString().trim().isEmpty()){
            Toast toast = Toast.makeText(this, getString(R.string.mainactivity_rellenar_datos), Toast.LENGTH_SHORT);
            toast.show();
        }else{
            /*ControladorCuentasUsuario cd = ControladorCuentasUsuario.getControladorCuentasUsuario();
            Cliente c= cd.validarInicioSesion(ident.getText().toString(), clave.getText().toString());
            if(c!=null){
                Intent i = new Intent(this, ActividadPantallaPrincipal.class);
                i.putExtra("cliente", c);
                startActivity(i);
                finish();
            }else{
                Toast toast = Toast.makeText(this, "Inicio de sesión incorrecto. Revise los datos e inténtelo de nuevo.", Toast.LENGTH_SHORT);
                toast.show();
            }*/
            //Este Worker se encarga de realizar el trabajo de conexión a internet y verificar el inicio de sesión del usuario.
            Map<String,Object> mapa = new HashMap<>();
            mapa.put("accion","iniciar_sesion");
            mapa.put("identificacion",ident.getText().toString());
            mapa.put("clave", clave.getText().toString());
            Data datos = new Data.Builder()
                    .putAll(mapa)
                    .build();

            OneTimeWorkRequest otwr = new OneTimeWorkRequest.Builder(ConectorInternet.class)
                    .setInputData(datos)
                    .build();
            WorkManager.getInstance().getWorkInfoByIdLiveData(otwr.getId())
                    .observe(this, new Observer<WorkInfo>() {
                        @Override
                        public void onChanged(WorkInfo workInfo) {
                            if(workInfo != null && workInfo.getState().isFinished()){
                                System.out.println(workInfo.getState().name());
                                if(workInfo.getState().name().equals("FAILED")){ //El inicio de sesión ha sido incorrecto, por cualquier motivo, hará que el Worker emita un flag de error.
                                    Toast toast = Toast.makeText(getBaseContext(), getString(R.string.mainactivity_inicio_sesion_incorrecto), Toast.LENGTH_SHORT);
                                    toast.show();
                                }else if(workInfo.getState().name().equals("SUCCEEDED")){
                                    Intent i = new Intent(getBaseContext(), ActividadPantallaPrincipal.class);
                                    startActivity(i);
                                    finish();
                                }
                            }
                        }
                    });
            WorkManager.getInstance().enqueue(otwr);
        }
    }

    /**
     * Evento para abrir la interfaz de registro cuando se pulsa el botón "Abrir cuenta"
     * @param v
     */
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
