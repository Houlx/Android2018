package com.ssdut.houlx.secondhandtransactionsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author houlx
 */
public class CommodityReleaseActivity extends AppCompatActivity {

    final List<String> types = new ArrayList<>();
    Commodity commodity;

    String commodityName;
    String commodityType;
    String commodityPrice;
    int commodityImageId;
    String commodityDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commodity_release);

        final EditText editTextReleaseCommodityName = findViewById(R.id.editText_release_commodity_name);
        final EditText editTextReleaseCommodityPrice = findViewById(R.id.editText_release_commodity_price);
        final EditText editTextReleaseCommodityDescription = findViewById(R.id.editText_release_commodity_description);
        Spinner spinnerReleaseCommodityType = findViewById(R.id.spinner_release_commodity_type);
        Button buttonReleaseCommodityChooseImage = findViewById(R.id.button_release_commodity_choose_image);
        Button buttonReleaseCommoditySubmit = findViewById(R.id.button_release_commodity_submit);

        types.add("Type1");
        types.add("Type2");
        types.add("Type3");

        //Param R.drawable.ic_launcher_foreground for imageId is only for test.
//        Log.d("TEST", "onCreate: " + Double.parseDouble(editTextReleaseCommodityPrice.getText().toString()));
//        commodity = new Commodity(editTextReleaseCommodityName.getText().toString(), "", Double.parseDouble(editTextReleaseCommodityPrice.getText().toString()), R.drawable.ic_launcher_foreground, editTextReleaseCommodityDescription.getText().toString());

        TypeAdapter adapter = new TypeAdapter(this);
        spinnerReleaseCommodityType.setAdapter(adapter);
        adapter.setTypes(types);

        spinnerReleaseCommodityType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                commodityType = types.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonReleaseCommoditySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commodityName = editTextReleaseCommodityName.getText().toString();
                commodityPrice = editTextReleaseCommodityPrice.getText().toString();
                //This imageId is only for test.
                commodityImageId = 1;
                commodityDescription = editTextReleaseCommodityDescription.getText().toString();
                if (!"".equals(commodityName) && !"".equals(commodityType) && !"".equals(commodityPrice) && !"".equals(commodityDescription)) {
                    commodity = new Commodity(commodityName, commodityType, Double.parseDouble(commodityPrice), commodityImageId, commodityDescription);
                    commodity.save();
                    finish();
                } else {
                    Toast.makeText(CommodityReleaseActivity.this, R.string.uncompleted_input, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
