package com.ssdut.houlx.secondhandtransactionsystem;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author houlx
 */
public class CommodityListActivity extends AppCompatActivity {

    private List<Commodity> commodityList = new ArrayList<>();
    CommodityAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_list);


        initCommodity();
        RecyclerView recyclerView = findViewById(R.id.recycler_view_commodity_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CommodityAdapter(commodityList);
        recyclerView.setAdapter(adapter);

        FloatingActionButton buttonAddCommodity = findViewById(R.id.button_add_commodity);
        buttonAddCommodity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommodityListActivity.this, CommodityReleaseActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initCommodity() {
        Connector.getDatabase();
        //TODO: Read commodity information from LitePal database.
//        Commodity commodity = new Commodity("test", "test", 0, 0, "test");
//        commodity.save();
        commodityList = DataSupport.findAll(Commodity.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        commodityList = DataSupport.findAll(Commodity.class);
        adapter.setmCommodityList(commodityList);
        adapter.notifyDataSetChanged();
    }
}
