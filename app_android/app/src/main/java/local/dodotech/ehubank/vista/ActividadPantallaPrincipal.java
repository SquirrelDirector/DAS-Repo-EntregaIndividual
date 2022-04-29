package local.dodotech.ehubank.vista;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Locale;

import local.dodotech.ehubank.R;
import local.dodotech.ehubank.controlador.ContenedorDatos;
import local.dodotech.ehubank.controlador.ControladorClientes;
import local.dodotech.ehubank.controlador.ControladorCuentaBancaria;
import local.dodotech.ehubank.controlador.ControladorCuentasUsuario;
import local.dodotech.ehubank.vista.fragmentsPrincipal.FragmentListadoCuentasBancarias;
import local.dodotech.ehubank.vista.fragmentsPrincipal.FragmentPerfilUsuario;
import local.dodotech.ehubank.vista.fragmentsPrincipal.FragmentUltimosMovimientos;

public class ActividadPantallaPrincipal extends AppCompatActivity implements FragmentListadoCuentasBancarias.OnFragmentInteractionListener,
                                                                             FragmentUltimosMovimientos.OnFragmentInteractionListener,
                                                                             FragmentPerfilUsuario.OnFragmentInteractionListener{

    private FragmentUltimosMovimientos fgum;
    private FragmentPerfilUsuario fgpu;
    private FragmentListadoCuentasBancarias flcu;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Log.d("D", "El idioma que estaba seleccionado: "+prefs.getAll().get("idioma_pref"));
        Locale myLocale = new Locale(prefs.getString("idioma_pref","es"));
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);


        setContentView(R.layout.pagina_principal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), Actividad_Nuevo.class);
                startActivityForResult(i, 0);
            }
        });

        flcu = new FragmentListadoCuentasBancarias();
        fgpu = new FragmentPerfilUsuario();
        fgum = new FragmentUltimosMovimientos();
    }

    /**
     * Menu de acceso a preferencias
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_actividad_pantalla_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, ActividadPreferencias.class);
            startActivityForResult(i, 1);
            return true;
        }else if (id==R.id.action_cerrar_sesion){
            boolean cierreSesion=ControladorCuentasUsuario.getControladorCuentasUsuario().cerrarSesion();
            if(cierreSesion){
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d("APPrincipal_OnFragInter", "Alguna cosa");
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    //public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        /*private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }*/

        /**
         * Returns a new instance of this fragment for the given section
         * number.

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }*/
        /*
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.pagina_principal, container, false);
            return rootView;
        }*/
    //}

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0:
                    return  fgum;
                case 1:
                    return flcu;
                case 2:
                    return fgpu;
                default:
                    return fgum;
            }
            //return new FragmentUltimosMovimientos(); //Supuestamente nunca debería llegar a este punto
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("APANTPRINC", "REQ: "+requestCode+". RES:"+resultCode);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            fgum.refreshAdapter();
            String idUsuario = ControladorCuentasUsuario.getControladorCuentasUsuario().getIdentificador();
            if(data.getStringExtra("cuenta")!=null){
                flcu.agregarDatosAdaptador(ControladorCuentaBancaria.getControladorCuentaBancaria().getCuentaBancaria(data.getStringExtra("cuenta")));
                fgum.refreshAdapter();
                if(ControladorCuentaBancaria.getControladorCuentaBancaria().getCuentasBancarias(idUsuario).size()>0){
                    findViewById(R.id.fragment_principal_cuentas_bancarias_no_cuentas).setVisibility(View.GONE);
                    findViewById(R.id.fragment_principal_cuentas_bancarias_rv_cuentas_bancarias).setVisibility(View.VISIBLE);
                }else{
                    findViewById(R.id.fragment_principal_cuentas_bancarias_no_cuentas).setVisibility(View.VISIBLE);
                    findViewById(R.id.fragment_principal_cuentas_bancarias_rv_cuentas_bancarias).setVisibility(View.GONE);
                }
            }
            if(ControladorCuentaBancaria.getControladorCuentaBancaria().getUltimasTransacciones(idUsuario).size()>0){
                findViewById(R.id.fragment_principal_ultimas_transacciones_no_transacciones).setVisibility(View.GONE);
                findViewById(R.id.fragment_principal_ultimas_transacciones_rv_ultimas_transacciones).setVisibility(View.VISIBLE);
            }else{
                findViewById(R.id.fragment_principal_ultimas_transacciones_no_transacciones).setVisibility(View.VISIBLE);
                findViewById(R.id.fragment_principal_ultimas_transacciones_rv_ultimas_transacciones).setVisibility(View.GONE);
            }

        }else if(requestCode==1 && resultCode == RESULT_OK){
            Intent i = new Intent(this, ActividadPantallaPrincipal.class);
            finish();
            startActivity(i);
        }
    }
}
