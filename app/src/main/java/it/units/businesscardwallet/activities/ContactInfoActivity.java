package it.units.businesscardwallet.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import it.units.businesscardwallet.R;
import it.units.businesscardwallet.entities.Contact;
import it.units.businesscardwallet.fragments.BusinessCard;
import it.units.businesscardwallet.utils.DatabaseUtils;

public class ContactInfoActivity extends AppCompatActivity {

    private Contact contact;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        contact = (Contact) getIntent().getSerializableExtra("contact");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container_view, BusinessCard.newInstance(contact))
                .commit();

    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return super.onOptionsItemSelected(item);
            case R.id.action_delete:
                DatabaseUtils.contactsRef.document(contact.getEmail())
                        .delete().addOnSuccessListener(unused -> finish());
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contact_info_menu, menu);
        return true;
    }
}