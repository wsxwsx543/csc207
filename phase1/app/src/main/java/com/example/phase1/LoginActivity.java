package com.example.phase1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    FileSystem fileSystem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadUsers();

        setContentView(R.layout.activity_login);
        //Button initiation reference: https://www.youtube.com/watch?v=GtxVILjLcw8
        final Button loginButton = findViewById(R.id.login);
        final Button registerButton = findViewById(R.id.register);

        //OnClickListener setup
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

    }

    public boolean checkPasswordCorrect(){
        // EditText initiation
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        UserManager userManagerInstance = UserManager.getInstance();

        if(userManagerInstance.getUsers().containsKey(username)){
            if (password.equals(userManagerInstance.getUsers().get(username).getPassword())){
                userManagerInstance.setCurUser(userManagerInstance.getUsers().get(username));
                return true;
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        UserManager userManagerInstance = UserManager.getInstance();
        TextView usernameTextView = findViewById(R.id.username);
        switch (v.getId()){
            case R.id.login: {
                if (!checkPasswordCorrect()) {
                    Toast.makeText(this, "Invalid username or password.",
                            Toast.LENGTH_LONG).show();
                } else {
                    userManagerInstance.setCurUser(userManagerInstance.getUsers().get(usernameTextView.getText().toString()));
                    startActivity(new Intent(LoginActivity.this, ChooseOrCreatePlayerActivity.class));
                }
                break;
            }
            case R.id.register: {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            }
        }
    }

    // Try to load users data from local file named Users.ser
    public void loadUsers(){
        fileSystem = new FileSystem(this.getApplicationContext());
        if(fileSystem.load("Users.ser") instanceof HashMap){
            UserManager.getInstance().setUsers((HashMap<String, User>)
                    fileSystem.load("Users.ser"));
        } else {
            UserManager.getInstance().setUsers(new HashMap<>());
            fileSystem.save(UserManager.getInstance(), "Users.ser");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fileSystem.save(UserManager.getInstance(), "Users.ser");
    }

    @Override
    protected void onStop() {
        super.onStop();
        fileSystem.save(UserManager.getInstance(), "Users.ser");
    }
}
