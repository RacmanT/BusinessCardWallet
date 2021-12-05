package it.units.businesscardwallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import it.units.businesscardwallet.entities.Contact;

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