package com.example.bamaproject.sar.map;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bamaproject.sar.Fragment.home;
import com.example.bamaproject.sar.R;
import com.example.bamaproject.sar.model.DirectionsParser;
import com.example.bamaproject.sar.model.model_koordinat;
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
import com.google.android.gms.maps.model.PolylineOptions;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivityLokasiEvakuasi extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private DatabaseReference mDatabase;
    private String lati, longi, name;
    private Double dLat, dLong;
    LatLng latLangAwal,latLangTujuan;
    ProgressDialog pd;
    String titleMarker;
    private FirebaseAuth mAuth;
    private ProgressBar mRegProgresBar;

    double myLat,myLong;

    private static final int LOCATION_REQUEST = 500;

    private FirebaseFirestore mFirestore;

    ArrayList<LatLng> locations;

    private String longiExtra,latExytra,namaTitle ;


    private String mUserId, userImage, userName;
    List<model_koordinat> koordinatList;
    MarkerOptions markerOptions = new MarkerOptions();
    MarkerOptions markerOptionsTujuan= new MarkerOptions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapevakuasi);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mUserId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("SAR").child("KOORDINAT");
        pd = new ProgressDialog(MapsActivityLokasiEvakuasi.this);
        koordinatList = new ArrayList<>();
        locations = new ArrayList();

       latExytra = getIntent().getExtras().getString("lat1");
       longiExtra = getIntent().getExtras().getString("long1");
       namaTitle = getIntent().getExtras().getString("namatitle");

       myLat = Double.parseDouble(latExytra);
       myLong = Double.parseDouble(longiExtra);


        double lok1 = Double.parseDouble(home.getMyLokasiLat());
        double lok2 = Double.parseDouble(home.getMyLokasiLong());

        final LatLng c = new LatLng(lok1,lok2);
        final LatLng d = new LatLng(myLat,myLong);




        mFirestore.collection("Users").document(mUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                userName = documentSnapshot.getString("name");


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MapsActivityLokasiEvakuasi.this, "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtas2 = (Toolbar) findViewById(R.id.toolbar_map_evakuasi);
        setSupportActionBar(ToolBarAtas2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getRequestUrl(c,d);
                TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
                taskRequestDirections.execute(url);
            }
        });








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


        getMarkers();


    }


    public void addMarker(final LatLng latlang) {
        markerOptions.position(latlang);

        markerOptions.title("Me");


        markerOptions.snippet("" + latlang);

        //  markerOptions.icon(bitmapDescriptorFromVectorMotor(MapsActivityLacakTim.this, R.drawable.ic_motor));
        mMap.addMarker(markerOptions).showInfoWindow();



        //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLang.latitude,latLang.longitude), 14.0f));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                CameraPosition cameraPosition = new CameraPosition.Builder().target(latlang).zoom(17.0f).build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                Toast.makeText(MapsActivityLokasiEvakuasi.this, marker.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void addMarkerTujuan(final LatLng latlang) {
        markerOptionsTujuan.position(latlang);


            markerOptionsTujuan.title(namaTitle);


        markerOptionsTujuan.snippet("" + latlang);
        markerOptionsTujuan.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)) ;

        //  markerOptions.icon(bitmapDescriptorFromVectorMotor(MapsActivityLacakTim.this, R.drawable.ic_motor));
        mMap.addMarker(markerOptionsTujuan).showInfoWindow();

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latlang).zoom(14.0f).build();

        //  Toast.makeText(context, "Lokasi Kendaraan Berhasil Di Temukan"+latLang, Toast.LENGTH_LONG).show();
//        Snackbar snackbar = Snackbar.make(getView().findViewById(R.id.activity_main), "Lokasi Kendaraan Berhasil Di Temukan", Snackbar.LENGTH_LONG);
//        snackbar.show();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        //  Toast.makeText(context, "Lokasi Kendaraan Berhasil Di Temukan"+latLang, Toast.LENGTH_LONG).show();
//        Snackbar snackbar = Snackbar.make(getView().findViewById(R.id.activity_main), "Lokasi Kendaraan Berhasil Di Temukan", Snackbar.LENGTH_LONG);
//        snackbar.show();


        //  mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latLang.latitude,latLang.longitude), 14.0f));

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(MapsActivityLokasiEvakuasi.this, marker.getTitle(), Toast.LENGTH_SHORT).show();
                CameraPosition cameraPosition = new CameraPosition.Builder().target(latlang).zoom(17.0f).build();

                //  Toast.makeText(context, "Lokasi Kendaraan Berhasil Di Temukan"+latLang, Toast.LENGTH_LONG).show();
