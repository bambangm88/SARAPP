package com.example.bamaproject.sar.Main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bamaproject.sar.Fragment.akun;
import com.example.bamaproject.sar.Fragment.notifikasi;
import com.example.bamaproject.sar.Fragment.home;
import com.example.bamaproject.sar.Fragment.tim;
import com.example.bamaproject.sar.Login.Login;
import com.example.bamaproject.sar.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    Fragment fragment ;

    private FirebaseAuth mAuth ;

    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;

    public static String parsingNotif;

    private ProgressBar mRegProgresBar;

    private FirebaseFirestore mFirestore;

    private String mUserId;

    public String userName;
    private  String userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
                FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            sendToLogin();
        }


        mFirestore =  FirebaseFirestore.getInstance();
        mUserId = mAuth.getCurrentUser().getUid();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemIconTintList(null);

        notifikasi.b = "a" ;

        String notif = "a";
        fragment = new notifikasi();
        loadFragment(fragment) ;


        if (notif == parsingNotif){
            notifikasi.b = "a";
            fragment = new notifikasi();
            loadFragment(fragment) ;
        } else {
            fragment = new home();
            loadFragment(fragment) ;
        }











        mFirestore.collection("Users").document(mUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                userName = documentSnapshot.getString("name");
                userImage = documentSnapshot.getString("image");


                Toast.makeText(MainActivity.this,"Selamat Datang "+userName, Toast.LENGTH_LONG).show();


            }
            }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this,"Error:"+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });



    }
    private void sendToLogin(){
        Intent i = new Intent(MainActivity.this,Login.class);
        startActivity(i);
        finish();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    fragment = new home();
                    loadFragment(fragment) ;
                    return true;

                case R.id.navigation_notifikasi:
                    notifikasi.b = "a";
                    fragment = new notifikasi();
                    loadFragment(fragment) ;
                    return true;

                case R.id.navigation_tim:

                    fragment = new tim();
                    loadFragment(fragment) ;
                    return true;


                case R.id.navigation_akun:
                    fragment = new akun();
                    loadFragment(fragment) ;
                    return true;

            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    @Override
    public void onBackPressed() {


        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed(); return;
        } else {
            Toast.makeText(getBaseContext(), "Tekan Back Sekali Lagi Untuk Keluar", Toast.LENGTH_SHORT).show();
        } mBackPressed = System.currentTimeMillis();



    }



}
