package it.units.businesscardwallet.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import it.units.businesscardwallet.R;
import it.units.businesscardwallet.activities.SettingsActivity;

public class EditUserFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        ((SettingsActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.edit_profile));
        setPreferencesFromResource(R.xml.edit_user_fragment, rootKey);
        PreferenceManager.setDefaultValues(getContext(),R.xml.edit_user_fragment,false);
        EditTextPreference name = (EditTextPreference) findPreference("edit_last_name");
        //name.setDefaultValue("Darko");

    }


}