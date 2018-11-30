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
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bamaproject.sar.Detail.detail_evakuasi;

import com.example.bamaproject.sar.Fragment.notifikasi;
import com.example.bamaproject.sar.R;

import com.example.bamaproject.sar.model.data_model;
import com.example.bamaproject.sar.updateData;

import java.util.ArrayList;

//Class Adapter ini Digunakan Untuk Mengatur Bagaimana Data akan Ditampilkan
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{


    //Deklarasi Variable

public  notifikasi notifikasi;
    private ArrayList<data_model> listMahasiswa;
    private Context context;

    RequestOptions option;



    //Membuat Konstruktor, untuk menerima input dari Database
    public RecyclerViewAdapter( Context context, ArrayList<data_model> listMahasiswa, notifikasi notifikasi) {
        this.listMahasiswa = listMahasiswa;
        this.context = context;
        this.notifikasi = notifikasi ;

        option = new RequestOptions().centerCrop().placeholder(R.drawable.img).error(R.drawable.img_error);
    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nama, lat, longi, pukul,pesan;
        private LinearLayout ListItem;
        private ImageView image_list;

        ViewHolder(View itemView) {
            super(itemView);
            //Menginisialisasi View-View yang terpasang pada layout RecyclerView kita
            nama = itemView.findViewById(R.id.txtnama);
            pukul = itemView.findViewById(R.id.txtpukul);
            pesan = itemView.findViewById(R.id.txtpesan);
            ListItem = itemView.findViewById(R.id.list_item);
            image_list = itemView.findViewById(R.id.img_listrow);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_design, parent, false);
        return new ViewHolder(V);
    }



    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //Mengambil Nilai/Value yenag terdapat pada RecyclerView berdasarkan Posisi Tertentu
        final String Nama= listMahasiswa.get(position).getNama();
        final String Lat = listMahasiswa.get(position).getLat();
        final String Longi = listMahasiswa.get(position).getLongi();
        final String Pukul = listMahasiswa.get(position).getTanggal();
        final String Pesan = listMahasiswa.get(position).getPesan();

        //Memasukan Nilai/Value kedalam View (TextView: NIM, Nama, Jurusan)
        holder.nama.setText("Nama : "+Nama);
        holder.pukul.setText("Pukul: "+Pukul);
        holder.pesan.setText(Pesan);

        Glide.with(context).load(listMahasiswa.get(position).getImageUrl()).apply(option).into(holder.image_list);



        holder.ListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString("nama", listMahasiswa.get(position).getNama());
                bundle.putString("lat", listMahasiswa.get(position).getLat());
                bundle.putString("long", listMahasiswa.get(position).getLongi());
                bundle.putString("pukul", listMahasiswa.get(position).getTanggal());
                bundle.putString("getPrimaryKey", listMahasiswa.get(position).getKey());
                Intent intent = new Intent(v.getContext(), detail_evakuasi.class);
                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        });





        //Menampilkan Menu Update dan Delete saat user melakukan long klik pada salah satu item
        holder.ListItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                final String[] action = {"Lihat", "Delete"};
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setItems(action,  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i){
                            case 0:
                                Bundle bundle = new Bundle();
                                bundle.putString("nama", listMahasiswa.get(position).getNama());
                                bundle.putString("lat", listMahasiswa.get(position).getLat());
                                bundle.putString("long", listMahasiswa.get(position).getLongi());
                                bundle.putString("pukul", listMahasiswa.get(position).getTanggal());
                                bundle.putString("getPrimaryKey", listMahasiswa.get(position).getKey());
                                Intent intent = new Intent(view.getContext(), detail_evakuasi.class);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                                break;
                            case 1:
                                //Menggunakan interface untuk mengirim data mahasiswa, yang akan dihapus
                                notifikasi.DeleteData(listMahasiswa.get(position), position);
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