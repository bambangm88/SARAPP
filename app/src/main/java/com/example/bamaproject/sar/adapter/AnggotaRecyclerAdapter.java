package com.example.bamaproject.sar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bamaproject.sar.Fragment.tim;
import com.example.bamaproject.sar.R;
import com.example.bamaproject.sar.anggotaTim;
import com.example.bamaproject.sar.model.data_model;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

//Class Adapter ini Digunakan Untuk Mengatur Bagaimana Data akan Ditampilkan
public class AnggotaRecyclerAdapter extends RecyclerView.Adapter<AnggotaRecyclerAdapter.ViewHolder>{


    //Deklarasi Variable

    public anggotaTim anggotaTim;
    private ArrayList<data_model> listMahasiswa;
    private Context context;

    RequestOptions option;



    //Membuat Konstruktor, untuk menerima input dari Database
    public AnggotaRecyclerAdapter(Context context, ArrayList<data_model> listMahasiswa, anggotaTim anggotaTim) {
        this.listMahasiswa = listMahasiswa;
        this.context = context;
        this.anggotaTim = anggotaTim;


        option = new RequestOptions().centerCrop().placeholder(R.drawable.img).error(R.drawable.img_error);
    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nama, lat, longi, pukul,pesan;
        private RelativeLayout ListItem;
        private CircleImageView image_list;

        ViewHolder(View itemView) {
            super(itemView);
            //Menginisialisasi View-View yang terpasang pada layout RecyclerView kita
            nama = itemView.findViewById(R.id.labelUserList);
            ListItem = itemView.findViewById(R.id.list_anggota);
            image_list = itemView.findViewById(R.id.userListImageCircle);

        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.anggota_row, parent, false);
        return new ViewHolder(V);
    }



    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //Mengambil Nilai/Value yenag terdapat pada RecyclerView berdasarkan Posisi Tertentu
        final String Nama= listMahasiswa.get(position).getNama();

        //Memasukan Nilai/Value kedalam View (TextView: NIM, Nama, Jurusan)
        holder.nama.setText(Nama);


        Glide.with(context).load(listMahasiswa.get(position).getImageUrl()).apply(option).into(holder.image_list);

    }

    @Override
    public int getItemCount() {
        //Menghitung Ukuran/Jumlah Data Yang Akan Ditampilkan Pada RecyclerView
        return listMahasiswa.size();
    }


























}