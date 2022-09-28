package com.example.contact;

import android.os.Build;
import android.os.Bundle;
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

import java.util.ArrayList;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private Database db;
    private ContactAdapter adapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

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
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
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