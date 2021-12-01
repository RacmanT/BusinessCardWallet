package it.units.businesscardwallet.activities;

import static androidx.viewpager.widget.PagerAdapter.POSITION_NONE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.adapter.FragmentViewHolder;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.gson.Gson;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

import it.units.businesscardwallet.R;
import it.units.businesscardwallet.entities.Contact;
import it.units.businesscardwallet.fragments.BusinessCard;
import it.units.businesscardwallet.fragments.ContactList;
import it.units.businesscardwallet.fragments.LoginFragment;
import it.units.businesscardwallet.utils.AESHelper;
import it.units.businesscardwallet.utils.DatabaseUtils;


public class MainActivity extends AppCompatActivity {

    //https://stackoverflow.com/questions/55280032/how-to-update-document-in-firestore

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private Contact myContact;
    private final ArrayList<Contact> listContacts = new ArrayList<>();
    private FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatabaseUtils.init();
        redirectToLogin();


        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager_2);
        //viewPager2.setSaveEnabled(false);

        // TODO can be removed
        //DatabaseUtils.userRef.get().addOnSuccessListener(documentSnapshot -> {
            //myContact = documentSnapshot.toObject(Contact.class);
        myContact = new Contact("Mario", "Toni", "gommista", "mario@toni.lo", "3213321", "23, Via mare, Narnia");

        adapter = new FragmentAdapter(getSupportFragmentManager(), getLifecycle());

            viewPager2.setAdapter(adapter);

            /*new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
                if (position == 0) {
                    tab.setText("Home");
                } else {
                    tab.setText("Contacts");
                }
            }).attach();*/
            tabLayout.addOnTabSelectedListener(onTabSelectedListener);
            viewPager2.registerOnPageChangeCallback(onPageChangeCallback);

       // });


        /*new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
                if (position == 0) {
                    tab.setText("Home");
                } else {
                    tab.setText("Contacts");
                }
            }).attach();*/

       /* DatabaseUtils.contactsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            queryDocumentSnapshots.getDocuments().forEach(doc -> listContacts.add(doc.toObject(Contact.class)));
        });*/
        listContacts.add(myContact);

    }


    /*@Override
    protected void onResume() {
        super.onResume();
        redirectToLogin();

        DatabaseUtils.userRef.get().addOnSuccessListener(documentSnapshot -> {
            myContact = documentSnapshot.toObject(Contact.class);
            Log.i("TEST", myContact.toString());
            if (adapter != null) {

                adapter.notifyItemChanged(0);
            }
        });

        listContacts.clear();
        DatabaseUtils.contactsRef.get().addOnSuccessListener(queryDocumentSnapshots -> {
            queryDocumentSnapshots.getDocuments().forEach(doc -> listContacts.add(doc.toObject(Contact.class)));
            if (adapter != null) {
                adapter.notifyItemChanged(1);
            }
        });

    }*/


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
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


    private final TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            viewPager2.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };

    private final ViewPager2.OnPageChangeCallback onPageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);

            tabLayout.selectTab(tabLayout.getTabAt(position));
        }
    };

    private void redirectToLogin() {
        if (DatabaseUtils.userIsNotLogged()) {
            Intent intent = new Intent(MainActivity.this, AuthenticationActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private class FragmentAdapter extends FragmentStateAdapter {

        FragmentManager fragmentManager;

        public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
            this.fragmentManager = fragmentManager;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 1) {
                return ContactList.newInstance(listContacts);
            }
            return BusinessCard.newInstance(myContact);
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        @Override
        public long getItemId(int position) {
            String tag = "f" + position;
            if (fragmentManager.findFragmentByTag(tag) != null) {
                Log.i("TEST", fragmentManager.findFragmentByTag(tag).toString() + " at position " + position);
                return (long) fragmentManager.findFragmentByTag(tag).hashCode();
            }
            return super.getItemId(position);


        }

        @Override
        public boolean containsItem(long itemId) {
            Log.i("TEST", "itemId is " + itemId);
            fragmentManager.getFragments().stream().forEach(fragment -> Log.i("TEST", "Hash code is " + fragment.hashCode()));
                    Log.i("TEST", "Here is " + fragmentManager.getFragments().stream().anyMatch(fragment -> (long) fragment.hashCode() == itemId));
            return super.containsItem(itemId);
            //return fragmentManager.getFragments().stream().anyMatch(fragment -> fragment.hashCode() == itemId);
        }


    }
}
