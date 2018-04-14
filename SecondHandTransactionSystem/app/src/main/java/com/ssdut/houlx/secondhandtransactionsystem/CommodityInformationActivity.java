package com.ssdut.houlx.secondhandtransactionsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * @author houlx
 */
public class CommodityInformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_information);

        TextView commodityInformationName = findViewById(R.id.commodity_information_name);
        TextView commodityInformationType = findViewById(R.id.commodity_information_type);
        TextView commodityInformationPrice = findViewById(R.id.commodity_information_price);
        TextView commodityInformationDescription = findViewById(R.id.commodity_information_description);

        Intent intent = getIntent();
        Commodity commodity = (Commodity) intent.getSerializableExtra("commodity");

        commodityInformationName.setText(commodity.getName());
        commodityInformationType.setText(commodity.getType());
        commodityInformationPrice.setText(String.valueOf(commodity.getPrice()));
        commodityInformationDescription.setText(commodity.getDescription());
    }
}
