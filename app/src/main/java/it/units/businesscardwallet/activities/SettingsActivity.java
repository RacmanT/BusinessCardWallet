package it.units.businesscardwallet.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.Locale;
import java.util.Map;

import it.units.businesscardwallet.R;
import it.units.businesscardwallet.entities.Contact;
import it.units.businesscardwallet.fragments.SettingsFragment;

public class SettingsActivity extends AppCompatActivity implements PreferenceFragmentCompat.OnPreferenceStartFragmentCallback{

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Contact myContact = (Contact) getIntent().getSerializableExtra("myContact");
        initPref(myContact);


        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        // TODO remove
        super.onResume();
        Map<String, ?> prefs =  PreferenceManager.getDefaultSharedPreferences(this).getAll();
        Log.i("TEST", prefs.toString() );

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            setActionBarTitle("Settings");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        // Instantiate the new Fragment
        final Bundle args = pref.getExtras();
        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                getClassLoader(),
                pref.getFragment());
        fragment.setArguments(args);
        //fragment.setTargetFragment(caller, 0); // on official documentation even if it's deprecated
        // Replace the existing Fragment with the new Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settings, fragment)
                .addToBackStack(null)
                .commit();
        return true;
    }

    @SuppressWarnings("ConstantConditions")
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void initPref(Contact contact){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.edit().putString("edit_name", contact.getName()).apply();
        prefs.edit().putString("edit_last_name", contact.getLastName()).apply();
        prefs.edit().putString("edit_profession", contact.getProfession()).apply();
        prefs.edit().putString("edit_phone", contact.getPhoneNumber()).apply();
        // TODO language and theme
        if(Locale.getDefault().getLanguage().equals(new Locale("sl").getLanguage()) ||
           Locale.getDefault().getLanguage().equals(new Locale("it").getLanguage())){
           prefs.edit().putString("edit_language", Locale.getDefault().getLanguage()).apply();
        }
        prefs.edit().putBoolean("enable_dark_theme", isDarkMode()).apply();

    }

    private boolean isDarkMode(){
                return (getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

}