package com.example.chatapp.Register;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.Login.LoginActivity;
import com.example.chatapp.MainActivity;
import com.example.chatapp.R;
import com.example.chatapp.StartActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private EditText email,username,password;
    private Button signUpBtn;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private TextView alreadyHaveAnAccount;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        context=getApplicationContext();
        FirebaseApp.initializeApp(context);
        mAuth=FirebaseAuth.getInstance();
        initlization();

        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToLoginAcitivty();
            }
        });


        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtUserName=username.getText().toString().trim();
                String txtEmail=email.getText().toString().trim();
                String txtPassword=password.getText().toString().trim();

                if (TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtUserName) || TextUtils.isEmpty(txtPassword))
                {
                    Toast.makeText(RegisterActivity.this, "All Fields Are Required", Toast.LENGTH_SHORT).show();
                }else if (txtPassword.length() < 8){
                    Toast.makeText(RegisterActivity.this, "password must be at least 8 char", Toast.LENGTH_SHORT).show();
                }else {
                    verifyRegister(txtEmail,txtUserName,txtPassword);
                }
            }
        });


    }

    private void initlization()
    {
        toolbar=findViewById(R.id.toolbarRegister);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        email=findViewById(R.id.register_edit_email);
        username=findViewById(R.id.register_edit_fullName);
        password=findViewById(R.id.register_edit_password);
        signUpBtn=findViewById(R.id.register_btn_signUp);
        progressBar=findViewById(R.id.registerProgressBar);

        alreadyHaveAnAccount=findViewById(R.id.alreadyhaveAnAccount);
    }


    private void verifyRegister(String email, final String username, String password)
    {
        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    FirebaseUser firebaseUser=mAuth.getCurrentUser();
                    assert firebaseUser != null;
                    String userId=firebaseUser.getUid();

                    reference=FirebaseDatabase.getInstance().getReference("Users").child(userId);
                    HashMap<String,String> hashMap=new HashMap<>();
                    hashMap.put("id",userId);
                    hashMap.put("username",username);
                    hashMap.put("imageUrl","default");
                    hashMap.put("status","offline");
                    hashMap.put("search",username.toLowerCase());

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                sendUserToStartActivity();
                            }

                        }
                    });
                }
                else {
                    progressBar.setVisibility(View.INVISIBLE);
                    String message=task.getException().toString();
                    Toast.makeText(RegisterActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendUserToStartActivity()
    {
        Intent mainIntent=new Intent(RegisterActivity.this, StartActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        finish();
    }


    private void sendUserToLoginAcitivty()
    {
        Intent loginIntent=new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }


}
