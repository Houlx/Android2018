package com.ssdut.houlx.contactdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;

/**
 * @author houlx
 */
public class ContactListActivity extends AppCompatActivity {

    private List<Contact> contactList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        initContacts();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        ContactAdapter adapter = new ContactAdapter(contactList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    private void initContacts() {
        Contact alice = new Contact("Alice", "1234567", "alice@test.com", "FU St.", R.drawable.ic_person_outline_black_48dp);
        Log.d("PointerInit", Integer.toString(alice.hashCode()));

        Contact bob = new Contact("Bob", "0000000", "bob@test.com", "CK St.", R.drawable.ic_person_outline_black_48dp);
        Contact bill = new Contact("Bill", "2222222", "bill@test.com", "SH St.", R.drawable.ic_person_outline_black_48dp);
        contactList.add(alice);
        contactList.add(bob);
        contactList.add(bill);
    }
}
