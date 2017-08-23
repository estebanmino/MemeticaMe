package memeticame.memeticame.contacts;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import memeticame.memeticame.MainActivity;
import memeticame.memeticame.R;
import memeticame.memeticame.models.Contact;

public class ContactsActivity extends AppCompatActivity {

    private ContactsAdapter contactsAdapter;
    private DatabaseReference usersDatabase;
    private ArrayList<Contact> array_list_contacts;
    public ArrayList<String> number_list = new ArrayList<String>();
    public ArrayList<String> added_number_list = new ArrayList<String>();
    private FirebaseAuth mAuth;


    public void getContacts() {
        array_list_contacts = new ArrayList<Contact>();

        Cursor cursor_contacts = null;
        ContentResolver contentResolver = getContentResolver();
        try {
            cursor_contacts = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    null,
                    null,
                    ContactsContract.Contacts.DISPLAY_NAME+" ASC"
            );
        } catch (Exception ex) {
            Log.e("Error in contacts", ex.getMessage());
        }
        if (cursor_contacts != null) {

            if (cursor_contacts.getCount() > 0) {

                while (cursor_contacts.moveToNext()) {
                    Contact contact = new Contact();
                    String contact_id = cursor_contacts.getString(cursor_contacts.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone._ID));
                    String contact_name = cursor_contacts.getString(cursor_contacts.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String contact_phone = cursor_contacts.getString(cursor_contacts.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));

                    contact.setContact_name(contact_name);
                    contact.setContact_id(contact_id);
                    contact.setContact_phone(contact_phone);

                    String phone_number = contact.getContact_phone().replaceAll("-","");
                    if (number_list.contains(phone_number) &&
                            !added_number_list.contains(phone_number)) {
                        array_list_contacts.add(contact);
                        added_number_list.add(phone_number);
                    }
                }
            }
        }
        assert cursor_contacts != null;
        cursor_contacts.close();
        ListView contactsListView = (ListView) findViewById(R.id.contacts_list_view);

        contactsAdapter = new ContactsAdapter(this, array_list_contacts, mAuth);
        contactsListView.setAdapter(contactsAdapter);
    }

    private void showContacts() {
        // Check the SDK version and whether the permission is already granted or not.
        if (ContextCompat.checkSelfPermission(ContactsActivity.this,
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 0);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            getContacts();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        //back toolbar
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Add contacts");
        }


        usersDatabase = FirebaseDatabase.getInstance().getReference("users");
        usersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Contact contact = userSnapshot.getValue(Contact.class);
                    number_list.add(contact.getContact_phone().toString());
                }
                showContacts();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mAuth = FirebaseAuth.getInstance();



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
                Log.d("grantResult", Arrays.toString(grantResults));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
