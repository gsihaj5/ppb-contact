package com.example.contact;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Toast;


import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ContactAdapter extends ArrayAdapter<Contact> {
    Database database;

    public ContactAdapter(Context context, ArrayList<Contact> contacts) {
        super(context, 0, contacts);
        database = new Database(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextInputEditText name;
        private final TextInputEditText number;

        private final ImageButton buttonDelete;
        private final ImageButton buttonUpdate;

        public ViewHolder(View view) {
            super(view);

            name = view.findViewById(R.id.name);
            number = view.findViewById(R.id.number);

            buttonDelete = view.findViewById(R.id.delete);
            buttonUpdate = view.findViewById(R.id.update);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Contact contact = getItem(position);

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_list, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(contact.getName());
        viewHolder.number.setText(contact.getNumber());

        viewHolder.buttonDelete.setOnClickListener(view -> {
            remove(contact);
            database.deleteContact(contact.getName());
        });

        viewHolder.buttonUpdate.setOnClickListener(view -> {
            database.updateContact(contact.getName(),
                    new Contact(
                            viewHolder.name.getText().toString(),
                            viewHolder.number.getText().toString()
                    ));
            Toast.makeText(view.getContext(), contact.getName() + " updated to " + viewHolder.name.getText().toString(), Toast.LENGTH_LONG).show();
        });


        return convertView;
    }
}
