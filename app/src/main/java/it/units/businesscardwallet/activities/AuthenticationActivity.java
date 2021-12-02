package it.units.businesscardwallet.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import it.units.businesscardwallet.R;
import it.units.businesscardwallet.fragments.LoginFragment;
import it.units.businesscardwallet.utils.DatabaseUtils;

public class AuthenticationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //redirectToLogin();
        setContentView(R.layout.activity_authentication);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.authentication, new LoginFragment())
                    .commit();
        }

    }

    private void redirectToLogin() {
        if (DatabaseUtils.userIsNotLogged()) {
            Intent intent = new Intent(AuthenticationActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

    }
}