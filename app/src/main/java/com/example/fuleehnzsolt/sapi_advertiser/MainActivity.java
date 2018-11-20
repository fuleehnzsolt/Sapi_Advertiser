package com.example.fuleehnzsolt.sapi_advertiser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        lPhoneNumber = (EditText) findViewById(R.id.lPhoneNumber);
        lCode = (EditText) findViewById(R.id.lCode);
        lToRegisterButton = (Button) findViewById(R.id.lToRegisterButton);
        loginButton = (Button) findViewById(R.id.loginButton);

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

    }
}
