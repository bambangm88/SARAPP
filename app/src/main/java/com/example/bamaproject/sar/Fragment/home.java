package com.example.bamaproject.sar.Fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bamaproject.sar.Login.Login;
import com.example.bamaproject.sar.Login.Register;
import com.example.bamaproject.sar.Main.MainActivity;

import com.example.bamaproject.sar.R;

import com.example.bamaproject.sar.map.MapsActivityLacakTim;
import com.example.bamaproject.sar.model.data_model;
import com.example.bamaproject.sar.model.model_koordinat;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;

public class home extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Marker mCurrLocationMarker;
    private Location mLastLocation;
    LatLng latLng ;
    ProgressDialog pd;

    public  String tim ;
    public static String mylokasiLat ;
    public static String mylokasilong ;
    TextView koordinat ;
    String tanggal,waktu ;
    private FirebaseAuth mAuth;
    private ProgressBar mRegProgresBar;

    private FirebaseFirestore mFirestore;

    private String mUserId;

    public String userName;
    private  String userImage;

    private Button btnLacak;


    TextView t ;
    Handler mHandler ;







    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home,container, false);
        setHasOptionsMenu(true);
        mAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(getActivity());
        mRegProgresBar = view.findViewById(R.id.progressBarGps);
        mFirestore =  FirebaseFirestore.getInstance();




        mUserId = mAuth.getCurrentUser().getUid();





        btnLacak = view.findViewById(R.id.buttonLacak);
        t = view.findViewById(R.id.ayah);



        mengirim_koordinat ();





        deteksiGPS () ;


        btnLacak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent u = new Intent(getActivity(),MapsActivityLacakTim.class);
                startActivity(u);

            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setVisibility(View.INVISIBLE);

        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtas2 = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(ToolBarAtas2);

        SimpleDateFormat timeStampFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        Date myDate = new Date();
        tanggal = timeStampFormat.format(myDate);

        koordinat = view.findViewById(R.id.txtKoordinat);

        Button on = view.findViewById(R.id.tmbl_on);



        mHandler = new Handler();

        this.mHandler = new Handler();

        this.mHandler.postDelayed(m_Runnable,3000);

        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialogBantuan();
                mengirim_tim ();
            }
        });
        mengirim_tim ();
        return view;

    }




    private final Runnable m_Runnable = new Runnable()
    {
        public void run()

        {

                mengirim_koordinat();
                mengirim_tim_koordinat ();
                mengirim_tim ();

                mHandler.postDelayed(m_Runnable, 3000);

        }

    };//runnable





    public static String getMyLokasiLat(){

        return mylokasiLat;
    }

    public static String getMyLokasiLong(){

        return mylokasilong;
    }

