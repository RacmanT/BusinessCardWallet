package it.units.businesscardwallet.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import it.units.businesscardwallet.R;
import it.units.businesscardwallet.entities.Contact;
import it.units.businesscardwallet.fragments.BusinessCard;

public class ContactInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Contact contact = (Contact) getIntent().getSerializableExtra("contact");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container_view, BusinessCard.newInstance(contact))
                .commit();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
        return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_info_menu, menu);
        return true;
    }
}