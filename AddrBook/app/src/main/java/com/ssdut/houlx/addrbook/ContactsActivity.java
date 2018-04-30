package com.ssdut.houlx.addrbook;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.HeaderViewListAdapter;
import android.widget.TextView;

import com.ssdut.houlx.addrbook.db.Contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author houlx
 */
public class ContactsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    private PinyinComparator comparator;
    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    LinearLayoutManager manager;
    SearchView searchView;

    private List<Contact> contactList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nav_username);
        if (BmobUser.getCurrentUser().getEmail() == null) {
            navUsername.setText(BmobUser.getCurrentUser().getObjectId());
        } else {
            navUsername.setText(BmobUser.getCurrentUser().getEmail());
        }

        TextView navEmail = headerView.findViewById(R.id.nav_email);
        navEmail.setText(BmobUser.getCurrentUser().getUsername());
//        todo: implement user icon here.

        comparator = new PinyinComparator();

        recyclerView = findViewById(R.id.contact_recyclerView);
        BmobQuery<Contact> query = new BmobQuery<>();
        query.setLimit(1000);
        query.findObjects(new FindListener<Contact>() {
            @Override
            public void done(List<Contact> list, BmobException e) {
                if (e == null && list.size() != 0) {
                    contactList = list;
                    for (Contact contact : contactList) {
                        String pinyin = PinyinUtils.getPinyin(contact.getName());
                        String sortString = pinyin.substring(0, 1).toUpperCase();
                        if (sortString.matches("[A-Z]")) {
                            contact.setNameLetters(sortString.toUpperCase());
                        } else {
                            contact.setNameLetters("#");
                        }
                    }
                    Collections.sort(contactList, comparator);
                    manager = new LinearLayoutManager(ContactsActivity.this);
                    manager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(manager);
                    adapter = new ContactAdapter(contactList, ContactsActivity.this);
                    recyclerView.setAdapter(adapter);
                    recyclerView.addItemDecoration(new DividerItemDecoration(ContactsActivity.this, DividerItemDecoration.VERTICAL));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        TODO: fix search problem
        getMenuInflater().inflate(R.menu.contacts, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        assert searchManager != null;
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        } else if (id == R.id.action_log_out) {
//            Intent intent = new Intent(ContactsActivity.this, SignUpActivity.class);
//            startActivity(intent);
//            BmobUser.logOut();
//            finish();
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_feedback) {
//            TODO: Feedback Page
        } else if (id == R.id.nav_about) {
            //TODO: make an About dialog no need an activity
        } else if (id == R.id.nav_log_out) {
            Intent intent = new Intent(ContactsActivity.this, SignUpActivity.class);
            startActivity(intent);
            BmobUser.logOut();
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
