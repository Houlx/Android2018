package com.ssdut.houlx.secondhandtransactionsystem;

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
public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText editTextRegisterUsername = findViewById(R.id.edittext_register_username);
        final EditText editTextRegisterPassword = findViewById(R.id.edittext_register_password);
        Button buttonSaveUser = findViewById(R.id.button_save_user);
        buttonSaveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                String usernameInput;
                String passwordInput;
                if (!"".equals(usernameInput = editTextRegisterUsername.getText().toString()) && !"".equals(passwordInput = editTextRegisterPassword.getText().toString())) {
                    editor.putString("username", usernameInput);
                    editor.putString("password", passwordInput);
                    editor.apply();
                    Toast.makeText(RegisterActivity.this, R.string.register_success, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Cannot be null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
