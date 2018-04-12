package com.ssdut.houlx.secondhandtransactionsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author houlx
 */
public class LoginActivity extends AppCompatActivity {

    private EditText editTextUsername;
    private EditText editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextUsername = findViewById(R.id.edittext_username);
        editTextPassword = findViewById(R.id.edittext_password);
        Button buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameInput = editTextUsername.getText().toString();
                String passwordInput = editTextPassword.getText().toString();
                SharedPreferences preferences = getSharedPreferences("user", MODE_PRIVATE);
                String username = preferences.getString("username", "");
                String password = preferences.getString("password", "");
                if (!"".equals(username) && !"".equals(password) && username.equals(usernameInput) && password.equals(passwordInput)) {
                    Intent intent = new Intent(LoginActivity.this, CommodityReleaseActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, R.string.invalid_account, Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
