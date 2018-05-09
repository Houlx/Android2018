package com.ssdut.houlx.addrbook;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.FindListener;

/**
 * @author houlx
 */
public class ContactsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    private PinyinComparator comparator;
    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager manager;
    SearchView searchView;

    private List<Contact> contactList = new ArrayList<>();
    private List<Contact> contactFilterList = new ArrayList<>();

    TextView navUsername;
    TextView navEmail;

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
        navUsername = headerView.findViewById(R.id.nav_username);
        navEmail = headerView.findViewById(R.id.nav_email);

        if (BmobUser.getCurrentUser() != null) {
            navUsername.setText(BmobUser.getCurrentUser().getUsername());
            navEmail.setText(BmobUser.getCurrentUser().getEmail());
        }

        comparator = new PinyinComparator();

        recyclerView = findViewById(R.id.contact_recyclerView);
        final BmobQuery<Contact> query = new BmobQuery<>();
        query.setLimit(1000);
        query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
        query.findObjects(new FindListener<Contact>() {
            @Override
            public void done(List<Contact> list, BmobException e) {
                if (e == null && list.size() != 0) {
                    contactList.addAll(list);

                    fillLetterThenSort();

                    contactFilterList.addAll(contactList);
                    manager = new LinearLayoutManager(ContactsActivity.this);
                    manager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(manager);
                    adapter = new ContactAdapter(contactFilterList, ContactsActivity.this);
                    adapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent = new Intent(ContactsActivity.this, ContactDetailActivity.class);
                            intent.putExtra("contact", contactFilterList.get(position));
                            ContactsActivity.this.startActivity(intent);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                    recyclerView.addItemDecoration(new DividerItemDecoration(ContactsActivity.this, DividerItemDecoration.VERTICAL));
                }
            }
        });

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactsActivity.this, UpdateUserInfoActivity.class);
                startActivity(intent);
            }
        });

        swipeRefreshLayout = findViewById(R.id.swipe_fresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BmobQuery<Contact> query1 = new BmobQuery<>();
                query1.setLimit(1000);
                query1.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
                query1.findObjects(new FindListener<Contact>() {
                    @Override
                    public void done(List<Contact> list, BmobException e) {
                        contactList.clear();
                        contactList.addAll(list);
                        fillLetterThenSort();
                        contactFilterList.clear();
                        contactFilterList.addAll(contactList);
                        adapter.updateList(contactFilterList);

                    }
                });
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void fillLetterThenSort() {
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
        getMenuInflater().inflate(R.menu.contacts, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            contactFilterList.clear();
            contactFilterList.addAll(contactList);
        } else {
            contactFilterList.clear();
            for (Contact contact : contactList) {
                String name = contact.getName();
                if (name.contains(newText) || PinyinUtils.getPinyin(contact.getName()).toLowerCase().contains(newText.toLowerCase())) {
                    contactFilterList.add(contact);
                }
            }
        }
        Collections.sort(contactFilterList, comparator);
        adapter.updateList(contactFilterList);
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



        if (id == R.id.nav_feedback) {
            Uri uri = Uri.parse("https://github.com/Houlx/Android2018/issues");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(R.string.about);
            dialog.setMessage(R.string.copyright);
            dialog.setCancelable(true);
            dialog.show();
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

    @Override
    protected void onResume() {
        super.onResume();
        BmobUser.fetchUserJsonInfo(new FetchUserInfoListener<String>() {
            @Override
            public void done(String s, BmobException e) {

            }
        });
        navUsername.setText(BmobUser.getCurrentUser().getUsername());
        navEmail.setText(BmobUser.getCurrentUser().getEmail());
    }
}
