package com.corp.srihari.deca;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    DatabaseReference myRef;
    private ImageButton signUp;
    private ProgressBar progressBar;
    private EditText fullNameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private EditText cpasswordInput;
    private TextView bysign;
    private Button requestLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        DatabaseUtils databaseUtils = new DatabaseUtils();
        mAuth = databaseUtils.getAuthInstance();
        database = databaseUtils.getDatabaseInstance();

        signUp = (ImageButton) findViewById(R.id.signUpButton);
        progressBar = (ProgressBar) findViewById(R.id.signingupload);
        fullNameInput = (EditText) findViewById(R.id.name_edit);
        emailInput = (EditText) findViewById(R.id.email_edit);
        passwordInput = (EditText) findViewById(R.id.password_edit);
        cpasswordInput = (EditText) findViewById(R.id.cpassword_edit);

        bysign = (TextView) findViewById(R.id.bysigningup);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fullName = fullNameInput.getText().toString().trim();
                final String email = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();
                String cpassword = cpasswordInput.getText().toString().trim();

                if (TextUtils.isEmpty(fullName)) {
                    Toast.makeText(getApplicationContext(), "Please enter your full name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Please enter your email address.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(cpassword)) {
                    Toast.makeText(getApplicationContext(), "Please confirm your password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password.equals(cpassword)) {
                    Toast.makeText(getApplicationContext(), "Please reenter your passwords!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!email.contains("@")) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //Time to create the user
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);

                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException(), Toast.LENGTH_LONG).show();
                        } else {
                            saveBool("signedIn",true);
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            Toast.makeText(RegisterActivity.this, "Signed Up!", Toast.LENGTH_LONG).show();
                            myRef = database.getReference().child("Users").child(mAuth.getCurrentUser().getUid());
                            myRef.child("Email").setValue(mAuth.getCurrentUser().getEmail());
//                            for (String s : getResources().getStringArray(R.array.examChoices)) {
//                                myRef.child("Scores").child(s).setValue("s");
//                            }
                            finish();
                        }
                    }
                });


            }
        });

        requestLogin = (Button) findViewById(R.id.requestLogin);
        requestLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public void saveBool(String name, final boolean bool) {
        SharedPreferences sharedPreferences = getSharedPreferences("Settings_Money", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(name,bool);
        editor.commit();
    }
    @Override
    public void onBackPressed() {
        finish();
    }
}
