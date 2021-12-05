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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import it.units.businesscardwallet.R;
import it.units.businesscardwallet.activities.ContactInfoActivity;
import it.units.businesscardwallet.entities.Contact;

public class ContactList extends Fragment {

    private static final String ARG_PARAM_LIST = "ARG_PARAM_LIST";
    private List<Contact> contactList;
    //private ArrayAdapter<Contact> adapter;
    private ContactAdapter adapter2;
    private ListView listView;
    private TextView addSomeone;

    public ContactList() {

    }

    public static ContactList newInstance(ArrayList<Contact> list) {
        ContactList fragment = new ContactList();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_LIST, list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        contactList = (ArrayList<Contact>) getArguments().getSerializable(ARG_PARAM_LIST);
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

        listView = view.findViewById(R.id.contact_list);
        addSomeone = view.findViewById(R.id.add_someone);

        //adapter = new ArrayAdapter<>(getContext(), R.layout.fragment_contact_row, R.id.row_name, list);
        adapter2 = new ContactAdapter(getContext(), contactList);

        //listView.setAdapter(adapter);
        listView.setAdapter(adapter2);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
                    Intent intent = new Intent(getActivity(), ContactInfoActivity.class);
                    intent.putExtra("contact", contactList.get(position));
                    startActivity(intent);
                }
        );

        if (contactList.isEmpty()) {
            listView.setVisibility(View.GONE);
            addSomeone.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
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
            /*if(adapter != null){
                adapter.getFilter().filter(newText);
            }*/

            if (adapter2 != null) {
                adapter2.getFilter().filter(newText);
            }

            return false;
        }
    };



   private class ContactAdapter extends ArrayAdapter<Contact> {

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
                filteredContacts.clear();
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

}




