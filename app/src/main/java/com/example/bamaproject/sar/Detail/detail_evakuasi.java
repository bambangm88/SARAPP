package com.example.bamaproject.sar.Detail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bamaproject.sar.R;
import com.example.bamaproject.sar.map.MapsActivityLokasiEvakuasi;


public class detail_evakuasi extends AppCompatActivity {

    TextView tvNama,tvLat,tvLong,tvPukul;

    Button btnLok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_evakuasi);


        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtas2 = (Toolbar)findViewById(R.id.toolbar_view_rc);
        setSupportActionBar(ToolBarAtas2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnLok = (Button) findViewById(R.id.btnLok);

        tvNama = (TextView) findViewById(R.id.tvNama);
        tvLat = (TextView) findViewById(R.id.tvLat);

        tvLong = (TextView) findViewById(R.id.tvLong);
        tvPukul = (TextView) findViewById(R.id.tvPukul);



        final String nama = getIntent().getExtras().getString("nama");
        final String lat = getIntent().getExtras().getString("lat");
        final String longi = getIntent().getExtras().getString("long") ;
        String pukul = getIntent().getExtras().getString("pukul");
        String key = getIntent().getExtras().getString("getPrimaryKey") ;



        tvNama.setText(nama);
        tvPukul.setText(pukul);
        tvLat.setText(lat);
        tvLong.setText(longi);


        btnLok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            Bundle bundle = new Bundle();
            bundle.putString("lat1",lat);
            bundle.putString("long1",longi);
            bundle.putString("namatitle",nama);
            Intent i = new Intent(detail_evakuasi.this, MapsActivityLokasiEvakuasi.class);
            i.putExtras(bundle);
            startActivity(i);


            }
        });



    }



    public void btnLokasiKlik (View v) {




    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }





}
