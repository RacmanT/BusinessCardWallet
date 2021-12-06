package it.units.businesscardwallet.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import it.units.businesscardwallet.R;
import it.units.businesscardwallet.activities.AuthenticationActivity;
import it.units.businesscardwallet.utils.DatabaseUtils;

@SuppressWarnings("ConstantConditions")
public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        SwitchPreferenceCompat switchDarkTheme = findPreference("enable_dark_theme");
        switchDarkTheme.setOnPreferenceClickListener((preference) -> {
            if (switchDarkTheme.isChecked()) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            return false;
        });


        Preference delete = findPreference("delete_account");
        delete.setOnPreferenceClickListener(preference -> {
            showDeleteAlert();
            return true;
        });


        Preference logOut = findPreference("log_out");
        logOut.setOnPreferenceClickListener(preference -> {
            DatabaseUtils.signOut();
            getActivity().onBackPressed();
            return false;
        });
    }


    private void showDeleteAlert() {
        new AlertDialog.Builder(getContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getResources().getString(R.string.delete))
                .setMessage(getResources().getString(R.string.confirm_delete))
                .setPositiveButton(getResources().getString(R.string.yes), (dialog, which) -> {
                    DatabaseUtils.deleteAccount();
                    getActivity().finish();
                    startActivity(new Intent(getActivity(),AuthenticationActivity.class));
                })
                .setNegativeButton(getResources().getString(R.string.no), null)
                .show();
    }


}
