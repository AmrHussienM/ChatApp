package com.example.chatapp.Login;

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
import android.support.v7.app.ActionBar;

import com.example.chatapp.MainActivity;
import com.example.chatapp.R;
import com.example.chatapp.Register.RegisterActivity;
import com.example.chatapp.ResetPasswordActivity;
import com.example.chatapp.StartActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends AppCompatActivity {

    private EditText email,password;
    private Button signInBtn;
    private TextView forgotYourPassword;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialization();

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyLogin();
            }
        });

        forgotYourPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendUserToResetPasswordActivity();
            }
        });
    }

    private void initialization()
    {
        toolbar=findViewById(R.id.toolbarLogin);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth=FirebaseAuth.getInstance();

        email=findViewById(R.id.login_edit_email);
        password=findViewById(R.id.login_edit_password);
        signInBtn=findViewById(R.id.login_btn_signIn);
        forgotYourPassword=findViewById(R.id.forgotYourPassword);
        progressBar=findViewById(R.id.loginProgressBar);
    }

    private void verifyLogin()
    {

        String txtEmail=email.getText().toString().trim();
        String txtPassword=password.getText().toString().trim();

        if (TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPassword)){
            Toast.makeText(LoginActivity.this, "All Fields are requried", Toast.LENGTH_SHORT).show();
        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(txtEmail,txtPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        sendUserToStartActivity();
                    }
                    else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginActivity.this, "Authincation failed", Toast.LENGTH_SHORT).show();
                    }


                }
            });
        }

    }


    private void sendUserToResetPasswordActivity()
    {
        Intent resetIntent=new Intent(LoginActivity.this, ResetPasswordActivity.class);
        startActivity(resetIntent);
    }




    private void sendUserToStartActivity()
    {
        Intent mainIntent=new Intent(LoginActivity.this, StartActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(mainIntent);
        finish();
    }

}
