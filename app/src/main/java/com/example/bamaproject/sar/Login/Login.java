package com.example.bamaproject.sar.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.example.bamaproject.sar.Tim_Mana;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    public EditText mEmailField;
    private EditText mPasswordField;
    private Button mLoginButton;
    private Button mRegisterButton ;

    private FirebaseAuth mAuth;
    ProgressDialog pd;
    private ProgressBar mRegProgresBar;


//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser == null){
//            sendToMain();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(Login.this);
        mEmailField = (EditText)findViewById(R.id.eEmail);
        mPasswordField = (EditText)findViewById(R.id.ePassword);
        mLoginButton = (Button)findViewById(R.id.btnLogin);
        mRegisterButton = (Button)findViewById(R.id.btnRegister);

        mRegProgresBar = (ProgressBar)findViewById(R.id.progressBarLogin);

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this,Register.class);
                startActivity(i);
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setMessage("Login . .");
                pd.setCancelable(false);
                pd.show();


                String email = mEmailField.getText().toString().trim();
                String pwd = mPasswordField.getText().toString().trim();


                if ( !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pwd) ) {

                    mRegProgresBar.setVisibility(View.VISIBLE);

                    mAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                pd.cancel();
                                sendToMain();
                                mRegProgresBar.setVisibility(View.INVISIBLE);
                            } else {
                                pd.cancel();
                                Toast.makeText(Login.this,"Error"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                                mRegProgresBar.setVisibility(View.INVISIBLE);
                            }




                        }
                    });





                }

            }
        });


    }


    private void sendToMain() {
        Intent i = new Intent(Login.this, Tim_Mana.class);
        startActivity(i);
        finish();

    }





}
