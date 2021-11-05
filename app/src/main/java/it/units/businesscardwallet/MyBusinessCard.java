package it.units.businesscardwallet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class MyBusinessCard extends Fragment {

    private Contact myContact = new Contact("Patrick", "Bateman", "Vice President", "patrick.bateman@company.com", 343988666, "55 West 81st Street, Upper West Side");

    public MyBusinessCard() {
    }


/*
    public static MyBusinessCard newInstance(String param1, String param2) {
        MyBusinessCard fragment = new MyBusinessCard();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_business_card, container, false);
        ((TextView) view.findViewById(R.id.first_name)).setText(myContact.getName());
        ((TextView) view.findViewById(R.id.last_name)).setText(myContact.getLastName());
        ((TextView) view.findViewById(R.id.phone_number)).setText(String.valueOf(myContact.getPhoneNumber()));
        ((TextView) view.findViewById(R.id.email_address)).setText(myContact.getEmail());
        ((TextView) view.findViewById(R.id.address)).setText(myContact.getAddress());

        return view;
    }
}