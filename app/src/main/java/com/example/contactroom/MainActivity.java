package com.example.contactroom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactroom.adapter.RecyclerViewAdapter;
import com.example.contactroom.model.Contact;
import com.example.contactroom.model.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnContactClickListener {
    private static final int NEW_CONTACT_ACTIVITY_REQUEST_CODE = 1;
    public static final String TAG = "clicked";
    public static final String CONTACT_ID = "contact_id";
    private FloatingActionButton add_contact_fab;
    private ContactViewModel contactViewModel;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private LiveData< List<Contact> > contactList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
       add_contact_fab = findViewById(R.id.add_contact_fab);
        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this.getApplication()).create(ContactViewModel.class);




        contactViewModel.getAllContacts().observe(this, contacts -> {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            //set up an adapter
            recyclerViewAdapter = new RecyclerViewAdapter(contacts, MainActivity.this,this);
            recyclerView.setAdapter(recyclerViewAdapter);

        });





        add_contact_fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewContactActivity.class);
            startActivityForResult(intent, NEW_CONTACT_ACTIVITY_REQUEST_CODE);

        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_CONTACT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){

            assert data != null;
            String name = data.getStringExtra(NewContactActivity.NAME_KEY);
            String occupation = data.getStringExtra(NewContactActivity.OCCUPATION_KEY);

            assert data != null;
          Contact contact = new Contact(name, occupation);
           ContactViewModel.insert(contact);
        }
    }

    @Override
    public void onContactClick(int position) {

        Contact contact = Objects.requireNonNull(contactViewModel.allContacts.getValue()).get(position);
        Log.d(TAG, "onContactClick: "+ contact.getName());
        Intent intent = new Intent(MainActivity.this, NewContactActivity.class);
        intent.putExtra(CONTACT_ID, contact.getId());
        startActivity(intent);

    }
}