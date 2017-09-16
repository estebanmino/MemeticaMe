package memeticame.memeticame;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import memeticame.memeticame.chats.ChatRoomActivity;
import memeticame.memeticame.contacts.ChatsContactsAdapter;
import memeticame.memeticame.models.Contact;
import memeticame.memeticame.models.Database;
import memeticame.memeticame.models.Phone;

/**
 * Created by efmino on 18-08-17.
 */

public class FragmentChats extends Fragment {

    private ArrayList<String> myChatsList = new ArrayList<>();
    private ArrayList<String> chatsList = new ArrayList<>();
    private List<String> myPhoneContactsNumbers = new ArrayList<>();
    private List<String> myPhoneContactsNames = new ArrayList<>();

    private ChatsContactsAdapter arrayAdapter;
    private ArrayList<Contact> myPhoneContacts;
    private ArrayList<Contact> myPhoneChatsContacts = new ArrayList<>();

    private Database firebaseDatabase = new Database();
    private Phone mPhone = new Phone();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //database connection init
        firebaseDatabase.init();

        myPhoneContacts = mPhone.getContacts();
        for (Contact contact: myPhoneContacts) {
            myPhoneContactsNumbers.add(contact.getPhone());
            myPhoneContactsNames.add(contact.getEmail());
        }

        arrayAdapter = new ChatsContactsAdapter(getActivity(), myPhoneChatsContacts, firebaseDatabase.mAuth);
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ListView listview = view.findViewById(R.id.chats_list);

        listenChatContacts();

        listview.setAdapter(arrayAdapter);

        listenChatClick(listview);
    }

    private void listenChatClick(ListView listview) {
        listview.setOnItemClickListener((adapterView, view1, position, l) -> {

            //chat rooms uuid
            final DatabaseReference chatRoomReference = firebaseDatabase.getReference("users/"+
                    firebaseDatabase.getCurrentUser().getPhoneNumber()+"/contacts/");

            chatRoomReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(myPhoneChatsContacts.get(position).getPhone()) != null){
                        String chatRoomUid = dataSnapshot.child(
                                myPhoneChatsContacts.get(position).getPhone()).getValue().toString();
                        startActivity(ChatRoomActivity.getIntent(getActivity(),
                                myPhoneChatsContacts.get(position).getEmail(),
                                myPhoneChatsContacts.get(position).getPhone(),
                                chatRoomUid));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        });
    }

    private void listenChatContacts() {
        final DatabaseReference usersDatabase = firebaseDatabase.getReference("users/");

        usersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myChatsList.clear();
                chatsList.clear();
                myPhoneChatsContacts.clear();

                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    Contact contact = userSnapshot.getValue(Contact.class);
                    HashMap<String,String> MyContactsMap = contact.getContacts();

                    if(firebaseDatabase.getCurrentUser().getEmail().equals(contact.getEmail())
                            && MyContactsMap != null) {

                        myChatsList.addAll(MyContactsMap.entrySet().stream().map(
                                entry -> entry.getKey()).collect(Collectors.toList()));
                        for (String chat_number: myChatsList) {
                            if (myPhoneContactsNumbers.contains(chat_number)) {
                                int index = myPhoneContactsNumbers.indexOf(chat_number);
                                myPhoneChatsContacts.add(myPhoneContacts.get(index));
                            }
                            else {
                                Contact unknownContact = new Contact();
                                unknownContact.setEmail(chat_number);
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
    }
}
