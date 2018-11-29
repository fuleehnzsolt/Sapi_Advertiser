package com.example.fuleehnzsolt.sapi_advertiser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import com.example.fuleehnzsolt.sapi_advertiser.Data.User;

import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {

    private EditText rFirstName, rLastName, rPhoneNumber, rCode;
    private Button rCodeButton, registerButton;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private CollectionReference dbPhoneNumbers;
    private String phoneNumber;
    private String mVerificationId;
    private String verificationCode;
    private String firstName;
    private String lastName;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        rFirstName = findViewById(R.id.rfName);
        rLastName = findViewById(R.id.rlName);
        rPhoneNumber = findViewById(R.id.rPhoneNumber);
        rCode = findViewById(R.id.rCodeEdit);
        rCodeButton = findViewById(R.id.rGetCodeButton);
        registerButton = findViewById(R.id.regButton);

        rCode.setVisibility(View.INVISIBLE);
        registerButton.setVisibility(View.INVISIBLE);

        rCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phoneNumber = rPhoneNumber.getText().toString();
                phoneNumber = "+40" + phoneNumber;
                firstName = rFirstName.getText().toString();
                lastName = rLastName.getText().toString();

                if (TextUtils.isEmpty(phoneNumber)) {

                    Toast.makeText(RegisterActivity.this, "Enter your phone number, please!", Toast.LENGTH_LONG).show();

                } else {

                    CollectionReference collectionReference = db.collection("users");
                    Query query = collectionReference.whereEqualTo("phoneNumber", phoneNumber);

                    query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.isEmpty()) {
                                Log.d("Register", "phone nubmer doesn't exists");
                                Log.d("Register", "registering");

                                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                        phoneNumber,        // Phone number to verify
                                        60,                 // Timeout duration
                                        TimeUnit.SECONDS,   // Unit of timeout
                                        RegisterActivity.this,               // Activity (for callback binding)
                                        mCallbacks // OnVerificationStateChangedCallbacks
                                );


                            } else {

                                Toast.makeText(RegisterActivity.this, "This number is already registered", Toast.LENGTH_LONG).show();
                                Log.d("Register", "phone number exists");
                                Log.d("Register", "sending login sms");
                            }

                        }
                    });
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verificationCode = rCode.getText().toString();

                if (TextUtils.isEmpty(verificationCode)) {

                    Toast.makeText(RegisterActivity.this, "Enter the Code!", Toast.LENGTH_LONG).show();

                } else {

                    dbPhoneNumbers = db.collection("users");
                    User user = new User(
                            firstName,
                            lastName,
                            phoneNumber
                    );

                    dbPhoneNumbers.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            Toast.makeText(RegisterActivity.this, "Account creation succeed!", Toast.LENGTH_LONG).show();

                        }
                    });

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                    signInWithPhoneAuthCredential(credential);

                }

            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                dbPhoneNumbers = db.collection("users");
                User user = new User(
                        firstName,
                        lastName,
                        phoneNumber
                );

                dbPhoneNumbers.add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(RegisterActivity.this, "Account creation succeed!", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG);
                    }
                });


                signInWithPhoneAuthCredential(credential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                Toast.makeText(RegisterActivity.this, "Verification failed!", Toast.LENGTH_LONG).show();
                rCode.setVisibility(View.INVISIBLE);
                registerButton.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                mVerificationId = verificationId;
                mResendToken = token;

                Toast.makeText(RegisterActivity.this, "Register code sent...", Toast.LENGTH_SHORT).show();

                rCode.setVisibility(View.VISIBLE);
                registerButton.setVisibility(View.VISIBLE);
            }
        };

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "You are logged in!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, AdvertiseActivity.class));
                            finish();
                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(RegisterActivity.this, "Error: " + message, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}

