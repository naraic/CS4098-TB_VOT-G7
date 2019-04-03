package com.example.onlinetutorial;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button login;
    private TextView error_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText)findViewById(R.id.input_username);
        password = (EditText)findViewById(R.id.input_password);
        login = (Button) findViewById(R.id.button_login);
        error_login = (TextView)findViewById(R.id.text_error_login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                validate(user, pass);
            }
        });
    }

    @Override
    public void onBackPressed(){
        // Do nothing if the back button is pressed from this screen.
    }

    public void validate(String user, String pass){
        if((user.equals("admin")) && (pass.equals("1234"))){
            error_login.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
        else {
            error_login.setVisibility(View.VISIBLE);
        }
    }
}
