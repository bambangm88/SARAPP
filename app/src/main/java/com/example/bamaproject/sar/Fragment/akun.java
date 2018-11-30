package com.example.bamaproject.sar.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.bamaproject.sar.Login.Login;
import com.example.bamaproject.sar.Main.MainActivity;
import com.example.bamaproject.sar.Main.petunjuk;
import com.example.bamaproject.sar.Main.tentang;
import com.example.bamaproject.sar.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;


public class akun extends Fragment {

    private Button mLogoutBtn,mButtonTentang,mButtonPetunjuk;

    private CircleImageView mProfilCircle ;

    private TextView mProfileLabel;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;

    private String mUserId;

    RequestOptions option;


    public akun() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_akun, container, false);

        mAuth = FirebaseAuth.getInstance();
        mFirestore =  FirebaseFirestore.getInstance();


        mUserId = mAuth.getCurrentUser().getUid();


        mLogoutBtn = view.findViewById(R.id.buttonLogout);
        mButtonTentang = view.findViewById(R.id.buttonTentang);
        mButtonPetunjuk = view.findViewById(R.id.buttonPetunjuk);

        mProfilCircle = view.findViewById(R.id.profile_image_list);
        mProfileLabel = view.findViewById(R.id.labelAkun);


        mFirestore.collection("Users").document(mUserId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String userName = documentSnapshot.getString("name");
                String userImage = documentSnapshot.getString("image");

                mProfileLabel.setText(userName);




                option = new RequestOptions().centerCrop().placeholder(R.drawable.img).error(R.drawable.img_error);


                Glide.with(getActivity()).setDefaultRequestOptions(option).load(userImage).into(mProfilCircle);



            }
        });


        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAlertDialog() ;
            }
        });


        mButtonTentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),tentang.class);
                startActivity(i);
            }
        });


        mButtonPetunjuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),petunjuk.class);
                startActivity(i);
            }
        });




        return view;
    }






    //perintah delete ------------
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setMessage("Keluar Akun Anda?")
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAuth.signOut();
                        dialogInterface.dismiss();
                        getActivity().finish();

                        Intent u = new Intent(getActivity(),Login.class);
                        startActivity(u);




                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
//----------------


}
