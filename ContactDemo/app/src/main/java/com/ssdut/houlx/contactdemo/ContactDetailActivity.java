package com.ssdut.houlx.contactdemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


/**
 * @author houlx
 */
public class ContactDetailActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        Intent intent = getIntent();
        Contact contact = (Contact) intent.getSerializableExtra("contact");

        setTitle(contact.getName());

        TextView phone = findViewById(R.id.contact_phone_detail);
        TextView email = findViewById(R.id.contact_email_detail);
        TextView addr = findViewById(R.id.contact_addr_detail);

        phone.setText(getResources().getString(R.string.phone) + ": " + contact.getPhone());
        email.setText(getResources().getString(R.string.email) + ": " + contact.getEmail());
        addr.setText(getResources().getString(R.string.address) + ": " + contact.getAddr());
    }
}
