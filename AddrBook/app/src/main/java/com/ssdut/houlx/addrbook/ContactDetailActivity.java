package com.ssdut.houlx.addrbook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ssdut.houlx.addrbook.db.Contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author houlx
 */
public class ContactDetailActivity extends AppCompatActivity {

    private final int ITEM_PERSONAL_PHONE = 0;
    private final int ITEM_OFFICE = 1;
    private final int ITEM_OFFICE_PHONE = 2;
    private final int ITEM_EMAIL = 3;

    private RecyclerView contactDetailRecyclerView;
    private DetailListAdapter adapter;
    private Contact contact;
    private boolean star;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView contactBackgroundImage = findViewById(R.id.contact_background_image);
        Glide.with(this).load(R.drawable.timg).into(contactBackgroundImage);
        contactDetailRecyclerView = findViewById(R.id.contact_detail_recyclerView);
        final Intent intent = getIntent();
        contact = (Contact) intent.getSerializableExtra("contact");

        star = contact.isStar();

        int[] imageId = new int[]{
                R.drawable.ic_phone_indigo_900_36dp,
                R.drawable.ic_home_indigo_900_36dp,
                R.drawable.ic_work_indigo_900_36dp,
                R.drawable.ic_email_indigo_900_36dp
        };
        final String[] info = new String[]{
                contact.getPhonePersonal(),
                contact.getOffice(),
                contact.getPhoneOffice(),
                contact.getEmail()
        };

        List<Map<String, Object>> listItem = new ArrayList<>();

        for (int i = 0; i < info.length; i++) {
            Map<String, Object> map = new HashMap<>(16);
            map.put("imageId", imageId[i]);
            map.put("info", info[i]);
            listItem.add(map);
        }

        setTitle(contact.getName());

        contactDetailRecyclerView = findViewById(R.id.contact_detail_recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        contactDetailRecyclerView.setLayoutManager(manager);
        adapter = new DetailListAdapter(listItem);
        adapter.setmOnItemClickListener(new DetailListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == ITEM_OFFICE_PHONE) {
                    if (ContextCompat.checkSelfPermission(ContactDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ContactDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, ITEM_OFFICE_PHONE);
                    } else {
                        call(ITEM_OFFICE_PHONE);
                    }
                } else if (position == ITEM_PERSONAL_PHONE) {
                    if (ContextCompat.checkSelfPermission(ContactDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(ContactDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, ITEM_PERSONAL_PHONE);
                    } else {
                        call(ITEM_PERSONAL_PHONE);
                    }
                } else if (position == ITEM_EMAIL) {
                    Intent intent1 = new Intent(Intent.ACTION_SEND);
                    intent1.setType("message/rfc822");
                    intent1.putExtra(Intent.EXTRA_EMAIL, new String[]{contact.getEmail()});
                    startActivity(Intent.createChooser(intent1, "Select Application"));
                }
            }
        });
        contactDetailRecyclerView.setAdapter(adapter);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageResource(contact.isStar() ? R.drawable.ic_star_yellow_800_24dp : R.drawable.ic_star_white_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star = !star;
                if (star) {
                    fab.setImageResource(R.drawable.ic_star_yellow_800_24dp);
                } else {
                    fab.setImageResource(R.drawable.ic_star_white_24dp);
                }

                Snackbar.make(view, star ? "Add to favorite list" : "Remove from Favorite List", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                star = !star;
                                if (star) {
                                    fab.setImageResource(R.drawable.ic_star_yellow_800_24dp);
                                } else {
                                    fab.setImageResource(R.drawable.ic_star_white_24dp);
                                }
                            }
                        }).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ITEM_PERSONAL_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call(ITEM_PERSONAL_PHONE);
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            case ITEM_OFFICE_PHONE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call(ITEM_OFFICE_PHONE);
                }
            default:
        }
    }

    private void call(int requestCode) {
        switch (requestCode) {
            case ITEM_OFFICE_PHONE:
                try {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + contact.getPhoneOffice()));
                    startActivity(intent);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                break;
            case ITEM_PERSONAL_PHONE:
                try {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + contact.getPhonePersonal()));
                    startActivity(intent);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                break;
            default:
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("star", star);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }
}