public void deteksiGPS (){
    // Untuk Mendeteksi apakah GPS aktif atau tidak
    LocationManager lm = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
    if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
        // Build the alert dialog
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle("GPS Anda Belum Aktif");
        builder.setMessage("Untuk Melanjutkan Silahkan Aktifkan GPS Anda !");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                // Show location settings when the user acknowledges the alert dialog
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Lain Kali", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                // Show location settings when the user acknowledges the alert dialog
                getActivity().finish();
            }
        });

        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    } else {
        mRegProgresBar.setVisibility(View.VISIBLE);

    }

}



    public void mengirim_koordinat () {

        if (getActivity() == null) {

        } else {


            mFirestore.collection("Users").document(mUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    userName = documentSnapshot.getString("name");
                    userImage = documentSnapshot.getString("image");
                    tim = documentSnapshot.getString("tim");


                    if (koordinat.getText().equals("")) {


                    } else {


                        //Mendapatkan Instance dari Database
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference getReference;


                        getReference = database.getReference(); // Mendapatkan Referensi dari Database

                        getReference.child("SAR").child("KOORDINAT").child(userName)
                                .setValue(new model_koordinat("" + latLng.latitude, "" + latLng.longitude, userName,userImage,tim))
                                .addOnSuccessListener(getActivity(), new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        //Peristiwa ini terjadi saat user berhasil menyimpan datanya kedalam Database


                                    }
                                });


                    }

                }
            });


        }
    }



    public void mengirim_tim () {

        if (getActivity() == null) {

        } else {


            mFirestore.collection("Users").document(mUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    userName = documentSnapshot.getString("name");
                    userImage = documentSnapshot.getString("image");


                    if (koordinat.getText().equals("")) {


                    } else {


                        //Mendapatkan Instance dari Database
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference getReference;


                        getReference = database.getReference(); // Mendapatkan Referensi dari Database



                        getReference.child("SAR").child("ANGGOTA TIM").child(tim).child(userName)
                                .setValue(new model_koordinat("" + latLng.latitude, "" + latLng.longitude, userName,userImage,tim))
                                .addOnSuccessListener(getActivity(), new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        //Peristiwa ini terjadi saat user berhasil menyimpan datanya kedalam Database


                                    }
                                });


                    }

                }
            });


        }
    }



    public void mengirim_tim_koordinat () {

        if (getActivity() == null) {

        } else {


            mFirestore.collection("Users").document(mUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    userName = documentSnapshot.getString("name");
                    userImage = documentSnapshot.getString("image");


                    if (koordinat.getText().equals("")) {


                    } else {


                        //Mendapatkan Instance dari Database
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference getReference;

                        //Menyimpan Data yang diinputkan User kedalam Variable

                        getReference = database.getReference(); // Mendapatkan Referensi dari Database


                    /*
                    Jika Tidak, maka data dapat diproses dan meyimpannya pada Database
                    Menyimpan data referensi pada Database berdasarkan User ID dari masing-masing Akun
                    */
                        //.child(getUserID)
                        getReference.child("SAR").child("TIM").child(tim)
                                .setValue(new model_koordinat("" + latLng.latitude, "" + latLng.longitude, userName,userImage,tim))
                                .addOnSuccessListener(getActivity(), new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        //Peristiwa ini terjadi saat user berhasil menyimpan datanya kedalam Database


                                    }
                                });


                    }

                }
            });


        }
    }




    public void bantuan () {

        notifikasi.b = "a";

        if(koordinat.getText().equals("")){
            Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content),  "Lokasi Saat Ini Belum Ditemukan", Snackbar.LENGTH_SHORT);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.gagal));
            snackbar.show();
        } else {
            notifikasi.b = "a";
            pd.setMessage("Meminta Bantuan");
            pd.setCancelable(false);
            pd.show();
            notifikasi.b = "a";
            //Mendapatkan Instance dari Database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference getReference;

            //Menyimpan Data yang diinputkan User kedalam Variable

            getReference = database.getReference(); // Mendapatkan Referensi dari Database


                    /*
                    Jika Tidak, maka data dapat diproses dan meyimpannya pada Database
                    Menyimpan data referensi pada Database berdasarkan User ID dari masing-masing Akun
                    */
            //.child(getUserID)
            getReference.child("SAR").child("NOTIFIKASI").push()
                    .setValue(new data_model("" + latLng.latitude , "" + latLng.longitude , userName  , ""+tanggal, userImage ,"EVAKUASI DITEMUKAN !!!",tim ))
                    .addOnSuccessListener(getActivity(), new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            //Peristiwa ini terjadi saat user berhasil menyimpan datanya kedalam Database
                            pd.cancel();
                            Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Berhasil Mengirim Permintaan", Snackbar.LENGTH_SHORT);
                            View snackbarView = snackbar.getView();
                            snackbarView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.hijau));
                            snackbar.show();


                        }
                    });


        }

    }

    //perintah delete ------------
    private void showAlertDialogBantuan() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setMessage("Beritahu Tim?")
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        bantuan () ;
                        dialogInterface.dismiss();


                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
//----------------





    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Memulai Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mRegProgresBar.setVisibility(View.INVISIBLE);
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        latLng = new LatLng(location.getLatitude(), location.getLongitude());

        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latLng.latitude, latLng.longitude)).zoom(16).build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
      //  Toast.makeText(getActivity(),"Koordinat : "+latLng.latitude+","+latLng.longitude,Toast.LENGTH_LONG).show();

        koordinat.setText("Lat : "+latLng.latitude+", Long : "+latLng.longitude);

         mylokasiLat =""+latLng.latitude ;
         mylokasilong = ""+latLng.longitude ;

        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content),  "Lokasi Saat Ini Ditemukan . .", Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.hijau));
        snackbar.show();

        //menghentikan pembaruan lokasi
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }




    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }






    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Izin diberikan.
                    if (ContextCompat.checkSelfPermission(getActivity(),
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Izin ditolak.
                    Toast.makeText(getActivity(), "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }




}
