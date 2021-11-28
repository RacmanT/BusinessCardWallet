package it.units.businesscardwallet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import it.units.businesscardwallet.R;
import it.units.businesscardwallet.activities.MainActivity;


public class LoginFragment extends Fragment {


    private EditText emailAddress, password;
    private TextView registrationHint;
    private Button logIn;
    private FirebaseAuth mAuth;

    public LoginFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

    }

    @SuppressWarnings("ConstantConditions")
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
            String emailAddressContent = emailAddress.getText().toString();
            String passwordContent = password.getText().toString();

            if (!isValidEmail(emailAddressContent)) {
                emailAddress.setError("Email address is not valid");
                return;
            }
            if (passwordContent.length() == 0) {
                password.setError("Password cannot be empty");
                return;
            }

            mAuth.signInWithEmailAndPassword(emailAddressContent, passwordContent)
                    .addOnSuccessListener(authResult -> startActivity(new Intent(getContext(), MainActivity.class)))
                    .addOnFailureListener(e -> {
                        emailAddress.setError("Wrong credentials");
                        password.setError("Wrong credentials");
                    });

        });

        return view;
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}