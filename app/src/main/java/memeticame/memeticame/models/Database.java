package memeticame.memeticame.models;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by efmino on 24-08-17.
 */

public class Database {

    public FirebaseDatabase mDatabase;
    public FirebaseAuth mAuth;


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
}
