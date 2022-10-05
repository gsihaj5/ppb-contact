package com.example.contact;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.contact.databinding.FragmentFirstBinding;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private Database db;
    private ContactAdapter adapter;
    private LayoutInflater inflater;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        this.inflater = inflater;
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new Database(getContext());

        loadAllContact();

        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = String.valueOf(binding.namaInput.getText());

                try {
                    Contact contact = db.getContact(name);
                    ArrayList<Contact> contacts = new ArrayList<>();
                    contacts.add(contact);
                    ListView listView = binding.contactList;

                    adapter = new ContactAdapter(getContext(), contacts);
                    listView.setAdapter(adapter);

                } catch (Exception e) {
                    Toast.makeText(view.getContext(), "Not Found", Toast.LENGTH_LONG).show();
                }
            }
        });
        binding.clearSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAllContact();

            }
        });
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show_modal();
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    private void show_modal() {
        View addContactModal = inflater.inflate(R.layout.add_contact_dialog, null);

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setView(addContactModal);

        final TextInputEditText namaInput = addContactModal.findViewById(R.id.nama_input);
        final TextInputEditText telpInput = addContactModal.findViewById(R.id.telp_input);

        dialog.setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String name = String.valueOf(namaInput.getText());
                                String number = String.valueOf(telpInput.getText());
                                Contact contact = new Contact(name, number);

                                db.addContact(contact);
                                Toast.makeText(getContext(), "Created", Toast.LENGTH_LONG).show();
                            }
                        }
                )
                .setNegativeButton("Button", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        dialog.show();
    }

    private void loadAllContact() {

        ListView listView = binding.contactList;
        ArrayList<Contact> contacts = db.getAllContact();

        adapter = new ContactAdapter(getContext(), contacts);
        listView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}