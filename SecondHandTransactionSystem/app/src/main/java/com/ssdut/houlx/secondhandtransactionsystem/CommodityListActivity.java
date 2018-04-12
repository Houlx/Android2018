package com.ssdut.houlx.secondhandtransactionsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * @author houlx
 */
public class CommodityListActivity extends AppCompatActivity {

    private List<Commodity> commodityList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_list);

        initCommodity();
        RecyclerView recyclerView = findViewById(R.id.recycler_view_commodity_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        CommodityAdapter adapter = new CommodityAdapter(commodityList);
        recyclerView.setAdapter(adapter);

        Button buttonAddCommodity = findViewById(R.id.button_add_commodity);
        buttonAddCommodity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initCommodity() {
        //TODO: Read commodity information from LitePal database.
    }
}