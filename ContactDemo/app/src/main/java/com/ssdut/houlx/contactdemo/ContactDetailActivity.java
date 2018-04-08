package com.ssdut.houlx.contactdemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        final Contact contact = (Contact) intent.getSerializableExtra("contact");
        Log.d("PointerDetail", Integer.toString(contact.hashCode()));


        setTitle(contact.getName());


        TextView phone = findViewById(R.id.contact_phone_detail);
        TextView email = findViewById(R.id.contact_email_detail);
        TextView addr = findViewById(R.id.contact_addr_detail);


        phone.setText(getResources().getString(R.string.phone) + ": " + contact.getPhone());
        email.setText(getResources().getString(R.string.email) + ": " + contact.getEmail());
        addr.setText(getResources().getString(R.string.address) + ": " + contact.getAddr());

        Button editBtn = findViewById(R.id.edit_button);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactDetailActivity.this, EditActivity.class);
                intent.putExtra("edit_contact", contact);
//                Log.d("onClick", contact.getPhone());
                Log.d("PointerSendToEdit", Integer.toString(contact.hashCode()));

                startActivityForResult(intent, 1);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Contact resultContact = (Contact) data.getSerializableExtra("saved_contact");

                    Log.d("PointerResult", Integer.toString(resultContact.hashCode()));
//                    TextView phone = findViewById(R.id.contact_phone_detail);
//                    TextView email = findViewById(R.id.contact_email_detail);
//                    TextView addr = findViewById(R.id.contact_addr_detail);
//
//                    phone.setText(getResources().getString(R.string.phone) + ": " + resultContact.getPhone());
//                    email.setText(getResources().getString(R.string.email) + ": " + resultContact.getEmail());
//                    addr.setText(getResources().getString(R.string.address) + ": " + resultContact.getAddr());



//                    Log.d("onActivityResult", resultContact.getName());
                }
                break;
            default:

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
