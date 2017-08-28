package memeticame.memeticame.chats;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import memeticame.memeticame.R;
import memeticame.memeticame.models.Contact;
import memeticame.memeticame.models.Database;
import memeticame.memeticame.models.Message;

public class ChatRoomActivity extends AppCompatActivity {

    private Contact chatContact = new Contact();
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_CHAT_ROOM_UUID = "chatRoomUuid";

    public Database firebaseDatabase = new Database();
    private ArrayList<Message> messagesList = new ArrayList<Message>();
    private  ChatRoomAdapter chatRoomAdapter;
    private ListView listView;


    public static Intent getIntent(Context context, String name, String phone, String chatRoomUuid) {
        Intent intent = new Intent(context,ChatRoomActivity.class);
        intent.putExtra(KEY_USERNAME,name);
        intent.putExtra(KEY_PHONE,phone);
        intent.putExtra(KEY_CHAT_ROOM_UUID,chatRoomUuid);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        firebaseDatabase.init();

        chatContact.setName(getIntent().getStringExtra(KEY_USERNAME));
        chatContact.setPhone(getIntent().getStringExtra(KEY_PHONE));
        String chatRoomUuid = getIntent().getStringExtra(KEY_CHAT_ROOM_UUID);

        //back toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(chatContact.getName());
        }

        FloatingActionButton fabSend = (FloatingActionButton)findViewById(R.id.fab_send);
        final EditText editMessage = (EditText) findViewById(R.id.edit_message);

        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseDatabase.sendMessageTo(
                        editMessage.getText().toString(),
                        chatContact.getPhone());
            }
        });

        chatRoomAdapter  = new ChatRoomAdapter(ChatRoomActivity.this, messagesList, firebaseDatabase.mAuth);

        listView = (ListView) findViewById(R.id.reyclerview_message_list);


        Log.d("INCHATROOMACTIVITY", chatRoomUuid);
        DatabaseReference chatRommReference = firebaseDatabase.getReference("chatRooms/"+chatRoomUuid+"/");

        chatRommReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("ONDATACHANGE", "vgjhsoish");
                messagesList.clear();
                for(DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                    Message message = messageSnapshot.getValue(Message.class);
                    if (message.getAuthor()!=null) {
                        messagesList.add(message);
                    }
                }

                chatRoomAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        listView.setAdapter(chatRoomAdapter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
