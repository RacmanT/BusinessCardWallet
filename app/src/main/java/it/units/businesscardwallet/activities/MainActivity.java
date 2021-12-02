package it.units.businesscardwallet.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.stream.Collectors;

import it.units.businesscardwallet.R;
import it.units.businesscardwallet.entities.Contact;
import it.units.businesscardwallet.fragments.BusinessCard;
import it.units.businesscardwallet.fragments.ContactList;
import it.units.businesscardwallet.utils.AESHelper;
import it.units.businesscardwallet.utils.DatabaseUtils;

@SuppressWarnings("deprecation")
@SuppressLint("NonConstantResourceId")
public class MainActivity extends AppCompatActivity {

    //https://stackoverflow.com/questions/55280032/how-to-update-document-in-firestore

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Contact myContact = new Contact();
    private ArrayList<Contact> listContacts = new ArrayList<>();
    private FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseUtils.init();

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        adapter = new FragmentAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }


    @Override
    protected void onResume() {
        super.onResume();

        if (DatabaseUtils.userIsNotLogged()) {
            Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
            startActivity(intent);
            return;
        }

        DatabaseUtils.userRef.get().addOnSuccessListener(documentSnapshot -> {
            myContact = documentSnapshot.toObject(Contact.class);
            Log.i("TEST", myContact.toString());
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        });

        listContacts.clear();
        DatabaseUtils.contactsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            queryDocumentSnapshots.getDocuments().forEach(doc -> listContacts.add(doc.toObject(Contact.class)));
            //listContacts =  (ArrayList<Contact>) listContacts.stream().distinct().collect(Collectors.toList());
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_add:
                // TODO add qr code scanner
                /*options.setCaptureActivity(AnyOrientationCaptureActivity.class);
                options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
                options.setPrompt("Scan a Business Card QR Code");
                options.setOrientationLocked(false);
                options.setBeepEnabled(false);*/
                ScanOptions options = new ScanOptions();
                options.setOrientationLocked(false).setCaptureActivity(CustomScannerActivity.class);
                barcodeLauncher.launch(options);
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.putExtra("myContact", myContact);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Intent originalIntent = result.getOriginalIntent();
                    if (originalIntent == null) {
                        Log.d("MainActivity", "Cancelled scan");
                        Toast.makeText(MainActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                    } else if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                        Log.d("MainActivity", "Cancelled scan due to missing camera permission");
                        Toast.makeText(MainActivity.this, "Cancelled due to missing camera permission", Toast.LENGTH_LONG).show();
                    }
                } else {

                    try {
                        String decryptedData = AESHelper.decrypt(result.getContents());
                        Log.i("KEY", "decryptedData is    " + decryptedData);
                        Contact scannedContact = new Gson().fromJson(decryptedData, Contact.class);
                        if (scannedContact.equals(myContact)) {
                            return;
                        }
                        DatabaseUtils.contactsRef.document(scannedContact.getEmail()).set(scannedContact);
                        startActivity(new Intent(MainActivity.this, ContactInfoActivity.class).putExtra("contact", scannedContact));
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Invalid QR", Toast.LENGTH_LONG).show();
                    }
                }
            });


    private class FragmentAdapter extends FragmentStatePagerAdapter {

        public FragmentAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return BusinessCard.newInstance(myContact);
            } else {
                return ContactList.newInstance(listContacts);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        //https://stackoverflow.com/questions/18088076/update-fragment-from-viewpager

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0){
                return "Home";
            } else {
                return "Contacts";
            }
        }
    }
}
