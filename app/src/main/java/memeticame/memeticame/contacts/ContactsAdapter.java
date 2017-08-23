package memeticame.memeticame.contacts;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import memeticame.memeticame.R;
import memeticame.memeticame.models.Contact;

/**
 * Created by efmino on 18-08-17.
 */

public class ContactsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Contact> arrayList;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

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

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.contact_view,null);
        }

        final TextView contact_name = (TextView)convertView.findViewById(R.id.contact_name);
        TextView contact_phone = (TextView)convertView.findViewById(R.id.contact_phone);

        final String element_name = arrayList.get(position).getContact_name();
        final String element_phone = arrayList.get(position).getContact_phone();

        contact_name.setText(element_name);
        contact_phone.setText(element_phone);

        FloatingActionButton addBtn = (FloatingActionButton)convertView.findViewById(R.id.add_button);

        addBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                //notifyDataSetChanged();
                FirebaseUser currentUser = mAuth.getCurrentUser();

                Toast.makeText(context, "Name selected: "+ element_name + "\nPhone: " + element_phone + " "+ currentUser.getPhoneNumber(), Toast.LENGTH_LONG).show();
                DatabaseReference myUserPhoneReference = database.getReference("users/"+currentUser.getPhoneNumber()+"/contacts/"+element_phone);
                myUserPhoneReference.setValue(true);

            }
        });

        return convertView;
    }
}
