package memeticame.memeticame;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import memeticame.memeticame.models.Contact;

/**
 * Created by efmino on 18-08-17.
 */

public class FragmentChats extends Fragment {

    private ArrayList<String> myChatsList = new ArrayList<String>();
    private ArrayList<String> chatsList = new ArrayList<String>();
    private List<String> myPhoneContactsNumbers = new ArrayList<String>();
    private List<String> myPhoneContactsNames = new ArrayList<String>();

    private DatabaseReference usersDatabase;
    private ArrayAdapter arrayAdapter;
    private ArrayList<Contact> myPhoneContacts;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();


    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        myPhoneContacts = getContacts();
        for (Contact contact: myPhoneContacts) {
            myPhoneContactsNumbers.add(contact.getPhone());
            myPhoneContactsNames.add(contact.getName());
        }
        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, chatsList);
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
                myChatsList.clear();
                chatsList.clear();

                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Contact contact = userSnapshot.getValue(Contact.class);
                    HashMap<String,Boolean> MyContactsMap = contact.getContacts();

                    if(currentUser.getPhoneNumber().equals(contact.getPhone())
                            && MyContactsMap != null) {

                        for (Map.Entry<String, Boolean> entry : MyContactsMap.entrySet()) {
                            myChatsList.add(entry.getKey().toString());
                        }
                        for (String chat_number: myChatsList) {
                            if (myPhoneContactsNumbers.contains(chat_number)) {
                                int index = myPhoneContactsNumbers.indexOf(chat_number);
                                chatsList.add(myPhoneContactsNames.get(index));
                            }
                            else {
                                chatsList.add("Desconocido");

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
                    contact.setName(contact_name);
                    contact.setPhone(contact_phone.replace(" ",",").replace("-",""));
                    array_list_contacts.add(contact);
                }
            }
        }
        return array_list_contacts;
    }
}
