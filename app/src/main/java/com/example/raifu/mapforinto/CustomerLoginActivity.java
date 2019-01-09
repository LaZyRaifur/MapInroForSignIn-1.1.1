package com.example.raifu.mapforinto;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.raifu.mapforinto.Model.UserModerForDriver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerLoginActivity extends AppCompatActivity {


    ProgressBar progressBar;
    private Button btnLogin, btnRegistration;
    private EditText mEmail, mName, mPassword, mPhoneNumber;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);

        mAuth = FirebaseAuth.getInstance();
        mEmail = (EditText) findViewById(R.id.Driver_email);
        mName = (EditText) findViewById(R.id.Driver_name);
        mPassword = (EditText) findViewById(R.id.Driver_password);
        mPhoneNumber = (EditText) findViewById(R.id.Driver_phone);

        progressBar = findViewById(R.id.progressbar);

        // findViewById(R.id.Btn_registration).setOnClickListener(this::onClick);


        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    Intent intent = new Intent(CustomerLoginActivity.this, CustomerMapActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        //  mEmail = (EditText) findViewById(R.id.email);
        // mName = (EditText) findViewById(R.id.name);
        //  mPassword = (EditText) findViewById(R.id.password);
        //  mCurrentAddress = (EditText) findViewById(R.id.CurrentAddress);

        // btnLogin = (Button) findViewById(R.id.button_login);
        btnRegistration = (Button) findViewById(R.id.button_register);

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final String password = mPassword.getText().toString();
                // final String name = mName.getText().toString();
                //final String email = mEmail.getText().toString();
                //final String currentAddress = mCurrentAddress.getText().toString();
                final String name = mName.getText().toString().trim();
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();

                final String phone = mPhoneNumber.getText().toString().trim();

                if (name.isEmpty()) {
                    mName.setError(getString(R.string.input_error_name));
                    mName.requestFocus();
                    return;
                }
                if (name.isEmpty()) {
                    mEmail.setError(getString(R.string.input_error_email));
                    mEmail.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mEmail.setError(getString(R.string.input_error_email_invalid));
                    mEmail.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    mPassword.setError(getString(R.string.input_error_password));
                    mPassword.requestFocus();
                    return;
                }

                if (password.length() < 6) {
                    mPassword.setError(getString(R.string.input_error_password_length));
                    mPassword.requestFocus();
                    return;
                }

                if (phone.isEmpty()) {
                    mPhoneNumber.setError(getString(R.string.input_error_phone));
                    mPhoneNumber.requestFocus();
                    return;
                }

                if (phone.length() > 11) {
                    mPhoneNumber.setError(getString(R.string.input_error_phone_invalid));
                    mPhoneNumber.requestFocus();
                    return;
                }


                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CustomerLoginActivity
                        .this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (!task.isSuccessful()) {
//                            Toast.makeText(DriverLoginActivity.this, "Signup error", Toast.LENGTH_SHORT).show();
//                        } else {
//                            String user_id = mAuth.getCurrentUser().getUid();
//                            DatabaseReference current_user_db = FirebaseDatabase.getInstance()
// .getReference().child("Users").child("drivers").child(user_id).child("name");
//                            current_user_db.setValue(email);
//                        }
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.VISIBLE);
                            UserModerForDriver user = new UserModerForDriver(
                                    name,
                                    email,
                                    phone
                            );

                            String user_id = mAuth.getCurrentUser().getUid();

                            DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference()
                                    .child("Users").child("Customers")
                                    .child(user_id).child("name");
                            current_user_db.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(CustomerLoginActivity.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                    } else {
                                        //display a failure message
                                    }
                                }
                            });
// .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    progressBar.setVisibility(View.GONE);
//                                    if (task.isSuccessful()) {
//                                        Toast.makeText(DriverLoginActivity.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
//                                    } else {
//                                        //display a failure message
//                                    }
//                                }
//                            });

                        } else {
                            Toast.makeText(CustomerLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });

//        btnLogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final String email = mEmail.getText().toString();
//                final String password = mPassword.getText().toString();
//
//                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(DriverLoginActivity.this,
//                        new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (!task.isSuccessful()) {
//                                    Toast.makeText(DriverLoginActivity.this, "Signin error", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//            }
//        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {

        }

        mAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthListener);
    }

}
