package com.example.bamaproject.sar.map;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bamaproject.sar.Fragment.notifikasi;
import com.example.bamaproject.sar.R;
import com.example.bamaproject.sar.adapter.RecyclerViewAdapter;
import com.example.bamaproject.sar.model.data_model;
import com.example.bamaproject.sar.model.model_koordinat;
import com.example.bamaproject.sar.model.model_tim;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MapsActivityLacakTim extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private DatabaseReference mDatabase;
    private String lati,longi,name;
    private Double dLat,dLong;
    LatLng latLang;
    ProgressDialog pd;
    String titleMarker;
    private FirebaseAuth mAuth;
    private ProgressBar mRegProgresBar;
    private DatabaseReference reference;
    private FirebaseFirestore mFirestore;

    String latTim,longTim,namaTim;
    private String mUserId,userImage, userName;
    List<model_koordinat> koordinatList;
    MarkerOptions markerOptions = new MarkerOptions();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maplacak);
        mAuth = FirebaseAuth.getInstance();
        mFirestore =  FirebaseFirestore.getInstance();
        mUserId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("SAR").child("TIM");
        pd = new ProgressDialog(MapsActivityLacakTim.this);
        koordinatList = new ArrayList<>();





        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtas2 = (Toolbar) findViewById(R.id.toolbar_lacak);
        setSupportActionBar(ToolBarAtas2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (item.getItemId()) {
            case android.R.id.home:
                //Write your logic here
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        GetData();


        getMarkers();


    }


    public void addMarker(final LatLng latlang, String title) {
        markerOptions.position(latlang);
        markerOptions.title(title);

        markerOptions.snippet(""+latlang);

      //  markerOptions.icon(bitmapDescriptorFromVectorMotor(MapsActivityLacakTim.this, R.drawable.ic_motor));
        mMap.addMarker(markerOptions).showInfoWindow();

      CameraPosition cameraPosition = new CameraPosition.Builder().target(latlang).zoom(10.0f).build();

        //  Toast.makeText(context, "Lokasi Kendaraan Berhasil Di Temukan"+latLang, Toast.LENGTH_LONG).show();
//        Snackbar snackbar = Snackbar.make(getView().findViewById(R.id.activity_main), "Lokasi Kendaraan Berhasil Di Temukan", Snackbar.LENGTH_LONG);
//        snackbar.show();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

      //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLang.latitude,latLang.longitude), 14.0f));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {


                CameraPosition cameraPositionZoom = new CameraPosition.Builder().target(marker.getPosition()).zoom(16.0f).build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPositionZoom));

                Toast.makeText(MapsActivityLacakTim.this, marker.getTitle(), Toast.LENGTH_SHORT).show();

            }
        });
    }






    //Berisi baris kode untuk mengambil data dari Database dan menampilkannya kedalam Adapter
    private void GetData(){


        //Mendapatkan Referensi Database
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("SAR").child("TIM")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Inisialisasi ArrayList


                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                            //Mapping data pada DataSnapshot ke dalam objek mahasiswa
                            model_koordinat a = snapshot.getValue(model_koordinat.class);

                            //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                            a.setKey(snapshot.getKey());

                            latTim = a.getLat();
                            longTim = a.getLongi();
                            namaTim =a.getTim();


                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {


                        Toast.makeText(MapsActivityLacakTim.this,"Data Gagal Dimuat", Toast.LENGTH_LONG).show();
                        Log.e("MyListActivity", databaseError.getDetails()+" "+databaseError.getMessage());
                    }




                } );
    }

















    public void getMarkers(){

        pd.setMessage("Mencari Lokasi Tim");
        pd.setCancelable(false);
        pd.show();


    mDatabase.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            for (DataSnapshot s : dataSnapshot.getChildren()) {

                model_koordinat mk = s.getValue(model_koordinat.class);

                koordinatList.add(mk);

                for (int i = 0; i <koordinatList.size(); i++) {

                    pd.cancel();
                    Toast.makeText(MapsActivityLacakTim.this, "Lokasi Tim Ditemukan", Toast.LENGTH_SHORT).show();

                    lati = latTim;
                    longi = longTim;
                    namaTim = namaTim;
//                    Toast.makeText(MapsActivityLacakTim.this, name + lati + longi, Toast.LENGTH_SHORT).show();
//
//
                    dLat = Double.parseDouble(lati);
                    dLong = Double.parseDouble(longi);
//
                    latLang= new LatLng(Double.parseDouble(koordinatList.get(i).getLat()), Double.parseDouble(koordinatList.get(i).getLongi()));
                    titleMarker = koordinatList.get(i).getTim();
                    //latLang = new LatLng(Double.parseDouble(lati), Double.parseDouble(longi));
//
//                    // Menambah data marker untuk di tampilkan ke google map






                    addMarker(latLang,titleMarker);
                }
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            pd.cancel();
            Toast.makeText(MapsActivityLacakTim.this,"Error"+databaseError.getMessage(),Toast.LENGTH_SHORT).show();

        }
    });
}

















}
