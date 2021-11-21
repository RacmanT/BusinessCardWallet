package it.units.businesscardwallet.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;

import androidx.appcompat.widget.SearchView;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import it.units.businesscardwallet.R;
import it.units.businesscardwallet.activities.ContactInfoActivity;
import it.units.businesscardwallet.entities.Contact;


public class ContactList extends Fragment {

    private final List<Contact> contacts = new ArrayList<>();
    private ContactAdapter adapter;

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
        setHasOptionsMenu(true); // check if maintain
        super.onCreate(savedInstanceState);
    }


    // ========
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        //searchItem.expandActionView();
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getResources().getString(R.string.search));
        searchView.setOnQueryTextListener(onQueryTextListener);

    }

    private final SearchView.OnQueryTextListener onQueryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            adapter.getFilter().filter(newText);
            return false;
        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
    // ========

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_list, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        contacts.add(new Contact("Eren", "Jaeger", "Attack Titan", "singekyno@kyogin.su", 212212, "Paradise Island"));
        contacts.add(new Contact("Evelyn", "Richards", "EREH support", "mikasa@kyogin.su", 212212, "Paradise Island"));
        contacts.add(new Contact("Luis", "Carruthers", "EREH support", "mikasa@kyogin.su", 212212, "Paradise Island"));
        contacts.add(new Contact("Paul", "Owen", "EREH support", "mikasa@kyogin.su", 212212, "Paradise Island"));

        ListView listView = view.findViewById(R.id.contact_list);
        adapter = new ContactAdapter(getActivity(), contacts);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view1, position, id) -> {
                    Intent intent = new Intent(getActivity(), ContactInfoActivity.class);
                    intent.putExtra("contact", contacts.get(position));
                    startActivity(intent);
                }
        );
    }
}


class ContactAdapter extends ArrayAdapter<Contact> {

    private final List<Contact> contacts;
    private List<Contact> filteredContacts;
    private Filter filter;

    public ContactAdapter(Context context, List<Contact> contacts) {
        super(context, 0, contacts);
        this.contacts = new ArrayList<>(contacts);
        this.filteredContacts = new ArrayList<>(contacts);
        this.filter = new ContactFilter();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = filteredContacts.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_contact_row, parent, false);
        }
        TextView viewName = convertView.findViewById(R.id.row_name);
        String displayedName = contact.getName() + " " + contact.getLastName();
        viewName.setText(displayedName);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new ContactFilter();
        }
        return filter;
    }

    private class ContactFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            String prefix = constraint.toString().toLowerCase(Locale.ROOT);

            if (prefix.length() == 0) {
                results.values = contacts;
                results.count = contacts.size();
            } else {
                final List<Contact> nlist = contacts.stream().filter(contact ->
                        contact.getName().toLowerCase(Locale.ROOT).startsWith(prefix))
                        .collect(Collectors.toList());

                results.values = nlist;
                results.count = nlist.size();
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredContacts = (List<Contact>) results.values;
            notifyDataSetChanged();
            clear();
            filteredContacts.forEach(filteredContact -> {
                add(filteredContact);
                notifyDataSetInvalidated();
            });
        }


    }

}