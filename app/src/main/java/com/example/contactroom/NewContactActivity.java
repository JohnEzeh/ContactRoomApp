package com.example.contactroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.contactroom.model.Contact;
import com.example.contactroom.model.ContactViewModel;
import com.google.android.material.snackbar.Snackbar;

public class NewContactActivity extends AppCompatActivity {
    public static final String NAME_KEY = "name_key";
    public static final String OCCUPATION_KEY = "occupation_key";

    private EditText enter_name,enter_occupation;
private Button save_button;
    private ContactViewModel contactViewModel;
    private int contactId = 0;
    private boolean isEdit = false;
    private Button updateButton;
    private Button delete_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);
        enter_name = findViewById(R.id.enter_name);
        enter_occupation = findViewById(R.id.enter_occupation);
        save_button = findViewById(R.id.save_button);

        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(NewContactActivity.this
                .getApplication())
                .create(ContactViewModel.class);

        Bundle data = getIntent().getExtras();
        if (getIntent().hasExtra(MainActivity.CONTACT_ID)){
            contactId = getIntent().getIntExtra(MainActivity.CONTACT_ID, 0);
            contactViewModel.get(contactId).observe(this, contact -> {
                if (contact != null){
                    enter_name.setText(contact.getName());
                    enter_occupation.setText(contact.getOccupation());
                }

            });
            isEdit = true;
        }


        Intent replyIntent = new Intent();


        save_button.setOnClickListener(v -> {
       if (!TextUtils.isEmpty(enter_name.getText()) &&
               !TextUtils.isEmpty(enter_occupation.getText())){

           Contact contact = new Contact(enter_name.getText().toString().trim(),
                   enter_occupation.getText().toString().trim());

           String name = enter_name.getText().toString().trim();
           String occupation = enter_occupation.getText().toString().trim();

           replyIntent.putExtra(NAME_KEY, name);
           replyIntent.putExtra(OCCUPATION_KEY, occupation);

           setResult(RESULT_OK, replyIntent);

          // ContactViewModel.insert(contact);

       } else {
           setResult(RESULT_CANCELED, replyIntent);
       }



            finish();
        });



         delete_button  = findViewById(R.id.delete_button);
        //the delte button
        delete_button.setOnClickListener(v -> {
            Edit(true);
        });


        //update button
        updateButton  = findViewById(R.id.update_button);
        updateButton.setOnClickListener(v -> {

         Edit(false);

        });




        if (isEdit){
            save_button.setVisibility(View.GONE);
        } else{
            updateButton.setVisibility(View.GONE);
             delete_button.setVisibility(View.GONE
             );
        }

    }

    private void Edit(Boolean isDelete) {
        int id = contactId;
        String name = enter_name.getText().toString().trim();
        String occupation = enter_occupation.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(occupation)){
            Snackbar.make(enter_name, R.string.empty, Snackbar.LENGTH_LONG)
                    .show();
        } else {
            Contact contact = new Contact();
            contact.setId(contactId);
            contact.setName(name);
            contact.setOccupation(occupation);
            if (isDelete)
                ContactViewModel.delete(contact);
            else
            ContactViewModel.update(contact);
            finish();

        }
    }
}