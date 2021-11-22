package it.units.businesscardwallet.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.google.firebase.auth.FirebaseAuth;

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


        Preference logOut = findPreference("log_out");
        logOut.setOnPreferenceClickListener(preference -> {
            FirebaseAuth.getInstance().signOut();
            getActivity().onBackPressed();
            return false;
        });
    }


}
