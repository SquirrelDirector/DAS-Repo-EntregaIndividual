package local.dodotech.ehubank.vista.fragmentsPrincipal;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import local.dodotech.ehubank.R;
import local.dodotech.ehubank.controlador.ControladorCuentaBancaria;
import local.dodotech.ehubank.controlador.ControladorCuentasUsuario;
import local.dodotech.ehubank.modelo.CuentaBancaria;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentListadoCuentasBancarias.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentListadoCuentasBancarias#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListadoCuentasBancarias extends Fragment {

    private CuentasBancariasAdapter adp=new CuentasBancariasAdapter();;
    private OnFragmentInteractionListener mListener;

    public FragmentListadoCuentasBancarias() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_principal_cuentas_bancarias,container, false);
        RecyclerView rv = (RecyclerView)v.findViewById(R.id.fragment_principal_cuentas_bancarias_rv_cuentas_bancarias);
        LinearLayoutManager llm= new LinearLayoutManager(container.getContext(), LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(llm);
        rv.setAdapter(adp);
        if(ControladorCuentaBancaria.getControladorCuentaBancaria().getCuentasBancarias(ControladorCuentasUsuario.getControladorCuentasUsuario().getIdentificador()).size()==0){
            v.findViewById(R.id.fragment_principal_cuentas_bancarias_rv_cuentas_bancarias).setVisibility(View.GONE);
            v.findViewById(R.id.fragment_principal_cuentas_bancarias_no_cuentas).setVisibility(View.VISIBLE);
        }else{
            v.findViewById(R.id.fragment_principal_cuentas_bancarias_rv_cuentas_bancarias).setVisibility(View.VISIBLE);
            v.findViewById(R.id.fragment_principal_cuentas_bancarias_no_cuentas).setVisibility(View.GONE);
        }
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
        Log.d("FLCB", "Attaching...");
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
        Log.d("FLCB", "Detaching...");
        mListener = null;
    }

    public void agregarDatosAdaptador(CuentaBancaria cb) {
        Log.d("FLCB", "Notificando cambios en adaptador...");
        adp.agregarDato(cb);
        adp.notifyDataSetChanged();
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
}
