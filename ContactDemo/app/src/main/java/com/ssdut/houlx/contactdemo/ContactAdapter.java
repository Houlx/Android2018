package com.ssdut.houlx.contactdemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * @author houlx
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private List<Contact> mContactList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View contactView;
        ImageView contactImage;
        TextView contactName;

        ViewHolder(View view) {
            super(view);
            contactView = view;
            contactImage = view.findViewById(R.id.contact_image);
            contactName = view.findViewById(R.id.contact_name);
        }
    }

    ContactAdapter(List<Contact> contactList) {
        mContactList = contactList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.contactView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Contact contact = mContactList.get(position);

//                Log.d("onCreateViewHolder", contact.getPhone());
                Log.d("PointerViewHolder", Integer.toString(contact.hashCode()));

                //TODO: Start a new Activity to show detail of clicked contact.
                Intent intent = new Intent(parent.getContext(), ContactDetailActivity.class);
                intent.putExtra("contact", contact);
                parent.getContext().startActivity(intent);
            }
        });
        holder.contactImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Contact contact = mContactList.get(position);

                Log.d("onCreateViewHolder", contact.getPhone());

                //TODO: Start a new Activity as above.
                Intent intent = new Intent(parent.getContext(), ContactDetailActivity.class);
                intent.putExtra("contact", contact);
                parent.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = mContactList.get(position);
        holder.contactImage.setImageResource(contact.getImageId());
        holder.contactName.setText(contact.getName());
    }

    @Override
    public int getItemCount() {
        return mContactList.size();
    }
}
