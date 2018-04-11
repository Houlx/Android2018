package com.ssdut.houlx.secondhandtransactionsystem;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                editor.putString("username", editTextRegisterUsername.getText().toString());
                editor.putString("password", editTextRegisterPassword.getText().toString());
                editor.apply();
                finish();
            }
        });
    }
}
