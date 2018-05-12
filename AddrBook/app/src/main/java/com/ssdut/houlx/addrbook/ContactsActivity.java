package com.ssdut.houlx.addrbook;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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
import android.widget.Toast;

import com.ssdut.houlx.addrbook.db.Contact;

import org.litepal.crud.DataSupport;

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
    BottomNavigationView bottomNavigation;

    private List<Contact> contactList = new ArrayList<>();
    private List<Contact> contactFilterList = new ArrayList<>();
    private List<Contact> contactFavList = new ArrayList<>();

    TextView navUsername;
    TextView navEmail;

    private int position;

    private boolean optionMenuOn = true;
    private Menu aMenu;

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

        bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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

                    List<Contact> favs = DataSupport.findAll(Contact.class);
                    for (Contact contact : contactList) {
                        for (Contact contact1 : favs) {
                            if (contact.getPhonePersonal().equals(contact1.getPhonePersonal())) {
                                contact.setStar(true);
                            }
                        }
                    }


                    fillLetterThenSort();

                    contactFilterList.addAll(contactList);
                    for (Contact contact : contactList) {
                        if (contact.isStar()) {
                            contactFavList.add(contact);
                        }
                    }
                    manager = new LinearLayoutManager(ContactsActivity.this);
                    manager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(manager);
                    adapter = new ContactAdapter(contactFilterList, ContactsActivity.this);
                    adapter.setOnItemClickListener(new ContactAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent = new Intent(ContactsActivity.this, ContactDetailActivity.class);

                            if (bottomNavigation.getMenu().getItem(0).isChecked()) {
                                intent.putExtra("contact", contactFilterList.get(position));
                            } else if (bottomNavigation.getMenu().getItem(1).isChecked()) {
                                intent.putExtra("contact", contactFavList.get(position));
                            }
                            ContactsActivity.this.startActivityForResult(intent, 1);

                            ContactsActivity.this.position = position;
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

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_contact:
                    adapter.updateList(contactFilterList);
                    optionMenuOn = true;
                    checkOptionMenu();
                    swipeRefreshLayout.setEnabled(true);
                    return true;
                case R.id.navigation_fav:
                    adapter.updateList(contactFavList);
                    optionMenuOn = false;
                    checkOptionMenu();
                    swipeRefreshLayout.setEnabled(false);
                    return true;
                default:
            }
            return false;
        }
    };

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
    public boolean onPrepareOptionsMenu(Menu menu) {
        aMenu = menu;
        checkOptionMenu();
        return super.onPrepareOptionsMenu(menu);
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
        int id = item.getItemId();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    boolean star = data.getBooleanExtra("star", contactList.get(position).isStar());

                    if (bottomNavigation.getMenu().getItem(0).isChecked()) {
                        contactList.get(position).setStar(star);
                        if (star) {
                            contactList.get(position).save();
                        }
                        else {
                            List<Contact> deleteOne = DataSupport.where("phonePersonal = ?", contactList.get(position).getPhonePersonal()).find(Contact.class);

                            if (deleteOne.size() != 0) {
                                for (Contact contact : deleteOne) {
                                    contact.delete();
                                }
                            }
                        }
                    } else if (bottomNavigation.getMenu().getItem(1).isChecked()) {
                        contactFavList.get(position).setStar(star);
                        if (star) {
                            contactFavList.get(position).save();
                        }
                        else {
                            List<Contact> deleteOne = DataSupport.where("phonePersonal = ?", contactFavList.get(position).getPhonePersonal()).find(Contact.class);
                            if (deleteOne != null) {
                                for (Contact contact : deleteOne) {
                                    contact.delete();
                                }
                            }
                        }
                        adapter.updateList(contactFavList);
                    }
                    contactFavList.clear();
                    for (Contact contact : contactList) {
                        if (contact.isStar()) {
                            contactFavList.add(contact);
                        }
                    }

                    contactFilterList.clear();
                    contactFilterList.addAll(contactList);
                }
                break;
            default:
        }
    }

    private void checkOptionMenu() {
        if (null != aMenu) {
            if (optionMenuOn) {
                for (int i = 0; i < aMenu.size(); i++) {
                    aMenu.getItem(i).setVisible(true);
                    aMenu.getItem(i).setEnabled(true);
                }
            } else {
                for (int i = 0; i < aMenu.size(); i++) {
                    aMenu.getItem(i).setVisible(false);
                    aMenu.getItem(i).setEnabled(false);
                }
            }
        }
    }
}
