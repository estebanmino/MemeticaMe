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

import java.security.Timestamp;
import java.util.Date;
import java.util.UUID;

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
                if (dataSnapshot.hasChild(phone)) {
                    isMyContactResult ^= true;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return  isMyContactResult;
    }

    public void sendMessageTo(final String content, final String receiverPhone) {
        DatabaseReference currentUserCOntactsReference = mDatabase.getReference("users/"+
                mAuth.getCurrentUser().getPhoneNumber()+"/contacts");

        final String uuidMessage = UUID.randomUUID().toString();
        final String chatRoomUuid;
        currentUserCOntactsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(receiverPhone)) {
                    final String referencePath = "chatRooms/"+dataSnapshot.
                            child(receiverPhone).getValue().toString()+"/"+uuidMessage+"/";

                    final DatabaseReference contentReference = mDatabase.getReference(referencePath+"content");
                    final DatabaseReference authorReference = mDatabase.getReference(referencePath+"author");
                    final DatabaseReference timestampReference = mDatabase.getReference(referencePath+"timestamp");

                    contentReference.setValue(content);
                    authorReference.setValue(mAuth.getCurrentUser().getPhoneNumber());

                    Date date = new Date();
                    long timestamp =  date.getTime();
                    timestampReference.setValue(timestamp);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
