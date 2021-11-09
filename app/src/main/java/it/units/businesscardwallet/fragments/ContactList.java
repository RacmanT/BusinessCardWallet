package it.units.businesscardwallet.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import it.units.businesscardwallet.R;
import it.units.businesscardwallet.activities.ContactInfoActivity;
import it.units.businesscardwallet.entities.Contact;


public class ContactList extends Fragment {

    private final ArrayList<Contact> contacts = new ArrayList<>();

    public ContactList() {
        // Required empty public constructor
    }

    public static ContactList newInstance(String param1, String param2) {
        ContactList fragment = new ContactList();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_list, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contacts.add(new Contact("Eren", "Jaeger", "Attack Titan", "singekyno@kyogin.su", 212212, "Paradise Island"));
        contacts.add(new Contact("Mikasa", "Ackerman", "EREH support", "mikasa@kyogin.su", 212212, "Paradise Island"));


        ListView listView = view.findViewById(R.id.contact_list);
        ContactAdapter adapter = new ContactAdapter(getActivity(), contacts);
        listView.setAdapter(adapter);
//        listView.setOnItemClickListener((parent, view1, position, id) -> getParentFragmentManager().beginTransaction().replace(R.id.contact_list, new ContactInfo()).addToBackStack(null).commit());
        listView.setOnItemClickListener((parent, view1, position, id) ->
                startActivity(new Intent(getActivity(), ContactInfoActivity.class).putExtra("contact", contacts.get(position)))
        );
        /*listView.setOnItemClickListener((parent, view1, position, id) -> {
            getParentFragmentManager().beginTransaction().replace(R.id.view_pager_2, new MyBusinessCard()).addToBackStack(null).commit();
        });*/

    }
}

class ContactAdapter extends ArrayAdapter<Contact> {

    public ContactAdapter(Context context, ArrayList<Contact> contact) {
        super(context, 0, contact);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_contact_row, parent, false);
        }
        TextView viewName = convertView.findViewById(R.id.row_name);
        viewName.setText(contact.getName() + " " + contact.getLastName());
       /* convertView.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), ContactInfoActivity.class);
                intent.putExtra("contact", )
                v.getContext().startActivity(intent); }
                );*/



        return convertView;

    }

}