package com.ssdut.houlx.addrbook;

import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author houlx
 */
public class UpdateUserInfoActivity extends AppCompatActivity {
//    todo: 3.finish update user info activity, including username, password, icon

    EditText changeUsername;
    EditText oldPassword;
    EditText newPassword;
    EditText confirmPassword;

    Button submitChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        changeUsername = findViewById(R.id.change_username);
        changeUsername.setText(BmobUser.getCurrentUser().getUsername());

        oldPassword = findViewById(R.id.old_password);
        newPassword = findViewById(R.id.new_password);
        confirmPassword = findViewById(R.id.confirm_password);

        submitChange = findViewById(R.id.submit_change);
        submitChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SharedPreferences preferences = getSharedPreferences("SessionToken", MODE_PRIVATE);
                String token = preferences.getString("token", "");
                BmobUser newUser = new BmobUser();
                newUser.setSessionToken(token);

                if ("".equals(changeUsername.getText().toString())) {
                    changeUsername.setError("Username cannot be empty");
                }
                if ("".equals(oldPassword.getText().toString()) && "".equals(newPassword.getText().toString()) && "".equals(confirmPassword.getText().toString())) {
                    newUser.setUsername(changeUsername.getText().toString());
                    Log.d("currentUser", BmobUser.getCurrentUser().getObjectId());
                    newUser.update(BmobUser.getCurrentUser().getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(UpdateUserInfoActivity.this, "Success change username", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(UpdateUserInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    if (newPassword.getText().toString().equals(confirmPassword.getText().toString())) {
                        BmobUser.updateCurrentUserPassword(oldPassword.getText().toString(), newPassword.getText().toString(), new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e != null) {
                                    Toast.makeText(UpdateUserInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    //finish();
                                }
                            }
                        });
                        if (!changeUsername.getText().toString().equals(BmobUser.getCurrentUser().getUsername())) {
//                            SharedPreferences preferences1 = getSharedPreferences("SessionToken", MODE_PRIVATE);
                            //String token = preferences1.getString("token", "");
                            //BmobUser newUser = new BmobUser();
                            newUser.setUsername(changeUsername.getText().toString());
                            newUser.setSessionToken(token);
                            newUser.update(BmobUser.getCurrentUser().getObjectId(), new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(UpdateUserInfoActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                        finish();

                                    } else {
                                        Toast.makeText(UpdateUserInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        finish();
                    } else {
                        Toast.makeText(UpdateUserInfoActivity.this, "Confirm Password is not same", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
