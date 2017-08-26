package memeticame.memeticame;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private ArrayAdapter arrayAdapter;
    private ArrayList<Contact> myPhoneContacts;

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

        arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, chatsList);
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
}
