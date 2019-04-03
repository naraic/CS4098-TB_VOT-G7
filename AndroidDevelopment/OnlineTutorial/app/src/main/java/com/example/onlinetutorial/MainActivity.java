package com.example.onlinetutorial;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button login;
    private TextView error_login;
    private String[] validUsernames = {"paul", "rory", "he", "shahraiz", "ciaran"};
    private String[] validPasswords = {"password1", "password2", "password3", "password4", "password5"};

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
        Boolean validated = validateUsernameAndPassword(user, pass);

        if(validated == true){
            error_login.setText("");
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
        else {
            error_login.setText("Incorrect Username or Password. Please try again!");
        }
    }

    public boolean validateUsernameAndPassword(String username, String password){
        Boolean validPassword;
        Boolean returnResult = false;
        for (int x=0; x<validUsernames.length; x++){
            if (username.equals(validUsernames[x])){
                validPassword = validatePassword(x, password);
                if (validPassword == true) {
                    returnResult = true;
                    break;
                }
            }
        }
        return returnResult;
    }

    public boolean validatePassword(int index, String password){
        if (validPasswords[index].equals(password)){
            return true;
        }
        else {
            return false;
        }
    }
}
