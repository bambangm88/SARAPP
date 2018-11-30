package com.example.bamaproject.sar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bamaproject.sar.Fragment.home;
import com.example.bamaproject.sar.Login.Register;
import com.example.bamaproject.sar.Main.MainActivity;
import com.example.bamaproject.sar.model.model_koordinat;
import com.example.bamaproject.sar.model.model_tim;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Tim_Mana extends AppCompatActivity {

    private Spinner spinner1;

    private FirebaseAuth mAuth;
    private ProgressBar mRegProgresBar;

    private FirebaseFirestore mFirestore;

    private String mUserId;
    private FirebaseFirestore mStore;
    public String userName, pilihTim;
    private String userImage;

    private Button btnTim;

    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tim__mana);

        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new ItemSelectedListener());
        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        mFirestore = FirebaseFirestore.getInstance();
        mUserId = mAuth.getCurrentUser().getUid();

        btnTim = (Button)findViewById(R.id.buttonTim);

        pd = new ProgressDialog(Tim_Mana.this);


        btnTim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mengirim_data_tim();


            }
        });


        mFirestore.collection("Users").document(mUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                userName = documentSnapshot.getString("name");
                userImage = documentSnapshot.getString("image");


                Toast.makeText(Tim_Mana.this,"Silahkan Pilih Tim", Toast.LENGTH_LONG).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Tim_Mana.this,"Error:"+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });









    }


    public class ItemSelectedListener implements AdapterView.OnItemSelectedListener {

        //get strings of first item
        String firstItem = String.valueOf(spinner1.getSelectedItem());

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if (firstItem.equals(String.valueOf(spinner1.getSelectedItem()))) {
                // ToDo when first item is selected
            } else {

                pilihTim = parent.getItemAtPosition(pos).toString();


                Map<String,Object> userMap = new HashMap<>();
                userMap.put("name",userName);
                userMap.put("image",userImage);
                userMap.put("tim",pilihTim);

                mStore.collection("Users").document(mUserId).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                            Toast.makeText(Tim_Mana.this,pilihTim+" Dipilih",Toast.LENGTH_SHORT).show();

                    }
                });


                // Todo when item is selected by the user
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg) {

        }

    }


    public void mengirim_data_tim() {

        pd.setMessage("Loading . .");
        pd.setCancelable(false);
        pd.show();

            mFirestore.collection("Users").document(mUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {

                    userName = documentSnapshot.getString("name");
                    userImage = documentSnapshot.getString("image");



                        //Mendapatkan Instance dari Database
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference getReference;

                        //Menyimpan Data yang diinputkan User kedalam Variable

                        getReference = database.getReference(); // Mendapatkan Referensi dari Database

                        getReference.child("SAR").child("TIM").child(pilihTim).child(userName)
                                .setValue(new model_tim(""+userName))
                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                    pd.cancel();
                                   Intent i = new Intent(Tim_Mana.this,MainActivity.class);
                                   startActivity(i);
                                   finish();

                               }
                           }).addOnCanceledListener(new OnCanceledListener() {
                            @Override
                            public void onCanceled() {

                                Toast.makeText(Tim_Mana.this,"Error ; Periksa Internet Anda",Toast.LENGTH_SHORT).show();

                            }
                        });




                }
            });



    }
}