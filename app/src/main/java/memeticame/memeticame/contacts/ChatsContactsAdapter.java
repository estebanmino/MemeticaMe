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

import memeticame.memeticame.R;
import memeticame.memeticame.models.Contact;

/**
 * Created by efmino on 26-08-17.
 */

public class ChatsContactsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> arrayList;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    public ChatsContactsAdapter(Context context, ArrayList<String> arrayList, FirebaseAuth mAuth) {
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
            LayoutInflater layoutInflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.chat_preview,null);
        }

        final TextView contactName = convertView.findViewById(R.id.contact_name);
        final TextView lastMessage = convertView.findViewById(R.id.last_message);

        final String elementName = arrayList.get(position);

        contactName.setText(elementName);
        lastMessage.setText("last message");

        return convertView;
    }
}
