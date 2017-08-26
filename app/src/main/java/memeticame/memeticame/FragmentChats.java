package memeticame.memeticame;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import memeticame.memeticame.chats.ChatRoomActivity;
import memeticame.memeticame.contacts.ChatsContactsAdapter;
import memeticame.memeticame.models.Contact;
import memeticame.memeticame.models.Database;
import memeticame.memeticame.models.Phone;

/**
 * Created by efmino on 18-08-17.
 */

public class FragmentChats extends Fragment {

    private ArrayList<String> myChatsList = new ArrayList<String>();
    private ArrayList<String> chatsList = new ArrayList<String>();
    private List<String> myPhoneContactsNumbers = new ArrayList<String>();
    private List<String> myPhoneContactsNames = new ArrayList<String>();

    private ChatsContactsAdapter arrayAdapter;
    private ArrayList<Contact> myPhoneContacts;
    private ArrayList<Contact> myPhoneChatsContacts = new ArrayList<Contact>();

    private Database firebaseDatabase = new Database();
    private Phone mPhone = new Phone();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //database connection init
        firebaseDatabase.init();

        myPhoneContacts = mPhone.getContacts();
        for (Contact contact: myPhoneContacts) {
            myPhoneContactsNumbers.add(contact.getPhone());
            myPhoneContactsNames.add(contact.getName());
        }

        arrayAdapter = new ChatsContactsAdapter(getActivity(), myPhoneChatsContacts, firebaseDatabase.mAuth);
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ListView listview = (ListView) view.findViewById(R.id.chats_list);

        final DatabaseReference usersDatabase = firebaseDatabase.getReference("users/");

        usersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myChatsList.clear();
                chatsList.clear();
                myPhoneChatsContacts.clear();

                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Contact contact = userSnapshot.getValue(Contact.class);
                    HashMap<String,Boolean> MyContactsMap = contact.getContacts();

                    if(firebaseDatabase.getCurrentUser().getPhoneNumber().equals(contact.getPhone())
                            && MyContactsMap != null) {

                        for (Map.Entry<String, Boolean> entry : MyContactsMap.entrySet()) {
                            myChatsList.add(entry.getKey().toString());
                        }
                        for (String chat_number: myChatsList) {
                            if (myPhoneContactsNumbers.contains(chat_number)) {
                                int index = myPhoneContactsNumbers.indexOf(chat_number);
                                myPhoneChatsContacts.add(myPhoneContacts.get(index));
                            }
                            else {
                                Contact unknownContact = new Contact();
                                unknownContact.setName(chat_number);
                                unknownContact.setPhone(chat_number);
                                myPhoneChatsContacts.add(unknownContact);
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

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "Name selected:", Toast.LENGTH_LONG).show();
                startActivity(ChatRoomActivity.getIntent(getActivity(),
                        myPhoneChatsContacts.get(i).getName(),
                        myPhoneChatsContacts.get(i).getPhone()));
            }

        });
    }
}
