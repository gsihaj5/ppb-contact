package com.example.contact;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactAdapter extends ArrayAdapter<Contact> {
    Database database;

    public ContactAdapter(Context context, ArrayList<Contact> contacts) {
        super(context, 0, contacts);
        database = new Database(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_list, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView number = (TextView) convertView.findViewById(R.id.number);
        Button buttonDelete = (Button) convertView.findViewById(R.id.delete);
        name.setText(contact.getName());
        number.setText(contact.getNumber());

        buttonDelete.setOnClickListener(view -> {
            remove(contact);
            database.deleteContact(contact.getName());
        });

        return convertView;
    }
}