//        Snackbar snackbar = Snackbar.make(getView().findViewById(R.id.activity_main), "Lokasi Kendaraan Berhasil Di Temukan", Snackbar.LENGTH_LONG);
//        snackbar.show();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
    }



    public void getMarkers() {


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot s : dataSnapshot.getChildren()) {

                    model_koordinat mk = s.getValue(model_koordinat.class);

                    koordinatList.add(mk);





                        latLangAwal = new LatLng(Double.parseDouble(home.getMyLokasiLat()), Double.parseDouble(home.getMyLokasiLong()));
                        latLangTujuan = new LatLng(Double.parseDouble(latExytra), Double.parseDouble(longiExtra));


                        addMarker(latLangAwal);
                        addMarkerTujuan(latLangTujuan);






                    // Menambah data marker untuk di tampilkan ke google map

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd.cancel();
                Toast.makeText(MapsActivityLokasiEvakuasi.this, "Error" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }








    private String getRequestUrl(LatLng latLngAwal, LatLng latLngTujuan) {
        String str_org = "origin=" + latLngAwal.latitude + "," + latLngAwal.longitude ;

        String str_dest = "destination=" + latLngTujuan.latitude + "," + latLngTujuan.longitude ;

        String sensor = "sensor=false";

        String mode = "mode=driving";

        String api = "AIzaSyAS4JPBZ5jrkSUBU4jsLcDu91SuxP5iUJE";

        String key = "key=" + api;

        String param =  str_org + "&" + str_dest + "&" +sensor+ "&" +mode+ "&" + key;

        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;

        return url;


    }

    private String requestDirections(String reqUrl) throws IOException {
        String responseString = "";
        InputStream inputStream = null ;
        HttpURLConnection httpURLConnection = null ;
        try {
            URL url = new URL(reqUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection() ;
            httpURLConnection.connect() ;

            inputStream = httpURLConnection.getInputStream();
            InputStreamReader inputStreamReader =  new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuffer stringBuffer = new StringBuffer();

            String line = "";

            while ((line = bufferedReader.readLine()) != null) {

                stringBuffer.append(line);
            }

            responseString = stringBuffer.toString();
            bufferedReader.close();
            inputStreamReader.close();

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (inputStream != null){
                inputStream.close();
            }

            httpURLConnection.disconnect();

        }


        return  responseString;
    }



    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){

            case LOCATION_REQUEST :
                if (grantResults.length > 0 && grantResults[0] ==  PackageManager.PERMISSION_GRANTED ){
                    mMap.setMyLocationEnabled(true);
                }
                break;
        }

    }


    public class TaskRequestDirections extends AsyncTask<String, Void, String> {

        @Override
        protected  String doInBackground (String... strings){
            String responseString = "" ;

            try {
                responseString = requestDirections(strings[0]);
            } catch (Exception e){
                e.printStackTrace();
            }
            return  responseString ;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Parse json here
            TaskParser taskParser = new TaskParser();
            taskParser.execute(s);
        }
    }


    public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>> > {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject = null;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            //Get list route and display it into the map

            ArrayList points = null;

            PolylineOptions polylineOptions = null;

            for (List<HashMap<String, String>> path : lists) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

                for (HashMap<String, String> point : path) {
                    double lat = Double.parseDouble(point.get("lat"));
                    double lon = Double.parseDouble(point.get("lon"));

                    points.add(new LatLng(lat,lon));
                }

                polylineOptions.addAll(points);
                polylineOptions.width(10);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }

            if (polylineOptions!=null) {
                mMap.addPolyline(polylineOptions);
            } else {
                Toast.makeText(getApplicationContext(), "Direction not found!", Toast.LENGTH_SHORT).show();
            }

        }
    }















}

