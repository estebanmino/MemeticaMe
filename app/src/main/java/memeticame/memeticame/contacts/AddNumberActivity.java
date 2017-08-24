package memeticame.memeticame.contacts;

import android.content.Context;
import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import memeticame.memeticame.R;

public class AddNumberActivity extends AppCompatActivity {

    //private EditText editContactNumber = (EditText) findViewById(R.id.edit_contact_number);
    //private Button btnSearchContactNumber = (Button) findViewById(R.id.btn_search_contact_number);
    //private ListView listResults = (ListView) findViewById(R.id.list_results);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        //back toolbar
        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_add_number, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context,AddNumberActivity.class);
        return intent;
    }
}
