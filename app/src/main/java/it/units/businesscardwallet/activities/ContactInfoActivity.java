package it.units.businesscardwallet.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import it.units.businesscardwallet.R;
import it.units.businesscardwallet.entities.Contact;
import it.units.businesscardwallet.fragments.BusinessCard;

public class ContactInfoActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        contact = (Contact) getIntent().getSerializableExtra("contact");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container_view, BusinessCard.newInstance(contact))
                .commit();

    }

    @SuppressLint("NonConstantResourceId")
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_delete:
                DocumentReference documentReference = db.collection("users").document(mAuth.getCurrentUser().getUid())
                                                        .collection("contacts").document(contact.getEmail());
                documentReference.delete().addOnSuccessListener(unused -> {
                    finish();
                });
                return true;
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