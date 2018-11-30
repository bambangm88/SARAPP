package com.example.bamaproject.sar;

import android.app.ProgressDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bamaproject.sar.Fragment.tim;
import com.example.bamaproject.sar.adapter.AnggotaRecyclerAdapter;
import com.example.bamaproject.sar.adapter.UsersRecyclerAdapter;
import com.example.bamaproject.sar.model.data_model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class anggotaTim extends AppCompatActivity {

    //Deklarasi Variable untuk RecyclerView
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public static String tim = "" ;

    ImageView notfound;
    private ProgressBar mRegProgresBar;

    //Deklarasi Variable Database Reference dan ArrayList dengan Parameter Class Model kita.
    private DatabaseReference reference;
    private ArrayList<data_model> dataMahasiswa;

    private FirebaseAuth auth;
    ProgressDialog pd;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anggota_tim);


        Toolbar ToolBarAtas2 = findViewById(R.id.toolbar_anggotaTim);
        setSupportActionBar(ToolBarAtas2);


        recyclerView = findViewById(R.id.anggota_list);
        //getSupportActionBar().setTitle("Data Mahasiswa");

        MyRecyclerView();
        GetData();


        Toast.makeText(anggotaTim.this,"Daftar Anggota "+tim,Toast.LENGTH_LONG).show();

    }










    //Berisi baris kode untuk mengambil data dari Database dan menampilkannya kedalam Adapter
    private void GetData(){




        //Mendapatkan Referensi Database
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("SAR").child("ANGGOTA TIM").child(tim)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Inisialisasi ArrayList
                        dataMahasiswa = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                            //Mapping data pada DataSnapshot ke dalam objek mahasiswa
                            data_model mahasiswa = snapshot.getValue(data_model.class);

                            //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                            mahasiswa.setKey(snapshot.getKey());
                            dataMahasiswa.add(mahasiswa);


                        }

                        //Inisialisasi Adapter dan data Mahasiswa dalam bentuk Array
                        adapter = new AnggotaRecyclerAdapter(anggotaTim.this, dataMahasiswa, anggotaTim.this);

                        //Memasang Adapter pada RecyclerView
                        recyclerView.setAdapter(adapter);



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Toast.makeText(anggotaTim.this,"Data Gagal Dimuat", Toast.LENGTH_LONG).show();
                        Log.e("MyListActivity", databaseError.getDetails()+" "+databaseError.getMessage());
                    }




                } );
    }

    //Methode yang berisi kumpulan baris kode untuk mengatur RecyclerView
    private void MyRecyclerView(){
        //Menggunakan Layout Manager, Dan Membuat List Secara Vertical
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //Membuat Underline pada Setiap Item Didalam List
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(this, R.drawable.line));
        recyclerView.addItemDecoration(itemDecoration);
    }























}
