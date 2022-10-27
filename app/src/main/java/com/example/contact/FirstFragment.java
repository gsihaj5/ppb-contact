package com.example.contact;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.contact.databinding.FragmentFirstBinding;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private Database db;
    private ContactAdapter adapter;
    private LayoutInflater inflater;
    private ArrayList<HashMap<String, String>> list_contact;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        this.inflater = inflater;
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        list_contact = new ArrayList<>();
        return binding.getRoot();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = new Database(getContext());

//        loadAllContact();
        new GetContactService().execute();

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
//                loadAllContact();

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
        final Button camInput = addContactModal.findViewById(R.id.open_cam);
        camInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent camIntent = new Intent(view.getContext(), CameraActivity.class);
                startActivity(camIntent);
            }
        });


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

    private class GetContactService extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getContext(), "Loading JSON Data", Toast.LENGTH_LONG)
                    .show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();    // Making a request to url and getting response
            String url = "https://gist.githubusercontent.com/Baalmart/8414268/raw/43b0e25711472de37319d870cb4f4b35b1ec9d26/contacts";
            String jsonStr = sh.makeServiceCall(url);
            Log.e("Main", "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);                // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("contacts");                // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String id = c.getString("id");
                        String name = c.getString("name");
                        String email = c.getString("email");
                        String address = c.getString("address");
                        String gender = c.getString("gender");                // Phone node is JSON Object
                        JSONObject phone = c.getJSONObject("phone");
                        String mobile = phone.getString("mobile");
                        String home = phone.getString("home");
                        String office = phone.getString("office");                    // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();                // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("name", name);
                        contact.put("email", email);
                        contact.put("mobile", mobile);                // adding contact to contact list
                        list_contact.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e("Main", "Json parsing error: " + e.getMessage());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Log.e("Main", "Couldn't get json from server.");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            ListAdapter adapter = new SimpleAdapter(getContext(), list_contact, R.layout.contact_api_list, new String[]{"id", "name", "email", "mobile"}, new int[]{R.id.id, R.id.name, R.id.email, R.id.mobile});
            binding.contactList.setAdapter(adapter);
        }
    }

}