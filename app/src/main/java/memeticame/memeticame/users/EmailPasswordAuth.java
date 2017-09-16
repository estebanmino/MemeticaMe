package memeticame.memeticame.users;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import memeticame.memeticame.R;

public class EmailPasswordAuth extends AppCompatActivity {

    private EditText editEmail;
    private EditText editPassword;
    private Button btnSignIn;
    private Button btnSignUp;
    private Button btnToSignUp;
    private Button btnToSignIn;
    private TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_password_auth);

        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassword = (EditText) findViewById(R.id.edit_password);

        btnSignIn = (Button) findViewById(R.id.btn_sign_in);
        btnSignUp = (Button) findViewById(R.id.btn_sign_up);
        btnToSignUp = (Button) findViewById(R.id.btn_to_sign_up);
        btnToSignIn = (Button) findViewById(R.id.btn_to_sign_in);

        titleText = (TextView) findViewById(R.id.text_title);
        titleText.setText("Log In MemeticaMe");


        btnSignUp.setVisibility(EditText.GONE);
        btnToSignIn.setVisibility(EditText.GONE);

        onClickBtnToSignUp();
        onClickBtnToSignIn();
        onClickBtnSignIn();
        onClickBtnSignUp();
    }

    private void onClickBtnToSignUp() {
        btnToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSignIn.setVisibility(Button.GONE);
                btnToSignUp.setVisibility(Button.GONE);
                btnSignUp.setVisibility(Button.VISIBLE);
                btnToSignIn.setVisibility(Button.VISIBLE);
                titleText.setText("Register in MemeticaMe");
            }
        });
    }

    private void onClickBtnToSignIn() {
        btnToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSignIn.setVisibility(Button.VISIBLE);
                btnToSignUp.setVisibility(Button.VISIBLE);
                btnSignUp.setVisibility(Button.GONE);
                btnToSignIn.setVisibility(Button.GONE);
                titleText.setText("Log In MemeticaMe");
            }
        });
    }

    private void onClickBtnSignIn() {
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void onClickBtnSignUp() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
