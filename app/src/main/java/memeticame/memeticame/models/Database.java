package memeticame.memeticame.models;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import memeticame.memeticame.contacts.AddNumberActivity;
import memeticame.memeticame.contacts.ContactsAdapter;

/**
 * Created by efmino on 24-08-17.
 */

public class Database {

    public FirebaseDatabase mDatabase;
    public FirebaseAuth mAuth;
    public Boolean isMyContactResult = false;


    public void init() {
        mDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public DatabaseReference getReference(String somewhere) {
        return mDatabase.getReference(somewhere);
    }

    public boolean isMyContact(final String phone) {
        DatabaseReference usersReference =
                mDatabase.getReference("users/"+mAuth.getCurrentUser().getPhoneNumber()+"/contacts");

        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("datasnapshot", dataSnapshot.toString() + phone);

                if (dataSnapshot.hasChild(phone)) {
                    Log.d("indatabasecheck", "true");
                    isMyContactResult ^= true;
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        Log.d("returninf", isMyContactResult.toString());
        return  isMyContactResult;
    }
}
