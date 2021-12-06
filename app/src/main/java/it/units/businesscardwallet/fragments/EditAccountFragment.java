package it.units.businesscardwallet.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;

import androidx.preference.EditTextPreference;
import androidx.preference.PreferenceFragmentCompat;

import it.units.businesscardwallet.R;
import it.units.businesscardwallet.activities.SettingsActivity;
import it.units.businesscardwallet.utils.DatabaseUtils;

@SuppressWarnings("ConstantConditions")
public class EditAccountFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        ((SettingsActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.edit_account));
        setPreferencesFromResource(R.xml.edit_account_fragment, rootKey);

        EditTextPreference email = findPreference("edit_email");
        EditTextPreference password = findPreference("edit_password");

        email.setOnBindEditTextListener(checkEmailInput);
        password.setOnBindEditTextListener(checkPasswordInput);

        // TODO move to databaseUtil
        email.setOnPreferenceChangeListener((preference, newValue) -> {
            DatabaseUtils.getUser().updateEmail(newValue.toString())
                    .addOnSuccessListener(unused -> DatabaseUtils.userRef.update("email", newValue.toString()).isComplete());
            return true;
        });

        password.setOnPreferenceChangeListener((preference, newValue) -> DatabaseUtils.getUser().updatePassword(newValue.toString()).isComplete());

    }

    private final EditTextPreference.OnBindEditTextListener checkEmailInput = editText -> editText.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(s.toString()).matches()) {
                editText.setError("Value not accepted!");
                editText.requestFocus();
            }
        }
    });

    private final EditTextPreference.OnBindEditTextListener checkPasswordInput = editText -> editText.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() < 6) {
                editText.setError("Value not accepted!");
                editText.requestFocus();
            }
        }
    });


}