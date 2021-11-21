package it.units.businesscardwallet.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import it.units.businesscardwallet.R;


public class LoginFragment extends Fragment {


    private EditText emailAddress, password;
    private TextView registrationHint;
    private Button logIn;

    public LoginFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        emailAddress = view.findViewById(R.id.editTextTextEmailAddress);
        password = view.findViewById(R.id.editTextTextPassword);
        registrationHint = view.findViewById(R.id.registration_hint);
        logIn = view.findViewById(R.id.buttonLogIn);

        registrationHint.setOnClickListener(v -> getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.authentication, new RegistrationFragment())
                .commit());

        logIn.setOnClickListener(v -> {
            if (!isValidEmail(emailAddress.getText().toString())) {
                emailAddress.setError("Email address is not valid");
                return;
            }
            if (password.getText().length() == 0) {
                password.setError("Password cannot be empty");
                return;
            }

        });

        return view;
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}