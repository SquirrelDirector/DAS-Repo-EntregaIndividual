package local.dodotech.ehubank.vista;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
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

/**
 * Created by Christian on 26/03/2022.
 */

public class ActividadRegistro extends AppCompatActivity {
    private static final int CODIGO_FOTO_ARCHIVO = 1; //OnActivityResult
    private Uri uriimagen = null;
    private File fichImg = null;
    private DataOutputStream request;
    private String requestURL="http://164.92.218.241/";
    private HttpURLConnection httpConn;
    private final String boundary =  "*****";
    private final String crlf = "\r\n";
    private final String twoHyphens = "--";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrarse);
        Button imagen = (Button) findViewById(R.id.registrarse_btnSeleccionarFoto);

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realizarFoto();
            }
        });
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
            Toast toast = Toast.makeText(this, getString(R.string.actividad_registro_condiciones_necesarias), duration);
            toast.show();
        }else{
            if(ident.getText().toString().trim().isEmpty() || clave.getText().toString().trim().isEmpty() ||
                    nombre.getText().toString().trim().isEmpty() || apellidos.getText().toString().trim().isEmpty() ||
                    direccion.getText().toString().trim().isEmpty() || fechaNac.getText().toString().trim().isEmpty() ||
                    telContacto.getText().toString().trim().isEmpty()){
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(this, getString(R.string.actividad_registro_rellenar_datos), duration);
                toast.show();
            }else{
                /*registrarDatos(ident.getText().toString(), nombre.getText().toString(), apellidos.getText().toString(), clave.getText().toString(),
                        direccion.getText().toString(), fechaNac.getText().toString(), telContacto.getText().toString());*/
                //Log.d("ActividadRegistro", finalizar());
                //System.out.println(finalizar());


                /*ControladorCuentasUsuario cr = ControladorCuentasUsuario.getControladorCuentasUsuario();
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
                    Toast toast = Toast.makeText(this, "Se ha producido un fallo. Revise los datos e inténtelo de nuevo", duration);
                    toast.show();
                }*/
                /*Data datos = new Data.Builder()
                        .putString("accion","registrar")
                        .putString("identificacion",ident.getText().toString())
                        .putString("nombre",nombre.getText().toString())
                        .putString("apellidos",apellidos.getText().toString())
                        .putString("clave", clave.getText().toString())
                        .putString("direccion", direccion.getText().toString())
                        .putString("fecha_nacimiento", fechaNac.getText().toString())
                        .putString("telefono",telContacto.getText().toString())
                        .build();*/
                Map<String,Object> mapa = new HashMap<>();
                mapa.put("accion","registrar");
                mapa.put("identificacion",ident.getText().toString());
                mapa.put("nombre",nombre.getText().toString());
                mapa.put("apellidos",apellidos.getText().toString());
                mapa.put("clave", clave.getText().toString());
                mapa.put("direccion", direccion.getText().toString());
                mapa.put("fecha_nacimiento", fechaNac.getText().toString());
                mapa.put("telefono",telContacto.getText().toString());
                if(fichImg!=null){
                    Log.d("ActividadRegistro", "fichImg: "+fichImg);
                    mapa.put("imagen", fichImg.getPath());
                }
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
                                    if(workInfo.getState().name().equals("FAILED")){
                                        Toast toast = Toast.makeText(getBaseContext(), getString(R.string.actividad_registro_no_red), Toast.LENGTH_SHORT);
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
    }

    /**
     * Función que realiza las acciones necesarias para lanzar la cámara
     */
    private void realizarFoto() {
        Log.d("ActividadRegistro", "Botón pulsado para realizar foto");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String nombrefich = "IMG_" + timeStamp + "_";
        File directorio=this.getFilesDir();


        try {
            fichImg = File.createTempFile(nombrefich, ".jpg",directorio);
            uriimagen = FileProvider.getUriForFile(this, "local.dodotech.ehubank.FileProvider", fichImg);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent elIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        elIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriimagen);
        startActivityForResult(elIntent, CODIGO_FOTO_ARCHIVO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODIGO_FOTO_ARCHIVO && resultCode == RESULT_OK) {
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(fichImg);
            mediaScanIntent.setData(contentUri);
            this.sendBroadcast(mediaScanIntent);

            ImageView img = (ImageView)findViewById(R.id.registrarse_imgUsuario);
            img.setVisibility(View.VISIBLE);
            img.setImageURI(uriimagen);
        }
    }


    /**
     * Función que sube ficheros
     * @param fieldName
     * @param uploadFile
     * @throws IOException
     */
    public void addFilePart(String fieldName, File uploadFile) throws IOException {
        String fileName = uploadFile.getName();
        request.writeBytes(this.twoHyphens + this.boundary + this.crlf);
        request.writeBytes("Content-Disposition: form-data; name=\"" +
                fieldName + "\";filename=\"" +
                fileName + "\"" + this.crlf);
        request.writeBytes(this.crlf);

        //byte[] bytes = Files.readAllBytes(uploadFile.toPath());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        BitmapFactory.decodeFile(uriimagen.getPath()).compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] fototransformada = stream.toByteArray();
        request.write(fototransformada);
    }

}

