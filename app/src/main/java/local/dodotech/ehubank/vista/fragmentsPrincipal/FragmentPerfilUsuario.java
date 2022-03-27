package local.dodotech.ehubank.vista.fragmentsPrincipal;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import local.dodotech.ehubank.R;
import local.dodotech.ehubank.controlador.ControladorClientes;
import local.dodotech.ehubank.controlador.ControladorCuentaBancaria;
import local.dodotech.ehubank.controlador.ControladorCuentasUsuario;
import local.dodotech.ehubank.modelo.Cliente;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentPerfilUsuario.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentPerfilUsuario#newInstance} factory method to
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
        Cliente c = ControladorClientes.getControladorClientes().getCliente(ControladorCuentasUsuario.getControladorCuentasUsuario().getIdentificador());
        TextView lblNomCli = v.findViewById(R.id.fragment_perfil_usuario_nombreCliente);
        TextView lblApellidosCli = v.findViewById(R.id.fragment_perfil_usuario_apellidosCliente);
        TextView lblFechNacCli = v.findViewById(R.id.fragment_perfil_usuario_fechaNacimientoCliente);
        TextView lblDomicilioCli = v.findViewById(R.id.fragment_perfil_usuario_domicilioCliente);

        lblNomCli.setText(c.getNombre());
        lblApellidosCli.setText(c.getApellidos());
        lblFechNacCli.setText(c.getFechaNacimiento().toString());
        lblDomicilioCli.setText(c.getDireccion());

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
                boolean claveValida = ControladorCuentasUsuario.getControladorCuentasUsuario().validarInicioSesion(identificacionUsuario,claveAntigua)!=null;
                if(claveValida && claveNueva.equals(verificarClave)){
                    boolean claveModificada = ControladorCuentasUsuario.getControladorCuentasUsuario().modificarClaveUsuario(identificacionUsuario, claveNueva);
                    if(claveModificada){
                        Log.d("FPU", "Clave modificada con éxito");
                        Toast toast = Toast.makeText(getContext(), "Clave modificada con éxito", Toast.LENGTH_SHORT); //TODO Localizar como recurso
                        toast.show();
                        dialog.dismiss();
                    }else{
                        Log.d("FPU", "La clave no se ha podido modificar");
                        Toast toast = Toast.makeText(getContext(), "La clave no se ha podido modificar", Toast.LENGTH_LONG); //TODO Localizar como recurso
                        toast.show();
                    }
                }

                if(!claveValida){
                    Toast toast = Toast.makeText(getContext(), "La clave antigua no es válida", Toast.LENGTH_LONG); //TODO Localizar como recurso
                    toast.show();

                }
                if(!claveNueva.equals(verificarClave)){
                    Toast toast = Toast.makeText(getContext(), "Las claves no coinciden", Toast.LENGTH_LONG); //TODO Localizar como recurso
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
