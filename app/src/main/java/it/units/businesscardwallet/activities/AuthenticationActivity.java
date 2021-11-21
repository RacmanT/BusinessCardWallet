package it.units.businesscardwallet.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import it.units.businesscardwallet.R;
import it.units.businesscardwallet.fragments.LoginFragment;
import it.units.businesscardwallet.fragments.SettingsFragment;

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