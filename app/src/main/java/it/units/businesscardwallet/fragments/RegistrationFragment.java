package it.units.businesscardwallet.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Arrays;
import java.util.EnumMap;

import it.units.businesscardwallet.R;


public class RegistrationFragment extends Fragment {


    EditText name, lastName, profession, address, institution, emailAddress, password, confirmPassword, phone;
    private TextView logInHint;
    private Button registerButton;

    public RegistrationFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        name = view.findViewById(R.id.editTextName);

        lastName = view.findViewById(R.id.editTextLastName);
        phone = view.findViewById(R.id.editTextPhone);
        emailAddress = view.findViewById(R.id.editTextEmailAddress);
        password = view.findViewById(R.id.editTextPassword);
        confirmPassword = view.findViewById(R.id.editTextConfirmPassword);
        logInHint = view.findViewById(R.id.log_in_hint);
        registerButton = view.findViewById(R.id.buttonRegister);
        profession = view.findViewById(R.id.editTextProfession);
        address = view.findViewById(R.id.editTextAddress);
        institution = view.findViewById(R.id.editTextInstitution);
        registerButton = view.findViewById(R.id.buttonRegister);



        logInHint.setOnClickListener(v -> getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.authentication, new LoginFragment())
                .commit());

        registerButton.setOnClickListener(v -> {


            if(isMissingValue(name, lastName, profession, emailAddress, password, confirmPassword)){
                int missingValueColor = getContext().getColor(android.R.color.holo_red_light);
                setMissingValueColorHint(missingValueColor, name, lastName, profession, emailAddress, password, confirmPassword);
                return;
            }

            if(phone.getText().toString().length() != 0 && !Patterns.PHONE.matcher(phone.getText().toString()).matches()){
                phone.setError("Not a phone number");
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress.getText().toString()).matches()){
                emailAddress.setError("Not an email address");
                return;
            }

            if (password.getText().toString().length() < 6){
                password.setError("Password must have at least 6 characters");
                return;
            }

            if (!password.getText().toString().equals(confirmPassword.getText().toString())){
                password.setError("Password not confirmed");
                confirmPassword.setError("Password not confirmed");
                return;
            }







        });

        return view;
    }


    private boolean isMissingValue(EditText... editTexts){
        return Arrays.stream(editTexts).filter(editText -> name.getText().toString().isEmpty()).count() != 0;
    }
    private void setMissingValueColorHint(int color, EditText... editTexts) {
        Arrays.stream(editTexts).filter(editText -> name.getText().toString().isEmpty())
                .forEach(editText -> {
                    editText.setHintTextColor(color);
                    editText.setError(editText.getHint() + " cannot be empty");
                });
    }
}