package memeticame.memeticame;

import android.content.Context;
import android.database.Cursor;
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
import java.util.Map;

import memeticame.memeticame.contacts.ContactsAdapter;
import memeticame.memeticame.models.Contact;

/**
 * Created by efmino on 18-08-17.
 */

public class FragmentChats extends Fragment {

    private ArrayList<String> chats_list = new ArrayList<String>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference usersDatabase;
    private ArrayAdapter arrayAdapter;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ;
    private FirebaseUser currentUser = mAuth.getCurrentUser();


    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //TextView textView = rootView.findViewById(R.id.section_label);
        //textView.setText("In Chats fragment");


        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, chats_list);
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final ListView listview = (ListView) view.findViewById(R.id.chats_list);

        usersDatabase = FirebaseDatabase.getInstance().getReference(
                "users/");
        usersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                chats_list.clear();
                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Contact contact = userSnapshot.getValue(Contact.class);

                    HashMap<String,Boolean> map = contact.getContacts();
                    if (mAuth.getCurrentUser().getPhoneNumber().toString().equals(contact.getContact_phone().toString())) {
                        for (Map.Entry<String, Boolean> entry : map.entrySet()) {
                            chats_list.add(entry.getKey().toString());
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
}
