package com.example.bamaproject.sar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bamaproject.sar.Detail.detail_evakuasi;

import com.example.bamaproject.sar.Fragment.notifikasi;
import com.example.bamaproject.sar.Fragment.tim;
import com.example.bamaproject.sar.R;

import com.example.bamaproject.sar.anggotaTim;
import com.example.bamaproject.sar.model.data_model;
import com.example.bamaproject.sar.updateData;

import java.util.ArrayList;

//Class Adapter ini Digunakan Untuk Mengatur Bagaimana Data akan Ditampilkan
public class UsersRecyclerAdapter extends RecyclerView.Adapter<UsersRecyclerAdapter.ViewHolder>{


    //Deklarasi Variable

    public tim tim;
    private ArrayList<data_model> listMahasiswa;
    private Context context;

    RequestOptions option;



    //Membuat Konstruktor, untuk menerima input dari Database
    public UsersRecyclerAdapter( Context context, ArrayList<data_model> listMahasiswa, tim tim) {
        this.listMahasiswa = listMahasiswa;
        this.context = context;
        this.tim = tim;

        option = new RequestOptions().centerCrop().placeholder(R.drawable.img).error(R.drawable.img_error);
    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nama, lat, longi, pukul,pesan;
        private RelativeLayout ListItem;
        private ImageView image_list;

        ViewHolder(View itemView) {
            super(itemView);
            //Menginisialisasi View-View yang terpasang pada layout RecyclerView kita
            nama = itemView.findViewById(R.id.labelUserList);
            ListItem = itemView.findViewById(R.id.list_tim);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row, parent, false);
        return new ViewHolder(V);
    }



    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //Mengambil Nilai/Value yenag terdapat pada RecyclerView berdasarkan Posisi Tertentu
        final String Nama= listMahasiswa.get(position).getTim();

        //Memasukan Nilai/Value kedalam View (TextView: NIM, Nama, Jurusan)
        holder.nama.setText(Nama);





        holder.ListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                anggotaTim.tim = Nama ;

                Intent intent = new Intent(v.getContext(), anggotaTim.class);
                context.startActivity(intent);


            }
        });





        //Menampilkan Menu Update dan Delete saat user melakukan long klik pada salah satu item
        holder.ListItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                final String[] action = {"Lihat"};
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setItems(action,  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i){
                            case 0:
                                anggotaTim.tim = Nama ;

                                Intent intent = new Intent(view.getContext(), anggotaTim.class);
                                context.startActivity(intent);
                                break;

                        }
                    }
                });
                alert.create();
                alert.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        //Menghitung Ukuran/Jumlah Data Yang Akan Ditampilkan Pada RecyclerView
        return listMahasiswa.size();
    }


























}