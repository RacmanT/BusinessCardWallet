package it.units.businesscardwallet.fragments;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;
import androidx.recyclerview.widget.RecyclerView;

import it.units.businesscardwallet.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
        // TODO or this or app:fragment in xml
        //findPreference("edit_business_card").setFragment("it.units.businesscardwallet.fragments.EditUserFragment");


        SwitchPreferenceCompat switchDarkTheme = findPreference("enable_dark_theme");
        switchDarkTheme.setOnPreferenceClickListener((preference) -> {
            if (switchDarkTheme.isChecked()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            return false;
        });
    }


}
