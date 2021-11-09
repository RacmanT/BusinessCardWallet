package it.units.businesscardwallet.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import it.units.businesscardwallet.R;
import it.units.businesscardwallet.entities.Contact;


public class BusinessCard extends Fragment {


    private static final String ARG_PARAM_CONTACT = "ARG_PARAM_CONTACT";

    private Contact contact; //= new Contact("Patrick", "Bateman", "Vice President", "patrick.bateman@company.com", 343988666, "55 West 81st Street, Upper West Side");

    public BusinessCard() {
    }

    public static BusinessCard newInstance(Contact contact) {
        BusinessCard fragment = new BusinessCard();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_CONTACT, contact);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contact = (Contact) getArguments().getSerializable(ARG_PARAM_CONTACT); //https://stackoverflow.com/questions/17443081/fragment-initialization-with-complex-object
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_business_card, container, false);
        ((TextView) view.findViewById(R.id.first_name)).setText(this.contact.getName());
        ((TextView) view.findViewById(R.id.last_name)).setText(this.contact.getLastName());
        ((TextView) view.findViewById(R.id.profession)).setText(this.contact.getProfession());
        ((TextView) view.findViewById(R.id.phone_number)).setText(String.valueOf(this.contact.getPhoneNumber()));
        ((TextView) view.findViewById(R.id.email_address)).setText(this.contact.getEmail());
        ((TextView) view.findViewById(R.id.address)).setText(this.contact.getAddress());

        return view;
    }
}