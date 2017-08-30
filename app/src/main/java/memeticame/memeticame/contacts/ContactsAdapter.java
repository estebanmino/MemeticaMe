package memeticame.memeticame.contacts;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.UUID;

import memeticame.memeticame.R;
import memeticame.memeticame.models.Contact;
import memeticame.memeticame.models.Database;

/**
 * Created by efmino on 18-08-17.
 */

public class ContactsAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Contact> arrayList;
    private final FirebaseAuth mAuth;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final Database firebaseFirebase = new Database();

    public ContactsAdapter(Context context, ArrayList<Contact> arrayList, FirebaseAuth mAuth) {
        this.context = context;
        this.arrayList = arrayList;
        this.mAuth = mAuth;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        firebaseFirebase.init();

        if (convertView == null) {
            LayoutInflater layoutInflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.contact_view,null);
        }

        final TextView contactName = convertView.findViewById(R.id.contact_name);
        final TextView contactPhone = convertView.findViewById(R.id.contact_phone);

        final String elementName = arrayList.get(position).getName();
        final String elementPhone = arrayList.get(position).getPhone();

        contactName.setText(elementName);
        contactPhone.setText(elementPhone);

        FloatingActionButton addBtn = convertView.findViewById(R.id.add_button);


        addBtn.setOnClickListener(v -> {
            //notifyDataSetChanged();
            FirebaseUser currentUser = mAuth.getCurrentUser();

            Toast.makeText(context, "Name selected: " + elementName + "\nPhone: " +
                    elementPhone + " " + currentUser.getPhoneNumber(), Toast.LENGTH_LONG).show();

            String uuidChatRoom = UUID.randomUUID().toString();
            DatabaseReference myUserPhoneReference =
                    database.getReference("users/" + currentUser.getPhoneNumber() + "/contacts/" + elementPhone);
            myUserPhoneReference.setValue(uuidChatRoom);

            DatabaseReference contactUserPhoneReference =
                    database.getReference("users/" + elementPhone + "/contacts/" + currentUser.getPhoneNumber());
            contactUserPhoneReference.setValue(uuidChatRoom);
        });

        return convertView;
    }
}
