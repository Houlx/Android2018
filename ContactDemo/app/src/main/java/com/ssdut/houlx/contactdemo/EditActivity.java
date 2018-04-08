package com.ssdut.houlx.contactdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author houlx
 */
public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        final Intent intent = getIntent();
        final Contact contact = (Contact) intent.getSerializableExtra("edit_contact");

        Log.d("PointerEditReceived", Integer.toString(contact.hashCode()));

        final EditText editPhone = findViewById(R.id.editText_phone);
        final EditText editEmail = findViewById(R.id.editText_email);
        final EditText editAddress = findViewById(R.id.editText_address);

        editPhone.setText(contact.getPhone());
        editEmail.setText(contact.getEmail());
        editAddress.setText(contact.getAddr());

        Button saveBtn = findViewById(R.id.save_button);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent();
//                intent1.putExtra("edit_phone", editPhone.getText().toString());
//                intent1.putExtra("edit_email", editEmail.getText().toString());
//                intent1.putExtra("edit_address", editAddress.getText().toString());
                contact.setPhone(editPhone.getText().toString());
                contact.setEmail(editEmail.getText().toString());
                contact.setAddr(editAddress.getText().toString());

                Log.d("PointerSaved", Integer.toString(contact.hashCode()));
//                Log.d("EditContact", contact.getPhone());

                intent1.putExtra("saved_contact", contact);
                setResult(RESULT_OK, intent1);
                finish();
            }
        });
    }
}
