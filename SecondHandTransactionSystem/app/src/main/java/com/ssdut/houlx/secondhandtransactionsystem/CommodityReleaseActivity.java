package com.ssdut.houlx.secondhandtransactionsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * @author houlx
 */
public class CommodityReleaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_release);

        EditText editTextReleaseCommodityName = findViewById(R.id.editText_release_commodity_name);
        EditText edittextReleaseCommodityPrice = findViewById(R.id.editText_release_commodity_price);
        EditText editTextReleaseCommodityDescription = findViewById(R.id.editText_release_commodity_description);
        //TODO: Save commodity information from this page into LitePal database. Image upload. Spinner implement.
    }
}
