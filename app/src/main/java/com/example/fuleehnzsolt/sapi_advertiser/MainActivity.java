package com.example.fuleehnzsolt.sapi_advertiser;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private EditText lPhoneNumber, lCode;
    private Button lToRegisterButton, lGetCodeButton, loginButton;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private String phoneNumber;
    private String mVerificationId;
    private String verificationCode;
    private FirebaseFirestore db;
    private String passPhoneNumber, passFirstName, passLastName;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        lPhoneNumber = findViewById(R.id.lPhoneNumber);
        lCode = findViewById(R.id.lCode);
        lToRegisterButton = findViewById(R.id.lToRegisterButton);
        loginButton = findViewById(R.id.loginButton);
        lGetCodeButton = findViewById(R.id.lGetCodeButton);
        lCode.setVisibility(View.INVISIBLE);
        loginButton.setVisibility(View.INVISIBLE);

        lToRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent register = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(register);

            }
        });

        lGetCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phoneNumber = lPhoneNumber.getText().toString();

                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(MainActivity.this, "Enter your phone number!", Toast.LENGTH_LONG).show();
                } else {

                    CollectionReference collectionReference = db.collection("users");
                    Query query = collectionReference.whereEqualTo("phoneNumber", phoneNumber);

                    query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (queryDocumentSnapshots.isEmpty()) {
                                Toast.makeText(MainActivity.this, "This number isn't registered!", Toast.LENGTH_LONG).show();
                            } else {
                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    if (documentSnapshot.getString("phoneNumber").equals(phoneNumber)) {
                                        passPhoneNumber = documentSnapshot.getString("phoneNumber");
                                        passFirstName = documentSnapshot.getString("firstName");
                                        passLastName = documentSnapshot.getString("lastName");
                                        break;
                                    }
                                }

                                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                        phoneNumber,
                                        60,
                                        TimeUnit.SECONDS,
                                        MainActivity.this,
                                        mCallbacks
                                );
                            }
                        }
                    });

                }
            }

        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificationCode = lCode.getText().toString();

                if (TextUtils.isEmpty(verificationCode)) {
                    Toast.makeText(MainActivity.this, "Enter the Code", Toast.LENGTH_SHORT).show();
                } else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(MainActivity.this, "You are logged in!", Toast.LENGTH_SHORT).show();
                lCode.setVisibility(View.INVISIBLE);
                loginButton.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                mVerificationId = verificationId;
                mResendToken = token;

                Toast.makeText(MainActivity.this, "Code sent...", Toast.LENGTH_SHORT).show();

                lCode.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.VISIBLE);
            }
        };

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Successfully logged in!", Toast.LENGTH_SHORT).show();

                            finish();
                        } else {

                            String message = task.getException().toString();
                            Toast.makeText(MainActivity.this, "Error: " + message, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}
