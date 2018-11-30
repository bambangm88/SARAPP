package com.example.bamaproject.sar.Fragment;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bamaproject.sar.Main.MainActivity;
import com.example.bamaproject.sar.R;
import com.example.bamaproject.sar.adapter.RecyclerViewAdapter;

import com.example.bamaproject.sar.model.data_model;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.NOTIFICATION_SERVICE;


public class notifikasi extends Fragment {

    //Deklarasi Variable untuk RecyclerView
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public static String b = "" ;

    ImageView notfound;
    private ProgressBar mRegProgresBar;

    //Deklarasi Variable Database Reference dan ArrayList dengan Parameter Class Model kita.
    private DatabaseReference reference;
    private ArrayList<data_model> dataMahasiswa;

    private FirebaseAuth auth;
    ProgressDialog pd;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evakuasi,container, false);
        setHasOptionsMenu(true);
        //menerapkan tool bar sesuai id toolbar | ToolBarAtas adalah variabel buatan sndiri
        Toolbar ToolBarAtas2 = view.findViewById(R.id.toolbar_evakuasi);
        ((AppCompatActivity)getActivity()).setSupportActionBar(ToolBarAtas2);
        notfound = view.findViewById(R.id.notFound);
        pd = new ProgressDialog(getActivity());
        mRegProgresBar = view.findViewById(R.id.progressBarnotif);

        recyclerView = view.findViewById(R.id.datalist);
        //getSupportActionBar().setTitle("Data Mahasiswa");
        auth = FirebaseAuth.getInstance();
        MyRecyclerView();
        GetData();



        return view ;
    }





    //Berisi baris kode untuk mengambil data dari Database dan menampilkannya kedalam Adapter
    private void GetData(){


        mRegProgresBar.setVisibility(View.VISIBLE);


        //Mendapatkan Referensi Database
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("SAR").child("NOTIFIKASI")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Inisialisasi ArrayList
                        dataMahasiswa = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            mRegProgresBar.setVisibility(View.INVISIBLE);
                            //Mapping data pada DataSnapshot ke dalam objek mahasiswa
                            data_model mahasiswa = snapshot.getValue(data_model.class);

                            //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                            mahasiswa.setKey(snapshot.getKey());
                            dataMahasiswa.add(mahasiswa);







                            String a = "a";

                            if (a == b){

                            } else {

                                showNotif();
                                PowerManager pm = (PowerManager)getActivity().getSystemService(Context.POWER_SERVICE);
                                boolean isScreenOn = pm.isScreenOn();
                                if(isScreenOn==false)
                                {
                                    PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,""+b);
                                    wl.acquire(10000);
                                    PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,""+a);

                                    wl_cpu.acquire(10000);
                                }
                            }



                        }

                        //Inisialisasi Adapter dan data Mahasiswa dalam bentuk Array
                        adapter = new RecyclerViewAdapter(getActivity(),dataMahasiswa,notifikasi.this);

                        //Memasang Adapter pada RecyclerView
                        recyclerView.setAdapter(adapter);

                        notifikasi.b = "";


                        if (dataMahasiswa.isEmpty()){

                            notfound.setVisibility(View.VISIBLE);
                            mRegProgresBar.setVisibility(View.INVISIBLE);

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        mRegProgresBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity(),"Data Gagal Dimuat", Toast.LENGTH_LONG).show();
                        Log.e("MyListActivity", databaseError.getDetails()+" "+databaseError.getMessage());
                    }




                } );
    }

    //Methode yang berisi kumpulan baris kode untuk mengatur RecyclerView
    private void MyRecyclerView(){
        //Menggunakan Layout Manager, Dan Membuat List Secara Vertical
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //Membuat Underline pada Setiap Item Didalam List
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.line));
        recyclerView.addItemDecoration(itemDecoration);
    }





    private void showNotif() {

        NotificationManager notificationManager;

        MainActivity.parsingNotif = "a";
        notifikasi.b = "a";
        Intent mIntent = new Intent(getActivity(),MainActivity.class);
        Bundle bundle = new Bundle();


        bundle.putString("EVAKUASI DITEMUKAN !!!!!", "NOTIFIKASI BARU");
        mIntent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity());
        builder.setColor(getResources().getColor(R.color.colorAccent));
        builder.setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_user)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_user))
                .setTicker("NOTIFIKASI BARU")
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setLights(Color.RED, 3000, 3000)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentTitle("EVAKUASI DITEMUKAN")
                .setContentText("SEGERA MERAPAT");

        notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(115, builder.build());
    }



    public void DeleteData(data_model data,int position) {
        b = "a" ;

        String userID = auth.getUid();
        if(reference != null){
            reference.child("SAR")
                    .child("NOTIFIKASI")
                    .child(data.getKey())
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getActivity(), "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                        }
                    });
        }



    }
}
