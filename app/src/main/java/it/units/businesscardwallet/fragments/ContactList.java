package it.units.businesscardwallet.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import it.units.businesscardwallet.R;
import it.units.businesscardwallet.activities.ContactInfoActivity;
import it.units.businesscardwallet.entities.Contact;


public class ContactList extends Fragment {

    private final ArrayList<Contact> contacts = new ArrayList<>();
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
            Log.i("TEST", String.valueOf(adapter.contactListFiltered.isEmpty()));
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
        contacts.add(new Contact("Mikasa", "Ackerman", "EREH support", "mikasa@kyogin.su", 212212, "Paradise Island"));

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

// TODO check why is not working

class ContactAdapter extends ArrayAdapter<Contact> {

    private List<Contact> contactList;
    protected List<Contact> contactListFiltered;

    public ContactAdapter(Context context, ArrayList<Contact> contacts) {
        super(context, 0, contacts);
        contactList = contacts;
        contactListFiltered = new ArrayList<>(contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = getItem(position);
        //Contact contact = contactListFiltered.get(position);

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
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Log.i("TEST", "constraint is " + constraint);
                FilterResults results = new FilterResults();


                        List<Contact> filteredResults = contactList.stream()
                                .filter(contact -> contact.getName().toLowerCase(Locale.ROOT)
                                        .startsWith(constraint.toString().toLowerCase(Locale.ROOT)))
                                .collect(Collectors.toList());
                        Log.i("TEST", "Inside adapter " + filteredResults.size());

                        results.values = filteredResults;
                        results.count = filteredResults.size();


                return results;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                contactListFiltered = (List<Contact>) results.values;
                //ContactAdapter.this.notifyDataSetChanged();
                if(results.count > 0)
                {
                    notifyDataSetChanged();
                }else{
                    notifyDataSetInvalidated();
                }

            }


        };
    }
}