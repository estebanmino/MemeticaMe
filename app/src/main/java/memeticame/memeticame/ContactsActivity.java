package memeticame.memeticame;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactsActivity extends AppCompatActivity {

    private ListView contacts_list_view;
    private ContactsAdapter contactsAdapter;

    public void getContacts() {
        ArrayList<Contact> array_list_contacts = new ArrayList<Contact>();

        Log.d("print", "getContacts");

        Cursor cursor_contacts = null;
        ContentResolver contentResolver = getContentResolver();
        try {
            cursor_contacts = contentResolver.query(
                    ContactsContract.Contacts.CONTENT_URI,
                    null,
                    null,
                    null,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" ASC"
            );
        } catch (Exception ex) {
            Log.e("Error in contacts", ex.getMessage());
        };
        Log.d("getcount", "ooo " +  cursor_contacts.getCount());
        if (cursor_contacts.getCount() > 0) {

            while (cursor_contacts.moveToNext()) {
                Contact contact = new Contact();
                String contact_id = cursor_contacts.getString(cursor_contacts.getColumnIndex(ContactsContract.Contacts._ID));
                String contact_name = cursor_contacts.getString(cursor_contacts.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                contact.setContact_name(contact_name);
                //contact.setContact_id(contact_id);
                /*TO PHONE
                if (Integer.parseInt(cursor_contacts.getString(cursor_contacts.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {

                    Cursor cursor_phones = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contact_id,
                            null,
                            null
                    );
                    while (cursor_phones.moveToNext()) {
                        String contact_phone = cursor_phones.getString(cursor_phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contact.setContact_phone(contact_phone);
                    }
                    cursor_phones.close();
                }*/
                array_list_contacts.add(contact);
            }

        }
        //Toast.makeText(this, "getContact" , Toast.LENGTH_SHORT).show();
        contacts_list_view = (ListView)findViewById(R.id.contacts_list_view);
        //Log.d("this is my array", "arr: " + array_list_names);


        //adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,array_list_names);
        contactsAdapter = new ContactsAdapter(this, array_list_contacts);
        contacts_list_view.setAdapter(contactsAdapter);

        contacts_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                try {
                    Contact contact = (Contact)contactsAdapter.getItem(position);
                    Log.e("contact", "-"+contact.getContact_name());
                    Toast.makeText(getBaseContext(), "Name selected: "+ contact.getContact_name(), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    };

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



        showContacts();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                Log.d("grantResult", ""+grantResults);
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
