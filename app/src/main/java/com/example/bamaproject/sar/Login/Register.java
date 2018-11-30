package com.example.bamaproject.sar.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.bamaproject.sar.Main.MainActivity;
import com.example.bamaproject.sar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Register extends AppCompatActivity {
    private static final int PICK_IMAGE =1 ;
    private CircleImageView mImageBtn ;
    private EditText mNameField;
    private EditText mEmailField;
    private EditText mNIk;
    private EditText mPasswordField;
    private Button mLoginButton;
    private Button mRegisterButton ;

    private Uri imageUri;

    private StorageReference mStorage ;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;

    private ProgressBar mRegProgresBar;

    ProgressDialog pd;

    private DatabaseReference database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        mStorage = FirebaseStorage.getInstance().getReference().child("imagesProfile");
        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        pd = new ProgressDialog(Register.this);

        imageUri = null ;

        mImageBtn = (CircleImageView)findViewById(R.id.profile_image);
        mNameField = (EditText)findViewById(R.id.eName);
        mEmailField = (EditText)findViewById(R.id.eEmailRegister);
        mNIk = (EditText)findViewById(R.id.eNik);
        mPasswordField = (EditText)findViewById(R.id.ePwdRegister);
        mLoginButton = (Button)findViewById(R.id.btnBackLogin);
        mRegisterButton = (Button)findViewById(R.id.btnRegRegister);
        mRegProgresBar = (ProgressBar)findViewById(R.id.progressBarRegister);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (imageUri != null) {

                    mRegProgresBar.setVisibility(View.VISIBLE);
                    pd.setMessage("Sign Up . .");
                    pd.setCancelable(false);
                    pd.show();


                    final String name = mNameField.getText().toString().trim();
                    String email = mEmailField.getText().toString().trim();
                    String pwd = mPasswordField.getText().toString().trim();
                    final String nik = mNIk.getText().toString().trim();

                    if (!TextUtils.isEmpty(name)   &&  !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd) ){



                        mAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()){

                                    final String user_id = mAuth.getCurrentUser().getUid();
                                    final StorageReference user_profile = mStorage.child(user_id+".jpg");

                                    user_profile.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {



                                            if(task.isSuccessful()){


                                                user_profile.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {

                                                        // final String downloadUrl = task.getResult().getStorage().getDownloadUrl().toString();

                                                        Map<String,Object> userMap = new HashMap<>();
                                                        userMap.put("name",name);
                                                        userMap.put("image",uri.toString());
                                                        userMap.put("nik",nik);

                                                        mStore.collection("Users").document(user_id).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                mRegProgresBar.setVisibility(View.INVISIBLE);
                                                                pd.cancel();
                                                                sendToMain();

                                                            }
                                                        });





                                                    }
                                                });





                                            }else {
                                                Toast.makeText(Register.this,"Error"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                                mRegProgresBar.setVisibility(View.INVISIBLE);
                                                pd.cancel();
                                            }


                                        }
                                    });


                                }else {
                                    Toast.makeText(Register.this,"Error"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                    mRegProgresBar.setVisibility(View.INVISIBLE);
                                    pd.cancel();
                                }

                            }
                        });



                    }

                }

            }
        });


        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Register.this,Login.class);
                startActivity(i);
            }
        });

        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i,"Select Picture"),PICK_IMAGE);

            }
        });

    }

    private void sendToMain() {
        Toast.makeText(Register.this,"Register Berhasil",Toast.LENGTH_SHORT).show();
        Login a = new Login();
        a.mEmailField.setText(mEmailField.getText().toString().trim());
        Intent i = new Intent(Register.this, Login.class);
        startActivity(i);
        finish();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            mImageBtn.setImageURI(imageUri);
        }

    }
}
