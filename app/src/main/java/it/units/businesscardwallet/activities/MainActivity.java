package it.units.businesscardwallet.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.google.android.material.tabs.TabLayout;
import com.google.zxing.integration.android.IntentIntegrator;

import java.lang.reflect.Method;

import it.units.businesscardwallet.fragments.ContactList;
import it.units.businesscardwallet.R;
import it.units.businesscardwallet.fragments.BusinessCard;
import it.units.businesscardwallet.entities.Contact;

public class MainActivity extends AppCompatActivity {


    //https://stackoverflow.com/questions/55280032/how-to-update-document-in-firestore

    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private Contact myContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager_2);

        //TODO: fetch with database
        myContact = new Contact("Patrick", "Bateman", "Vice President", "patrick.bateman@company.com", 343988666, "55 West 81st Street, Upper West Side");

        FragmentManager fm = getSupportFragmentManager();
        FragmentAdapter adapter = new FragmentAdapter(fm, getLifecycle());
        viewPager2.setAdapter(adapter);

        tabLayout.addOnTabSelectedListener(onTabSelectedListener);
        viewPager2.registerOnPageChangeCallback(onPageChangeCallback);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_add:
                // TODO add qr code scanner
                new IntentIntegrator(this).initiateScan();
                return true;
            case R.id.action_print:
                return true;
            case R.id.action_settings:
                return true;
            case R.id.action_share:
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

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

    private class FragmentAdapter extends FragmentStateAdapter {
        public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 1) {
                return new ContactList();
            }
            return BusinessCard.newInstance(myContact);
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }


}
