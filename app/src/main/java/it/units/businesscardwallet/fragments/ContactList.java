package it.units.businesscardwallet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import it.units.businesscardwallet.R;
import it.units.businesscardwallet.activities.ContactInfoActivity;
import it.units.businesscardwallet.entities.Contact;
import it.units.businesscardwallet.utils.DatabaseUtils;


public class ContactList extends Fragment {

    private static final String ARG_PARAM_LIST = "ARG_PARAM_LIST";
    //private final List<Contact> contacts = new ArrayList<>();
    private List<Contact> list;
    private ArrayAdapter<Contact> adapter;
    private ListView listView;

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
        list = (ArrayList<Contact>)getArguments().getSerializable(ARG_PARAM_LIST);
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


        adapter = new ArrayAdapter<>(getContext(), R.layout.fragment_contact_row, R.id.row_name, list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view1, position, id) -> {
                    Intent intent = new Intent(getActivity(), ContactInfoActivity.class);
                    intent.putExtra("contact", list.get(position));
                    startActivity(intent);
                }
        );
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
            if(adapter != null){
                adapter.getFilter().filter(newText);
            }
            return false;
        }
    };

}




