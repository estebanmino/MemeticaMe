package memeticame.memeticame.contacts;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import memeticame.memeticame.MainActivity;
import memeticame.memeticame.R;
import memeticame.memeticame.models.Contact;
import memeticame.memeticame.models.Database;
import memeticame.memeticame.models.Phone;

public class ContactsActivity extends AppCompatActivity {

    private ContactsAdapter contactsAdapter;
    private DatabaseReference usersDatabase;
    private ArrayList<Contact> myPhoneContacts;
    private List<String> myPhoneContactsNumbers = new ArrayList<String>();
    private List<String> myPhoneContactsNames = new ArrayList<String>();

    public ArrayList<String> numberList = new ArrayList<String>();
    public ArrayList<String> addedNumberList = new ArrayList<String>();

    private Database firebaseDatabase = new Database();
    private Phone mPhone = new Phone();
    private ArrayList<Contact> myPhoneContactsInDatabase = new ArrayList<Contact>();


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        else if (item.getItemId() == R.id.add_contacts) {
            startActivity(AddNumberActivity.getIntent(ContactsActivity.this));
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context,ContactsActivity.class);
        return intent;
    }

    public void getContacts() {
        myPhoneContacts = mPhone.getContacts();
        for (Contact contact: myPhoneContacts) {
            myPhoneContactsNumbers.add(contact.getPhone());
            myPhoneContactsNames.add(contact.getName());
        }

        for (String contact_number: numberList) {
            if (myPhoneContactsNumbers.contains(contact_number)) {
                int index = myPhoneContactsNumbers.indexOf(contact_number);
                myPhoneContactsInDatabase.add(myPhoneContacts.get(index));
            }
        }

        ListView contactsListView = (ListView) findViewById(R.id.contacts_list_view);

        contactsAdapter = new ContactsAdapter(this, myPhoneContactsInDatabase, firebaseDatabase.mAuth);
        contactsListView.setAdapter(contactsAdapter);
    }

    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (ContextCompat.checkSelfPermission(ContactsActivity.this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 0);
        } else {
            getContacts();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        //database init
        firebaseDatabase.init();

        //back toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Add contacts");
        }

        usersDatabase = firebaseDatabase.getReference("users");
        usersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numberList.clear();
                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Contact contact = userSnapshot.getValue(Contact.class);
                    numberList.add(contact.getPhone().toString());
                }
                showContacts();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted
                    getContacts();

                } else {

                    // permission denied
                    Intent intent = new Intent(ContactsActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(this, "Until you grant the permission, we canot display the contacts", Toast.LENGTH_LONG).show();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}
