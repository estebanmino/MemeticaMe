package memeticame.memeticame.chats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

import memeticame.memeticame.R;
import memeticame.memeticame.models.Message;

/**
 * Created by efmino on 27-08-17.
 */

public class ChatRoomAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Message> messagesList;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public ChatRoomAdapter(Context context, ArrayList<Message> messagesList, FirebaseAuth mAuth) {
        this.context = context;
        this.messagesList = messagesList;
        this.mAuth = mAuth;
    }

    @Override
    public int getCount() {
        return messagesList.size();
    }

    @Override
    public Object getItem(int position) {
        return messagesList.get(position);
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
            convertView = layoutInflater.inflate(R.layout.chat_message_view,null);
        }

        final TextView message = convertView.findViewById(R.id.text_message_body);
        final TextView author = convertView.findViewById(R.id.text_message_name);
        final TextView timestamp = convertView.findViewById(R.id.text_message_time);

        final String messageAuthor = messagesList.get(position).getAuthor();
        final String messageContent = messagesList.get(position).getContent();
        final long messageTimestamp = messagesList.get(position).getTimestamp();

        message.setText(messageContent);
        author.setText(messageAuthor);
        Date date = new Date(messageTimestamp);
        timestamp.setText(date.toString());

        return convertView;
    }
}

