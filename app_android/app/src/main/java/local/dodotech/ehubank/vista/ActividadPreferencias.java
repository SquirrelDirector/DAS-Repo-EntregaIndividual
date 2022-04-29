package local.dodotech.ehubank.vista;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import local.dodotech.ehubank.R;

/**
 * Created by Christian on 25/03/2022.
 */

public class ActividadPreferencias extends AppCompatActivity {
    private GestorPreferencias gp;
    private SectionsPagerAdapter mSectionsPagerAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gp = new GestorPreferencias();
        setContentView(R.layout.container_preferencias);
        mSectionsPagerAdapter = new ActividadPreferencias.SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container_preferencias_vp);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

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
            return gp;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 1;

        }

    }
}
