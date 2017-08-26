package memeticame.memeticame.contacts;

import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import memeticame.memeticame.R;
import memeticame.memeticame.models.Contact;
import memeticame.memeticame.models.Database;

public class AddNumberActivity extends AppCompatActivity {


    private Database firebaseDatabase = new Database();
    private ContactsAdapter contactsAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        //back toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //check for child number
        firebaseDatabase.init();
        final DatabaseReference usersReference = firebaseDatabase.getReference("users/");


        //button search contact
        final EditText editContactNumber = (EditText) findViewById(R.id.edit_contact_number);
        final Button btnSearchContactNumber = (Button) findViewById(R.id.btn_search_contact_number);
        final ListView listResults = (ListView) findViewById(R.id.list_results);
        final ArrayList<Contact> myPhoneContactsInDatabase = new ArrayList<Contact>();

        final Contact foundContact = new Contact();

        btnSearchContactNumber.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myPhoneContactsInDatabase.clear();
                // your handler code here
                usersReference.addListenerForSingleValueEvent(new ValueEventListener() {


                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(editContactNumber.getText().toString().replace(" ",""))
                                && !editContactNumber.getText().toString().isEmpty()) {
                            DataSnapshot userSnapshot = dataSnapshot.child(editContactNumber.getText().toString().replace(" ",""));
                            Contact foundContact = userSnapshot.getValue(Contact.class);
                            myPhoneContactsInDatabase.add(foundContact);
                            contactsAdapter = new ContactsAdapter(AddNumberActivity.this, myPhoneContactsInDatabase, firebaseDatabase.mAuth);
                            listResults.setAdapter(contactsAdapter);
                        }
                        else {
                            Contact foundContact = new Contact();
                            foundContact.setName("NUMBER NOT FOUND");
                            myPhoneContactsInDatabase.add(foundContact);
                            ArrayAdapter arrayAdapter = new ArrayAdapter(AddNumberActivity.this,
                                    android.R.layout.simple_list_item_1, myPhoneContactsInDatabase);
                            listResults.setAdapter(arrayAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });

        //list view


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_add_number, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context,AddNumberActivity.class);
        return intent;
    }
}
