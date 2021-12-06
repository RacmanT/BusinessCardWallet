package it.units.businesscardwallet.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;

import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

import it.units.businesscardwallet.R;
import it.units.businesscardwallet.activities.SettingsActivity;
import it.units.businesscardwallet.utils.DatabaseUtils;

public class EditUserFragment extends PreferenceFragmentCompat {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        ((SettingsActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.edit_profile));
        setPreferencesFromResource(R.xml.edit_user_fragment, rootKey);
        EditTextPreference name = findPreference("edit_name");
        EditTextPreference lastName = findPreference("edit_last_name");
        EditTextPreference profession = findPreference("edit_profession");
        EditTextPreference phone = findPreference("edit_phone");
        EditTextPreference institution = findPreference("edit_institution");
        EditTextPreference address = findPreference("edit_address");

        name.setOnBindEditTextListener(checkTextInput);
        lastName.setOnBindEditTextListener(checkTextInput);
        profession.setOnBindEditTextListener(checkTextInput);
        institution.setOnBindEditTextListener(checkTextInput);
        phone.setOnBindEditTextListener(editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER));

        name.setOnPreferenceChangeListener((preference, newValue) -> DatabaseUtils.userRef.update("name", newValue.toString()).isComplete());
        lastName.setOnPreferenceChangeListener((preference, newValue) ->DatabaseUtils.userRef.update("lastName", newValue.toString()).isComplete());
        profession.setOnPreferenceChangeListener((preference, newValue) -> DatabaseUtils.userRef.update("profession", newValue.toString()).isComplete());
        institution.setOnPreferenceChangeListener((preference, newValue) -> DatabaseUtils.userRef.update("institution", newValue.toString()).isComplete());
        address.setOnPreferenceChangeListener((preference, newValue) -> DatabaseUtils.userRef.update("address", newValue.toString()).isComplete());
        phone.setOnPreferenceChangeListener((preference, newValue) -> DatabaseUtils.userRef.update("phoneNumber", newValue.toString()).isComplete());

    }

    private final EditTextPreference.OnBindEditTextListener checkTextInput = editText -> editText.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().trim().isEmpty() || !s.toString().matches("[a-zA-Z]+")) {
                editText.setError("Value not accepted!");
                editText.requestFocus();
            }
        }
    });


}