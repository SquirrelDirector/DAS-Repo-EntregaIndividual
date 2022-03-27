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
import android.widget.Adapter;
import android.widget.TextView;

import local.dodotech.ehubank.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentUltimosMovimientos.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentUltimosMovimientos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentUltimosMovimientos extends Fragment {

    private OnFragmentInteractionListener mListener;
    private UltimosMovimientosAdapter adp=new UltimosMovimientosAdapter();;

    public FragmentUltimosMovimientos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_principal_ultimas_transacciones,container, false);
        RecyclerView rv = (RecyclerView)v.findViewById(R.id.fragment_principal_ultimas_transacciones_rv_ultimas_transacciones);
        LinearLayoutManager llm= new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL,false);
        rv.setLayoutManager(llm);
        rv.setAdapter(adp);

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
        Log.d("FUM", "Attaching...");
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        Log.d("FUM", "Detaching...");
        super.onDetach();
        mListener = null;
    }

    public void refreshAdapter(){
        adp.updateAdapter();
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
