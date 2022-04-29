package local.dodotech.ehubank.vista;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import local.dodotech.ehubank.R;

/**
 * Created by Christian on 25/03/2022.
 */

public class GestorPreferencias extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.conf_preferencias);
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.d("GestorPreferencias", "Cambio en preferencias: "+s+". SP: "+sharedPreferences.getAll().get(s));
        String val = sharedPreferences.getAll().get(s).toString();
        Resources res = getContext().getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        if(val.equals("en")){
            conf.setLocale(new Locale("en")); // API 17+ only.
            sharedPreferences.edit().putString("idioma_pref", "en").commit();
        }else if (val.equals("es")) {
            conf.setLocale(new Locale("es")); // API 17+ only.
            sharedPreferences.edit().putString("idioma_pref","es").commit();
        }
        res.updateConfiguration(conf, dm);
        Intent intent = new Intent();
        intent.putExtra("idioma",val);
        getActivity().setResult(Activity.RESULT_OK, intent);
    }


}
