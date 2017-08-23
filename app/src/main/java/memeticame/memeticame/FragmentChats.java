package memeticame.memeticame;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.BoolRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import memeticame.memeticame.contacts.ContactsAdapter;
import memeticame.memeticame.models.Contact;

/**
 * Created by efmino on 18-08-17.
 */

public class FragmentChats extends Fragment {

    private ArrayList<String> my_chats_list = new ArrayList<String>();
    private ArrayList<String> chats_list = new ArrayList<String>();
    private List<String> my_phone_contacts_numbers = new ArrayList<String>();
    private List<String> my_phone_contacts_names = new ArrayList<String>();
    private ArrayList<Contact> my_phone_contacts;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersDatabase;
    private ArrayAdapter arrayAdapter;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();


    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        my_phone_contacts = getContacts();
        for (Contact contact: my_phone_contacts) {
            my_phone_contacts_numbers.add(contact.getContact_phone());
            my_phone_contacts_names.add(contact.getContact_name());
        }
        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, chats_list);
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ListView listview = (ListView) view.findViewById(R.id.chats_list);

        usersDatabase = FirebaseDatabase.getInstance().getReference("users/");

        usersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                my_chats_list.clear();
                chats_list.clear();

                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Contact contact = userSnapshot.getValue(Contact.class);
                    HashMap<String,Boolean> MyContactsMap = contact.getContacts();

                    if(currentUser.getPhoneNumber().equals(contact.getContact_phone())
                            && MyContactsMap != null) {

                        for (Map.Entry<String, Boolean> entry : MyContactsMap.entrySet()) {
                            my_chats_list.add(entry.getKey().toString());
                        }
                        for (String chat_number: my_chats_list) {
                            if (my_phone_contacts_numbers.contains(chat_number)) {
                                int index = my_phone_contacts_numbers.indexOf(chat_number);
                                chats_list.add(my_phone_contacts_names.get(index));
                            }
                            else {
                                chats_list.add("Desconocido");

                            }
                        }
                    }
                }
                arrayAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listview.setAdapter(arrayAdapter);
    }

    public ArrayList<Contact>  getContacts() {
        ArrayList<Contact> array_list_contacts = new ArrayList<Contact>();

        final ArrayList<String> number_list = new ArrayList<String>();

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
                    contact.setContact_name(contact_name);
                    contact.setContact_phone(contact_phone.replace(" ",",").replace("-",""));
                    array_list_contacts.add(contact);
                }
            }
        }
        return array_list_contacts;
    }
}
