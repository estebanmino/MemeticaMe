package memeticame.memeticame.chats;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import memeticame.memeticame.R;
import memeticame.memeticame.contacts.AddNumberActivity;
import memeticame.memeticame.models.Contact;

public class ChatRoomActivity extends AppCompatActivity {

    //private FloatingActionButton fabSend = (FloatingActionButton) findViewById(R.id.fab_send);
    //private EditText editMessage = (EditText) findViewById(R.id.edit_message);
    private Contact chatContact = new Contact();
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PHONE = "phone";


    public static Intent getIntent(Context context, String name, String phone) {
        Log.d("oooo","getIntent");
        Intent intent = new Intent(context,ChatRoomActivity.class);
        intent.putExtra(KEY_USERNAME,name);
        intent.putExtra("PHONE",phone);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);


        chatContact.setName(getIntent().getStringExtra(KEY_USERNAME));
        chatContact.setPhone(getIntent().getStringExtra(KEY_PHONE));

        //back toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(chatContact.getName());
        }


    }
}
