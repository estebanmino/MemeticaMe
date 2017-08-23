package memeticame.memeticame;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import memeticame.memeticame.contacts.ContactsAdapter;
import memeticame.memeticame.models.Contact;

/**
 * Created by efmino on 18-08-17.
 */

public class FragmentChats extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //TextView textView = rootView.findViewById(R.id.section_label);
        //textView.setText("In Chats fragment");



        return inflater.inflate(R.layout.fragment_chats, container, false);
    }
}
