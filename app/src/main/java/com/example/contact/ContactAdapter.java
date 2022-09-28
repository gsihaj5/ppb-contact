package com.example.contact;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

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

        TextInputEditText name = (TextInputEditText) convertView.findViewById(R.id.name);
        TextInputEditText number = (TextInputEditText) convertView.findViewById(R.id.number);

        ImageButton buttonDelete = (ImageButton) convertView.findViewById(R.id.delete);
        ImageButton buttonUpdate = (ImageButton) convertView.findViewById(R.id.update);
        name.setText(contact.getName());
        number.setText(contact.getNumber());

        buttonDelete.setOnClickListener(view -> {
            remove(contact);
            database.deleteContact(contact.getName());
        });

        buttonUpdate.setOnClickListener(view -> {
            database.updateContact(contact.getName(), new Contact(name.getText().toString(), number.getText().toString()));
            Toast.makeText(view.getContext(), contact.getName() + " updated to " + name.getText().toString(), Toast.LENGTH_LONG).show();
        });

        return convertView;
    }
}
