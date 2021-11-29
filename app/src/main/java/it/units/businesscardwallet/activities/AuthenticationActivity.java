package it.units.businesscardwallet.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import it.units.businesscardwallet.R;
import it.units.businesscardwallet.fragments.LoginFragment;

public class AuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_authentication);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.authentication, new LoginFragment())
                    .commit();
        }

    }

}