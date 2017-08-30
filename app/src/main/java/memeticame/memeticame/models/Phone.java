package memeticame.memeticame.models;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

import memeticame.memeticame.MainActivity;

/**
 * Created by efmino on 24-08-17.
 */

public class Phone {

    public ArrayList<Contact> getContacts() {
        ArrayList<Contact> array_list_contacts = new ArrayList<>();
        Context applicationContext = MainActivity.getContextOfApplication();
        Cursor cursor_contacts = null;

        ContentResolver contentResolver = applicationContext.getContentResolver();
        try {
            cursor_contacts = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    null,
                    null,
                    ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            );
        } catch (Exception ex) {
            Log.e("Error in contacts", ex.getMessage());
        }
        if (cursor_contacts != null) {

            if (cursor_contacts.getCount() > 0) {

                while (cursor_contacts.moveToNext()) {
                    Contact contact = new Contact();
                    String contact_name = cursor_contacts.getString(cursor_contacts.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String contact_phone = cursor_contacts.getString(cursor_contacts.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));
                    contact.setName(contact_name);
                    contact.setPhone(contact_phone.replace(" ",",").replace("-",""));
                    array_list_contacts.add(contact);
                }
            }
            cursor_contacts.close();
        }
        return array_list_contacts;
    }
}
