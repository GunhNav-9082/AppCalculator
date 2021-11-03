package com.example.appandroidcalculator;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainCalculatorActivity extends AppCompatActivity {

    ImageButton btnAdd ,btnSub,btnMulti,btnDiv,btnSignOut;
    EditText edtA, edtB;
    TextView kq;
    ImageView imgAvatar;
    TextView textView_UserName,textView_UserEmail;
    GoogleSignInClient mGoogleSignInClient;

    private ServiceConnection serviceConnection;

    private boolean isConnected;
    private MyService myService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maincaculator);
        initView();
        connectService();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnSignOut = findViewById(R.id.btnSignOut);
        textView_UserName = findViewById(R.id.textView_AccGoogle);
        imgAvatar = findViewById(R.id.imgAvatar);

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnSignOut:
                        signOut();
                        Intent intent = new Intent(MainCalculatorActivity.this,LoginActivity    .class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                }
            }
        });
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            Uri personPhoto = acct.getPhotoUrl();

            textView_UserName.setText(personName);

            Glide.with(this).load(String.valueOf(personPhoto)).into(imgAvatar);
        }
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainCalculatorActivity.this, "Sign Out Successfully", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }

    private void connectService() {

        Intent intent = new Intent(this, MyService.class);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MyService.MyBinder myBinder = (MyService.MyBinder) service;

                myService = myBinder.getService();
                isConnected = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isConnected = false;
                myService = null;
            }
        };
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    void initView()
    {
        btnAdd =(ImageButton) findViewById(R.id.btnAdd);
        btnSub =(ImageButton) findViewById(R.id.btnSub);
        btnMulti =(ImageButton) findViewById(R.id.btnMulti);
        btnDiv=(ImageButton) findViewById(R.id.btnDiv);
        edtA = (EditText) findViewById(R.id.edtA);
        edtB = (EditText) findViewById(R.id.edtB);
        kq = (TextView)findViewById(R.id.textViewKq) ;



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected){
                    return;
                }
                kq.setText("");
                int result = myService.add(
                        Integer.parseInt(edtA.getText().toString()),
                        Integer.parseInt(edtB.getText().toString()));

                kq.setText(kq.getText().toString() +result);
                Toast.makeText(myService, "Result:" + result, Toast.LENGTH_SHORT).show();


            }

        });



        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected){
                    return;
                }
                kq.setText("");
                int result = myService.sub(
                        Integer.parseInt(edtA.getText().toString()),
                        Integer.parseInt(edtB.getText().toString()));

                kq.setText(kq.getText().toString() +result);
                Toast.makeText(myService, "Result:" + result, Toast.LENGTH_SHORT).show();

            }
        });


        btnMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected){
                    return;
                }
                kq.setText("");
                int result = myService.multi(
                        Integer.parseInt(edtA.getText().toString()),
                        Integer.parseInt(edtB.getText().toString()));

                kq.setText(kq.getText().toString() +result);
                Toast.makeText(myService, "Result:" + result, Toast.LENGTH_SHORT).show();

            }
        });


        btnDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected){
                    return;
                }
                kq.setText("");
                int result = myService.div(
                        Integer.parseInt(edtA.getText().toString()),
                        Integer.parseInt(edtB.getText().toString()));

                kq.setText(kq.getText().toString() +result);
                Toast.makeText(myService, "Result:" + result, Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }


}
