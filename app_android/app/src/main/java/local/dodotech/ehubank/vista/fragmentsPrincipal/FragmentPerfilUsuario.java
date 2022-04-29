package local.dodotech.ehubank.vista.fragmentsPrincipal;

import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import local.dodotech.ehubank.R;
import local.dodotech.ehubank.controlador.ConectorInternet;
import local.dodotech.ehubank.controlador.ContenedorDatos;
import local.dodotech.ehubank.controlador.ControladorClientes;
import local.dodotech.ehubank.controlador.ControladorCuentaBancaria;
import local.dodotech.ehubank.controlador.ControladorCuentasUsuario;
import local.dodotech.ehubank.modelo.Cliente;
import local.dodotech.ehubank.vista.ActividadPantallaPrincipal;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentPerfilUsuario.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentPerfilUsuario} factory method to
 * create an instance of this fragment.
 */
public class FragmentPerfilUsuario extends Fragment {

    private OnFragmentInteractionListener mListener;


    public FragmentPerfilUsuario() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_perfil_usuario,container, false);

        TextView lblNomCli = v.findViewById(R.id.fragment_perfil_usuario_nombreCliente);
        TextView lblApellidosCli = v.findViewById(R.id.fragment_perfil_usuario_apellidosCliente);
        TextView lblFechNacCli = v.findViewById(R.id.fragment_perfil_usuario_fechaNacimientoCliente);
        TextView lblDomicilioCli = v.findViewById(R.id.fragment_perfil_usuario_domicilioCliente);
        Map<String,Object> mapa = new HashMap<>();
        mapa.put("accion","seleccionar_usuario");
        mapa.put("identificacion",ControladorCuentasUsuario.getControladorCuentasUsuario().getIdentificador());
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
                            if(workInfo.getState().name().equals("SUCCEEDED")){
                                Log.d("FPU", "Se han obtenido datos del usuario");
                                //Se necesita un thread de UI para actualizar en tiempo la interfaz
                                //https://stackoverflow.com/questions/16425146/runonuithread-in-fragment
                                //https://stackoverflow.com/a/45472774
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        lblNomCli.setText(workInfo.getOutputData().getString("nombre"));
                                        lblApellidosCli.setText(workInfo.getOutputData().getString("apellidos"));
                                        lblFechNacCli.setText(workInfo.getOutputData().getString("fecha_nacimiento"));
                                        lblDomicilioCli.setText(workInfo.getOutputData().getString("domicilio"));
                                        String pathImagen = workInfo.getOutputData().getString("path_imagen");
                                        Log.d("FPU", "Imagen a tomar: "+pathImagen);
                                        Drawable d = ContenedorDatos.getContenedorDatos().getImagenCliente();
                                        if(pathImagen!=null && !pathImagen.trim().equals("") && d!=null){
                                            Log.d("FPU", "Imagen a tomar: "+pathImagen);
                                            //Drawable d = ContenedorDatos.getContenedorDatos().LoadImageFromWebOperations(pathImagen);
                                            ImageView img = getActivity().findViewById(R.id.fragment_perfil_usuario_imagen);
                                            img.setImageDrawable(d);
                                        }else{
                                            ImageView img = getActivity().findViewById(R.id.fragment_perfil_usuario_imagen);
                                            img.setImageDrawable(getActivity().getDrawable(R.mipmap.ic_account_circle));
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
        WorkManager.getInstance().enqueue(otwr);




        Button btnCambioClave = v.findViewById(R.id.fragment_perfil_usuario_btnCambioClave);
        btnCambioClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                evtModificarClave(view);
            }

        });
        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Evento disparado al pulsar el botón de "Modificar contraseña"
     * @param view
     */
    public void evtModificarClave(View view){
        //Código tomado de https://stackoverflow.com/questions/10903754/input-text-dialog-android
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Modificar contraseña");
        //https://stackoverflow.com/questions/35808145/how-do-i-call-findviewbyid-on-an-alertdialog-builder
        View v = getLayoutInflater().inflate(R.layout.interfaz_cambio_clave, null);
        builder.setView(v);
        final EditText txtValidarClave=v.findViewById(R.id.interfaz_cambio_clave_txtClaveAntigua);
        final EditText txtClaveNueva = v.findViewById(R.id.interfaz_cambio_clave_txtClaveNueva);
        final EditText txtVerificarClave = v.findViewById(R.id.interfaz_cambio_clave_txtConfirmarClave);
        // Set up the buttons
        builder.setPositiveButton("Cambiar contraseña", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String claveAntigua=txtValidarClave.getText().toString();
                String identificacionUsuario = ControladorCuentasUsuario.getControladorCuentasUsuario().getIdentificador();
                String claveNueva = txtClaveNueva.getText().toString();
                String verificarClave = txtVerificarClave.getText().toString();

                if(claveNueva.equals(verificarClave)){
                    Log.d("fpu", "Clave antigua OK");
                    Map<String, Object> datos=new HashMap<>();
                    datos.put("accion", "modificar_clave");
                    datos.put("identificacion", identificacionUsuario);
                    datos.put("clave", claveNueva);
                    datos.put("clave_antigua", claveAntigua);
                    Data datosCambioClave = new Data.Builder()
                                                .putAll(datos)
                                                .build();
                    OneTimeWorkRequest otwr2 = new OneTimeWorkRequest.Builder(ConectorInternet.class)
                                                .setInputData(datosCambioClave)
                                                .build();
                    WorkManager.getInstance().getWorkInfoByIdLiveData(otwr2.getId())
                        .observe(getActivity(), new Observer<WorkInfo>() {

                            @Override
                            public void onChanged(@Nullable WorkInfo workInfo) {
                                if(workInfo!=null && workInfo.getState().isFinished()){
                                    //Paso 2: Tras comprobar la validez de ambas, se procede a modificar la clave
                                    boolean claveModificada = workInfo.getState().name().equals("SUCCEEDED");
                                    Log.d("FPU", "Estado 2 - Clave Modificada: "+claveModificada);
                                    if(claveModificada){
                                        Log.d("FPU", "Clave modificada con éxito");
                                        Toast toast = Toast.makeText(getContext(), getString(R.string.fragment_perfil_usuario_cambio_exito), Toast.LENGTH_SHORT);
                                        toast.show();
                                        dialog.dismiss();
                                    }else /*if(!workInfo.getState().name().equals("RUNNING"))*/{
                                        Log.d("FPU", "INFO: "+workInfo.getState().name());
                                        String dato = workInfo.getOutputData().getString("error");
                                        Map datos = workInfo.getOutputData().getKeyValueMap();
                                        System.out.println("DATOS.SIZE(): "+datos.size());
                                        if(dato.equals("ERR_INICIO_SESION")){
                                            Toast toast = Toast.makeText(getContext(), getString(R.string.fragment_perfil_usuario_clave_antigua_no_valida), Toast.LENGTH_LONG);
                                            toast.show();
                                        }else{
                                            Log.d("FPU", "La clave no se ha podido modificar");
                                            Toast toast = Toast.makeText(getContext(), getString(R.string.fragment_perfil_usuario_clave_error), Toast.LENGTH_LONG);
                                            toast.show();
                                        }
                                    }
                                }
                            }
                        });
                    WorkManager.getInstance().enqueue(otwr2);
                }else{
                    Toast toast = Toast.makeText(getContext(), getString(R.string.fragment_perfil_usuario_clave_no_coincide), Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
